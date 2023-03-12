package org.coli.routegenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@EqualsAndHashCode
public class PointsMap extends HashMap<String, Point> {

    @Getter
    @Setter
    private String startingPointLabel;

}
