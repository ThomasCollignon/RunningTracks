package org.coli.routegenerator.service;

import org.coli.routegenerator.persistence.RouteDB;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.coli.routegenerator.service.route.RouteFinderService;
import org.coli.routegenerator.service.route.RouteIndexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.LONG_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.LONG_ROUTE_LIBERSART_COORDINATES;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RouteServiceTest {

    final String ROUTE_KEY = "Libersart-10";
    @InjectMocks
    private RouteService routeService;
    @Mock
    private RouteFinderService routeFinderServiceMock;
    @Mock
    private RouteIndexService routeIndexServiceMock;
    @Mock
    private RouteDBRepository routeDBRepositoryMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getAnotherRoute_notInDB() {
        when(routeFinderServiceMock.findAndSortRoutes(eq(RUN_ZONE_LIBERSART), anyInt(), any()))
                .thenReturn(asList(SHORT_ROUTE_LIBERSART, LONG_ROUTE_LIBERSART));
        when(routeDBRepositoryMock.findById(any())).thenReturn(Optional.empty());
        when(routeDBRepositoryMock.save(any())).thenReturn(new RouteDB(ROUTE_KEY,
                                                                       asList(SHORT_ROUTE_LIBERSART_COORDINATES,
                                                                              LONG_ROUTE_LIBERSART_COORDINATES),
                                                                       0));
        when(routeIndexServiceMock.getRouteIndex(any())).thenReturn(0);
        assertEquals(SHORT_ROUTE_LIBERSART_COORDINATES, routeService.getAnotherRouteCoords(10, RUN_ZONE_LIBERSART));

        verify(routeFinderServiceMock).findAndSortRoutes(eq(RUN_ZONE_LIBERSART), eq(10000), any());
    }
}