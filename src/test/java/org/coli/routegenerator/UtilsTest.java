package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    void includeRoutesFromFile() throws IOException {
        Set<String> result = Utils.includeRoutesFromFile("includeRoute.txt");
        assertThat(result).contains("C - L")
                          .contains("Sablière - Tumuli")
                          .hasSize(2);
    }

    @Test
    void excludeRoutesFromFile() throws IOException {
        Set<String> result = Utils.excludeRoutesFromFile("excludeRoute.txt");
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

    }
}
