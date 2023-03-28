package org.coli.routegenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;
import static org.coli.routegenerator.Utils.reverseRoute;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Route extends ArrayList<Point> {

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
    public String toString() {
        StringBuilder output = new StringBuilder();
        this.forEach(p -> output.append(p.getLabel())
                                .append(ROUTE_SEPARATOR));
        String outputString = output.toString();
        return output.substring(0, outputString.length() - ROUTE_SEPARATOR.length());
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
                                  .collect(toSet());
    }

    int getDistanceTo(Point destination) {
        return getLastPoint().getLinkedPoints()
                             .get(destination);
    }

    Point getLastPoint() {
        return this.get(this.size() - 1);
    }

    boolean includesAllRoutesToInclude(Set<String> includedRoutes) {
        String routeString = this.toString();
        return includedRoutes.stream()
                             .allMatch(routeToInclude -> routeString.contains(routeToInclude) || routeString.contains(
                                     reverseRoute(routeToInclude)));
    }

    boolean includesAnyRouteToExclude(Set<String> excludedRoutes) {
        return excludedRoutes.stream()
                             .anyMatch(this.toString()::contains);
    }
}
