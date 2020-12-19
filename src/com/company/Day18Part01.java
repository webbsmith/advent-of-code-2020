package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day18Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day18.txt"));
        long sumOfAll = 0;
        for (String line : lines) {
            sumOfAll += mathItUp(line);
        }
        System.out.println(sumOfAll);
    }

    private static long mathItUp(String line) {
        try {
            return Long.parseLong(line);
        } catch (NumberFormatException e) {
            // continue to evaluate expression
        }

        int openParenthesesCount = 0;
        StringBuilder insideParentheses = new StringBuilder();
        StringBuilder partialInteger = new StringBuilder();
        Character operator1 = null;
        Character operator2 = null;
        Long side1 = null;
        Long side2 = null;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (openParenthesesCount > 0) {
                if (c == '(') {
                    insideParentheses.append(c);
                    openParenthesesCount++;
                } else if (c != ')') {
                    insideParentheses.append(c);
                } else {
                    openParenthesesCount--;
                    if (openParenthesesCount == 0) {
                        String innerExpression = insideParentheses.substring(0);
                        if (operator1 != null && side1 != null) {
                            if (operator1 == '*') {
                                side1 *= mathItUp(innerExpression);
                            } else {
                                side1 += mathItUp(innerExpression);
                            }
                            operator1 = null;
                        } else {
                            side1 = mathItUp(innerExpression);
                        }
                        insideParentheses = new StringBuilder();
                    } else {
                        insideParentheses.append(c);
                    }
                }
                continue;
            }

            if (c == '(') {
                openParenthesesCount++;
                continue;
            }

            if (c >= '0' && c <= '9') {
                partialInteger.append(c);
            } else if (c == '*' || c == '+') {
                if (operator1 == null)
                    operator1 = c;
                else
                    operator2 = c;
            } else if (c == ' ') {
                if (partialInteger.length() != 0) {
                    long number = Long.parseLong(partialInteger.toString());
                    partialInteger = new StringBuilder();
                    if (side1 == null)
                        side1 = number;
                    else
                        side2 = number;
                } else if (operator2 != null) {
                    long result;
                    if (operator1 == '*')
                        result = side1 * side2;
                    else
                        result = side1 + side2;

                    operator1 = operator2;
                    operator2 = null;
                    side1 = result;
                    side2 = null;
                }
            }
        }
        if (partialInteger.length() > 0) {
            side2 = Long.parseLong(partialInteger.toString());
            if (operator1 == '*') {
                return side1 * side2;
            } else {
                return side1 + side2;
            }
        }
        return side1;
    }
}
