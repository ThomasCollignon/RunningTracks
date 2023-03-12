package org.coli.routegenerator;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.coli.routegenerator.Constants.RESOURCES_PATH;

public class Coordinates extends HashMap<String, String> {

    public Coordinates() {
        super();
        Utils.readFileAndConsumeLines(RESOURCES_PATH + "coordinates.txt", this::parseStream);
    }

    private void parseStream(Stream<String> stream) {
        stream.filter(line -> !line.isEmpty())
              .map(line -> line.split(" "))
              .forEach(array -> this.put(array[0], array[1] + " " + array[2]));
    }

    public String toCoordinates(String label) {
        return this.get(label);
    }
}
