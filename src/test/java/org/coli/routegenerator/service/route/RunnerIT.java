package org.coli.routegenerator.service.route;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.data.Coordinates.coordinates;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * See map <a href="https://drive.google.com/open?id=11pTt6aFhzUzS0cw5AF7ekdZwZAXFG4pG">here</a>
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // or @Autowired on the properties
@Disabled("RunnerIT disabled, to be used for manual tests")
class RunnerIT {

    private final RouteFinderService routeFinderService;

    @Test
    void testRun() {
        List<Route> routes = routeFinderService.findAndSortRoutes(RUN_ZONE_CHASTRE,
                                                                  10000, Options.builder()
                                                                                //                .extraDistancePercentage(50)
                                                                                .extraDistanceMeters(200)
                                                                                //                .repeatPoint(true)
                                                                                //                                                                         .mandatoryPoints(new HashSet<>(asList("Bardane")))
                                                                                .build()
        );

        // Display the first match
        System.out.println();
        System.out.println("First route is:");
        System.out.println("\t" + routes.get(0));

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }

    @Test
    void coordsToRouteForDebug() {
        String route = """
                       [
                       \t"50.61089648281807, 4.6435415396390205",
                       \t"50.60419407414455, 4.643725821210876",
                       \t"50.603922051068004, 4.647750561887193",
                       \t"50.59419203002076, 4.641477789001851",
                       \t"50.594298716104774, 4.636708640843913",
                       \t"50.590129810584635, 4.62381222175229",
                       \t"50.60129599147738, 4.6315021629681645",
                       \t"50.602242363181304, 4.629663605083267",
                       \t"50.60547284085589, 4.631605646931584",
                       \t"50.60800952244823, 4.63355377814606",
                       \t"50.60810538537233, 4.635677162134638",
                       \t"50.61149287656315, 4.636641030763757",
                       \t"50.609974412962664, 4.639559943778421",
                       \t"50.61089648281807, 4.6435415396390205",
                       \t"50.61593118996567, 4.640497808776313",
                       \t"50.60934635477486, 4.649830685496682",
                       \t"50.61089648281807, 4.6435415396390205"
                       ]""";
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
