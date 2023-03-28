package org.coli.routegenerator;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class CachedRoutes extends ArrayList<Route> {

    private int counter = 0;

    public CachedRoutes(List<Route> routes) {
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
