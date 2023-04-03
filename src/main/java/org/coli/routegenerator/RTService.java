package org.coli.routegenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;
import static org.coli.routegenerator.Constants.PROFILE_PROD;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;

@Service
@Slf4j
@RequiredArgsConstructor
public class RTService {

    private final RouteFinder routeFinder;

    private final Map<String, CachedRoutes> routesCache = new HashMap<>();

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    /**
     * Load cache at startup, for 8 to 20 km
     */
    @EventListener
    public void loadCacheChastre(ContextRefreshedEvent event) {
        final String zone = RUN_ZONE_CHASTRE;
        log.debug("Appease SonarLint by using the parameter event " + event);
        if (PROFILE_PROD.equals(activeProfiles)) {
            StopWatch cacheInitStopWatch = new StopWatch();
            cacheInitStopWatch.start();
            IntStream.range(8, 20).forEach(distanceKm -> findRoutesAndFillCache(distanceKm, zone));
            cacheInitStopWatch.stop();
            log.info("Cache initialized for zone" + zone
                             + ", it took " + cacheInitStopWatch.getTotalTimeSeconds() + " seconds.");
        }
    }

    /**
     * Returns a route in another direction than the previously returned one. Algo: the returned route is the most
     * remote route from the last one, in the sense of the distance between the routes center, being the average of
     * coordinates.
     */
    List<String> getAnotherRoute(int distanceKm, String runZone) {
        String cacheKey = buildCacheKey(distanceKm, runZone);
        if (routesCache.containsKey(cacheKey)) {
            return Utils.toListOfCoordinates(routesCache.get(cacheKey).next());
        }
        findRoutesAndFillCache(distanceKm, runZone);
        return getAnotherRoute(distanceKm, runZone);
    }

    private void findRoutesAndFillCache(int distanceKm, String runZone) {
        String cacheKey = buildCacheKey(distanceKm, runZone);
        List<Route> routes = routeFinder.findRoutes(runZone, distanceKm * 1000, Options.builder().build());
        if (routes.isEmpty()) {
            throw new RTException("No route found");
        }
        shuffle(routes);
        routesCache.put(cacheKey, new CachedRoutes(Utils.rtSort2(routes)));
    }

    private String buildCacheKey(int distanceKm, String runZone) {
        return runZone + "-" + distanceKm;
    }
}