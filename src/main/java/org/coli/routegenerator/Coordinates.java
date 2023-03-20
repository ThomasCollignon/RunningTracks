package org.coli.routegenerator;

import java.util.HashMap;
import java.util.stream.Stream;

public class Coordinates extends HashMap<String, String> {

    public Coordinates() {
        super();
        Utils.readFileAndConsumeLines("coordinates.txt", this::parseStream);
    }

    public String toCoordinates(String label) {
        return this.get(label);
    }

    private void parseStream(Stream<String> stream) {
        stream.filter(line -> !line.isEmpty())
              .map(line -> line.split(" "))
              .forEach(array -> this.put(array[0], array[1] + " " + array[2]));
    }
}
