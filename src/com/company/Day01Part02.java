package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day01Part02 {

    public static void main(String[] args) throws IOException {
        String fileName = "inputs/day01.txt";

        List<String> years = Files.readAllLines(Paths.get(fileName));

        int goal = 2020;
        Map<Integer, List<Integer>> addendMap = new HashMap<>();

        for (int i = 0; i < years.size(); i++) {
            int currentYear = Integer.parseInt(years.get(i));
            int remainingToGoal = goal - currentYear;

            List<Integer> addends = addendMap.get(remainingToGoal);
            // There is an entry in addendMap for the remaining amount to reach 2020
            if (addends != null) {
                System.out.println("values= " +  currentYear + " " + addends.get(0) + " " + addends.get(1));
                System.out.println("product= " + currentYear * addends.get(0) * addends.get(1));
                System.exit(0);
            }
            // Add all sums for currentYear and other years to addendMap
            for (int j = i + 1; j < years.size(); j++) {

                int otherYear = Integer.parseInt(years.get(j));
                addendMap.put(currentYear + otherYear,
                        Arrays.asList(currentYear, otherYear));
            }
        }
        System.err.println("No solution found");
        System.exit(1);
    }
}
