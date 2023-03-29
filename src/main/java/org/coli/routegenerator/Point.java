package org.coli.routegenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

public record Point(
        String label,
        Map<Point, Integer> linkedPoints
) {

    Point(String label) {
        this(label, new HashMap<>());
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
                ", linkedPoints=" + linkedPoints.keySet()
                                                .stream()
                                                .map(Point::label)
                                                .collect(toSet()) +
                '}';
    }
}
