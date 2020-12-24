package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day24Part01 {
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

        int blackTiles = 0;
        for (Integer count : pointCount.values()) {
            if (count % 2 == 1) blackTiles++;
        }

        System.out.println(blackTiles);
    }

    private static class Point {
        private int x = 0;// starts at 0 in reference row, 1 at below (increments by 2 within row)
        private int y = 0;

        public Point() {
        }

        public void move(String direction) {
            switch (direction) {
                case "e"  -> x += 2;
                case "se" -> { x += 1; y -= 1;}
                case "sw" -> { x -= 1; y -= 1;}
                case "w" -> x -= 2;
                case "nw" -> { x -= 1; y += 1;}
                case "ne" -> { x += 1; y += 1;}
            }
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
    }
}
