package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day07Part02 {
    public static class BagRule {
        public int count;
        public String color;
        public BagRule(int count, String color) {
            this.count = count;
            this.color = color;
        }
    }
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day07.txt"));
        Map<String, List<BagRule>> bagRules = new HashMap<>();
        for (String l : lines) {
            String[] words = l.split(" ");
            String key = words[0] + " " + words[1];
            List<BagRule> innerBags = new ArrayList<>();
            bagRules.put(key, innerBags);

            // Add first rule (first check if empty)
            String val1 = words[5] + " " + words[6];
            if (val1.equals("other bags.")) {
                continue;
            }
            int count1 = Integer.parseInt(words[4]);
            innerBags.add(new BagRule(count1, val1));

            // Add remaining rules
            for (int i = 8; i < words.length; i += 4) {
                innerBags.add(new BagRule(Integer.parseInt(words[i]),
                        words[i + 1] + " " + words[i + 2]));
            }

        }

        // Count bags inside shiny gold bag
        int count = countInnerBags(bagRules.get("shiny gold"), bagRules);
        System.out.println(count);
    }

    private static int countInnerBags(List<BagRule> rules, Map<String, List<BagRule>> allBagRules) {
        int sum = 0;
        for (BagRule rule : rules) {
            sum += rule.count + rule.count * countInnerBags(allBagRules.get(rule.color), allBagRules);
        }
        return sum;
    }

    private static boolean canContainShinyGold(Map<String, List<String>> bagRules, List<String> colors) {
        for (String color : colors) {
            if (color.equals("shiny gold") || canContainShinyGold(bagRules, bagRules.get(color))) {
                return true;
            }
        }
        return false;
    }
}
