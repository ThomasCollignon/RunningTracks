package org.coli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.coli.Constants.RESOURCES_PATH;
import static org.coli.Constants.RUN_ZONE_CHASTRE;
import static org.coli.Coordinates.toRouteCoordinates;
import static org.coli.PointsLoader.load;

/**
 * See map <a href="https://drive.google.com/open?id=11pTt6aFhzUzS0cw5AF7ekdZwZAXFG4pG">here</a>
 */
public class Main {

//    private static final PointsMap data = load(RUN_ZONE_LIBERSART, "Home");
    private static final PointsMap pointsMap = load(RUN_ZONE_CHASTRE, "Commune");

    public static void main(String[] args) {
//        printData();
        List<Route> routes = RouteFinder.findRoutes(pointsMap,
                                                    12000,
                                                    new Parameters()
//                        .setExtraDistancePercentage(50)
                        .setExtraDistanceMeters(200)
//                        .setRepeatPoint(true)
//                        .setMandatoryPoints(new HashSet<>(asList("Bardane")))
//                        .setPatternsToAvoid(patternLoader(false))
//                        .setPatternsToInclude(patternLoader(true))
                                                         );

        // Print routes
        routes.forEach(r -> System.out.println(r.toString()));

        // Display the first match as an array
        System.out.println();
        System.out.println("First route is:");
        String routeString = routes.get(0).toString();
        String routeString_noPrefix = routeString.substring(routeString.indexOf(pointsMap.getStartingPointLabel()));
        System.out.println(toRouteCoordinates(routeString_noPrefix));
    }

    private static void printData() {
        System.out.println(pointsMap.keySet());
        pointsMap.forEach((k, v) -> {
            System.out.println(k);
            v.printListLinks();
            System.out.println();
        });
    }

    private static Set<String> patternLoader(boolean include) {
        Set<String> patternsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(
                RESOURCES_PATH + "patternsTo" + (include ? "Include" : "Avoid") + ".txt"), UTF_8)) {
            stream.forEach(line -> {
                patternsSet.add(line);
                if (!include) {
                    patternsSet.add(new StringBuilder(line).reverse().toString());
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return patternsSet;
    }

}

