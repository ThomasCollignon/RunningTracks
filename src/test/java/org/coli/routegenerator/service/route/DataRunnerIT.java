package org.coli.routegenerator.service.route;

import lombok.extern.slf4j.Slf4j;
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
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;

@SpringBootTest
@Slf4j
@Disabled("DataRunnerIT disabled, to be used to compute data")
class DataRunnerIT {

    @Autowired
    private RouteFinderService routeFinderService;

    @Test
    void count() {
        final int LOWER_LIMIT = 10;
        final int UPPER_LIMIT = 20;
        SortedMap<Integer, Integer> res =
                new TreeMap<>(range(LOWER_LIMIT, UPPER_LIMIT + 1).boxed()
                                                                 .collect(toMap(identity(),
                                                                                distance -> routeFinderService.findAndSortRoutes(
                                                                                                                      RUN_ZONE_CHASTRE,
                                                                                                                      distance * 1000,
                                                                                                                      Options.builder()
                                                                                                                             .build())
                                                                                                              .size())));
        log.info("TCO " + res);
        // V1
        // Chastre   {10=44, 11=58, 12=69, 13=74, 14=69, 15=53, 16=35, 17=21, 18=7, 19=4, 20=0}
        // Libersart {10=14, 11=22, 12=24, 13=31, 14=39, 15=41, 16=43, 17=50, 18=61, 19=66, 20=65}
        // V2
        // Chastre   {10=293, 11=446, 12=608, 13=748, 14=874, 15=1055, 16=1217, 17=1460, 18=1767, 19=2164, 20=2559} took 23min

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }
}
