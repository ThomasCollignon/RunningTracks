package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.LONG_ROUTE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE;
import static org.coli.routegenerator.TestConstants.TEST_POINTS;
import static org.coli.routegenerator.Utils.excludeRoutesFromFile;
import static org.coli.routegenerator.Utils.includeRoutesFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteFinderTest {

    private final RouteFinder routeFinder = new RouteFinder();

    @Test
    void findRoutes_include_exclude() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 3000,
                                                         Options.builder()
                                                                .extraDistanceMeters(1000)
                                                                .includeRoutes(includeRoutesFromFile(
                                                                        "includeRoutes.txt"))
                                                                .build());
        assertThat(foundRoutes).contains(LONG_ROUTE)
                               .hasSize(1);

        foundRoutes = routeFinder.findRoutes(TEST_POINTS, 3000,
                                             Options.builder()
                                                    .extraDistanceMeters(1000)
                                                    .excludeRoutes(excludeRoutesFromFile(
                                                            "excludeRoutes.txt"))
                                                    .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .hasSize(1);

    }

    @Test
    void findRoutes_long_by_extra_distance() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 3800, Options.builder()
                                                                                   .extraDistanceMeters(100)
                                                                                   .build());
        assertThat(foundRoutes).contains(LONG_ROUTE)
                               .hasSize(1);
        assertEquals(3870, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_multiple() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 3000,
                                                         Options.builder()
                                                                .extraDistanceMeters(1000)
                                                                .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .contains(LONG_ROUTE)
                               .hasSize(2);
    }

    @Test
    void findRoutes_none() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 3000, Options.builder()
                                                                                   .extraDistanceMeters(100)
                                                                                   .build());
        assertThat(foundRoutes).isEmpty();
    }


    @Test
    void findRoutes_short_by_extra_distance() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 2200, Options.builder()
                                                                                   .extraDistanceMeters(100)
                                                                                   .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .hasSize(1);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_short_by_extra_percentage() {
        List<Route> foundRoutes = routeFinder.findRoutes(TEST_POINTS, 2200, Options.builder()
                                                                                   .extraDistancePercentageFlag(true)
                                                                                   .extraDistancePercentage(1)
                                                                                   .build());
        assertThat(foundRoutes).isEmpty();

        foundRoutes = routeFinder.findRoutes(TEST_POINTS, 2200, Options.builder()
                                                                       .extraDistancePercentageFlag(true)
                                                                       .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .hasSize(1);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }
}
