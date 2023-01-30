package org.coli.routegenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.coli.routegenerator.Constants.RESOURCES_EXTENSION;
import static org.coli.routegenerator.Constants.RESOURCES_PATH;

public class PointsLoader {

    private final PointsMap pointsMap = new PointsMap();

    public static PointsMap load(String runZone, String startingPoint) {
        PointsLoader pointsLoader = new PointsLoader();
        pointsLoader.fillPointsData(runZone);
        pointsLoader.pointsMap.setStartingPointLabel(startingPoint);
        return pointsLoader.pointsMap;
    }

    private void fillPointsData(String runZone) {
        try (Stream<String> stream =
                     Files.lines(Paths.get(RESOURCES_PATH + runZone + RESOURCES_EXTENSION), UTF_8)) {
            stream.forEach(this::parseLine);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        String[] elements = line.split(" ");
        addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
    }

    private void addLink(String point1Label, String point2Label, Integer distance) {
        if (!pointsMap.containsKey(point1Label)) {
            pointsMap.put(point1Label, new Point(point1Label));
        }
        if (!pointsMap.containsKey(point2Label)) {
            pointsMap.put(point2Label, new Point(point2Label));
        }
        Point point1 = pointsMap.get(point1Label);
        Point point2 = pointsMap.get(point2Label);
        Map<Point, Integer> point1Links = point1.getLinkedPoints();
        if (!point1Links.containsKey(point2)) {
            point1Links.put(point2, distance);
        }
        Map<Point, Integer> point2Links = point2.getLinkedPoints();
        if (!point2Links.containsKey(point1)) {
            point2Links.put(point1, distance);
        }
    }
}
