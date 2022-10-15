package org.coli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.Constants.STARTING_POINT;

public class CoordinatesTest {

    Coordinates coordinates = new Coordinates();

    @Test
    public void toCoordinatesTest() {
        assertThat(coordinates.toCoordinates(STARTING_POINT)).isEqualTo("50.65292179181078, 4.718251399671132");
    }
}
