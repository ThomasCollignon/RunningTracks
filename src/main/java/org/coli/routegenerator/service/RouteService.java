package org.coli.routegenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.mapper.Mapper;
import org.coli.routegenerator.persistence.RouteDB;
import org.coli.routegenerator.persistence.RouteDBRepository;
import org.coli.routegenerator.service.route.Options;
import org.coli.routegenerator.service.route.Route;
import org.coli.routegenerator.service.route.RouteFinderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final RouteFinderService routeFinderService;

    private final RouteDBRepository routeDBRepository;

    static String buildRouteKey(int distanceKm, String runZone) {
        return runZone + "-" + distanceKm;
    }

    public List<String> getAnotherRouteCoords(int distanceKm, String runZone) {
        String routeKey = buildRouteKey(distanceKm, runZone);
        RouteDB routeDB = routeDBRepository.findById(routeKey)
                                           .orElseGet(() -> getAnotherRouteWhenEmptyDB(distanceKm, runZone, routeKey));
        List<String> routeCoords = routeDB.getRoutes().get(routeDB.getCurrentIndex());
        incrementIndex(routeDB);
        return routeCoords;
    }

    RouteDB getAnotherRouteWhenEmptyDB(int distanceKm, String runZone, String routeKey) {
        log.info("BUG HERE IF THIS HAPPENS AT RUNTIME. Nothing in DB for route key " + routeKey);
        List<Route> routes =
                routeFinderService.findAndSortRoutes(runZone, distanceKm * 1000, Options.builder().build());
        List<List<String>> routesCoordinates = routes.stream()
                                                     .map(Mapper::toListOfCoordinates)
                                                     .toList();
        return routeDBRepository.save(new RouteDB(routeKey, routesCoordinates, 0));
    }

    private void incrementIndex(RouteDB routeDB) {
        long routesListSize = routeDB.getRoutes().size();
        int currentIndex = routeDB.getCurrentIndex();
        routeDB.setCurrentIndex(currentIndex < routesListSize - 1 ? currentIndex + 1 : 0);
        routeDBRepository.save(routeDB);
    }
}
