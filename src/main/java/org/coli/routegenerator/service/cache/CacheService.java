package org.coli.routegenerator.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.exception.RTException;
import org.coli.routegenerator.service.route.Options;
import org.coli.routegenerator.service.route.Route;
import org.coli.routegenerator.service.route.RouteFinderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final RouteFinderService routeFinderService;

    private final Map<String, CachedRoutes> routesCache = new HashMap<>();

    /**
     * Returns a route in another direction than the previously returned one. Algo: the returned route is the most
     * remote route from the last one, in the sense of the distance between the routes center, being the average of
     * coordinates.
     */
    public Route getAnotherRoute(int distanceKm, String runZone) {
        String cacheKey = buildCacheKey(distanceKm, runZone);
        if (routesCache.containsKey(cacheKey)) return routesCache.get(cacheKey).next();
        findRoutesAndFillCache(distanceKm, runZone);
        return getAnotherRoute(distanceKm, runZone);
    }

    void findRoutesAndFillCache(int distanceKm, String runZone) {
        String cacheKey = buildCacheKey(distanceKm, runZone);
        List<Route> routes =
                routeFinderService.findAndSortRoutes(runZone, distanceKm * 1000, Options.builder().build());
        if (routes.isEmpty()) {
            throw new RTException("No route found");
        }
        routesCache.put(cacheKey, new CachedRoutes(routes));
    }

    private String buildCacheKey(int distanceKm, String runZone) {
        return runZone + "-" + distanceKm;
    }
}