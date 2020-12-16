package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day10Part02 {
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

        return poss;
    }
}
