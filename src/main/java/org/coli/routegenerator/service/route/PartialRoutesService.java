package org.coli.routegenerator.service.route;

import lombok.RequiredArgsConstructor;
import org.coli.routegenerator.data.PointsLoader;
import org.coli.routegenerator.model.Point;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.join;
import static org.coli.routegenerator.constant.Constant.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.service.route.RouteHelper.ROUTE_SEPARATOR;

@Service
@RequiredArgsConstructor
public class PartialRoutesService {

    private final PointsLoader pointsLoader;

    /**
     * @return a Route where useless intermediary points have been removed
     */
    Route applyPartialRoutesTo(Route route) {
        List<List<String>> partialRoutes = STARTING_POINT_CHASTRE.equals(route.getStartingPointLabel())
                                           ? pointsLoader.getPartialRoutesChastre()
                                           : pointsLoader.getPartialRoutesLibersart();
        Route routeClone = new Route(route);
        for (List<String> partialRoute : partialRoutes) {
            if (!routeClone.toString().contains(toString(partialRoute))) continue;
            partialRoute.stream()
                        .skip(1)
                        .limit(partialRoute.size() - 2L)
                        .forEach(pointLabel -> routeClone.remove(new Point(pointLabel)));
        }

        return routeClone;
    }

    private String toString(List<String> partialRoute) {
        return join(ROUTE_SEPARATOR, partialRoute);
    }
}
