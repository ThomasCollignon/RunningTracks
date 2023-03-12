package org.coli.routegenerator;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.coli.routegenerator.Constants.RESOURCES_EXTENSION;
import static org.coli.routegenerator.Constants.RESOURCES_PATH;
import static org.coli.routegenerator.Constants.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.Constants.RUN_ZONE_LIBERSART;

@Component
public class PointsLoader {

    private final PointsMap pointsMap = new PointsMap();
    @Getter
    private PointsMap pointsMapChastre;
    @Getter
    private PointsMap pointsMapLibersart;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        String[] elements = line.split(" ");
        addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
    }

    private void addLink(String point1Label, String point2Label, Integer distance) {
        pointsMap.computeIfAbsent(point1Label, k -> new Point(point1Label));
        pointsMap.computeIfAbsent(point2Label, k -> new Point(point2Label));
        Point point1 = pointsMap.get(point1Label);
        Point point2 = pointsMap.get(point2Label);
        point1.getLinkedPoints()
              .computeIfAbsent(point2, k -> distance);
        point2.getLinkedPoints()
              .computeIfAbsent(point1, k -> distance);
    }

    @PostConstruct
    void loadPointsMap() {
        pointsMapChastre = load(RUN_ZONE_CHASTRE, "Commune");
        pointsMapLibersart = load(RUN_ZONE_LIBERSART, "Home");
    }
}
