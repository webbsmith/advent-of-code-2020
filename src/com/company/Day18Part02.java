package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day18Part02 {
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

        // Modify line so that + is executed first
        line = prioritizeSums(line);


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

    private static String prioritizeSums(String line) {
        StringBuilder modifiedLine = new StringBuilder();
        StringBuilder leftSide = new StringBuilder();
        StringBuilder rightSide = new StringBuilder();
        boolean leftSideFound = false;
        int openParenthesesCount = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (leftSideFound) {
                rightSide.append(c);
            } else {
                leftSide.append(c);
            }

            if (c == '(') {
                openParenthesesCount++;
                continue;
            }
            if (c == ')') {
                openParenthesesCount--;
            }

            if (openParenthesesCount == 0) {
                if (c == '+') {
                    leftSideFound = true;
                    leftSide.append(line.charAt(++i)); // add following space
                } else if (c == '*') {
                    modifiedLine.append(leftSide)
                            .append(line.charAt(++i)); // add following space
                    leftSide = new StringBuilder(); // reset leftSide
                } else if (leftSideFound && (c == ' ')) {
                    modifiedLine.append('(')
                            .append(leftSide)
                            .append(rightSide.substring(0, rightSide.length()-1)) // remove extra space
                            .append(')')
                            .append(line.substring(i));
                    line = modifiedLine.toString();

                    // reset stringbuilders and index to process remaining +'s
                    leftSideFound = false;
                    modifiedLine = new StringBuilder();
                    leftSide = new StringBuilder();
                    rightSide = new StringBuilder();
                    i = -1;
                } else if (leftSideFound && i == line.length() - 1) {
                    // only add parentheses if it's not the entire string
                    if (leftSide.length() + rightSide.length() != line.length()) {
                        modifiedLine.append('(')
                                .append(leftSide)
                                .append(rightSide)
                                .append(')');
                    } else {
                        modifiedLine.append(leftSide).append(rightSide);
                    }
                } else if (!leftSideFound && i == line.length() - 1 && rightSide.length() == 0) {
                    modifiedLine.append(leftSide);
                }
            }
        }
        return modifiedLine.toString();
    }
}
