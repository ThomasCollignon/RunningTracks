package org.coli.routegenerator.data;

import lombok.Generated;
import lombok.Getter;
import org.coli.routegenerator.exception.RTException;
import org.coli.routegenerator.model.Point;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static java.util.stream.IntStream.range;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.coli.routegenerator.constant.Constant.RUN_ZONE_LIBERSART;
import static org.coli.routegenerator.constant.Constant.STARTING_POINT_CHASTRE;
import static org.coli.routegenerator.constant.Constant.STARTING_POINT_LIBERSART;

/**
 * Runs at startup, with @PostConstruct
 */
@Component
public class PointsLoader {

    private static final String RESOURCES_EXTENSION = ".txt";

    private static final String PARTIAL_SUFFIX = "_partial_routes";

    private final PointsMap pointsMap = new PointsMap();

    private List<List<String>> partialRoutes = new ArrayList<>();

    @Generated
    @Getter
    private PointsMap pointsMapChastre;

    @Generated
    @Getter
    private PointsMap pointsMapLibersart;

    @Generated
    @Getter
    private List<List<String>> partialRoutesChastre;

    @Generated
    @Getter
    private List<List<String>> partialRoutesLibersart;

    static void verifyPartialRoutesArePossible(PointsMap pointsMapArg, List<List<String>> partialRoutes) {
        partialRoutes.forEach(route -> verifyPartialRouteIsPossible(pointsMapArg, route));
    }

    static List<List<String>> toAllPartialRoutesBySizeDesc(List<List<String>> partialRoutes) {
        List<List<String>> result = new ArrayList<>();
        partialRoutes.forEach(partialRoute -> result.addAll(toAllPartialRoutes(partialRoute)));
        return result.stream()
                     .distinct()
                     .sorted(Comparator.<List<String>>comparingInt(List::size).reversed())
                     .toList();
    }

    private static void verifyPartialRouteIsPossible(PointsMap pointsMapArg, List<String> partialRoute) {
        IntStream.range(0, partialRoute.size() - 2)
                 .filter(index -> !pointsMapArg.get(partialRoute.get(index))
                                               .linkedPointsDistance()
                                               .containsKey(new Point(partialRoute.get(index + 1))))
                 .forEach(index -> {
                     throw new RTException("Partial route " + partialRoute.get(index) + " - "
                                                   + partialRoute.get(index + 1) + " is invalid.");
                 });
    }

    private static PointsMap loadPointsMap(String runZone, String startingPoint) {
        PointsLoader pointsLoader = new PointsLoader();
        pointsLoader.fillPointsData(runZone);
        pointsLoader.pointsMap.setStartingPointLabel(startingPoint);
        return pointsLoader.pointsMap;
    }

    private static List<List<String>> loadPartialRoutes(String runZone) {
        PointsLoader pointsLoader = new PointsLoader();
        pointsLoader.fillPartialRouteData(runZone);
        return pointsLoader.partialRoutes;
    }

    private static List<List<String>> toAllPartialRoutes(List<String> partialRoute) {
        List<List<String>> result = new ArrayList<>();
        range(0, partialRoute.size() - 2)
                .forEach(index1 -> range(index1 + 3, partialRoute.size() + 1)
                        .forEach(index2 -> {
                            List<String> subRoute = partialRoute.subList(index1, index2);
                            result.add(subRoute);
                            List<String> subRouteReversed = new ArrayList<>(subRoute);
                            reverse(subRouteReversed);
                            result.add(subRouteReversed);
                        }));
        return result;
    }

    private void addLink(String point1Label, String point2Label, Integer distance) {
        pointsMap.computeIfAbsent(point1Label, k -> new Point(point1Label));
        pointsMap.computeIfAbsent(point2Label, k -> new Point(point2Label));
        Point point1 = pointsMap.get(point1Label);
        Point point2 = pointsMap.get(point2Label);
        point1.linkedPointsDistance().computeIfAbsent(point2, k -> distance);
        point2.linkedPointsDistance().computeIfAbsent(point1, k -> distance);
    }

    private void fillPointsData(String runZone) {
        Reader.readFileAndConsumeLines(runZone + RESOURCES_EXTENSION, this::parsePointsStream);
    }

    private void fillPartialRouteData(String runZone) {
        Reader.readFileAndConsumeLines(runZone + PARTIAL_SUFFIX + RESOURCES_EXTENSION, this::parsePartialPointsStream);
        partialRoutes = toAllPartialRoutesBySizeDesc(partialRoutes);
    }

    @PostConstruct
    private void loadPointsMap() {
        pointsMapChastre = loadPointsMap(RUN_ZONE_CHASTRE, STARTING_POINT_CHASTRE);
        partialRoutesChastre = loadPartialRoutes(RUN_ZONE_CHASTRE);
        verifyPartialRoutesArePossible(pointsMapChastre, partialRoutesChastre);

        pointsMapLibersart = loadPointsMap(RUN_ZONE_LIBERSART, STARTING_POINT_LIBERSART);
        partialRoutesLibersart = loadPartialRoutes(RUN_ZONE_LIBERSART);
        verifyPartialRoutesArePossible(pointsMapLibersart, partialRoutesLibersart);
    }

    private void parsePointsStream(Stream<String> stream) {
        stream.forEach(line -> {
            String[] elements = line.split(" ");
            addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
        });
    }

    private void parsePartialPointsStream(Stream<String> stream) {
        stream.forEach(line -> partialRoutes.add(asList(line.split(" "))));
    }
}
