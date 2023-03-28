package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.coli.routegenerator.TestConstants.ONE_METER;
import static org.coli.routegenerator.TestConstants.ROUTE_CHASTRE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;
import static org.coli.routegenerator.TestConstants.TEST_POINTS_LIBERSART;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class UtilsTest {

    private Set<String> fileContent;

    @Test
    void excludeRoutesFromFile() {
        Set<String> result = Utils.excludeRoutesFromFile("excludeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("L - C")
                          .hasSize(2);
    }

    @Test
    void includeRoutesFromFile() {
        Set<String> result = Utils.includeRoutesFromFile("includeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("Sablière - Tumuli")
                          .hasSize(2);
    }

    @Test
    void readFileAndConsumeLines() {
        Consumer<Stream<String>> consumer = stream -> fileContent = stream.collect(toSet());
        Utils.readFileAndConsumeLines("anyData.txt", consumer);
        assertThat(fileContent).isEqualTo(new HashSet<>(singleton("foo bar")));

        assertThrows(RTException.class, () -> Utils.readFileAndConsumeLines("I don't exist", consumer));
    }

    @Test
    void distanceBetweenCentersOf() {
        ROUTE_CHASTRE.computeCenter();
        SHORT_ROUTE_LIBERSART.computeCenter();
        assertThat(Utils.distanceBetweenCentersOf(ROUTE_CHASTRE, ROUTE_CHASTRE))
                .isEqualTo(0d, withPrecision(ONE_METER));
        assertThat(Utils.distanceBetweenCentersOf(ROUTE_CHASTRE, SHORT_ROUTE_LIBERSART))
                .isEqualTo(0.08418d, withPrecision(ONE_METER));
    }

    @Test
    void reverseRoute() {
        assertThat(Utils.reverseRoute("foo - bar")).isEqualTo("bar - foo");
    }

    @Test
    void toListOfCoordinates() {
        assertThat(Utils.toListOfCoordinates(SHORT_ROUTE_LIBERSART)).isEqualTo(SHORT_ROUTE_LIBERSART_COORDINATES);
    }

    @Test
    void rtSort() {
        List<Route> routes = prepareRoutesForRtSort();
        List<Route> sortedRoutes = Utils.rtSort(routes);

        assertEquals(1d, sortedRoutes.get(0).getCenterLat());
        assertEquals(1d, sortedRoutes.get(0).getCenterLng());

        assertEquals(7d, sortedRoutes.get(1).getCenterLat());
        assertEquals(3d, sortedRoutes.get(1).getCenterLng());

        assertEquals(2d, sortedRoutes.get(2).getCenterLat());
        assertEquals(6d, sortedRoutes.get(2).getCenterLng());

        assertEquals(5d, sortedRoutes.get(3).getCenterLat());
        assertEquals(2d, sortedRoutes.get(3).getCenterLng());

        assertEquals(4d, sortedRoutes.get(4).getCenterLat());
        assertEquals(4d, sortedRoutes.get(4).getCenterLng());

        assertEquals(3d, sortedRoutes.get(5).getCenterLat());
        assertEquals(2d, sortedRoutes.get(5).getCenterLng());
    }

    private List<Route> prepareRoutesForRtSort() {
        Route route1_1 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(1d).when(route1_1).getCenterLat();
        doReturn(1d).when(route1_1).getCenterLng();
        Route route3_2 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(3d).when(route3_2).getCenterLat();
        doReturn(2d).when(route3_2).getCenterLng();
        Route route5_2 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(5d).when(route5_2).getCenterLat();
        doReturn(2d).when(route5_2).getCenterLng();
        Route route7_3 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(7d).when(route7_3).getCenterLat();
        doReturn(3d).when(route7_3).getCenterLng();
        Route route4_4 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(4d).when(route4_4).getCenterLat();
        doReturn(4d).when(route4_4).getCenterLng();
        Route route2_6 = spy(new Route(TEST_POINTS_LIBERSART));
        doReturn(2d).when(route2_6).getCenterLat();
        doReturn(6d).when(route2_6).getCenterLng();
        return asList(route1_1, route3_2, route5_2, route7_3, route4_4, route2_6);
    }
}
