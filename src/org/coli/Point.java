package org.coli;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

@Value
@RequiredArgsConstructor
public class Point {

    String label;
    Map<Point, Integer> linkedPoints = new HashMap<>();

    public void printListLinks() {
        this.getLinkedPoints().forEach((k, v) -> System.out.println(" " + k.getLabel() + " " + v));
    }

    @Override
    public boolean equals(Object o) {
        // Impossible to directly test the equality of two points because linked points cause recursion
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return label.equals(point.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return "Point{" +
                "label='" + label + '\'' +
                ", linkedPoints=" + linkedPoints.keySet().stream().map(Point::getLabel).collect(toSet()) +
                '}';
    }
}
