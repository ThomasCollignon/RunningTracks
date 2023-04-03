package org.coli.routegenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.coli.routegenerator.Constants.RUN_ZONE_LIBERSART;

@Service
@RequiredArgsConstructor
@Slf4j
class RouteFinder {

    /**
     * see <a href="https://developers.google.com/maps/documentation/javascript/directions#waypoint-limits">Limitation
     * of Google Directions API</a>
     */
    private static final int MAX_NUMBER_OF_WAYPOINTS = 25;

    private final PointsLoader pointsLoader;

    private List<Route> routes;

    /**
     * Initialized with default options
     */
    private Options options;

    List<Route> findRoutes(String runZone, int distance, Options providedOptions) {
        log.info("Finding routes in " + runZone + " of " + distance + " meters");
        log.debug("with " + providedOptions);
        PointsMap pointsMap = runZone.equals(RUN_ZONE_LIBERSART)
                              ? pointsLoader.getPointsMapLibersart()
                              : pointsLoader.getPointsMapChastre();
        options = providedOptions;
        routes = new ArrayList<>();
        search(new Route(pointsMap), distance);
        return routes;
    }

    private void addRouteIfMatchesCriteria(Route route) {
        if (startingPointInTheMiddle(route)) return;
        if (route.size() > MAX_NUMBER_OF_WAYPOINTS) return;

        Route reversedRoute = new Route(route);
        Collections.reverse(reversedRoute);
        if (route.getLastPoint().label().equals(route.getStartingPointLabel())
                && options.getMandatoryPoints().stream().allMatch(l -> route.contains(new Point(l)))
                && (options.isReverseTwinDisplayed() || !routes.contains(reversedRoute))
                && !route.includesAnyRouteToExclude(options.getExcludeRoutes())
                && route.includesAllRoutesToInclude(options.getIncludeRoutes())
                && noSimilarRouteAlreadyAdded(route, routes)) {
            route.computeCenter();
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