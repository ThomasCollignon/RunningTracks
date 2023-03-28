package org.coli.routegenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.coli.routegenerator.TestConstants.LONG_ROUTE_LIBERSART;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;

class CachedRoutesTest {

    @Test
    void next() {
        CachedRoutes cachedRoutes = new CachedRoutes(asList(SHORT_ROUTE_LIBERSART, LONG_ROUTE_LIBERSART));
        Assertions.assertThat(cachedRoutes.next()).isEqualTo(SHORT_ROUTE_LIBERSART);
        Assertions.assertThat(cachedRoutes.next()).isEqualTo(LONG_ROUTE_LIBERSART);
        Assertions.assertThat(cachedRoutes.next()).isEqualTo(SHORT_ROUTE_LIBERSART);
    }
}