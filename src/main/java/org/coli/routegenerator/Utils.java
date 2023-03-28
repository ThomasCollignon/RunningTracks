package org.coli.routegenerator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.coli.routegenerator.Constants.ROUTE_SEPARATOR;
import static org.coli.routegenerator.Coordinates.coordinates;

@Slf4j
public class Utils {

    private Utils() {
    }

    /**
     * Sorts the routes in the order they should be displayed at each new request. Algo: the next route is the most
     * remote route from the two last ones, in the sense of the distance between the routes center, being the average of
     * coordinates.
     */
    static List<Route> rtSort2(List<Route> routes) {
        List<Route> sortedRoutes = new ArrayList<>();
        Route routeOneAgo = routes.get(0);
        Route routeTwoAgo;
        sortedRoutes.add(routeOneAgo);
        List<Route> remainingRoutes;
        int counter = 0;
        while (sortedRoutes.size() < routes.size()) {
            remainingRoutes = routes.stream().filter(r -> !sortedRoutes.contains(r)).collect(toList());
            routeTwoAgo = sortedRoutes.get(counter);
            routeOneAgo = getFarthestRouteFrom2(routeOneAgo, routeTwoAgo, remainingRoutes);
            sortedRoutes.add(routeOneAgo);
            if (sortedRoutes.size() > 2) counter++;
        }
        return sortedRoutes;
    }

    static Double distanceBetweenCentersOf(Route route1, Route route2) {
        double latsSquared = Math.pow((route1.getCenterLat() - route2.getCenterLat()), 2);
        double lngsSquared = Math.pow((route1.getCenterLng() - route2.getCenterLng()), 2);
        return Math.sqrt(latsSquared + lngsSquared);
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
                    .map(label -> coordinates().getOrException(label))
                    .collect(toList());
    }

    private static Route getFarthestRouteFrom2(Route routeOneAgo, Route routeTwoAgo, List<Route> routes) {
        return routes.stream()
                     .max(comparing(r -> distanceBetweenCentersOf(r, routeOneAgo)
                             + distanceBetweenCentersOf(r, routeTwoAgo)))
                     .orElseThrow(() -> new RTException("Problem when comparing routes"));
    }

    private static Stream<String> readFileFromResourcesDirectory(String filename) {
        log.info("reading file resources/" + filename);
        String fileString;
        try (InputStream inputStream = Utils.class.getClassLoader()
                                                  .getResourceAsStream(filename)) {
            if (inputStream == null) throw new RTException("Can't find file " + filename + " in /resources.");
            fileString = IOUtils.toString(inputStream, UTF_8);
        } catch (Exception e) {
            throw new RTException("Issue reading or parsing file " + filename + "\n" + e.getMessage());
        }
        return Arrays.stream(fileString.split("\n"));
    }
}
