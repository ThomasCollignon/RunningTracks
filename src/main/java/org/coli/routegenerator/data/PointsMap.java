package org.coli.routegenerator.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.coli.routegenerator.model.Point;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
public class PointsMap extends HashMap<String, Point> {

    @Getter
    @Setter
    private String startingPointLabel;
}
