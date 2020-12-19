package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day01Part01 {

    public static void main(String[] args) throws IOException {
        String fileName = "inputs/day01.txt";

        List<String> years = Files.readAllLines(Paths.get(fileName));

        int goal = 2020;
        Set<Integer> addends = new HashSet<>();
        years.forEach(year -> {
            int currentYear = Integer.parseInt(year);
            int addendNeeded = goal - currentYear;
            if (addends.contains(addendNeeded)) {
                System.out.println("values= " +  currentYear + " " + addendNeeded);
                System.out.println("product= " + currentYear * addendNeeded);
                System.exit(0);
            }
            addends.add(currentYear);
        });
        System.err.println("No solution found");
        System.exit(1);
    }
}
