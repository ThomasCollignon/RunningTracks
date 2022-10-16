package org.coli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coli.TestConstants.RUN_ZONE_TEST;
import static org.coli.TestConstants.point_home;
import static org.coli.TestConstants.point_tumuli;

public class PointsLoaderTest {

    @Test
    public void loadTest() {
        PointsMap pointsMap = PointsLoader.load(RUN_ZONE_TEST, "Home");
        assertThat(pointsMap).isEqualTo(TestConstants.pointsMap);
        assertThat(pointsMap.get("Home").getLinkedPoints()).containsKey(point_tumuli);
        assertThat(pointsMap.get("Tumuli").getLinkedPoints()).containsKey(point_home);
    }
}
