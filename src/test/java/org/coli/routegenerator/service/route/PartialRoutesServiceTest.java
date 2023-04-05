package org.coli.routegenerator.service.route;

import org.coli.routegenerator.data.PointsLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.TestConstants.LONG_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.initLongRouteLibersart;
import static org.coli.routegenerator.constant.TestConstants.point_c;
import static org.coli.routegenerator.constant.TestConstants.point_sabliere;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class PartialRoutesServiceTest {

    @InjectMocks
    private PartialRoutesService partialRoutesService;

    @Mock
    private PointsLoader pointsLoaderMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void applyPartialRoutes() {
        when(pointsLoaderMock.getPartialRoutesLibersart()).thenReturn(asList(
                asList("Tumuli", "Sablière", "C", "L"),
                asList("L", "C", "Sablière", "Tumuli"),
                asList("Tumuli", "Sablière", "C"),
                asList("C", "Sablière", "Tumuli"),
                asList("Sablière", "C", "L"),
                asList("L", "C", "Sablière")
        ));
        Route route = LONG_ROUTE_LIBERSART;
        route = partialRoutesService.applyPartialRoutesTo(route);
        Route expectedRoute = initLongRouteLibersart();
        expectedRoute.remove(point_c);
        expectedRoute.remove(point_sabliere);
        assertThat(route).isEqualTo(expectedRoute);
        assertThat(route.getCurrentDistance()).isEqualTo(expectedRoute.getCurrentDistance());
    }
}