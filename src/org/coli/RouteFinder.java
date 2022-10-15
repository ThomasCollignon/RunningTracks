package org.coli;

import java.util.*;
import java.util.stream.Collectors;

class RouteFinder {

    private static List<Route> routes = new ArrayList<>();

    /**
     * Initialized with default parameters
     */
    private static Parameters parameters = new Parameters();

    private static void findRoute(Map<String, Point> data, int distance) {
        search(new Route(data), distance);
        Collections.sort(routes);
        routes.forEach(r -> System.out.println(r.toString()));
    }

    static void findRoute(Map<String, Point> data, int distance, Parameters providedParameters) {
        parameters = providedParameters;
        findRoute(data, distance);
    }

    private static void search(Route route, int distance) {
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
    private static void addRouteIfMatchesCriteria(Route route) {
        Route reversedRoute = new Route(route);
        Collections.reverse(reversedRoute);
        if (route.getLastPoint().getLabel().equals(parameters.getStartingPointLabel()) &&
                parameters.getMandatoryPoints().stream().allMatch(l -> route.contains(new Point(l))) &&
                (parameters.isReverseTwinDisplayed() || !routes.contains(reversedRoute)) &&
                !route.includesPatternToAvoid() &&
                route.includesPatternToInclude()) {
            routes.add(new Route(route));
        }
    }


    private static void continueSearch(Route route, int distance) {
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
    private static int totalDistanceFlag(Route route, int distance) {
        if (parameters.isExtraDistancePercentageFlag() && parameters.getExtraDistanceMeters() != 0) {
            throw new RuntimeException("Both extra distance percentage and meters can't be filled at the same time.");
        }
        double lowerBound = 0;
        double upperBound = 0;
        if (parameters.isExtraDistancePercentageFlag()) {
            double exceedingPercentageDouble = (double) parameters.getExtraDistancePercentage();
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

    private static class Route extends ArrayList<Point> implements Comparable<Route> {

        private static final String SEPARATOR = " - ";

        private int currentDistance;

        Route(Map<String, Point> data) {
            super();
            setCurrentDistance(0);
            this.add(data.get(parameters.getStartingPointLabel()));
        }

        Route(Route toBeCloned) {
            super(toBeCloned);
            currentDistance = toBeCloned.getCurrentDistance();
        }

        /**
         * Checks if the route goes twice by the same point
         *
         * @return <code>true</code> if there is at least one point (excluding home point) by witch the route passes more than once
         */
        public boolean containsNoRepeat() {
            Set<Point> pointsSet = new HashSet<>();
            pointsSet.addAll(this);
            return this.size() - pointsSet.size() > 1 ? false : true;
        }

        public String toString() {
            StringBuilder output = new StringBuilder();
            output.append(getCurrentDistance() + " ");
            output.append(toStringPoints());
            return output.toString();
        }

        private String toStringPoints() {
            StringBuilder output = new StringBuilder();
            this.forEach(p -> output.append(p.getLabel() + SEPARATOR));
            String outputString = output.toString();
            return output.toString().substring(0, outputString.length() - SEPARATOR.length());
        }

        @Override
        public boolean add(Point point) {
            if (this.size() < 1) {
                this.setCurrentDistance(0);
            } else {
                this.setCurrentDistance(this.getCurrentDistance() + this.getDistanceTo(point));
            }
            return super.add(point);
        }

        Set<Point> getAvailableNextPoints() {
            Set<Point> availableNextPoints = new HashSet<>();
            availableNextPoints.addAll(getLastPoint().getLinkedPoints().keySet());
            if (this.size() > 1 && !parameters.isTurnaround()) {
                availableNextPoints.remove(this.get(this.size() - 2));
            }
            return availableNextPoints.stream()
                                      .filter(p -> (!this.contains(p) || p.getLabel().equals("Tumuli") || p.getLabel()
                                                                                                           .equals("Anglée")) ||
                                              parameters.isRepeatPoint() ||
                                              p.getLabel().equals(parameters.getStartingPointLabel()))
                                      .collect(Collectors.toSet());
        }

        private Point getLastPoint() {
            return this.get(this.size() - 1);
        }

        int getDistanceTo(Point destination) {
            return getLastPoint().getLinkedPoints().get(destination);
        }

        int getCurrentDistance() {
            return currentDistance;
        }

        Route setCurrentDistance(int currentDistance) {
            this.currentDistance = currentDistance;
            return this;
        }

        @Override
        public int compareTo(Route o) {
            return Integer.valueOf(this.getCurrentDistance()).compareTo(Integer.valueOf(o.getCurrentDistance()));
        }

        boolean includesPatternToAvoid() {
            String routeString = this.toStringPoints();
            return parameters.getPatternsToAvoid()
                             .stream()
                             .anyMatch(routeString::contains);
        }

        boolean includesPatternToInclude() {
            String routeString = this.toStringPoints();
            return parameters.getPatternsToInclude()
                             .stream()
                             .allMatch(routeString::contains);
        }
    }
}