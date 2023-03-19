package org.coli.routegenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE;
import static org.coli.routegenerator.TestConstants.TEST_POINTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    void getRandomRoute() {
        when(routeFinderMock.findRoutes(any(), anyInt(), any())).thenReturn(singletonList(SHORT_ROUTE));
        rtService.getRandomRoute(10, TEST_POINTS);
        rtService.getRandomRoute(20, TEST_POINTS);
        verify(routeFinderMock, times(2)).findRoutes(any(), anyInt(), any());
        rtService.getRandomRoute(10, TEST_POINTS);
        verifyNoMoreInteractions(routeFinderMock);
    }

    @Test
    void getRandomRoute_noRoute() {
        when(routeFinderMock.findRoutes(any(), anyInt(), any())).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> rtService.getRandomRoute(10, TEST_POINTS)).isInstanceOf(RTException.class);
    }
}