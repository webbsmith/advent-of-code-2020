package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day06.txt"));
        int sum = 0;
        Set<Character> unique = new HashSet<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                for (char c : line.toCharArray()) {
                    unique.add(c);
                }
            } else {
                sum += unique.size();
                unique = new HashSet<>();
            }
        }
        sum += unique.size(); // add last entry
        System.out.println(sum);
    }
}
