package org.coli.routegenerator.service.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coli.routegenerator.data.PointsLoader;
import org.coli.routegenerator.data.PointsMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // or @Autowired on the properties
@Slf4j
class PartialRouteCaseIT {

    private final PartialRoutesService partialRoutesService;

    private final PointsLoader pointsLoader;

    @Test
    void debug() {
        PointsMap pointsChastre = pointsLoader.getPointsMapChastre();
        Route route = new Route(pointsChastre);
        route.add(pointsChastre.get("Tombes"));
        route.add(pointsChastre.get("Delvaux"));
        route.add(pointsChastre.get("Gotteaux"));
        route.add(pointsChastre.get("Bouvier"));
        route.add(pointsChastre.get("Monument"));
        route.add(pointsChastre.get("Cortil4"));
        route.add(pointsChastre.get("SG2"));
        route.add(pointsChastre.get("SG1"));
        route.add(pointsChastre.get("Croix-St-Géry"));
        route.add(pointsChastre.get("Champs6"));
        route.add(pointsChastre.get("Champs7"));
        route.add(pointsChastre.get("Champs3"));
        route.add(pointsChastre.get("Champs1"));
        route.add(pointsChastre.get("Champs2"));
        route.add(pointsChastre.get("Tchatche"));
        route.add(pointsChastre.get("C2"));
        route.add(pointsChastre.get("C3"));
        route.add(pointsChastre.get("Tunnel"));
        route.add(pointsChastre.get(route.getStartingPointLabel()));

        Route routeOut = partialRoutesService.applyPartialRoutesTo(route);
        String expectedRouteString = "Commune-Chastre - Tombes - Cortil4 - SG2 - Champs7 - Tunnel - Commune-Chastre";
        assertEquals(expectedRouteString, routeOut.toString());
    }
}
