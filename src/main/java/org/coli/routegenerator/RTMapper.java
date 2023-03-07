package org.coli.routegenerator;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class RTMapper {

    static List<String> toListOfCoordinates(Route route) {
        Coordinates coordinates = new Coordinates();
        return route.stream()
                    .map(Point::getLabel)
                    .map(coordinates::toCoordinates)
                    .collect(toList());
    }

}
