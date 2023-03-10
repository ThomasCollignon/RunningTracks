package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_COORDINATES;

class UtilsTest {

    @Test
    void includeRoutesFromFile() throws IOException {
        Set<String> result = Utils.includeRoutesFromFile("includeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("Sablière - Tumuli")
                          .hasSize(2);
    }

    @Test
    void excludeRoutesFromFile() throws IOException {
        Set<String> result = Utils.excludeRoutesFromFile("excludeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("L - C")
                          .hasSize(2);
    }

    @Test
    void reverseRoute() {
        assertThat(Utils.reverseRoute("foo - bar")).isEqualTo("bar - foo");
    }

    @Test
    void toListOfCoordinates() {
        assertThat(Utils.toListOfCoordinates(SHORT_ROUTE)).isEqualTo(SHORT_ROUTE_COORDINATES);
    }
}
