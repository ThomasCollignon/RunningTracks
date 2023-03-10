package org.coli.routegenerator;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;

@Service
public class RTService {

    private static final String STARTING_POINT_LABEL = "Commune";
    private PointsMap pointsMap;
    private final Map<String, List<Route>> routesCache = new HashMap<>();

    public List<String> getRandomRoute(int distanceKm) {
        int extraDistanceMeters = 500;
        String cacheKey = distanceKm + "-" + 500;
        if (routesCache.containsKey(cacheKey)) {
            List<Route> routes = routesCache.get(cacheKey);
            shuffle(routes);
            return Utils.toListOfCoordinates(routes.get(0));
        }
        List<Route> routes = RouteFinder.findRoutes(pointsMap, distanceKm * 1000,
                Parameters.builder()
                          .extraDistanceMeters(extraDistanceMeters)
                          .build());
        if (routes.isEmpty()) {
            throw new RuntimeException("No route found");
        }
        routesCache.put(cacheKey, routes);
        shuffle(routes);
        return Utils.toListOfCoordinates(routes.get(0));
    }

    @PostConstruct
    void loadPointsMap() {
        pointsMap = PointsLoader.load(RUN_ZONE_CHASTRE, STARTING_POINT_LABEL);
    }
}