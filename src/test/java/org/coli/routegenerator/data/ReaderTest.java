package org.coli.routegenerator.data;

import org.coli.routegenerator.exception.RTException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReaderTest {

    private Set<String> fileContent;

    @Test
    void readFileAndConsumeLines() {
        Consumer<Stream<String>> consumer = stream -> fileContent = stream.collect(toSet());
        Reader.readFileAndConsumeLines("anyData.txt", consumer);
        assertThat(fileContent).isEqualTo(new HashSet<>(singleton("foo bar")));

        assertThrows(RTException.class, () -> Reader.readFileAndConsumeLines("I don't exist", consumer));
    }
}
