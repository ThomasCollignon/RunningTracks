package org.coli.routegenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.coli.routegenerator.Constants.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.Constants.STARTING_POINT_LIBERSART;

public class TestConstants {

    final static double ONE_METER = 0.00001d; // approx

    // Libersart
    final static Point point_home = new Point("Home");
    final static Point point_tumuli = new Point("Tumuli");
    final static Point point_anglee = new Point("Anglée");
    final static Point point_daix = new Point("Daix");
    final static Point point_c = new Point("C"); // long route
    final static Point point_l = new Point("L"); // long route
    final static Point point_sabliere = new Point("Sablière"); // long route

    final static PointsMap TEST_POINTS_LIBERSART = initPointsMapLibersart();
    final static Route LONG_ROUTE_LIBERSART = initLongRouteLibersart();
    final static Route SHORT_ROUTE_LIBERSART = initShortRouteLibersart();

    final static List<String> SHORT_ROUTE_LIBERSART_COORDINATES =
            asList("50.65292179181078, 4.718251399671132",
                   "50.64997714337066, 4.713064208095772",
                   "50.65311797305349, 4.711112599509838",
                   "50.65458508553609, 4.721509615286826",
                   "50.65292179181078, 4.718251399671132");

    // Chastre
    final static Point point_commune_chastre = new Point("Commune-Chastre");
    final static Point point_c1 = new Point("C1");
    final static Point point_c3 = new Point("C3");
    final static Point point_tunnel = new Point("Tunnel");
    final static Point point_rond_point = new Point("Rond-Point");

    final static PointsMap TEST_POINTS_CHASTRE = initPointsMapChastre();
    final static Route ROUTE_CHASTRE = initRouteChastre();

    private static Route initLongRouteLibersart() {
        Route longRoute = new Route(TEST_POINTS_LIBERSART);
        longRoute.add(point_anglee);
        longRoute.add(point_daix);
        longRoute.add(point_l);
        longRoute.add(point_c);
        longRoute.add(point_sabliere);
        longRoute.add(point_tumuli);
        longRoute.add(point_home);
        return longRoute;
    }

    private static PointsMap initPointsMapChastre() {
        point_commune_chastre.getLinkedPoints()
                             .put(point_rond_point, 550);
        point_commune_chastre.getLinkedPoints()
                             .put(point_c1, 300);
        point_c1.getLinkedPoints()
                .put(point_commune_chastre, 300);
        point_rond_point.getLinkedPoints()
                        .put(point_commune_chastre, 550);
        point_c1.getLinkedPoints()
                .put(point_c3, 270);
        point_c3.getLinkedPoints()
                .put(point_c1, 270);
        point_c3.getLinkedPoints()
                .put(point_tunnel, 600);
        point_tunnel.getLinkedPoints()
                    .put(point_c3, 600);
        point_tunnel.getLinkedPoints()
                    .put(point_rond_point, 1100);
        point_rond_point.getLinkedPoints()
                        .put(point_tunnel, 1100);


        Map<String, Point> mapTmp = new HashMap<String, Point>() {{
            put("Commune-Chastre", point_commune_chastre);
            put("C1", point_c1);
            put("C3", point_c3);
            put("Rond-Point", point_rond_point);
            put("Tunnel", point_tunnel);
        }};

        PointsMap pointsMapTmp = new PointsMap();
        pointsMapTmp.putAll(mapTmp);
        pointsMapTmp.setStartingPointLabel(STARTING_POINT_CHASTRE);

        return pointsMapTmp;
    }

    private static PointsMap initPointsMapLibersart() {
        point_home.getLinkedPoints()
                  .put(point_tumuli, 290);
        point_home.getLinkedPoints()
                  .put(point_anglee, 600);
        point_tumuli.getLinkedPoints()
                    .put(point_home, 290);
        point_tumuli.getLinkedPoints()
                    .put(point_daix, 950);
        point_tumuli.getLinkedPoints()
                    .put(point_sabliere, 1000);
        point_anglee.getLinkedPoints()
                    .put(point_home, 600);
        point_anglee.getLinkedPoints()
                    .put(point_daix, 400);
        point_daix.getLinkedPoints()
                  .put(point_anglee, 400);
        point_daix.getLinkedPoints()
                  .put(point_tumuli, 950);
        point_daix.getLinkedPoints()
                  .put(point_l, 260);
        point_c.getLinkedPoints()
               .put(point_sabliere, 1100);
        point_c.getLinkedPoints()
               .put(point_l, 220);
        point_l.getLinkedPoints()
               .put(point_c, 220);
        point_l.getLinkedPoints()
               .put(point_daix, 260);
        point_sabliere.getLinkedPoints()
                      .put(point_c, 1100);
        point_sabliere.getLinkedPoints()
                      .put(point_tumuli, 1000);

        Map<String, Point> mapTmp = new HashMap<String, Point>() {{
            put("Home", point_home);
            put("Tumuli", point_tumuli);
            put("Anglée", point_anglee);
            put("Daix", point_daix);
            put("C", point_c);
            put("L", point_l);
            put("Sablière", point_sabliere);
        }};

        PointsMap pointsMapTmp = new PointsMap();
        pointsMapTmp.putAll(mapTmp);
        pointsMapTmp.setStartingPointLabel(STARTING_POINT_LIBERSART);

        return pointsMapTmp;
    }

    private static Route initRouteChastre() {
        Route shortRoute = new Route(TEST_POINTS_CHASTRE);
        shortRoute.add(point_c1);
        shortRoute.add(point_c3);
        shortRoute.add(point_tunnel);
        shortRoute.add(point_rond_point);
        shortRoute.add(point_commune_chastre);
        return shortRoute;
    }

    private static Route initShortRouteLibersart() {
        Route shortRoute = new Route(TEST_POINTS_LIBERSART);
        shortRoute.add(point_anglee);
        shortRoute.add(point_daix);
        shortRoute.add(point_tumuli);
        shortRoute.add(point_home);
        return shortRoute;
    }
}
