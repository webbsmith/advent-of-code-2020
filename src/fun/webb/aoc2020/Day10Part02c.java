package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Trying Winston's dynamic programming solution
public class Day10Part02c {

    static Map<Integer, Long> branchCount = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<Integer> lines = Files.readAllLines(Path.of("inputs/day10.txt"))
                .stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());

        lines.add(0, 0); // Add outlet jolts


        long[] ways = new long[lines.get(lines.size() - 1) + 1];
        ways[lines.get(0)] = 1;

        for (Integer line : lines)
            for (int j = 1; j <= 3; j++)
                if (line - j > -1)
                    ways[line] += ways[line - j];

        System.out.println(ways[ways.length - 1]);
    }

}
