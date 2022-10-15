package org.coli;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static org.coli.Constants.ROUTE_SEPARATOR;

public class Coordinates extends HashMap<String, String> {

    public Coordinates() {
        super();
        try (Stream<String> stream = lines(Paths.get("resources/coordinates.txt"), UTF_8)) {
            stream.map(line -> line.split(" "))
                  .forEach(array -> this.put(array[0], array[1] + " " + array[2]));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toCoordinates(String label) {
        return this.get(label);
    }

    public static String toRouteCoordinates(String route) {
        Coordinates coordinates = new Coordinates();
        return Stream.of(route.split(ROUTE_SEPARATOR))
                     .map(coordinates::toCoordinates)
                     .map(coord -> "'" + coord + "'")
                     .collect(joining(",\n"));
    }
}
