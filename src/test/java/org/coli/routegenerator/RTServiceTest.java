package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_COORDINATES;
import static org.coli.routegenerator.TestConstants.TEST_POINTS;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RTServiceTest {

    @Test
    void getRandomRoute() {
        RTService rtService = new RTService(new RouteFinder(), new HashMap<>());
        assertThat(rtService.getRandomRoute(2, TEST_POINTS)).isEqualTo(SHORT_ROUTE_COORDINATES);
        assertThrows(RTException.class, () -> rtService.getRandomRoute(20, TEST_POINTS));
    }
}