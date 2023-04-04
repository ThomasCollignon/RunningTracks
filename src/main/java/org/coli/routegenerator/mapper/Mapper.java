package org.coli.routegenerator.mapper;

import org.coli.routegenerator.model.Point;
import org.coli.routegenerator.service.route.Route;

import java.util.List;

import static org.coli.routegenerator.data.Coordinates.coordinates;

public class Mapper {

    private Mapper() {
    }

    public static List<String> toListOfCoordinates(Route route) {
        return route.stream()
                    .map(Point::label)
                    .map(label -> coordinates().getOrException(label))
                    .toList();
    }
}
