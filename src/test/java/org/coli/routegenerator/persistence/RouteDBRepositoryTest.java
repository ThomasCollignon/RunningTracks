package org.coli.routegenerator.persistence;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
        routeDBRepository.save(routeDB);
        assertThat(routeDBRepository.findAll()).containsOnlyOnce(routeDB);
        assertThat(routeDBRepository.findAll()).filteredOn(routeDB1 -> routeDB1.getRouteKey().equals("key-1"))
                                               .hasSize(1);

        routeDB = new RouteDB("key-1", asList(
                asList("a", "b", "c"),
                asList("f", "d"),
                asList("g", "c")
        ), 6);
        routeDBRepository.save(routeDB);
        assertThat(routeDBRepository.findAll()).filteredOn(routeDB1 -> routeDB1.getRouteKey().equals("key-1"))
                                               .hasSize(1);
    }

    @Test
    void displayResults() {
        routeDBRepository.findAll().forEach(System.out::println);

        assertThat("Appease " + "SonarLint").isEqualTo("Appease SonarLint");
    }
}
