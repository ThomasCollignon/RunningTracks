package org.coli.routegenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

class RouteFinder {

    private final List<Route> routes;

    /**
     * Initialized with default parameters
     */
    private Parameters parameters;

    private RouteFinder() {
        routes = new ArrayList<>();
        parameters = Parameters.builder()
                               .build();
    }

    static List<Route> findRoutes(PointsMap pointsMap, int distanceInMeters, Parameters providedParameters) {
        RouteFinder routeFinder = new RouteFinder();
        routeFinder.parameters = providedParameters;
        return routeFinder.findRoutes(pointsMap, distanceInMeters);
    }

    /**
     * Adds the route to the output list only if it matches the criteria:
     * - the last point is the home point
     * - all the mandatory points are reached at least once (cf parameters)
     * - this route reversed is not already present in the list (cf parameters)
     */
    private void addRouteIfMatchesCriteria(Route route) {
        Route reversedRoute = new Route(route);
        Collections.reverse(reversedRoute);
        if (route.getLastPoint()
                 .getLabel()
                 .equals(route.getStartingPointLabel()) &&
                parameters.getMandatoryPoints()
                          .stream()
                          .allMatch(l -> route.contains(new Point(l))) &&
                (parameters.isReverseTwinDisplayed() || !routes.contains(reversedRoute)) &&
                !route.includesAnyRouteToExclude(parameters.getExcludeRoutes()) &&
                route.includesAllRoutesToInclude(parameters.getIncludeRoutes())) {
            routes.add(new Route(route));
        }
    }

    private void continueSearch(Route route, int distance) {
        route.getAvailableNextPoints(parameters.isTurnaround(), parameters.isRepeatPoint())
             .forEach(p -> {
                 Route newRoute = new Route(route);
                 newRoute.add(p);
                 search(newRoute, distance);
             });
    }

    private List<Route> findRoutes(PointsMap pointsMap, int distance) {
        search(new Route(pointsMap), distance);
        sort(routes);
        return routes;
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
        if (parameters.isExtraDistancePercentageFlag()) {
            double exceedingPercentageDouble = parameters.getExtraDistancePercentage();
            lowerBound = ((100 - exceedingPercentageDouble) / 100) * distance;
            upperBound = ((100 + exceedingPercentageDouble) / 100) * distance;
        } else {
            int extraDistanceMeters = parameters.getExtraDistanceMeters();
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