package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day13.txt"));
        int earliestTimestamp = Integer.parseInt(lines.get(0));
        List<Integer> busIds = Arrays.stream(lines.get(1).split(","))
                .filter(s -> !s.equals("x"))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        int busToTake = -1;
        int timeToWait = -1;
        for (int i = earliestTimestamp; busToTake == -1 && i < Integer.MAX_VALUE; i++) {
            for (int busId : busIds) {
                if (i % busId == 0) {
                    busToTake = busId;
                    timeToWait = i - earliestTimestamp;
                    break;
                }
            }
        }
        System.out.println(busToTake * timeToWait);
    }
}
