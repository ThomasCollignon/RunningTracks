package org.coli.routegenerator;

import lombok.Builder;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

/**
 * see <a href="https://www.baeldung.com/lombok-builder-default-value">Lombok builder default value</a>
 */
@Value
@Builder
public class Parameters {

    /**
     * If false the path won't include turnarounds
     */
    @Builder.Default
    boolean turnaround = false;

    /**
     * If false the route won't encounter twice the same point (except home point)
     */
    @Builder.Default
    boolean repeatPoint = false;

    /**
     * Defines the range that is accepted around the chosen distance
     */
    @Builder.Default
    int extraDistancePercentage = 10;

    /**
     * Defines the extra or lower meters that are accepted around the chosen distance
     */
    @Builder.Default
    int extraDistanceMeters = 0;

    /**
     * Points through which the route must pass. If empty there is no restrictions.
     */
    @Builder.Default
    Set<String> mandatoryPoints = new HashSet<>();

    /**
     * Set of routes to include, in written direction, points separated by " - "
     */
    @Builder.Default
    Set<String> includeRoutes = new HashSet<>();

    /**
     * Set of routes to avoid, in both directions, points separated by " - "
     */
    @Builder.Default
    Set<String> excludeRoutes = new HashSet<>();

    /**
     * Accepts identical routes with points in opposite orders.
     */
    @Builder.Default
    boolean reverseTwinDisplayed = false;

    @Builder.Default
    boolean extraDistancePercentageFlag = false;

}
