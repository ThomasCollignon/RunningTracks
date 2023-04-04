package org.coli.routegenerator.data;

import lombok.Generated;
import lombok.Getter;
import org.coli.routegenerator.constant.Constant;
import org.coli.routegenerator.model.Point;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;

/**
 * Runs at startup, with @PostConstruct
 */
@Component
public class PointsLoader {

    private static final String RESOURCES_EXTENSION = ".txt";

    private final PointsMap pointsMap = new PointsMap();

    @Generated
    @Getter
    private PointsMap pointsMapChastre;

    @Generated
    @Getter
    private PointsMap pointsMapLibersart;

    private static PointsMap load(String runZone, String startingPoint) {
        PointsLoader pointsLoader = new PointsLoader();
        pointsLoader.fillPointsData(runZone);
        pointsLoader.pointsMap.setStartingPointLabel(startingPoint);
        return pointsLoader.pointsMap;
    }

    private void addLink(String point1Label, String point2Label, Integer distance) {
        pointsMap.computeIfAbsent(point1Label, k -> new Point(point1Label));
        pointsMap.computeIfAbsent(point2Label, k -> new Point(point2Label));
        Point point1 = pointsMap.get(point1Label);
        Point point2 = pointsMap.get(point2Label);
        point1.linkedPoints().computeIfAbsent(point2, k -> distance);
        point2.linkedPoints().computeIfAbsent(point1, k -> distance);
    }

    private void fillPointsData(String runZone) {
        Reader.readFileAndConsumeLines(runZone + RESOURCES_EXTENSION, this::parseStream);
    }

    @PostConstruct
    private void loadPointsMap() {
        pointsMapChastre = load(RUN_ZONE_CHASTRE, Constant.STARTING_POINT_CHASTRE);
        pointsMapLibersart = load(RUN_ZONE_LIBERSART, Constant.STARTING_POINT_LIBERSART);
    }

    private void parseStream(Stream<String> stream) {
        stream.forEach(line -> {
            String[] elements = line.split(" ");
            addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
        });
    }
}
