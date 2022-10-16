package org.coli;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.RouteFinder.findRoutes;
import static org.coli.TestConstants.point_anglee;
import static org.coli.TestConstants.point_c;
import static org.coli.TestConstants.point_daix;
import static org.coli.TestConstants.point_home;
import static org.coli.TestConstants.point_l;
import static org.coli.TestConstants.point_sabliere;
import static org.coli.TestConstants.point_tumuli;
import static org.coli.TestConstants.pointsMap;

public class RouteFinderTest {

    private static final Parameters testParameters = new Parameters().setExtraDistanceMeters(100);
    static Route expectedLongRoute = new Route(pointsMap, testParameters);
    static Route expectedShortRoute = new Route(pointsMap, testParameters);

    @BeforeAll
    static void setUp() {
        expectedShortRoute.add(point_anglee);
        expectedShortRoute.add(point_daix);
        expectedShortRoute.add(point_tumuli);
        expectedShortRoute.add(point_home);

        expectedLongRoute.add(point_anglee);
        expectedLongRoute.add(point_daix);
        expectedLongRoute.add(point_l);
        expectedLongRoute.add(point_c);
        expectedLongRoute.add(point_sabliere);
        expectedLongRoute.add(point_tumuli);
        expectedLongRoute.add(point_home);
    }

    @Test
    public void findRoutesTest_short() {
        List<Route> foundRoutes = findRoutes(pointsMap, 2200,
                                                   new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).contains(expectedShortRoute);
        assertThat(foundRoutes).hasSize(1);
        assertThat(foundRoutes).first().extracting(Route::getCurrentDistance).isEqualTo(2240);
    }

    @Test
    public void findRoutesTest_long() {
        List<Route> foundRoutes = findRoutes(pointsMap, 3800,
                                                   new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).contains(expectedLongRoute);
        assertThat(foundRoutes).hasSize(1);
        assertThat(foundRoutes).first().extracting(Route::getCurrentDistance).isEqualTo(3870);
    }

    @Test
    public void findRoutesTest_none() {
        List<Route> foundRoutes = findRoutes(pointsMap, 3000,
                                                   new Parameters().setExtraDistanceMeters(100));
        assertThat(foundRoutes).isEmpty();
    }

    @Test
    public void findRoutesTest_multiple() {
        List<Route> foundRoutes = findRoutes(pointsMap, 3000,
                                                   new Parameters().setExtraDistanceMeters(1000));
        assertThat(foundRoutes).contains(expectedShortRoute);
        assertThat(foundRoutes).contains(expectedLongRoute);
        assertThat(foundRoutes).hasSize(2);
    }
}
