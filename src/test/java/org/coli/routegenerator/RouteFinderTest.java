package org.coli.routegenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.Utils.excludeRoutesFromFile;
import static org.coli.routegenerator.Utils.includeRoutesFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteFinderTest {

    private static final Parameters testParameters = Parameters.builder()
                                                               .extraDistanceMeters(100)
                                                               .build();
    static Route expectedLongRoute = new Route(TestConstants.pointsMap, testParameters);
    static Route expectedShortRoute = new Route(TestConstants.pointsMap, testParameters);

    @BeforeAll
    static void setUp() {
        expectedShortRoute.add(TestConstants.point_anglee);
        expectedShortRoute.add(TestConstants.point_daix);
        expectedShortRoute.add(TestConstants.point_tumuli);
        expectedShortRoute.add(TestConstants.point_home);

        expectedLongRoute.add(TestConstants.point_anglee);
        expectedLongRoute.add(TestConstants.point_daix);
        expectedLongRoute.add(TestConstants.point_l);
        expectedLongRoute.add(TestConstants.point_c);
        expectedLongRoute.add(TestConstants.point_sabliere);
        expectedLongRoute.add(TestConstants.point_tumuli);
        expectedLongRoute.add(TestConstants.point_home);
    }

    @Test
    void findRoutes_short() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 2200, Parameters.builder()
                                                                                                  .extraDistanceMeters(
                                                                                                          100)
                                                                                                  .build());
        assertThat(foundRoutes).contains(expectedShortRoute)
                               .hasSize(1);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_long() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3800, Parameters.builder()
                                                                                                  .extraDistanceMeters(
                                                                                                          100)
                                                                                                  .build());
        assertThat(foundRoutes).contains(expectedLongRoute)
                               .hasSize(1);
        assertEquals(3870, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_none() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000, Parameters.builder()
                                                                                                  .extraDistanceMeters(
                                                                                                          100)
                                                                                                  .build());
        assertThat(foundRoutes).isEmpty();
    }

    @Test
    void findRoutes_multiple() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000,
                Parameters.builder()
                          .extraDistanceMeters(1000)
                          .build());
        assertThat(foundRoutes).contains(expectedShortRoute)
                               .contains(expectedLongRoute)
                               .hasSize(2);
    }

    @Test
    void findRoutes_include() throws IOException {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000,
                Parameters.builder()
                          .extraDistanceMeters(1000)
                          .includeRoutes(includeRoutesFromFile("includeRoute.txt"))
                          .build());
        assertThat(foundRoutes).contains(expectedLongRoute)
                               .hasSize(1);
    }

    @Test
    void findRoutes_exclude() throws IOException {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000,
                Parameters.builder()
                          .extraDistanceMeters(1000)
                          .excludeRoutes(excludeRoutesFromFile("excludeRoute.txt"))
                          .build());
        assertThat(foundRoutes).contains(expectedShortRoute)
                               .hasSize(1);
    }
}
