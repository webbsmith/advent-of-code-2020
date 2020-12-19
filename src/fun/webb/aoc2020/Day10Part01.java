package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day10Part01 {
    public static void main(String[] args) throws IOException {
        List<Integer> lines = Files.readAllLines(Path.of("inputs/day10.txt"))
                .stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());

        int threeJoltDiffs = 1; // go ahead and count the final difference from the device's adapter
        int oneJoltDiffs = 0;

        // Check first line
        int difference = lines.get(0);
        if (difference == 3) {
            threeJoltDiffs++;
        } else if (difference == 1) {
            oneJoltDiffs++;
        }

        // Check remaining
        for (int i = 1; i < lines.size(); i++) {
            difference = lines.get(i) - lines.get(i-1);
            if (difference == 3) {
                threeJoltDiffs++;
            } else if (difference == 1) {
                oneJoltDiffs++;
            }
        }

        System.out.println(threeJoltDiffs * oneJoltDiffs);
    }
}
