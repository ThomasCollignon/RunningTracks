package org.coli.routegenerator.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

    @Test
    void toStringTest() {
        Point point = new Point("point label");
        point.linkedPointsDistance()
             .put(new Point("linked point 1"), 5);
        point.linkedPointsDistance()
             .put(new Point("linked point 2"), 10);
        assertThat(point.toString()).hasToString(
                "Point{label='point label', linkedPointsDistance=[linked point 1, linked point 2]}");
    }
}
