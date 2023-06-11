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
import java.util.stream.Stream;

import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.service.RouteService.buildRouteKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteDBInitService {

    private static final int FROM = 8;

    private static final int TO = 14;

    private final RouteService routeService;

    private final RouteDBRepository routeDBRepository;

    /**
     * Done here because I can't force persistence in tests, even with @Transactional and @Rollback(false).
     */
    @EventListener(ContextRefreshedEvent.class)
    public void fillDBAtStartup() {
        Stream.of(RUN_ZONE_CHASTRE, RUN_ZONE_LIBERSART).forEach(this::fillForRunZone);
    }

    private void fillForRunZone(String runZone) {
        List<Integer> distancesThatMissRecord = IntStream.range(FROM, TO + 1)
                                                         .filter(distanceKm -> !routeDBRepository.existsById(
                                                                 buildRouteKey(distanceKm, runZone)))
                                                         .boxed().toList();
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
