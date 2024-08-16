package org.coli.routegenerator.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.coli.routegenerator.exception.RTException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
class Reader {

    private Reader() {
    }

    /**
     * @param fileName A file in /resources directory
     * @param consumer A consumer to run on each line of the file
     */
    static void readFileAndConsumeLines(String fileName, Consumer<Stream<String>> consumer) {
        consumer.accept(readFileFromResourcesDirectory(fileName));
    }

    private static Stream<String> readFileFromResourcesDirectory(String filename) {
        log.info("reading file resources/" + filename);
        String fileString;
        try (InputStream inputStream = Reader.class.getClassLoader()
                                                   .getResourceAsStream(filename)) {
            if (inputStream == null) throw new RTException("Can't find file " + filename + " in /resources.");
            fileString = IOUtils.toString(inputStream, UTF_8);
        } catch (Exception e) {
            throw new RTException("Issue reading or parsing file " + filename + "\n" + e.getMessage());
        }
        return Arrays.stream(fileString.split("\n")).map(s -> s.replace("\r", ""));
    }
}
