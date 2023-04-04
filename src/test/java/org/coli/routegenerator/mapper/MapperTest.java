package org.coli.routegenerator.mapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART;
import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;

class MapperTest {

    @Test
    void toListOfCoordinates() {
        assertThat(Mapper.toListOfCoordinates(SHORT_ROUTE_LIBERSART)).isEqualTo(SHORT_ROUTE_LIBERSART_COORDINATES);
    }
}
