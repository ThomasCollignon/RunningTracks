package org.coli.routegenerator;

import org.junit.jupiter.api.Test;

import static org.coli.routegenerator.Coordinates.coordinates;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinatesTest {

    @Test
    void getOrException() {
        assertEquals("50.61593118996567, 4.640497808776313", coordinates().getOrException("Tunnel"));
        Coordinates coordinatesInstance = coordinates();
        assertThrows(RTException.class, () -> coordinatesInstance.getOrException("not found"));
    }
}
