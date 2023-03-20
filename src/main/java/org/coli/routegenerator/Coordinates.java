package org.coli.routegenerator;

import java.util.HashMap;
import java.util.stream.Stream;

public class Coordinates extends HashMap<String, String> {

    private static Coordinates instance;

    private Coordinates() {
        super();
        Utils.readFileAndConsumeLines("coordinates.txt", this::parseStream);
    }

    public static Coordinates coordinates() {
        if (instance == null) {
            instance = new Coordinates();
        }
        return instance;
    }

    public String getOrException(String label) {
        if (!this.containsKey(label)) throw new RTException("No coordinate found for the label " + label);
        return this.get(label);
    }

    private void parseStream(Stream<String> stream) {
        stream.filter(line -> !line.isEmpty())
              .map(line -> line.split(" "))
              .forEach(array -> this.put(array[0], array[1] + " " + array[2]));
    }
}
