package org.coli.routegenerator.restapi;

import org.coli.routegenerator.service.RouteService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.coli.routegenerator.constant.TestConstants.SHORT_ROUTE_LIBERSART_COORDINATES;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Using @MockBean is bad for test performance. Still, no better way to have a UT of the web part with lower tiers
 * mocked.
 */
@WebMvcTest
@Tag("slow")
class RTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService routeServiceMock;

    @Test
    void home() {
        when(routeServiceMock.getAnotherRouteCoords(anyInt(), any())).thenReturn(SHORT_ROUTE_LIBERSART_COORDINATES);
        try {
            this.mockMvc.perform(get("/"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(equalTo(expectedShortRouteCoordinatesFormatted())));
        } catch (Exception e) {
            throw new RuntimeException("Error when using mockMvc\n" + e.getMessage());
        }
    }

    private String expectedShortRouteCoordinatesFormatted() {
        return SHORT_ROUTE_LIBERSART_COORDINATES.stream()
                                                .map(coord -> "\"" + coord + "\"")
                                                .toList()
                                                .toString()
                                                .replace("\", \"", "\",\"");
    }
}
