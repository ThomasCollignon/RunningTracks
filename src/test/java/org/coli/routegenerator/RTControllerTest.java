package org.coli.routegenerator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.stream.Collectors.toList;
import static org.coli.routegenerator.TestConstants.SHORT_ROUTE_COORDINATES;
import static org.coli.routegenerator.TestConstants.TEST_POINTS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Using @MockBean is bad for test performance.
 * Still, no better way to have a UT of the web part with lower tiers mocked.
 */
@WebMvcTest
@Tag("slow")
class RTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RTService rtServiceMock;

    @MockBean
    private PointsLoader pointsLoaderMock;

    @Test
    void home() throws Exception {
        when(rtServiceMock.getRandomRoute(anyInt(), any())).thenReturn(SHORT_ROUTE_COORDINATES);
        when(pointsLoaderMock.getPointsMapChastre()).thenReturn(TEST_POINTS);
        this.mockMvc.perform(get("/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(equalTo(expectedShortRouteCoordinatesFormatted())));
    }

    private String expectedShortRouteCoordinatesFormatted() {
        return SHORT_ROUTE_COORDINATES.stream()
                                      .map(coord -> "\"" + coord + "\"")
                                      .collect(toList())
                                      .toString()
                                      .replace("\", \"", "\",\"");
    }
}
