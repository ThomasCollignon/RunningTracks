package org.coli.routegenerator.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.stream.IntStream;

import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheInit {

    private static final String PROFILE_PROD = "PROD";

    private final CacheService cacheService;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    /**
     * Load cache at startup
     */
    @EventListener
    public void initCache(ContextRefreshedEvent event) {
        log.debug("Appease SonarLint by using the parameter event " + event);
        if (PROFILE_PROD.equals(activeProfiles)) loadCacheChastre(8, 19);
    }

    /**
     * @param from included
     * @param to   included
     */
    void loadCacheChastre(int from, int to) {
        final String zone = RUN_ZONE_CHASTRE;
        StopWatch cacheInitStopWatch = new StopWatch();
        cacheInitStopWatch.start();
        IntStream.range(from, to + 1).forEach(distanceKm -> cacheService.findRoutesAndFillCache(distanceKm, zone));
        cacheInitStopWatch.stop();
        log.info("Cache initialized for zone" + zone
                         + ", it took " + cacheInitStopWatch.getTotalTimeSeconds() + " seconds.");
    }
}
