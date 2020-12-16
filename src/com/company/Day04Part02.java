package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day04Part02 {
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
                List<String> fields = Arrays.asList(allFields.split("[ :]"));
//                Collections.sort(fields);
                System.out.print("[" + fields.size() + "]: " + fields);
                int validationsMatched = 0;
                for (int f = 0; f < fields.size(); f++) {
                    if (fields.get(f).equals("byr")) {
                        try {
                            int birthYear = Integer.parseInt(fields.get(++f));
                            if (birthYear <= 2002 && birthYear >= 1920) {
                                validationsMatched++;
                            }
                        } catch (NumberFormatException e) {
                            // invalid
                        }
                    } else if (fields.get(f).equals("iyr")) {
                        try {
                            int issueYear = Integer.parseInt(fields.get(++f));
                            if (issueYear >= 2010 && issueYear <= 2020) {
                                validationsMatched++;
                            }
                        } catch (NumberFormatException e) {
                            // invalid
                        }
                    } else if (fields.get(f).equals("eyr")) {
                        try {
                            int expirationYear = Integer.parseInt(fields.get(++f));
                            if (expirationYear >= 2020 && expirationYear <= 2030) {
                                validationsMatched++;
                            }
                        } catch (NumberFormatException e) {
                            // invalid
                        }
                    } else if (fields.get(f).equals("hgt")) {
                        try {
                            String height = fields.get(++f);
                            if (height.contains("cm")) {
                                int cm = Integer.parseInt(height.substring(0, 3));
                                if (cm >= 150 && cm <= 193) {
                                    validationsMatched++;
                                }
                            } else if (height.contains("in")) {
                                int inches = Integer.parseInt(height.substring(0, 2));
                                if (inches >= 59 && inches <= 76) {
                                    validationsMatched++;
                                }
                            }
                        } catch (NumberFormatException e) {
                            // invalid
                        }
                    } else if (fields.get(f).equals("hcl")) {
                        String hairColor = fields.get(++f);
                        if (hairColor.matches("#[0-9a-f]{6}")) {
                            validationsMatched++;
                        }
                    } else if (fields.get(f).equals("ecl")) {
                        String eyeColor = fields.get(++f);
                        if (eyeColor.matches("amb|blu|brn|gry|grn|hzl|oth")) {
//                            System.out.println("valid eye color: " + eyeColor);
                            validationsMatched++;
                        }
                    } else if (fields.get(f).equals("pid")) {
                        String passportId = fields.get(++f);
                        if (passportId.matches("[0-9]{9}")) {
                            validationsMatched++;
                        }
                    }
                }
                if (validationsMatched == 7) {
                    validCount++;
                    System.out.println();
                } else {
                    System.out.println(" ========> invalid");
                }
                passportStart = i + 1;
            }
        }

        System.out.println("valid: " + validCount);
    }

}
