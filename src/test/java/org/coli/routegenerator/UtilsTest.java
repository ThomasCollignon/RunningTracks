package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_COORDINATES;

class UtilsTest {

    private Set<String> fileContent;

    @Test
    void excludeRoutesFromFile() throws IOException {
        Set<String> result = Utils.excludeRoutesFromFile("excludeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("L - C")
                          .hasSize(2);
    }

    @Test
    void includeRoutesFromFile() throws IOException {
        Set<String> result = Utils.includeRoutesFromFile("includeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("Sablière - Tumuli")
                          .hasSize(2);
    }

    @Test
    void readFileAndConsumeLines() {
        Utils.readFileAndConsumeLines("src/test/resources/anyData.txt", this::streamConsumer);
        assertThat(fileContent).isEqualTo(new HashSet<>(singleton("foo bar")));

//        assertThrows(IOException.class, () -> Utils.readFileAndConsumeLines("I don't exist", this::streamConsumer));
    }

    @Test
    void reverseRoute() {
        assertThat(Utils.reverseRoute("foo - bar")).isEqualTo("bar - foo");
    }

    private void streamConsumer(Stream<String> stream) {
        fileContent = stream.collect(toSet());
    }

    @Test
    void toListOfCoordinates() {
        assertThat(Utils.toListOfCoordinates(SHORT_ROUTE)).isEqualTo(SHORT_ROUTE_COORDINATES);
    }
}
