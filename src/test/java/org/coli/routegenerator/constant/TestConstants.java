package org.coli.routegenerator.constant;

import org.coli.routegenerator.data.PointsMap;
import org.coli.routegenerator.model.Point;
import org.coli.routegenerator.service.route.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.coli.routegenerator.constant.Constant.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.constant.Constant.STARTING_POINT_LIBERSART;

public class TestConstants {

    public final static List<String> SHORT_ROUTE_LIBERSART_COORDINATES = asList("50.65292, 4.71825",
                                                                                "50.64998, 4.71306",
                                                                                "50.65312, 4.71111",
                                                                                "50.65459, 4.72151",
                                                                                "50.65292, 4.71825");

    public final static List<String> LONG_ROUTE_LIBERSART_COORDINATES = asList("50.65292, 4.71825",
                                                                               "50.64998, 4.71306",
                                                                               "50.65312, 4.71111",
                                                                               "50.65427, 4.70800",
                                                                               "50.65529, 4.70530",
                                                                               "50.66302, 4.71578",
                                                                               "50.65459, 4.72151",
                                                                               "50.65292, 4.71825");

    public final static double ONE_METER = 0.00001d; // approx

    public final static List<String> PARTIAL_ROUTE_LIBERSART_VALID = asList("Tumuli", "Sablière", "C", "L");

    public final static List<String> PARTIAL_ROUTE_LIBERSART_INVALID = asList("Daix", "C", "Sablière");

    public final static Point point_c = new Point("C"); // long route

    public final static Point point_sabliere = new Point("Sablière"); // long route

    private final static Point point_home = new Point("Home");

    private final static Point point_tumuli = new Point("Tumuli");

    private final static Point point_anglee = new Point("Anglée");

    private final static Point point_daix = new Point("Daix");

    private final static Point point_l = new Point("L"); // long route

    public final static PointsMap TEST_POINTS_LIBERSART = initPointsMapLibersart();

    public final static Route LONG_ROUTE_LIBERSART = initLongRouteLibersart();

    public final static Route SHORT_ROUTE_LIBERSART = initShortRouteLibersart();

    private final static Point point_commune_chastre = new Point("Commune-Chastre");

    private final static Point point_c1 = new Point("C1");

    private final static Point point_c3 = new Point("C3");

    private final static Point point_tunnel = new Point("Tunnel");

    private final static Point point_rond_point = new Point("Rond-Point");

    public final static PointsMap TEST_POINTS_CHASTRE = initPointsMapChastre();

    public final static Route ROUTE_CHASTRE = initRouteChastre();

    public static Route initLongRouteLibersart() {
        Route longRouteLibersart = new Route(TEST_POINTS_LIBERSART);
        longRouteLibersart.add(point_anglee);
        longRouteLibersart.add(point_daix);
        longRouteLibersart.add(point_l);
        longRouteLibersart.add(point_c);
        longRouteLibersart.add(point_sabliere);
        longRouteLibersart.add(point_tumuli);
        longRouteLibersart.add(point_home);
        return longRouteLibersart;
    }

    private static PointsMap initPointsMapChastre() {
        point_commune_chastre.linkedPointsDistance().put(point_rond_point, 550);
        point_commune_chastre.linkedPointsDistance().put(point_c1, 300);
        point_c1.linkedPointsDistance().put(point_commune_chastre, 300);
        point_rond_point.linkedPointsDistance().put(point_commune_chastre, 550);
        point_c1.linkedPointsDistance().put(point_c3, 270);
        point_c3.linkedPointsDistance().put(point_c1, 270);
        point_c3.linkedPointsDistance().put(point_tunnel, 600);
        point_tunnel.linkedPointsDistance().put(point_c3, 600);
        point_tunnel.linkedPointsDistance().put(point_rond_point, 1100);
        point_rond_point.linkedPointsDistance().put(point_tunnel, 1100);

        Map<String, Point> mapTmp = new HashMap<>() {{
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
        point_home.linkedPointsDistance().put(point_tumuli, 290);
        point_home.linkedPointsDistance().put(point_anglee, 600);
        point_tumuli.linkedPointsDistance().put(point_home, 290);
        point_tumuli.linkedPointsDistance().put(point_daix, 950);
        point_tumuli.linkedPointsDistance().put(point_sabliere, 1000);
        point_anglee.linkedPointsDistance().put(point_home, 600);
        point_anglee.linkedPointsDistance().put(point_daix, 400);
        point_daix.linkedPointsDistance().put(point_anglee, 400);
        point_daix.linkedPointsDistance().put(point_tumuli, 950);
        point_daix.linkedPointsDistance().put(point_l, 260);
        point_c.linkedPointsDistance().put(point_sabliere, 1100);
        point_c.linkedPointsDistance().put(point_l, 220);
        point_l.linkedPointsDistance().put(point_c, 220);
        point_l.linkedPointsDistance().put(point_daix, 260);
        point_sabliere.linkedPointsDistance().put(point_c, 1100);
        point_sabliere.linkedPointsDistance().put(point_tumuli, 1000);

        Map<String, Point> mapTmp = new HashMap<>() {{
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
        Route routeChastre = new Route(TEST_POINTS_CHASTRE);
        routeChastre.add(point_c1);
        routeChastre.add(point_c3);
        routeChastre.add(point_tunnel);
        routeChastre.add(point_rond_point);
        routeChastre.add(point_commune_chastre);
        routeChastre.setCenterLat(50.61168);
        routeChastre.setCenterLng(4.64163);
        return routeChastre;
    }

    private static Route initShortRouteLibersart() {
        Route shortRouteLibersart = new Route(TEST_POINTS_LIBERSART);
        shortRouteLibersart.add(point_anglee);
        shortRouteLibersart.add(point_daix);
        shortRouteLibersart.add(point_tumuli);
        shortRouteLibersart.add(point_home);
        shortRouteLibersart.setCenterLat(50.65256);
        shortRouteLibersart.setCenterLng(4.71522);
        return shortRouteLibersart;
    }
}
