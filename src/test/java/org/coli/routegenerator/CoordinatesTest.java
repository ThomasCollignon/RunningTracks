package org.coli.routegenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordinatesTest {

    public static final String COORD_HOME = "50.65292179181078, 4.718251399671132";
    public static final String COORD_TUMULI = "50.65458508553609, 4.721509615286826";
    public static final String COORD_COMMUNE = "50.61089648281807, 4.6435415396390205";
    public static final String COORD_J = "50.644979988609876, 4.72050056810488";
    public static final String COORD_X = "50.645449716673504, 4.716275789668594";
    public static final String COORD_DE_FRUTOS = "50.64739226124664, 4.712199177409909";
    public static final String COORD_ANGLEE = "50.64997714337066, 4.713064208095772";

    @Test
    public void toRouteCoordinates() {
        final String routeString = "Home - Tumuli - Commune - J - X - De-Frutos - Anglée - Home";
        String expected = "";
        expected += "'" + COORD_HOME + "',\n";
        expected += "'" + COORD_TUMULI + "',\n";
        expected += "'" + COORD_COMMUNE + "',\n";
        expected += "'" + COORD_J + "',\n";
        expected += "'" + COORD_X + "',\n";
        expected += "'" + COORD_DE_FRUTOS + "',\n";
        expected += "'" + COORD_ANGLEE + "',\n";
        expected += "'" + COORD_HOME + "'";
        Assertions.assertThat(Coordinates.toRouteCoordinates(routeString))
                  .isEqualTo(expected);
    }
}
