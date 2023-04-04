package org.coli.routegenerator.service.cache;

import org.coli.routegenerator.exception.RTException;
import org.coli.routegenerator.service.route.RouteFinderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.ROUTE_CHASTRE;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CacheServiceTest {

    @InjectMocks
    private CacheService cacheService;

    @Mock
    private RouteFinderService routeFinderServiceMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getAnotherRoute_withCache() {
        when(routeFinderServiceMock.findAndSortRoutes(eq(RUN_ZONE_LIBERSART),
                                                      anyInt(),
                                                      any())).thenReturn(singletonList(
                SHORT_ROUTE_LIBERSART));
        when(routeFinderServiceMock.findAndSortRoutes(eq(RUN_ZONE_CHASTRE),
                                                      anyInt(),
                                                      any())).thenReturn(singletonList(ROUTE_CHASTRE));

        cacheService.getAnotherRoute(10, RUN_ZONE_LIBERSART);
        cacheService.getAnotherRoute(10, RUN_ZONE_CHASTRE);
        cacheService.getAnotherRoute(20, RUN_ZONE_LIBERSART);
        verify(routeFinderServiceMock, times(3)).findAndSortRoutes(any(), anyInt(), any());
        cacheService.getAnotherRoute(10, RUN_ZONE_LIBERSART);
        cacheService.getAnotherRoute(10, RUN_ZONE_CHASTRE);
        verifyNoMoreInteractions(routeFinderServiceMock);
        clearInvocations(routeFinderServiceMock);
        cacheService.getAnotherRoute(20, RUN_ZONE_CHASTRE);
        verify(routeFinderServiceMock).findAndSortRoutes(eq(RUN_ZONE_CHASTRE), eq(20000), any());
    }

    @Test
    void getAnotherRoute_noRoute() {
        when(routeFinderServiceMock.findAndSortRoutes(any(), anyInt(), any())).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> cacheService.getAnotherRoute(10,
                                                              RUN_ZONE_LIBERSART)).isInstanceOf(RTException.class);
    }
}