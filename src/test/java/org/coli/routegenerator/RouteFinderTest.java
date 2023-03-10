package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.LONG_ROUTE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE;
import static org.coli.routegenerator.TestConstants.TEST_POINTS;
import static org.coli.routegenerator.Utils.excludeRoutesFromFile;
import static org.coli.routegenerator.Utils.includeRoutesFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteFinderTest {

    @Test
    void findRoutes_short() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 2200, Parameters.builder()
                                                                                      .extraDistanceMeters(100)
                                                                                      .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .hasSize(1);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_long() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 3800, Parameters.builder()
                                                                                      .extraDistanceMeters(100)
                                                                                      .build());
        assertThat(foundRoutes).contains(LONG_ROUTE)
                               .hasSize(1);
        assertEquals(3870, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_none() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 3000, Parameters.builder()
                                                                                      .extraDistanceMeters(100)
                                                                                      .build());
        assertThat(foundRoutes).isEmpty();
    }

    @Test
    void findRoutes_multiple() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 3000,
                                                         Parameters.builder()
                                                                   .extraDistanceMeters(1000)
                                                                   .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .contains(LONG_ROUTE)
                               .hasSize(2);
    }

    @Test
    void findRoutes_include() throws IOException {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 3000,
                                                         Parameters.builder()
                                                                   .extraDistanceMeters(1000)
                                                                   .includeRoutes(includeRoutesFromFile(
                                                                           "includeRoutes.txt"))
                                                                   .build());
        assertThat(foundRoutes).contains(LONG_ROUTE)
                               .hasSize(1);
    }

    @Test
    void findRoutes_exclude() throws IOException {
        List<Route> foundRoutes = RouteFinder.findRoutes(TEST_POINTS, 3000,
                                                         Parameters.builder()
                                                                   .extraDistanceMeters(1000)
                                                                   .excludeRoutes(excludeRoutesFromFile(
                                                                           "excludeRoutes.txt"))
                                                                   .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE)
                               .hasSize(1);
    }
}
