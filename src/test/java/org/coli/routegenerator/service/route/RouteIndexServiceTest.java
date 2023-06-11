package org.coli.routegenerator.service.route;

import org.coli.routegenerator.persistence.RouteDB;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.TestConstants.LONG_ROUTE_LIBERSART_COORDINATES;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RouteIndexServiceTest {

    final String ROUTE_KEY = "Libersart-10";

    @InjectMocks
    private RouteIndexService routeIndexService;
    @Mock
    private RouteDBRepository routeDBRepositoryMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getRouteIndex() {
        when(routeDBRepositoryMock.save(any())).thenReturn(new RouteDB());

        final RouteDB routeDb = new RouteDB(ROUTE_KEY, asList(SHORT_ROUTE_LIBERSART_COORDINATES,
                                                              LONG_ROUTE_LIBERSART_COORDINATES,
                                                              SHORT_ROUTE_LIBERSART_COORDINATES,
                                                              LONG_ROUTE_LIBERSART_COORDINATES,
                                                              SHORT_ROUTE_LIBERSART_COORDINATES,
                                                              LONG_ROUTE_LIBERSART_COORDINATES),
                                            0);

        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();
        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();
        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();
        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();
        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();
        assertThat(routeIndexService.getRouteIndex(routeDb)).isBetween(0, 5);
        routeIndexService.clearRouteKeyFlag();

        int firstRandomIndex = routeIndexService.getRouteIndex(routeDb);
        routeDb.setCurrentIndex(firstRandomIndex + 1);
        assertThat(firstRandomIndex).isBetween(0, 5);
        assertThat(routeIndexService.getRouteIndex(routeDb)).isEqualTo(firstRandomIndex + 1);
    }
}