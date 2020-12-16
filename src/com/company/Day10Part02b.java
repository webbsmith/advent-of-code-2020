package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// The first attempt took too long to complete
public class Day10Part02b {

    static Map<Integer, Long> branchCount = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<Integer> lines = Files.readAllLines(Path.of("inputs/day10.txt"))
                .stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());

        lines.add(0, 0); // Add outlet jolts

        long solution = countPossibilities(0, lines);

        System.out.println(solution);
    }

    private static long countPossibilities(int i, List<Integer> lines) {
        Long branches = branchCount.get(i);
        if (branches != null) {
            return branches;
        }

        long poss = 0;

        for (int j = i + 1; j < i + 4; j++) {
            if (j == lines.size() - 1) {
                if (lines.get(j) - lines.get(i) < 4) {
                    poss++;
                }
                break;
            }
            if (lines.get(j) - lines.get(i) < 4) {
                poss += countPossibilities(j, lines);
            }
        }

        branchCount.put(i, poss);
        return poss;
    }
}
