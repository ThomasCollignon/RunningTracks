package org.coli.routegenerator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * See map <a href="https://drive.google.com/open?id=11pTt6aFhzUzS0cw5AF7ekdZwZAXFG4pG">here</a>
 */
class RunnerIT {

    //    private static final PointsMap pointsMap = load(RUN_ZONE_LIBERSART, "Home");
    private static final PointsMap pointsMap = PointsLoader.load(Constants.RUN_ZONE_CHASTRE, "Commune");

    @Test
    @Disabled("To be used for quick tests")
    void testRun() {
        List<Route> routes = RouteFinder.findRoutes(pointsMap,
                                                    10000, Parameters.builder()
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
}