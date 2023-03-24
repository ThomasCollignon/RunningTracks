package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    private Set<String> fileContent;

    @Test
    void excludeRoutesFromFile() {
        Set<String> result = Utils.excludeRoutesFromFile("excludeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("L - C")
                          .hasSize(2);
    }

    @Test
    void includeRoutesFromFile() {
        Set<String> result = Utils.includeRoutesFromFile("includeRoutes.txt");
        assertThat(result).contains("C - L")
                          .contains("Sablière - Tumuli")
                          .hasSize(2);
    }

    @Test
    void readFileAndConsumeLines() {
        Consumer<Stream<String>> consumer = stream -> fileContent = stream.collect(toSet());
        Utils.readFileAndConsumeLines("anyData.txt", consumer);
        assertThat(fileContent).isEqualTo(new HashSet<>(singleton("foo bar")));

        assertThrows(RTException.class, () -> Utils.readFileAndConsumeLines("I don't exist", consumer));
    }

    @Test
    void reverseRoute() {
        assertThat(Utils.reverseRoute("foo - bar")).isEqualTo("bar - foo");
    }

    @Test
    void toListOfCoordinates() {
        assertThat(Utils.toListOfCoordinates(SHORT_ROUTE_LIBERSART)).isEqualTo(SHORT_ROUTE_LIBERSART_COORDINATES);
    }
}
