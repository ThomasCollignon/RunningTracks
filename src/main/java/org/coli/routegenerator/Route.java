package org.coli.routegenerator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean add(Point point) {
        if (this.size() < 1) {
            currentDistance = 0;
        } else {
            currentDistance += getDistanceTo(point);
        }
        return super.add(point);
    }

    @Override
    public int compareTo(Route o) {
        return Integer.compare(this.getCurrentDistance(), o.getCurrentDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Route points = (Route) o;
        return currentDistance == points.currentDistance && startingPointLabel.equals(points.startingPointLabel);
    }

    Set<Point> getAvailableNextPoints(boolean turnaround, boolean repeatPoint) {
        Set<Point> availableNextPoints = new HashSet<>(getLastPoint().getLinkedPoints()
                                                                     .keySet());
        if (this.size() > 1 && !turnaround) {
            availableNextPoints.remove(this.get(this.size() - 2));
        }
        return availableNextPoints.stream()
                                  .filter(p -> !this.contains(p) || repeatPoint || p.getLabel()
                                                                                    .equals(this.getStartingPointLabel()))
                                  .collect(Collectors.toSet());
    }

    int getDistanceTo(Point destination) {
        return getLastPoint().getLinkedPoints()
                             .get(destination);
    }

    Point getLastPoint() {
        return this.get(this.size() - 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startingPointLabel, currentDistance);
    }

    boolean includesAllRoutesToInclude(Set<String> includedRoutes) {
        String routeString = this.toStringPoints();
        return includedRoutes.stream()
                             .allMatch(routeToInclude -> routeString.contains(routeToInclude) || routeString.contains(
                                     reverseRoute(routeToInclude)));
    }

    boolean includesAnyRouteToExclude(Set<String> excludedRoutes) {
        return excludedRoutes.stream()
                             .anyMatch(this.toStringPoints()::contains);
    }

    @Override
    public String toString() {
        return getCurrentDistance() + " " + toStringPoints();
    }

    private String toStringPoints() {
        StringBuilder output = new StringBuilder();
        this.forEach(p -> output.append(p.getLabel())
                                .append(ROUTE_SEPARATOR));
        String outputString = output.toString();
        return output.substring(0, outputString.length() - ROUTE_SEPARATOR.length());
    }
}
