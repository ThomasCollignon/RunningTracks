package org.coli.routegenerator.service.route;

import lombok.RequiredArgsConstructor;
import org.coli.routegenerator.persistence.RouteDB;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RouteIndexService {

    private final RouteDBRepository routeDBRepository;

    // If absent, this routeKey hasn't been asked yet, and so the index should be random
    private final Set<String> routeKeyFlag = new HashSet<>();

    public int getRouteIndex(RouteDB routeDB) {
        int index;
        if (routeKeyFlag.contains(routeDB.getRouteKey())) index = routeDB.getCurrentIndex();
        else {
            index = ThreadLocalRandom.current().nextInt(0, routeDB.getRoutes().size());
            routeKeyFlag.add(routeDB.getRouteKey());
        }
        incrementIndex(routeDB);
        return index;
    }

    // For UTs
    void clearRouteKeyFlag() {
        routeKeyFlag.clear();
    }

    private void incrementIndex(RouteDB routeDB) {
        long routesListSize = routeDB.getRoutes().size();
        int currentIndex = routeDB.getCurrentIndex();
        routeDB.setCurrentIndex(currentIndex < routesListSize - 1 ? currentIndex + 1 : 0);
        routeDBRepository.save(routeDB);
    }
}
