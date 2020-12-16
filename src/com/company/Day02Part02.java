package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day02Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day02.txt"));
        AtomicInteger meetsRequirements = new AtomicInteger();

        lines.forEach(line -> {
            String[] split = line.split(" ");
            String countRange = split[0];
            char letter = split[1].charAt(0);
            String password = split[2];

            String[] posRequirement = countRange.split("-");
            int firstIdx = Integer.parseInt(posRequirement[0]) - 1;
            int secondIdx = Integer.parseInt(posRequirement[1]) - 1;

            if (password.charAt(firstIdx) == letter ^ (password.length() > secondIdx && password.charAt(secondIdx) == letter))
                meetsRequirements.incrementAndGet();

        });

        System.out.println("Passwords meeting requirements = " + meetsRequirements.get());
    }
}
