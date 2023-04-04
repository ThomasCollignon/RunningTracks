package org.coli.routegenerator.service.route.sort;

import org.coli.routegenerator.service.route.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.coli.routegenerator.constant.TestConstants.ONE_METER;
import static org.coli.routegenerator.constant.TestConstants.ROUTE_CHASTRE;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.TEST_POINTS_LIBERSART;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class RouteSortServiceTest {

    @Spy
    RouteSortService routeSortServiceSpy;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void rtSort2() {
        List<Route> routes = getRoutesWithStubbedCenter();
        doNothing().when(routeSortServiceSpy).computeCenter(any());
        doAnswer(call -> call.getArgument(0)).when(routeSortServiceSpy).sort(any());
        routeSortServiceSpy.rtSort2(routes);
        verify(routeSortServiceSpy, times(6)).computeCenter(any());
        verify(routeSortServiceSpy).sort(any());
    }

    @Test
    void sort() {
        List<Route> routes = getRoutesWithStubbedCenter();
        List<Route> sortedRoutes = routeSortServiceSpy.sort(routes);

        assertEquals(1d, sortedRoutes.get(0).getCenterLat());
        assertEquals(1d, sortedRoutes.get(0).getCenterLng());

        assertEquals(7d, sortedRoutes.get(1).getCenterLat());
        assertEquals(3d, sortedRoutes.get(1).getCenterLng());

        assertEquals(2d, sortedRoutes.get(2).getCenterLat());
        assertEquals(6d, sortedRoutes.get(2).getCenterLng());

        assertEquals(3d, sortedRoutes.get(3).getCenterLat());
        assertEquals(2d, sortedRoutes.get(3).getCenterLng());

        assertEquals(5d, sortedRoutes.get(4).getCenterLat());
        assertEquals(2d, sortedRoutes.get(4).getCenterLng());

        assertEquals(4d, sortedRoutes.get(5).getCenterLat());
        assertEquals(4d, sortedRoutes.get(5).getCenterLng());
    }

    @Test
    void distanceBetweenCentersOf() {
        assertThat(routeSortServiceSpy.distanceBetweenCentersOf(ROUTE_CHASTRE, ROUTE_CHASTRE))
                .isEqualTo(0d, withPrecision(ONE_METER));
        assertThat(routeSortServiceSpy.distanceBetweenCentersOf(ROUTE_CHASTRE, SHORT_ROUTE_LIBERSART))
                .isEqualTo(0.08418d, withPrecision(ONE_METER));
    }

    @Test
    void computeCenter() {
        routeSortServiceSpy.computeCenter(SHORT_ROUTE_LIBERSART);
        assertThat(SHORT_ROUTE_LIBERSART.getCenterLat()).isEqualTo(50.652560067d, withPrecision(ONE_METER));
        assertThat(SHORT_ROUTE_LIBERSART.getCenterLng()).isEqualTo(4.715228808d, withPrecision(ONE_METER));
    }

    private List<Route> getRoutesWithStubbedCenter() {
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
