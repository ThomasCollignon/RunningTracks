package org.coli.routegenerator.service.route;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.coli.routegenerator.data.PointsMap;
import org.coli.routegenerator.model.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toSet;
import static org.coli.routegenerator.service.route.RouteHelper.ROUTE_SEPARATOR;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Route extends ArrayList<Point> {

    private final String startingPointLabel;

    private int currentDistance;

    @Setter
    @EqualsAndHashCode.Exclude
    private double centerLat;

    @Setter
    @EqualsAndHashCode.Exclude
    private double centerLng;

    public Route(PointsMap pointsMap) {
        super();
        currentDistance = 0;
        this.add(pointsMap.get(pointsMap.getStartingPointLabel()));
        startingPointLabel = pointsMap.getStartingPointLabel();
    }

    Route(Route toBeCloned) {
        super(toBeCloned);
        currentDistance = toBeCloned.getCurrentDistance();
        startingPointLabel = toBeCloned.getStartingPointLabel();
        centerLat = toBeCloned.getCenterLat();
        centerLng = toBeCloned.getCenterLng();
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
        this.forEach(p -> output.append(p.label())
                                .append(ROUTE_SEPARATOR));
        String outputString = output.toString();
        return output.substring(0, outputString.length() - ROUTE_SEPARATOR.length());
    }

    Set<Point> getAvailableNextPoints(boolean turnaround, boolean repeatPoint) {
        Set<Point> availableNextPoints = new HashSet<>(getLastPoint().linkedPointsDistance().keySet());

        // Shuffle to prevent getting the routes always in the same order
        List<Point> availableNextPointsShuffled = new ArrayList<>(availableNextPoints);
        shuffle(availableNextPointsShuffled);
        availableNextPoints = new HashSet<>(availableNextPointsShuffled);

        if (this.size() > 1 && !turnaround) {
            availableNextPoints.remove(this.get(this.size() - 2));
        }
        return availableNextPoints.stream()
                                  .filter(p -> !this.contains(p) || repeatPoint || p.label()
                                                                                    .equals(this.getStartingPointLabel()))
                                  .collect(toSet());
    }

    Point getLastPoint() {
        return this.get(this.size() - 1);
    }

    boolean includesAllRoutesToInclude(Set<String> includedRoutes) {
        String routeString = this.toString();
        return includedRoutes.stream()
                             .allMatch(routeToInclude -> routeString.contains(routeToInclude) || routeString.contains(
                                     RouteHelper.reverseRoute(routeToInclude)));
    }

    boolean includesAnyRouteToExclude(Set<String> excludedRoutes) {
        return excludedRoutes.stream()
                             .anyMatch(this.toString()::contains);
    }

    private int getDistanceTo(Point destinationPoint) {
        return getLastPoint().linkedPointsDistance()
                             .get(destinationPoint);
    }
}
