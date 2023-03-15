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
public class Options {

    static final int DEFAULT_EXTRA_DISTANCE_METERS = 500;
    static final int DEFAULT_EXTRA_DISTANCE_PERCENTAGE = 10;

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
     * Only applies if extraDistancePercentageFlag is true (default is false)
     */
    @Builder.Default
    int extraDistancePercentage = DEFAULT_EXTRA_DISTANCE_PERCENTAGE;

    /**
     * Defines the extra or lower meters that are accepted around the chosen distance.
     * Doesn't apply if extraDistancePercentageFlag is true (default is false)
     */
    @Builder.Default
    int extraDistanceMeters = DEFAULT_EXTRA_DISTANCE_METERS;

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

    /**
     * The extra distance uses a percentage, not a distance in meters
     */
    @Builder.Default
    boolean extraDistancePercentageFlag = false;

}
