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
        parameters = new Parameters();
    }

    private List<Route> findRoutes(PointsMap pointsMap, int distance) {
        search(new Route(pointsMap, parameters), distance);
        sort(routes);
        return routes;
    }

    static List<Route> findRoutes(PointsMap pointsMap, int distance, Parameters providedParameters) {
        RouteFinder routeFinder = new RouteFinder();
        routeFinder.parameters = providedParameters;
        return routeFinder.findRoutes(pointsMap, distance);
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
     * Adds the route to the output list only if it matches the criteria:
     * - the last point is the home point
     * - all the mandatory points are reached at least once (cf parameters)
     * - this route reversed is not already present in the list (cf parameters)
     *
     * @param route
     */
    private void addRouteIfMatchesCriteria(Route route) {
        Route reversedRoute = new Route(route);
        Collections.reverse(reversedRoute);
        if (route.getLastPoint().getLabel().equals(route.getStartingPointLabel()) &&
                parameters.getMandatoryPoints().stream().allMatch(l -> route.contains(new Point(l))) &&
                (parameters.isReverseTwinDisplayed() || !routes.contains(reversedRoute)) &&
                !route.includesPatternToAvoid() &&
                route.includesPatternToInclude()) {
            routes.add(new Route(route));
        }
    }

    private void continueSearch(Route route, int distance) {
        route.getAvailableNextPoints().forEach(p -> {
            Route newRoute = new Route(route);
            newRoute.add(p);
            search(newRoute, distance);
        });
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
        if (parameters.isExtraDistancePercentageFlag() && parameters.getExtraDistanceMeters() != 0) {
            throw new RuntimeException("Both extra distance percentage and meters can't be filled at the same time.");
        }
        double lowerBound = 0;
        double upperBound = 0;
        if (parameters.isExtraDistancePercentageFlag()) {
            double exceedingPercentageDouble = parameters.getExtraDistancePercentage();
            lowerBound = ((100 - exceedingPercentageDouble) / 100) * distance;
            upperBound = ((100 + exceedingPercentageDouble) / 100) * distance;
        }
        int extraDistanceMeters = parameters.getExtraDistanceMeters();
        if (extraDistanceMeters != 0) {
            lowerBound = distance - extraDistanceMeters;
            upperBound = distance + extraDistanceMeters;
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