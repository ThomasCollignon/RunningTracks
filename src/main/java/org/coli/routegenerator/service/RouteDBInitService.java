package org.coli.routegenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.service.RouteService.buildRouteKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteDBInitService {

    private static final int FROM = 8;

    private static final int TO = 20;

    private final RouteService routeService;

    private final RouteDBRepository routeDBRepository;

    /**
     * Done here because I can't force persistence in tests, even with @Transactional and @Rollback(false).
     */
    @EventListener(ContextRefreshedEvent.class)
    public void fillDBAtStartup() {
        final String runZone = RUN_ZONE_CHASTRE;

        List<Integer> distancesThatMissRecord =
                IntStream.range(FROM, TO + 1)
                         .filter(distanceKm -> !routeDBRepository.existsById(buildRouteKey(distanceKm, runZone)))
                         .boxed()
                         .toList();
        if (distancesThatMissRecord.isEmpty()) return;
        StopWatch fillDBStopWatch = new StopWatch();
        fillDBStopWatch.start();
        distancesThatMissRecord.forEach(distanceKm -> routeService.getAnotherRouteWhenEmptyDB(distanceKm,
                                                                                              runZone,
                                                                                              buildRouteKey(distanceKm,
                                                                                                            runZone)));
        fillDBStopWatch.stop();
        log.info("DB filled for zone" + runZone + ", it took " + fillDBStopWatch.getTotalTimeSeconds() + " seconds.");
    }
}
