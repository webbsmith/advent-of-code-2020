package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day24Part02 {

    private static final String[] DIRECTIONS = new String[] {"e", "se", "sw", "w", "nw", "ne"};

    public static void main(String [] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day24.txt"));
        List<List<String>> instructions = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> instruction = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                String s = line.substring(j, j+1);
                if (s.equals("s") || s.equals("n")) {
                    s += line.substring(++j, j+1);
                }
                instruction.add(s);
            }
            instructions.add(instruction);
        }

        Map<Point, Integer> pointCount = new HashMap<>();
        for (List<String> instruction : instructions) {
            Point p = new Point();
            for (String command : instruction) {
                p.move(command);
            }
            Integer count = pointCount.getOrDefault(p, 0);
            pointCount.put(p, ++count);
        }

        Map<Point, Point> tileStates = new ConcurrentHashMap<>();
        for (Map.Entry<Point, Integer> count : pointCount.entrySet()) {
            Point p = count.getKey();
            if (count.getValue() % 2 == 1) {
                p.flipToBlack();
                tileStates.put(p, p);
            }
            tileStates.put(p, p);
        }

        for (int day = 1; day <= 100; day++) {
            tileStates = determineNextDayState(tileStates);
        }
        System.out.println(countBlackTiles(tileStates.keySet()));
    }

    private static Map<Point, Point> determineNextDayState(Map<Point, Point> tileState) {
        Map<Point, Point> nextDayState = new ConcurrentHashMap<>();
        for (Point p : tileState.keySet()) {
            flipIfNeeded(tileState, nextDayState, p);
        }
        return nextDayState;
    }

    private static void flipIfNeeded(Map<Point, Point> tileState, Map<Point, Point> nextDayState, Point p) {
        if (p.nextStateDetermined) {
            return;
        }

        List<Point> neighbors = new ArrayList<>();
        for (String direction : DIRECTIONS) {
            Point neighborTile = new Point(p.x, p.y).move(direction);
            neighbors.add(tileState.getOrDefault(neighborTile, neighborTile));
        }

        int blackTileNeighborCount = countBlackTiles(neighbors);
        if (!p.isBlack() && blackTileNeighborCount == 0) {
            // stop recursively processing neighbor tiles if all are white
            return;
        }

        // first flip black to white
        Point nextState = p.copyForNextState();
        tileState.put(p, p); // label original tile "next state determined"

        if (p.isBlack()) {
            if (blackTileNeighborCount == 0 || blackTileNeighborCount > 2) {
                nextState.flipToWhite();
            }
        } else {
            // flip white to black
            if (blackTileNeighborCount == 2) {
                nextState.flipToBlack();
            }
        }
        nextDayState.put(nextState, nextState);

        for (Point neighbor : neighbors) {
            flipIfNeeded(tileState, nextDayState, neighbor);
        }
    }

    private static int countBlackTiles(Collection<Point> neighbors) {
        return (int) neighbors.stream().filter(Point::isBlack).count();
    }

    private static class Point {
        private int x = 0;// starts at 0 in reference row, 1 at below (increments by 2 within row)
        private int y = 0;
        private boolean isBlack = false;
        private boolean nextStateDetermined = false;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private Point(int x, int y, boolean isBlack) {
            this.x = x;
            this.y = y;
            this.isBlack = isBlack;
        }

        public Point copyForNextState() {
            nextStateDetermined = true;
            return new Point(x, y, isBlack);
        }

        public Point move(String direction) {
            switch (direction) {
                case "e"  -> x += 2;
                case "se" -> { x += 1; y -= 1;}
                case "sw" -> { x -= 1; y -= 1;}
                case "w" -> x -= 2;
                case "nw" -> { x -= 1; y += 1;}
                case "ne" -> { x += 1; y += 1;}
            }
            return this;
        }

        public Point flipToBlack() {
            isBlack = true;
            return this;
        }

        public Point flipToWhite() {
            isBlack = false;
            return this;
        }

        public boolean isBlack() {
            return isBlack;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

    }
}
