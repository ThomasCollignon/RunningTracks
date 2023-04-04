package org.coli.routegenerator.service.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.data.PointsLoader;
import org.coli.routegenerator.data.PointsMap;
import org.coli.routegenerator.model.Point;
import org.coli.routegenerator.service.route.sort.RouteSortService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;
import static java.util.Collections.shuffle;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteFinderService {

    /**
     * see <a href="https://developers.google.com/maps/documentation/javascript/directions#waypoint-limits">Limitation
     * of Google Directions API</a>
     */
    private static final int MAX_NUMBER_OF_WAYPOINTS = 25;

    private final PointsLoader pointsLoader;

    private final RouteSortService routeSortService;

    private List<Route> routes;

    /**
     * Initialized with default options
     */
    private Options options;

    public List<Route> findAndSortRoutes(String runZone, int distance, Options providedOptions) {
        log.info("Finding routes in " + runZone + " of " + distance + " meters");
        log.debug("with " + providedOptions);
        PointsMap pointsMap = runZone.equals(RUN_ZONE_LIBERSART)
                              ? pointsLoader.getPointsMapLibersart()
                              : pointsLoader.getPointsMapChastre();
        options = providedOptions;
        routes = new ArrayList<>();
        search(new Route(pointsMap), distance);
        return sortedRoutes();
    }

    private List<Route> sortedRoutes() {
        shuffle(routes);
        return routeSortService.rtSort2(routes);
    }

    private void addRouteIfMatchesCriteria(Route route) {
        if (startingPointInTheMiddle(route)) return;
        if (route.size() > MAX_NUMBER_OF_WAYPOINTS) return;

        Route reversedRoute = new Route(route);
        reverse(reversedRoute);
        if (route.getLastPoint().label().equals(route.getStartingPointLabel())
                && options.getMandatoryPoints().stream().allMatch(l -> route.contains(new Point(l)))
                && (options.isReverseTwinDisplayed() || !routes.contains(reversedRoute))
                && !route.includesAnyRouteToExclude(options.getExcludeRoutes())
                && route.includesAllRoutesToInclude(options.getIncludeRoutes())
                && noSimilarRouteAlreadyAdded(route, routes)) {
            routes.add(new Route(route));
        }
    }

    private boolean noSimilarRouteAlreadyAdded(Route route, List<Route> routes) {
        return routes.stream()
                     .noneMatch(routeInList -> percentageOfSimilarity(routeInList, route) * 100 >
                             options.getSimilarityExclusionPercentage());
    }

    /**
     * The number of common elements divided by the number of elements in the route with the most elements
     */
    private float percentageOfSimilarity(Route route1, Route route2) {
        List<Point> route1Worker = new ArrayList<>(route1);
        List<Point> route2Worker = new ArrayList<>(route2);
        route1Worker.remove(route1Worker.size() - 1);
        route2Worker.remove(route2Worker.size() - 1);
        List<Point> routeWithMostPoints = route1Worker.size() > route2Worker.size() ? route1Worker : route2Worker;
        List<Point> routeWithLessPoints = route1Worker.size() > route2Worker.size() ? route2Worker : route1Worker;
        long commonPointsCount = routeWithLessPoints.stream().filter(routeWithMostPoints::contains).count();
        return (float) commonPointsCount / routeWithMostPoints.size();
    }

    private boolean startingPointInTheMiddle(Route route) {
        return route.stream().filter(point -> point.label().equals(route.getStartingPointLabel())).count() > 2;
    }

    private void continueSearch(Route route, int distance) {
        route.getAvailableNextPoints(options.isTurnaround(), options.isRepeatPoint())
             .forEach(p -> {
                 Route newRoute = new Route(route);
                 newRoute.add(p);
                 search(newRoute, distance);
             });
    }

    private void search(Route route, int distance) {
        int totalDistanceFlag = totalDistanceFlag(route, distance);
        if (totalDistanceFlag > 0) return;
        if (totalDistanceFlag == 0) addRouteIfMatchesCriteria(route);
        continueSearch(route, distance);
    }

    /**
     * Check if the current total distance is withing required range.
     *
     * @param route    The current route
     * @param distance The required distance
     * @return <ul>
     * <li><code>-1</code> if the current distance is below lower bound
     * <li><code>0</code> if the current distance is within required range
     * <li><code>1</code> if the current distance is above upper bound
     * </ul>
     */
    private int totalDistanceFlag(Route route, int distance) {
        double lowerBound = 0;
        double upperBound = 0;
        if (options.isExtraDistancePercentageFlag()) {
            double exceedingPercentageDouble = options.getExtraDistancePercentage();
            lowerBound = ((100 - exceedingPercentageDouble) / 100) * distance;
            upperBound = ((100 + exceedingPercentageDouble) / 100) * distance;
        } else {
            int extraDistanceMeters = options.getExtraDistanceMeters();
            if (extraDistanceMeters != 0) {
                lowerBound = (double) distance - extraDistanceMeters;
                upperBound = (double) distance + extraDistanceMeters;
            }
        }
        if (route.getCurrentDistance() < lowerBound) {
            return -1;
        }
        if (route.getCurrentDistance() > upperBound) {
            return 1;
        }
        return 0;
    }
}