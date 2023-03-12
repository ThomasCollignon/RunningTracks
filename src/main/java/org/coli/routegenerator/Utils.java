package org.coli.routegenerator;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;

public class Utils {

    private Utils() {
    }

    static Set<String> excludeRoutesFromFile(String fileName) throws IOException {
        return readFileFromResourcesDirectory(fileName).flatMap(route -> Stream.of(route, reverseRoute(route)))
                                                       .collect(toSet());
    }

    static Set<String> includeRoutesFromFile(String fileName) throws IOException {
        return readFileFromResourcesDirectory(fileName).collect(toSet());
    }

    static void readFileAndConsumeLines(String fileName, Consumer<Stream<String>> consumer) {
        try (Stream<String> stream = lines(Paths.get(fileName), UTF_8)) {
            consumer.accept(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filename ex.: "myFile.txt"
     * @return a Stream of the lines of this file
     */
    static Stream<String> readFileFromResourcesDirectory(String filename) throws IOException {
        InputStream inputStream = Utils.class.getClassLoader()
                                             .getResourceAsStream(filename);
        if (inputStream == null) throw new FileNotFoundException("Can't find file " + filename + " in /resources.");
        return Arrays.stream(IOUtils.toString(inputStream, UTF_8)
                                    .split("\n"));
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
}