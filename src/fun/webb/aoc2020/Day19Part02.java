package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day19Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day19.txt"));
        int maxRuleIdx = 200; // max for creating array size
        int actualMaxRuleIdx = -1; // determined after seeing data

        String[] rules = new String[maxRuleIdx];
        List<String> messages = new ArrayList<>();

        int ruleA = -1;
        int ruleB = -1;
        int lineNum = 0;
        for (; ; lineNum++) {
            String line = lines.get(lineNum);
            if (line.isEmpty()) break;
            int colonIndex = line.indexOf(":");
            int ruleIdx = Integer.parseInt(line.substring(0, colonIndex));
            rules[ruleIdx] = line.substring(colonIndex + 2);
            if (ruleIdx == 8) {
                rules[ruleIdx] = "42+"; // same as 42 | 42 8
            } else if (ruleIdx == 11) {
                rules[ruleIdx] = "42 31 | 42 11 31";
            }

            if (rules[ruleIdx].equals("\"a\"")) {
                ruleA = ruleIdx;
            } else if (rules[ruleIdx].equals("\"b\"")) {
                ruleB = ruleIdx;
            }
            actualMaxRuleIdx = Math.max(actualMaxRuleIdx, ruleIdx);
        }
        for (; lineNum < lines.size(); lineNum++) {
            String line = lines.get(lineNum);
            messages.add(line);
        }

        // convert rules to regex
        String[] regexRules = Arrays.copyOf(rules, actualMaxRuleIdx + 1);
        regexRules[ruleA] = "a"; // remove double quotes
        regexRules[ruleB] = "b"; // remove double quotes

        // track all completed conversions
        Map<Integer, Boolean> rulesConverted = new HashMap<>();
        rulesConverted.put(ruleA, Boolean.TRUE);
        rulesConverted.put(ruleB, Boolean.TRUE);

        // replace all instances of a and b rules with the char
        String aI = Integer.toString(ruleA);
        String bI = Integer.toString(ruleB);
        for (int i = 0; i < regexRules.length; i++) {
            String s = regexRules[i];
            regexRules[i] = s
                    .replaceAll("(\\d+)", "'$1'") // add surrounding quotes to help isolate integers
                    .replaceAll("'" + aI + "'", regexRules[ruleA])
                    .replaceAll("'" + bI + "'", regexRules[ruleB]);
        }

        // replace rule 11 with itself several times
        for (int i = 0; i < 5; i++) {
            regexRules[11] = regexRules[11].replaceAll("'11'", "(" + regexRules[11] + ")");
        }
        // finally remove 11 so the below loop will complete
        regexRules[11] = regexRules[11].replaceAll("'11'","");

        // replace rules with value of finished rules until all are finished
        Stack<Integer> rulesToReplace = new Stack<>();
        while (rulesConverted.size() < regexRules.length) {
            for (int i = 0; i < regexRules.length; i++) {
                if (rulesConverted.get(i) != null) continue; // skip finished rules
                String regexRule = regexRules[i];
                for (int finishedRuleIdx : rulesToReplace) {
                    String replacement = regexRules[finishedRuleIdx];
                    if (regexRules[finishedRuleIdx].contains("|")) {
                        replacement = "(" + replacement + ")";
                    }
                    regexRule = regexRule.replaceAll("'" + finishedRuleIdx + "'", replacement);
                }
                regexRules[i] = regexRule;

                // when the rule contains no numbers it is finished
                if (!regexRule.matches(".*[0-9].*")) {
                    rulesConverted.put(i, Boolean.TRUE);
                    rulesToReplace.add(i);
                }
            }
        }

        // remove all spaces from rules
        for (int i = 0; i < regexRules.length; i++) {
            regexRules[i] = regexRules[i].replaceAll(" ", "");
        }

        // debug
        for (int i = 0; i < regexRules.length; i++) {
            System.out.println(i + ": " + rules[i]);
            System.out.println(i + ": " + regexRules[i]);
            System.out.println();
        }

        // determine if messages match rule 0
        long matchCount = messages.stream().filter(m -> m.matches(regexRules[0])).count();
        System.out.println(matchCount + " matches");
    }
}
