package org.coli.routegenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;

@Service
@RequiredArgsConstructor
public class RTService {

    private final RouteFinder routeFinder;

    private final Map<String, CachedRoutes> routesCache = new HashMap<>();

    /**
     * Returns a route in another direction than the previously returned one. Algo: the returned route is the most
     * remote route from the last one, in the sense of the distance between the routes center, being the average of
     * coordinates.
     */
    List<String> getAnotherRoute(int distanceKm, String runZone) {
        String cacheKey = runZone + "-" + distanceKm;
        if (routesCache.containsKey(cacheKey)) {
            return Utils.toListOfCoordinates(routesCache.get(cacheKey).next());
        }
        List<Route> routes = routeFinder.findRoutes(runZone, distanceKm * 1000, Options.builder()
                                                                                       .build());
        if (routes.isEmpty()) {
            throw new RTException("No route found");
        }
        shuffle(routes);
        routesCache.put(cacheKey, new CachedRoutes(Utils.rtSort2(routes)));
        return getAnotherRoute(distanceKm, runZone);
    }
}