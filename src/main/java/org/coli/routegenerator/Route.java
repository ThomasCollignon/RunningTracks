package org.coli.routegenerator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;
import static org.coli.routegenerator.Utils.reverseRoute;

@Getter
public class Route extends ArrayList<Point> implements Comparable<Route> {

    private final String startingPointLabel;
    private int currentDistance;

    Route(PointsMap pointsMap) {
        super();
        currentDistance = 0;
        this.add(pointsMap.get(pointsMap.getStartingPointLabel()));
        startingPointLabel = pointsMap.getStartingPointLabel();
    }

    Route(Route toBeCloned) {
        super(toBeCloned);
        currentDistance = toBeCloned.getCurrentDistance();
        startingPointLabel = toBeCloned.getStartingPointLabel();
    }

    /**
     * Checks if the route goes twice by the same point
     *
     * @return <code>true</code> if there is at least one point (excluding home point) by witch the route passes more than once
     */
    public boolean containsNoRepeat() {
        Set<Point> pointsSet = new HashSet<>(this);
        return this.size() - pointsSet.size() <= 1;
    }

    @Override
    public String toString() {
        return getCurrentDistance() + " " + toStringPoints();
    }

    private String toStringPoints() {
        StringBuilder output = new StringBuilder();
        this.forEach(p -> output.append(p.getLabel() + ROUTE_SEPARATOR));
        String outputString = output.toString();
        return output
                .substring(0, outputString.length() - ROUTE_SEPARATOR.length());
    }

    @Override
    public boolean add(Point point) {
        if (this.size() < 1) {
            currentDistance = 0;
        } else {
            currentDistance += getDistanceTo(point);
        }
        return super.add(point);
    }

    Set<Point> getAvailableNextPoints(boolean turnaround, boolean repeatPoint) {
        Set<Point> availableNextPoints = new HashSet<>(getLastPoint().getLinkedPoints()
                                                                     .keySet());
        if (this.size() > 1 && !turnaround) {
            availableNextPoints.remove(this.get(this.size() - 2));
        }
        return availableNextPoints.stream()
                                  .filter(p -> !this.contains(p)
                                          || repeatPoint
                                          || p.getLabel()
                                              .equals(this.getStartingPointLabel()))
                                  .collect(Collectors.toSet());
    }

    Point getLastPoint() {
        return this.get(this.size() - 1);
    }

    int getDistanceTo(Point destination) {
        return getLastPoint().getLinkedPoints()
                             .get(destination);
    }

    @Override
    public int compareTo(Route o) {
        return Integer.valueOf(this.getCurrentDistance())
                      .compareTo(Integer.valueOf(o.getCurrentDistance()));
    }

    boolean includesAnyRouteToExclude(Set<String> excludedRoutes) {
        return excludedRoutes.stream()
                             .anyMatch(this.toStringPoints()::contains);
    }

    boolean includesAllRoutesToInclude(Set<String> includedRoutes) {
        String routeString = this.toStringPoints();
        return includedRoutes.stream()
                             .allMatch(routeToInclude -> routeString.contains(routeToInclude) ||
                                     routeString.contains(reverseRoute(routeToInclude)));
    }
}
