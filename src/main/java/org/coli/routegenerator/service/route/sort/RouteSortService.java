package org.coli.routegenerator.service.route.sort;

import org.coli.routegenerator.exception.RTException;
import org.coli.routegenerator.service.route.Route;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;
import static java.util.Comparator.comparing;
import static org.coli.routegenerator.data.Coordinates.coordinates;

@Service
public class RouteSortService {

    /**
     * Sorts the routes in the order they should be displayed at each new request. Algo: the next route is the most
     * remote route from the two last ones, in the sense of the distance between the routes center, being the average of
     * coordinates.
     */
    public List<Route> rtSort2(List<Route> routes) {
        routes.forEach(this::computeCenter);
        return sort(routes);
    }

    List<Route> sort(List<Route> routes) {
        List<Route> sortedRoutes = new ArrayList<>();
        Route routeOneAgo = routes.get(0);
        Route routeTwoAgo;
        sortedRoutes.add(routeOneAgo);
        List<Route> remainingRoutes;
        int counter = 0;
        while (sortedRoutes.size() < routes.size()) {
            remainingRoutes = routes.stream().filter(r -> !sortedRoutes.contains(r)).toList();
            routeTwoAgo = sortedRoutes.get(counter);
            routeOneAgo = getFarthestRouteFrom2(routeOneAgo, routeTwoAgo, remainingRoutes);
            sortedRoutes.add(routeOneAgo);
            if (sortedRoutes.size() > 2) counter++;
        }
        return sortedRoutes;
    }

    Double distanceBetweenCentersOf(Route route1, Route route2) {
        double latsSquared = Math.pow((route1.getCenterLat() - route2.getCenterLat()), 2);
        double lngsSquared = Math.pow((route1.getCenterLng() - route2.getCenterLng()), 2);
        return Math.sqrt(latsSquared + lngsSquared);
    }

    Route getFarthestRouteFrom2(Route routeOneAgo, Route routeTwoAgo, List<Route> routes) {
        return routes.stream()
                     .max(comparing(r -> distanceBetweenCentersOf(r, routeOneAgo)
                             + distanceBetweenCentersOf(r, routeTwoAgo)))
                     .orElseThrow(() -> new RTException("Problem when comparing routes"));
    }

    void computeCenter(Route route) {
        List<String> lats = new ArrayList<>();
        List<String> lngs = new ArrayList<>();
        route.stream()
             .limit(route.size() - 1L) // skip arrival (starting point)
             .skip(1) // skip starting point
             .map(point -> coordinates().getOrException(point.label())
                                        .split(","))
             .forEach(coordsString -> {
                 lats.add(coordsString[0]);
                 lngs.add(coordsString[1]);
             });
        route.setCenterLat(averageOf(lats));
        route.setCenterLng(averageOf(lngs));
    }

    private double averageOf(List<String> stringCoords) {
        return stringCoords.stream()
                           .mapToDouble(Double::parseDouble)
                           .average()
                           .orElse(NaN);
    }
}
