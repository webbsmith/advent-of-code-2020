package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day04Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day04.txt"));
        int validCount = 0;

        int passportStart = 0;
        for (int i = 0; i <= lines.size(); i++) {
            if (lines.size() == i || lines.get(i).isBlank()) {
                StringBuilder sb = new StringBuilder();
                for (int j = passportStart; j < i; j++) {
                    sb.append(lines.get(j)).append(" ");
                }
                String allFields = sb.toString();
                List<String> fields = Arrays.asList(allFields.split(" "));
                Collections.sort(fields);
                System.out.print("[" + fields.size() + "]: " + fields);
                if (allFields.contains("byr:") &&
                        allFields.contains("iyr:") &&
                        allFields.contains("eyr:") &&
                        allFields.contains("hgt:") &&
                        allFields.contains("hcl:") &&
                        allFields.contains("ecl:") &&
                        allFields.contains("pid:")) {
                    validCount++;
                    System.out.println();
                } else {
                    System.out.println(" ==> invalid");
                }
                passportStart = i+1;
            }
        }

        System.out.println("valid: " + validCount);
    }

}
