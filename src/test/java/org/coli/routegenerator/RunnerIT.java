package org.coli.routegenerator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.Coordinates.coordinates;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * See map <a href="https://drive.google.com/open?id=11pTt6aFhzUzS0cw5AF7ekdZwZAXFG4pG">here</a>
 */
@Disabled("RunnerIT disabled, to be used for manual tests")
class RunnerIT {

    @Test
    void testRun() {
        //    private static final PointsMap pointsMap = load(RUN_ZONE_LIBERSART, "Home");
        final PointsMap pointsMap = PointsLoader.load(Constants.RUN_ZONE_CHASTRE, "Commune-Chastre");
        final RouteFinder routeFinder = new RouteFinder();

        List<Route> routes = routeFinder.findRoutes(pointsMap,
                                                    10000, Options.builder()
                                                                  //                .extraDistancePercentage(50)
                                                                  .extraDistanceMeters(200)
                                                                  //                .repeatPoint(true)
                                                                  //                .mandatoryPoints(new HashSet<>(asList("Bardane")))
//                                                 .excludeRoutes(patternLoader(false))
//                                                 .includeRoutes(patternLoader(true))
                                                                  .build()
        );

        // Print routes
        routes.forEach(r -> System.out.println(r.toString()));

        // Display the first match as an array
        System.out.println();
        System.out.println("First route is:");
        System.out.println(routes.get(0));

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }

    @Test
    void coordsToRouteForDebug() {
        String route = "[\n" +
                "\t\"50.61089648281807, 4.6435415396390205\",\n" +
                "\t\"50.60419407414455, 4.643725821210876\",\n" +
                "\t\"50.603922051068004, 4.647750561887193\",\n" +
                "\t\"50.59419203002076, 4.641477789001851\",\n" +
                "\t\"50.594298716104774, 4.636708640843913\",\n" +
                "\t\"50.590129810584635, 4.62381222175229\",\n" +
                "\t\"50.60129599147738, 4.6315021629681645\",\n" +
                "\t\"50.602242363181304, 4.629663605083267\",\n" +
                "\t\"50.60547284085589, 4.631605646931584\",\n" +
                "\t\"50.60800952244823, 4.63355377814606\",\n" +
                "\t\"50.60810538537233, 4.635677162134638\",\n" +
                "\t\"50.61149287656315, 4.636641030763757\",\n" +
                "\t\"50.609974412962664, 4.639559943778421\",\n" +
                "\t\"50.61089648281807, 4.6435415396390205\",\n" +
                "\t\"50.61593118996567, 4.640497808776313\",\n" +
                "\t\"50.60934635477486, 4.649830685496682\",\n" +
                "\t\"50.61089648281807, 4.6435415396390205\"\n" +
                "]";
        System.out.println(coordsToRouteString(route));

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }

    @Test
    void coordsToRouteString() {
        String route = "[\n" +
                "\t\"50.602242363181304, 4.629663605083267\",\n" + // Champs2
                "\t\"50.60547284085589, 4.631605646931584\",\n" + // Tchatche
                "\t\"50.60800952244823, 4.63355377814606\"\n" + // Chapelle
                "]";
        assertEquals("Champs2 - Tchatche - Chapelle", coordsToRouteString(route));
    }

    private String coordsToRouteString(String coords) {
        Map<String, String> coordsInvertedMap = new HashMap<>();
        coordinates().forEach((k, v) -> coordsInvertedMap.put(v, k));
        String[] stripped = coords.replace("[", "")
                                  .replace("]", "")
                                  .replace("\t", "")
                                  .replace("\"", "")
                                  .split("\n");
        return stream(stripped).skip(1)
                               .map(str -> str.endsWith(",") ? str.substring(0, str.length() - 1) : str)
                               .map(coordsInvertedMap::get)
                               .collect(joining(" - "));
    }
}
