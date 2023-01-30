package org.coli.routegenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteFinderTest {

    private static final Parameters testParameters = new Parameters().setExtraDistanceMeters(100);
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
    public void findRoutesTest_short() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 2200,
                                                         new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).contains(expectedShortRoute);
        assertThat(foundRoutes).hasSize(1);
        assertEquals(2240, foundRoutes.get(0).getCurrentDistance());
    }

    @Test
    public void findRoutesTest_long() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3800,
                                                         new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).contains(expectedLongRoute);
        assertThat(foundRoutes).hasSize(1);
        assertEquals(3870, foundRoutes.get(0).getCurrentDistance());
    }

    @Test
    public void findRoutesTest_none() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000,
                                                         new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).isEmpty();
    }

    @Test
    public void findRoutesTest_multiple() {
        List<Route> foundRoutes = RouteFinder.findRoutes(TestConstants.pointsMap, 3000,
                                                         new Parameters().setExtraDistanceMeters(1000));
        assertThat(foundRoutes).contains(expectedShortRoute);
        assertThat(foundRoutes).contains(expectedLongRoute);
        assertThat(foundRoutes).hasSize(2);
    }
}
