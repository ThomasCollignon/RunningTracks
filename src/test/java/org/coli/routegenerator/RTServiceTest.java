package org.coli.routegenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.Constants.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.TestConstants.ROUTE_CHASTRE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RTServiceTest {

    @InjectMocks
    private RTService rtService;

    @Mock
    private RouteFinder routeFinderMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getRandomRoute_withCache() {
        when(routeFinderMock.findRoutes(eq(RUN_ZONE_LIBERSART), anyInt(), any()))
                .thenReturn(singletonList(SHORT_ROUTE_LIBERSART));
        when(routeFinderMock.findRoutes(eq(RUN_ZONE_CHASTRE), anyInt(), any()))
                .thenReturn(singletonList(ROUTE_CHASTRE));

        rtService.getRandomRoute(10, RUN_ZONE_LIBERSART);
        rtService.getRandomRoute(10, RUN_ZONE_CHASTRE);
        rtService.getRandomRoute(20, RUN_ZONE_LIBERSART);
        verify(routeFinderMock, times(3)).findRoutes(any(), anyInt(), any());
        rtService.getRandomRoute(10, RUN_ZONE_LIBERSART);
        rtService.getRandomRoute(10, RUN_ZONE_CHASTRE);
        verifyNoMoreInteractions(routeFinderMock);
        clearInvocations(routeFinderMock);
        rtService.getRandomRoute(20, RUN_ZONE_CHASTRE);
        verify(routeFinderMock).findRoutes(eq(RUN_ZONE_CHASTRE), eq(20000), any());
    }

    @Test
    void getRandomRoute_noRoute() {
        when(routeFinderMock.findRoutes(any(), anyInt(), any())).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> rtService.getRandomRoute(10, RUN_ZONE_LIBERSART)).isInstanceOf(RTException.class);
    }
}