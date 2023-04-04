package org.coli.routegenerator.service.cache;

import lombok.EqualsAndHashCode;
import org.coli.routegenerator.service.route.Route;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
class CachedRoutes extends ArrayList<Route> {

    private int counter = 0;

    CachedRoutes(List<Route> routes) {
        super(routes);
    }

    Route next() {
        Route result;
        try {
            result = this.get(counter);
        } catch (IndexOutOfBoundsException e) {
            counter = 0;
            result = this.get(counter);
        }
        counter++;
        return result;
    }
}
