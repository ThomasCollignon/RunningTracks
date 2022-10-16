package org.coli;

import lombok.Data;

import java.util.HashMap;

@Data
public class PointsMap extends HashMap<String, Point> {

    private String startingPointLabel;

}
