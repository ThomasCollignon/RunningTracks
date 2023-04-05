package org.coli.routegenerator.data;

import org.coli.routegenerator.exception.RTException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.coli.routegenerator.constant.TestConstants.PARTIAL_ROUTE_LIBERSART_INVALID;
import static org.coli.routegenerator.constant.TestConstants.PARTIAL_ROUTE_LIBERSART_VALID;
import static org.coli.routegenerator.constant.TestConstants.TEST_POINTS_LIBERSART;

class PointsLoaderTest {

    @Test
    void verifyPartialRoutesArePossible() {
        final List<List<String>> oneValidRoute = singletonList(PARTIAL_ROUTE_LIBERSART_VALID);
        assertThatCode(() -> PointsLoader.verifyPartialRoutesArePossible(TEST_POINTS_LIBERSART, oneValidRoute))
                .doesNotThrowAnyException();

        final List<List<String>> oneInvalidRoute = singletonList(PARTIAL_ROUTE_LIBERSART_INVALID);
        assertThatThrownBy(() -> PointsLoader.verifyPartialRoutesArePossible(TEST_POINTS_LIBERSART, oneInvalidRoute))
                .isInstanceOf(RTException.class).hasMessageContaining("Partial route");

        final List<List<String>> bothRoutes = asList(PARTIAL_ROUTE_LIBERSART_VALID, PARTIAL_ROUTE_LIBERSART_INVALID);
        assertThatThrownBy(() -> PointsLoader.verifyPartialRoutesArePossible(TEST_POINTS_LIBERSART, bothRoutes))
                .isInstanceOf(RTException.class).hasMessageContaining("Partial route");
    }

    @Test
    void toAllPartialRoutesBySizeDesc() {
        final List<String> PARTIAL_ROUTE_LIBERSART_2 = asList("Daix", "L", "C", "Sablière");
        List<List<String>> actual = PointsLoader.toAllPartialRoutesBySizeDesc(
                asList(PARTIAL_ROUTE_LIBERSART_VALID, PARTIAL_ROUTE_LIBERSART_2));
        List<List<String>> expected = asList(
                asList("Daix", "L", "C", "Sablière"),
                asList("Sablière", "C", "L", "Daix"),
                asList("Tumuli", "Sablière", "C", "L"),
                asList("L", "C", "Sablière", "Tumuli"),
                asList("Daix", "L", "C"),
                asList("C", "L", "Daix"),
                asList("Tumuli", "Sablière", "C"),
                asList("C", "Sablière", "Tumuli"),
                asList("Sablière", "C", "L"),
                asList("L", "C", "Sablière")
        );
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        List<Long> sizesOfExpected = expected.stream().mapToLong(List::size).boxed().toList();
        List<Long> sizesOfActual = actual.stream().mapToLong(List::size).boxed().toList();
        assertThat(sizesOfActual).isEqualTo(sizesOfExpected);
    }
}
