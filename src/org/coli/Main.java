package org.coli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * See map here: https://drive.google.com/open?id=11pTt6aFhzUzS0cw5AF7ekdZwZAXFG4pG
 */
public class Main {

    private static Map<String, Point> data = new HashMap<>();

    public static void main(String[] args) {
        fillPointsData();
//        printData();
        RouteFinder.findRoute(data,
                10000,
                new Parameters()
//                        .setExtraDistancePercentage(50)
                        .setExtraDistanceMeters(100)
//                        .setRepeatPoint(true)
//                        .setMandatoryPoints(new HashSet<>(asList("Bardane")))
//                        .setPatternsToAvoid(patternLoader(false))
//                        .setPatternsToInclude(patternLoader(true))
        );
    }

    private static void printData() {
        System.out.println(data.keySet());
        data.forEach((k, v) -> {
                    System.out.println(k);
                    v.printListLinks();
                    System.out.println();
                }
        );
    }

    private static void fillPointsData() {
        try (Stream<String> stream = Files.lines(Paths.get("resources/data.txt"), StandardCharsets.UTF_8)) {
            stream.forEach(s -> parseLine(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> patternLoader(boolean include) {
        Set<String> patternsSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get("resources/patternsTo" + (include ? "Include" : "Avoid") + ".txt"),
                StandardCharsets.UTF_8)) {
            stream.forEach(line -> {
                patternsSet.add(line);
                if (!include) {
                    patternsSet.add(new StringBuilder(line).reverse().toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patternsSet;
    }


    private static void parseLine(String line) {
        String[] elements = line.split(" ");
        addLink(elements[0], elements[1], Integer.valueOf(elements[2]));
    }

    private static void addLink(String point1Label, String point2Label, Integer distance) {
        if (!data.containsKey(point1Label)) {
            data.put(point1Label, new Point(point1Label));
        }
        if (!data.containsKey(point2Label)) {
            data.put(point2Label, new Point(point2Label));
        }
        Point point1 = data.get(point1Label);
        Point point2 = data.get(point2Label);
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

