package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day07Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day07.txt"));
        Map<String, List<String>>  bagRules = new HashMap<>();
        for (String l : lines) {
            String[] words = l.split(" ");
            String key = words[0] + " " + words[1];
            List<String> innerBags = new ArrayList<>();
            bagRules.put(key, innerBags);

            String val1 = words[5] + " " + words[6];
            if (val1.equals("other bags.")) {
                continue;
            }

            innerBags.add(val1);
            for (int i = 9; i < words.length; i += 4) {
                innerBags.add(words[i] + " " + words[i + 1]);
            }
        }

//        bagRules.forEach((k, v) -> System.out.println(k + " -> " + v));
        int shinyGold = 0;
        for (List<String> colors : bagRules.values()) {
            if (canContainShinyGold(bagRules, colors)) shinyGold++;
        }

        System.out.println(shinyGold);
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
