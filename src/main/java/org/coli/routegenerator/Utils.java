package org.coli.routegenerator;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;

public class Utils {

    private Utils() {
    }

    static Set<String> excludeRoutesFromFile(String fileName) {
        return readFileFromResourcesDirectory(fileName).flatMap(route -> Stream.of(route, reverseRoute(route)))
                                                       .collect(toSet());
    }

    static Set<String> includeRoutesFromFile(String fileName) {
        return readFileFromResourcesDirectory(fileName).collect(toSet());
    }

    /**
     * @param fileName A file in /resources directory
     * @param consumer A consumer to run on each line of the file
     */
    static void readFileAndConsumeLines(String fileName, Consumer<Stream<String>> consumer) {
        consumer.accept(readFileFromResourcesDirectory(fileName));
    }

    static String reverseRoute(String route) {
        String[] elements = route.split(ROUTE_SEPARATOR);
        return elements[1] + ROUTE_SEPARATOR + elements[0];
    }

    static List<String> toListOfCoordinates(Route route) {
        return route.stream()
                    .map(Point::getLabel)
                    .map(label -> new Coordinates().toCoordinates(label))
                    .collect(toList());
    }

    private static Stream<String> readFileFromResourcesDirectory(String filename) {
        InputStream inputStream = Utils.class.getClassLoader()
                                             .getResourceAsStream(filename);
        if (inputStream == null) throw new RTException("Can't find file " + filename + " in /resources.");
        String fileString;
        try {
            fileString = IOUtils.toString(inputStream, UTF_8);
        } catch (IOException e) { // Not covered by UT because hard to reproduce or mock
            throw new RTException("Issue parsing file " + filename + "\n" + e.getMessage());
        }
        return Arrays.stream(fileString.split("\n"));
    }
}
