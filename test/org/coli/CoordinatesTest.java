package org.coli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.Constants.STARTING_POINT;
import static org.coli.Coordinates.toRouteCoordinates;

public class CoordinatesTest {

    final Coordinates coordinates = new Coordinates();

    public static final String COORD_HOME = "50.65292179181078, 4.718251399671132";
    public static final String COORD_TUMULI = "50.65458508553609, 4.721509615286826";
    public static final String COORD_COMMUNE = "50.651471876108474, 4.724653804004082";
    public static final String COORD_J = "50.644979988609876, 4.72050056810488";
    public static final String COORD_X = "50.645449716673504, 4.716275789668594";
    public static final String COORD_DE_FRUTOS = "50.64739226124664, 4.712199177409909";
    public static final String COORD_ANGLEE = "50.64997714337066, 4.713064208095772";

    @Test
    public void toCoordinatesTest() {
        assertThat(coordinates.toCoordinates(STARTING_POINT)).isEqualTo(COORD_HOME);
    }

    @Test
    public void toRouteCoordinatesStaticTest() {
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
        assertThat(toRouteCoordinates(routeString)).isEqualTo(expected);
    }
}
