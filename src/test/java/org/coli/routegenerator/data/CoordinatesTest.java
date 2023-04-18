package org.coli.routegenerator.data;

import org.coli.routegenerator.exception.RTException;
import org.junit.jupiter.api.Test;

import static org.coli.routegenerator.data.Coordinates.coordinates;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinatesTest {

    @Test
    void getOrException() {
        assertEquals("50.61593, 4.64050", coordinates().getOrException("Tunnel"));
        Coordinates coordinatesInstance = coordinates();
        assertThrows(RTException.class, () -> coordinatesInstance.getOrException("not found"));
    }
}
