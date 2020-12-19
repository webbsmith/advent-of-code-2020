package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day05Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day05.txt"));
        long maxSeatId = 0;
        for (String l : lines) {
            String binaryString = l.replaceAll("F|L", "0").replaceAll("B|R", "1");
            int row = Integer.parseInt(binaryString.substring(0, 7), 2);
            int column = Integer.parseInt(binaryString.substring(7, 10), 2);
            maxSeatId = Math.max(maxSeatId, (row * 8) + column);
        }
        System.out.println(maxSeatId);
    }
}
