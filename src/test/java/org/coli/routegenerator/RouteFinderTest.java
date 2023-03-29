package org.coli.routegenerator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.Constants.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.Constants.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.TestConstants.LONG_ROUTE_LIBERSART;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.TestConstants.TEST_POINTS_CHASTRE;
import static org.coli.routegenerator.TestConstants.TEST_POINTS_LIBERSART;
import static org.coli.routegenerator.Utils.excludeRoutesFromFile;
import static org.coli.routegenerator.Utils.includeRoutesFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
class RouteFinderTest {

    @InjectMocks
    private RouteFinder routeFinder;

    @Mock
    private PointsLoader pointsLoaderMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(pointsLoaderMock.getPointsMapLibersart()).thenReturn(TEST_POINTS_LIBERSART);
        when(pointsLoaderMock.getPointsMapChastre()).thenReturn(TEST_POINTS_CHASTRE);
    }

    @Test
    void avoid_multiple_passage_through_starting_point() {
        List<Route> routes = routeFinder.findRoutes(RUN_ZONE_CHASTRE, 10000, Options.builder()
                                                                                    .build());
        assertTrue(routes.stream()
                         .noneMatch(route -> route.stream()
                                                  .filter(point -> point.getLabel()
                                                                        .equals(STARTING_POINT_CHASTRE))
                                                  .count() > 2));
    }

    @Test
    void findRoutes_include_exclude() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000,
                                                         Options.builder()
                                                                .extraDistanceMeters(1000)
                                                                .includeRoutes(includeRoutesFromFile(
                                                                        "includeRoutes.txt"))
                                                                .build());
        assertThat(foundRoutes).containsOnly(LONG_ROUTE_LIBERSART);

        foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000,
                                             Options.builder()
                                                    .extraDistanceMeters(1000)
                                                    .excludeRoutes(excludeRoutesFromFile(
                                                            "excludeRoutes.txt"))
                                                    .build());
        assertThat(foundRoutes).containsOnly(SHORT_ROUTE_LIBERSART);
    }

    @Test
    void findRoutes_long_by_extra_distance() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3800, Options.builder()
                                                                                          .extraDistanceMeters(100)
                                                                                          .build());
        assertThat(foundRoutes).containsOnly(LONG_ROUTE_LIBERSART);
        assertEquals(3870, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_multiple() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000, Options.builder()
                                                                                          .extraDistanceMeters(1000)
                                                                                          .build());
        assertThat(foundRoutes).contains(SHORT_ROUTE_LIBERSART)
                               .contains(LONG_ROUTE_LIBERSART)
                               .hasSize(2);
    }

    @Test
    void findRoutes_multiple_exclude_if_similar() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000,
                                                         Options.builder()
                                                                .extraDistanceMeters(1000)
                                                                .similarityExclusionPercentage(50)
                                                                .build());
        assertThat(foundRoutes).containsAnyElementsOf(asList(SHORT_ROUTE_LIBERSART, LONG_ROUTE_LIBERSART))
                               .hasSize(1);
    }

    @Test
    void findRoutes_multiple_mandatoryPoints() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000,
                                                         Options.builder()
                                                                .extraDistanceMeters(1000)
                                                                .mandatoryPoints(new HashSet<>(asList("Daix", "C")))
                                                                .build());
        assertThat(foundRoutes).containsOnly(LONG_ROUTE_LIBERSART);
    }

    @Test
    void findRoutes_none() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 3000, Options.builder()
                                                                                          .extraDistanceMeters(100)
                                                                                          .build());
        assertThat(foundRoutes).isEmpty();
    }

    @Test
    void findRoutes_short_by_extra_distance() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 2200, Options.builder()
                                                                                          .extraDistanceMeters(100)
                                                                                          .build());
        assertThat(foundRoutes).containsOnly(SHORT_ROUTE_LIBERSART);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }

    @Test
    void findRoutes_short_by_extra_percentage() {
        List<Route> foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 2200,
                                                         Options.builder()
                                                                .extraDistancePercentageFlag(true)
                                                                .extraDistancePercentage(1)
                                                                .build());
        assertThat(foundRoutes).isEmpty();

        foundRoutes = routeFinder.findRoutes(RUN_ZONE_LIBERSART, 2200, Options.builder()
                                                                              .extraDistancePercentageFlag(true)
                                                                              .build());
        assertThat(foundRoutes).containsOnly(SHORT_ROUTE_LIBERSART);
        assertEquals(2240, foundRoutes.get(0)
                                      .getCurrentDistance());
    }
}
