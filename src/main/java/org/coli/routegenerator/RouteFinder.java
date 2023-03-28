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
        log.debug("Finding routes in " + runZone + " of " + distance + "m, with " + providedOptions);
        PointsMap pointsMap = runZone.equals(RUN_ZONE_LIBERSART)
                              ? pointsLoader.getPointsMapLibersart()
                              : pointsLoader.getPointsMapChastre();
        options = providedOptions;
        routes = new ArrayList<>();
        search(new Route(pointsMap), distance);
        return routes;
    }

    /**
     * Adds the route to the output list only if it matches the criteria: - the last point is the home point - all the
     * mandatory points are reached at least once (cf options) - this route reversed is not already present in the list
     * (cf options)
     */
    private void addRouteIfMatchesCriteria(Route route) {
        if (startingPointInTheMiddle(route)) return;
        if (route.size() > MAX_NUMBER_OF_WAYPOINTS) return;

        Route reversedRoute = new Route(route);
        Collections.reverse(reversedRoute);
        if (route.getLastPoint()
                 .getLabel()
                 .equals(route.getStartingPointLabel()) && options.getMandatoryPoints()
                                                                  .stream()
                                                                  .allMatch(l -> route.contains(new Point(l))) && (options.isReverseTwinDisplayed() || !routes.contains(
                reversedRoute)) && !route.includesAnyRouteToExclude(options.getExcludeRoutes()) && route.includesAllRoutesToInclude(
                options.getIncludeRoutes())) {
            route.computeCenter();
            routes.add(new Route(route));
        }
    }

    private boolean startingPointInTheMiddle(Route route) {
        return route.stream()
                    .filter(point -> point.getLabel()
                                          .equals(route.getStartingPointLabel()))
                    .count() > 2;
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
        switch (totalDistanceFlag(route, distance)) {
            case -1:
                continueSearch(route, distance);
                break;
            case 0:
                addRouteIfMatchesCriteria(route);
                continueSearch(route, distance);
                break;
            default:
                break;
        }
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