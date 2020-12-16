package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day12Part02 {
    private static int wayPointEastWest = 10;
    private static int wayPointNorthSouth = 1;
    private static int shipEastWest = 0;
    private static int shipNorthSouth = 0;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day12.txt"));
        int direction = 0;
        for (String line : lines) {
            char instr = line.charAt(0);
            int moveBy = Integer.parseInt(line.substring(1));
            switch (instr) { // enhanced switch added in JDK 13
                case 'N' -> wayPointNorthSouth += moveBy;
                case 'S' -> wayPointNorthSouth -= moveBy;
                case 'E' -> wayPointEastWest += moveBy;
                case 'W' -> wayPointEastWest -= moveBy;
                case 'L' -> changeDirection(direction, -moveBy);
                case 'R' -> changeDirection(direction, moveBy);
                case 'F' -> {
                    shipEastWest += (wayPointEastWest * moveBy);
                    shipNorthSouth += (wayPointNorthSouth * moveBy);
                }
            }
        }
        System.out.println(Math.abs(shipEastWest) + Math.abs(shipNorthSouth));
    }

    // keep direction within 0-359
    private static void changeDirection(int direction, int moveBy) {
        direction += moveBy;
        direction %= 360;
        if (direction < 0)
            direction = 360 + direction;

        switch (direction) {
//            case 0 ->
            case 90 -> {
                int tmp = wayPointNorthSouth;
                wayPointNorthSouth = -wayPointEastWest;
                wayPointEastWest = tmp;
            }
            case 180 -> {
                wayPointNorthSouth = -wayPointNorthSouth;
                wayPointEastWest = -wayPointEastWest;
            }
            case 270 -> {
                int tmp = wayPointNorthSouth;
                wayPointNorthSouth = wayPointEastWest;
                wayPointEastWest = -tmp;
            }
            default -> throw new RuntimeException("unhandled direction " + direction);
        }
    }
}
