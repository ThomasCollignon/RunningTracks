package org.coli.routegenerator;

import lombok.Generated;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

import static org.coli.routegenerator.Constants.RESOURCES_EXTENSION;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.Constants.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.Constants.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.Constants.STARTING_POINT_LIBERSART;

@Component
public class PointsLoader {

    private final PointsMap pointsMap = new PointsMap();

    @Generated
    @Getter
    private PointsMap pointsMapChastre;

    @Generated
    @Getter
    private PointsMap pointsMapLibersart;

    public static PointsMap load(String runZone, String startingPoint) {
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
        point1.linkedPoints()
              .computeIfAbsent(point2, k -> distance);
        point2.linkedPoints()
              .computeIfAbsent(point1, k -> distance);
    }

    private void fillPointsData(String runZone) {
        Utils.readFileAndConsumeLines(runZone + RESOURCES_EXTENSION, this::parseStream);
    }

    @PostConstruct
    private void loadPointsMap() {
        pointsMapChastre = load(RUN_ZONE_CHASTRE, STARTING_POINT_CHASTRE);
        pointsMapLibersart = load(RUN_ZONE_LIBERSART, STARTING_POINT_LIBERSART);
    }

    private void parseStream(Stream<String> stream) {
        stream.forEach(line -> {
            String[] elements = line.split(" ");
            addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
        });
    }
}
