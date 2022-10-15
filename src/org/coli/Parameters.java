package org.coli;

import java.util.HashSet;
import java.util.Set;

public class Parameters {

    /**
     * If false the path won't include turnarounds
     */
    private boolean turnaround;

    /**
     * If false the route won't encounter twice the same point (except home point)
     */
    private boolean repeatPoint;

    /**
     * Defines the range that is accepted around the chosen distance
     */
    private int extraDistancePercentage;

    /**
     * Defines the extra or lower meters that are accepted around the chosen distance
     */
    private int extraDistanceMeters;

    /**
     * Points through which the route must pass. If empty there is no restrictions.
     */
    private Set<String> mandatoryPoints;

    /**
     * Suite of points to avoid, in both directions
     */
    private Set<String> patternsToAvoid;

    /**
     * Suite of points to avoid, in this directions
     */
    private Set<String> patternsToInclude;

    /**
     * Accepts identical routes with points in opposite orders.
     */
    private boolean reverseTwinDisplayed;

    private String startingPointLabel;

    private boolean extraDistancePercentageFlag = false;

    Parameters() {
        this.turnaround = false;
        this.repeatPoint = false;
        this.extraDistancePercentage = 10;
        this.extraDistanceMeters = 0;
        this.mandatoryPoints = new HashSet<>();
        this.patternsToAvoid = new HashSet<>();
        this.patternsToInclude = new HashSet<>();
        this.reverseTwinDisplayed = false;
        this.startingPointLabel = "Home";
    }

    Set<String> getPatternsToAvoid() {
        return patternsToAvoid;
    }

    Parameters setPatternsToAvoid(Set<String> patternsToAvoid) {
        this.patternsToAvoid = patternsToAvoid;
        return this;
    }

    int getExtraDistanceMeters() {
        return extraDistanceMeters;
    }

    Parameters setExtraDistanceMeters(int extraDistanceMeters) {
        this.extraDistanceMeters = extraDistanceMeters;
        return this;
    }

    boolean isExtraDistancePercentageFlag() {
        return extraDistancePercentageFlag;
    }

    String getStartingPointLabel() {
        return startingPointLabel;
    }

    public Parameters setStartingPointLabel(String startingPointLabel) {
        this.startingPointLabel = startingPointLabel;
        return this;
    }

    boolean isReverseTwinDisplayed() {
        return reverseTwinDisplayed;
    }

    public Parameters setReverseTwinDisplayed(boolean reverseTwinDisplayed) {
        this.reverseTwinDisplayed = reverseTwinDisplayed;
        return this;
    }

    Set<String> getMandatoryPoints() {
        return mandatoryPoints;
    }

    public Parameters setMandatoryPoints(Set<String> mandatoryPoints) {
        this.mandatoryPoints = mandatoryPoints;
        return this;
    }

    boolean isTurnaround() {
        return turnaround;
    }

    public Parameters setTurnaround(boolean turnaround) {
        this.turnaround = turnaround;
        return this;
    }

    boolean isRepeatPoint() {
        return repeatPoint;
    }

    public Parameters setRepeatPoint(boolean repeatPoint) {
        this.repeatPoint = repeatPoint;
        return this;
    }

    int getExtraDistancePercentage() {
        return extraDistancePercentage;
    }

    public Parameters setExtraDistancePercentage(int extraDistancePercentage) {
        extraDistancePercentageFlag = true;
        this.extraDistancePercentage = extraDistancePercentage;
        return this;
    }

    public Set<String> getPatternsToInclude() {
        return patternsToInclude;
    }

    Parameters setPatternsToInclude(Set<String> patternsToInclude) {
        this.patternsToInclude = patternsToInclude;
        return this;
    }
}
