package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day06.txt"));
        int sum = 0;
        int[] letterCount = new int[26];
        int groupSize = 0;
        for (String line : lines) {
            if (!line.isEmpty()) {
                groupSize++;
                for (char c : line.toCharArray()) {
                    letterCount[c%26]++;
                }
            } else {
                for (int n : letterCount) {
                    if (n == groupSize) sum++;
                }
                letterCount = new int[26];
                groupSize = 0;
            }
        }
        for (int n : letterCount) {
            if (n == groupSize) sum++;
        }
        System.out.println(sum);
    }
}
