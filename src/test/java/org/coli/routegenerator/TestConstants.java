package org.coli.routegenerator;

import java.util.HashMap;
import java.util.Map;

public class TestConstants {

    static Point point_home = new Point("Home");
    static Point point_tumuli = new Point("Tumuli");
    static Point point_anglee = new Point("Anglée");
    static Point point_daix = new Point("Daix");
    static Point point_c = new Point("C");
    static Point point_l = new Point("L");
    static Point point_sabliere = new Point("Sablière");

    static PointsMap pointsMap = initPointsMap();

    private static PointsMap initPointsMap() {
        point_home.getLinkedPoints().put(point_tumuli, 290);
        point_home.getLinkedPoints().put(point_anglee, 600);
        point_tumuli.getLinkedPoints().put(point_home, 290);
        point_tumuli.getLinkedPoints().put(point_daix, 950);
        point_tumuli.getLinkedPoints().put(point_sabliere, 1000);
        point_anglee.getLinkedPoints().put(point_home, 600);
        point_anglee.getLinkedPoints().put(point_daix, 400);
        point_daix.getLinkedPoints().put(point_anglee, 400);
        point_daix.getLinkedPoints().put(point_tumuli, 950);
        point_daix.getLinkedPoints().put(point_l, 260);
        point_c.getLinkedPoints().put(point_sabliere, 1100);
        point_c.getLinkedPoints().put(point_l, 220);
        point_l.getLinkedPoints().put(point_c, 220);
        point_l.getLinkedPoints().put(point_daix, 260);
        point_sabliere.getLinkedPoints().put(point_c, 1100);
        point_sabliere.getLinkedPoints().put(point_tumuli, 1000);

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
        pointsMapTmp.setStartingPointLabel("Home");

        return pointsMapTmp;
    }
}
