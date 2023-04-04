package org.coli.routegenerator.service.route;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RouteHelperTest {

    @Test
    void reverseRoute() {
        assertThat(RouteHelper.reverseRoute("foo - bar")).isEqualTo("bar - foo");
    }
}
