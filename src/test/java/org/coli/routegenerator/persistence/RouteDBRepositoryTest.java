package org.coli.routegenerator.persistence;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // or @Autowired on the properties
class RouteDBRepositoryTest {

    private final RouteDBRepository routeDBRepository;

    @Test
    void saveAndFind() {
        RouteDB routeDB = new RouteDB("key-1", asList(
                asList("a", "b", "c"),
                asList("f", "d"),
                asList("g", "c")
        ), 5);
        routeDBRepository.save(routeDB); // Not persisted because @DataJpaTest does rollback by default.
        assertThat(routeDBRepository.findAll()).containsExactly(routeDB).hasSize(1);
    }
}
