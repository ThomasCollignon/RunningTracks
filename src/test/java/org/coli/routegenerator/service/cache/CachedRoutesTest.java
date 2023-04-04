package org.coli.routegenerator.service.cache;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.TestConstants.LONG_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART;

class CachedRoutesTest {

    @Test
    void next() {
        CachedRoutes cachedRoutes = new CachedRoutes(asList(SHORT_ROUTE_LIBERSART, LONG_ROUTE_LIBERSART));
        assertThat(cachedRoutes.next()).isEqualTo(SHORT_ROUTE_LIBERSART);
        assertThat(cachedRoutes.next()).isEqualTo(LONG_ROUTE_LIBERSART);
        assertThat(cachedRoutes.next()).isEqualTo(SHORT_ROUTE_LIBERSART);
    }
}