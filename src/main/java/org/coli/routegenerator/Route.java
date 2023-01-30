package org.coli.routegenerator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;

@Getter
public class Route extends ArrayList<Point> implements Comparable<Route> {

    private final String startingPointLabel;
    private int currentDistance;
    private Parameters parameters;

    Route(PointsMap pointsMap, Parameters parameters) {
        super();
        this.parameters = parameters;
        currentDistance = 0;
        this.add(pointsMap.get(pointsMap.getStartingPointLabel()));
        startingPointLabel = pointsMap.getStartingPointLabel();
    }

    Route(Route toBeCloned) {
        super(toBeCloned);
        currentDistance = toBeCloned.getCurrentDistance();
        startingPointLabel = toBeCloned.getStartingPointLabel();
        parameters = toBeCloned.getParameters();
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
        this.forEach(p -> output.append(p.getLabel() + ROUTE_SEPARATOR));
        String outputString = output.toString();
        return output.toString().substring(0, outputString.length() - ROUTE_SEPARATOR.length());
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

    Set<Point> getAvailableNextPoints() {
        Set<Point> availableNextPoints = new HashSet<>(getLastPoint().getLinkedPoints().keySet());
        if (this.size() > 1 && !parameters.isTurnaround()) {
            availableNextPoints.remove(this.get(this.size() - 2));
        }
        return availableNextPoints.stream()
                                  .filter(p -> !this.contains(p)
                                          || parameters.isRepeatPoint()
                                          || p.getLabel().equals(this.getStartingPointLabel()))
                                  .collect(Collectors.toSet());
    }

    Point getLastPoint() {
        return this.get(this.size() - 1);
    }

    int getDistanceTo(Point destination) {
        return getLastPoint().getLinkedPoints().get(destination);
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