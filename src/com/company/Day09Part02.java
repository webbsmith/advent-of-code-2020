package com.company;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class Day09Part02 {
    static final int INVALID_NUMBER = 552655238;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day09.txt"));
        boolean solutionNotFound = true;
        for (int i = 0; i < lines.size() && solutionNotFound; i++) {
            int sum = Integer.parseInt(lines.get(i));
            for (int j = i + 1; j < lines.size(); j++) {
                sum += Integer.parseInt(lines.get(j));
                if (sum > INVALID_NUMBER) {
                    break;
                } else if (sum == INVALID_NUMBER) {
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;
                    System.out.println("values that add up to " + INVALID_NUMBER + ": ");
                    for (int k = i; k <= j; k++) {
                        int current = Integer.parseInt(lines.get(k));
                        System.out.print(current + " ");
                        max = Math.max(max, current);
                        min = Math.min(min, current);
                    }
                    System.out.println("\n\nsolution: " + (max + min));
                    solutionNotFound = false;
                }
            }
        }

    }
}
