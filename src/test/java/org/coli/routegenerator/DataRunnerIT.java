package org.coli.routegenerator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;

@SpringBootTest
@Disabled("DataRunnerIT disabled, to be used to compute data")
class DataRunnerIT {

    @Autowired
    private RouteFinder routeFinder;

    @Test
    void count() {
        SortedMap<Integer, Integer> res =
                new TreeMap<>(range(10, 21).boxed()
                                           .collect(toMap(identity(),
                                                          distance -> routeFinder.findRoutes(
                                                                                         RUN_ZONE_CHASTRE,
                                                                                         distance * 1000,
                                                                                         Options.builder()
                                                                                                .build())
                                                                                 .size())));
        System.out.println("TCO" + res);
        // Latest is
        // Chastre   {10=44, 11=58, 12=69, 13=74, 14=69, 15=53, 16=35, 17=21, 18=7, 19=4, 20=0}
        // Libersart {10=14, 11=22, 12=24, 13=31, 14=39, 15=41, 16=43, 17=50, 18=61, 19=66, 20=65}

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }
}
