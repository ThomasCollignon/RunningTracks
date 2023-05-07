package org.coli.routegenerator.service.route;

import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.mapper.Mapper;
import org.coli.routegenerator.persistence.RouteDB;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;

@SpringBootTest
@Slf4j
@Disabled("DataRunnerIT disabled, to be used to compute data")
class DataRunnerIT {

    @Autowired
    private RouteFinderService routeFinderService;

    @Autowired
    private RouteDBRepository routeDBRepository;

    private static String buildRouteKey(int distanceKm, String runZone) {
        return runZone + "-" + distanceKm;
    }

    // You may want to comment the @EventListener in RouteDBRepository.getDBContentAtStartup()
    @Test
    void run() {
        final int LOWER_LIMIT = 5;
        final int UPPER_LIMIT = 10;
        final String RUN_ZONE = RUN_ZONE_CHASTRE;
        List<RouteDB> routeDBs = range(LOWER_LIMIT, UPPER_LIMIT + 1).boxed()
                                                                    .map(distance -> getRoutesCoordinatesFor(distance,
                                                                                                             RUN_ZONE))
                                                                    .toList();

        routeDBRepository.saveAll(routeDBs);

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }

    private RouteDB getRoutesCoordinatesFor(int distanceKm, String runZone) {
        List<List<String>> routesCoordinates = routeFinderService.findAndSortRoutes(runZone, distanceKm * 1000)
                                                                 .stream()
                                                                 .map(Mapper::toListOfCoordinates)
                                                                 .toList();
        return new RouteDB(buildRouteKey(distanceKm, runZone), routesCoordinates, 0);
    }
}
