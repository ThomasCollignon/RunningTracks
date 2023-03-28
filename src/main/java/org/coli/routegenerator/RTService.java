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

    private final Map<String, List<Route>> routesCache = new HashMap<>();

    List<String> getRandomRoute(int distanceKm, String runZone) {
        String cacheKey = runZone + "-" + distanceKm;
        if (routesCache.containsKey(cacheKey)) {
            List<Route> routes = routesCache.get(cacheKey);
            shuffle(routes);
            return Utils.toListOfCoordinates(routes.get(0));
        }
        List<Route> routes = routeFinder.findRoutes(runZone, distanceKm * 1000, Options.builder()
                                                                                       .build());
        if (routes.isEmpty()) {
            throw new RTException("No route found");
        }
        routesCache.put(cacheKey, routes);
        shuffle(routes);
        return Utils.toListOfCoordinates(routes.get(0));
    }
}