package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day12Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day12.txt"));
        int direction = 0;
        int eastWest = 0;
        int northSouth = 0;
        for (String line : lines) {
            char instr = line.charAt(0);
            int moveBy = Integer.parseInt(line.substring(1));
            switch (instr) { // enhanced switch added in JDK 13
                case 'N' -> northSouth += moveBy;
                case 'S' -> northSouth -= moveBy;
                case 'E' -> eastWest += moveBy;
                case 'W' -> eastWest -= moveBy;
                case 'L' -> direction = changeDirection(direction, -moveBy);
                case 'R' -> direction = changeDirection(direction, moveBy);
                case 'F' -> {
                    switch (direction) {
                        case 0 -> eastWest += moveBy;
                        case 90 -> northSouth -= moveBy;
                        case 180 -> eastWest -= moveBy;
                        case 270 -> northSouth += moveBy;
                        default -> throw new RuntimeException("unhandled direction " + direction);
                    }
                }
            }
        }
        System.out.println(Math.abs(eastWest) + Math.abs(northSouth));
    }

    // keep direction within 0-359
    private static int changeDirection(int direction, int moveBy) {
        direction += moveBy;
        direction %= 360;
        if (direction < 0) {
            return 360 + direction;
        } else {
            return direction;
        }
    }
}
