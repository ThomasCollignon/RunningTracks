package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.withPrecision;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.coli.routegenerator.TestConstants.ONE_METER;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;

class RouteTest {

    @Test
    void computeCenter() {
        SHORT_ROUTE_LIBERSART.computeCenter();
        assertThat(SHORT_ROUTE_LIBERSART.getCenterLat()).isEqualTo(50.652560067d, withPrecision(ONE_METER));
        assertThat(SHORT_ROUTE_LIBERSART.getCenterLng()).isEqualTo(4.715228808d, withPrecision(ONE_METER));
    }
}
