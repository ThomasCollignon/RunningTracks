package org.coli.routegenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static org.coli.routegenerator.Options.DEFAULT_EXTRA_DISTANCE_METERS;

@Service
@AllArgsConstructor
public class RTService {

    private final RouteFinder routeFinder;
    private final Map<String, List<Route>> routesCache;

    List<String> getRandomRoute(int distanceKm, PointsMap pointsMap) {
        String cacheKey = distanceKm + "-" + DEFAULT_EXTRA_DISTANCE_METERS;
        if (routesCache.containsKey(cacheKey)) {
            List<Route> routes = routesCache.get(cacheKey);
            shuffle(routes);
            return Utils.toListOfCoordinates(routes.get(0));
        }
        List<Route> routes = routeFinder.findRoutes(pointsMap, distanceKm * 1000,
                                                    Options.builder()
                                                           .build());
        if (routes.isEmpty()) {
            throw new RTException("No route found");
        }
        routesCache.put(cacheKey, routes);
        shuffle(routes);
        return Utils.toListOfCoordinates(routes.get(0));
    }
}