package org.coli.routegenerator.service.route;

public class RouteHelper {

    static final String ROUTE_SEPARATOR = " - ";

    private RouteHelper() {
    }

    static String reverseRoute(String route) {
        String[] elements = route.split(ROUTE_SEPARATOR);
        return elements[1] + ROUTE_SEPARATOR + elements[0];
    }
}
