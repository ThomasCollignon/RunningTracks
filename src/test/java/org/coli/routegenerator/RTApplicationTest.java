package org.coli.routegenerator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("slow")
class RTApplicationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }
}