package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day21Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day21.txt"));

        // Count all associations of allergen -> ingredient
        Map<String, Allergen> allergens = new HashMap<>();
        List<String> allIngredients = new ArrayList<>();
        for (String line : lines) {
            String[] allergenNames = line.substring(line.indexOf("(contains ") + 10, line.length() - 1).split(", ");
            String[] ingredientNames = line.substring(0, line.indexOf("(")).split(" ");
            allIngredients.addAll(Arrays.asList(ingredientNames));
            for (String allergenName : allergenNames) {
                Allergen existing = allergens.get(allergenName);
                if (existing == null) {
                    allergens.put(allergenName, new Allergen(allergenName, ingredientNames));
                } else {
                    existing.updateIngredients(ingredientNames);
                }
            }
        }

        int allergensFound = 0;
        List<String> ingredientsWithAllergens = new ArrayList<>();
        // Take out the ingredients that occur every time the allergen is present
        do {
            for (Allergen allergen : allergens.values()) {
                if (allergen.ingredientFound) continue;
                // First remove already determined allergen ingredients
                for (String allergenIngredient : ingredientsWithAllergens) {
                    allergen.possibleIngredients.remove(allergenIngredient);

                }
                // Find max occurrence of ingredient in allergen list
                int max = allergen.possibleIngredients.values().stream().max(Integer::compareTo).get();
                List<String> ingredientsWithMax = new ArrayList<>();
                for (Map.Entry<String, Integer> ingredient : allergen.possibleIngredients.entrySet()) {
                    int count = ingredient.getValue();
                    if (count == max) {
                        ingredientsWithMax.add(ingredient.getKey());
                    }
                }
                // If only one max is found
                if (ingredientsWithMax.size() == 1) {
                    String allergenIngredient = ingredientsWithMax.get(0);
                    ingredientsWithAllergens.add(allergenIngredient);
                    allergen.ingredientFound = true;
                    allergensFound++;
                    while (allIngredients.contains(allergenIngredient)) {
                        allIngredients.remove(allergenIngredient);
                    }
                }
            }
        } while (allergensFound < allergens.size());

        System.out.println("allergen-containing ingredients: " + ingredientsWithAllergens);
        System.out.println("non-allergen ingredients: " + allIngredients);
        System.out.println("non-allergen ingredient count: " + allIngredients.size());
    }

    private static class Allergen {
        private final String name;
        private final Map<String, Integer> possibleIngredients;
        private boolean ingredientFound;

        public Allergen(String name, String[] initialIngredients) {
            this.name = name;
            this.possibleIngredients = new HashMap<>();
            for (String s : initialIngredients) {
                possibleIngredients.put(s, 1);
            }
        }

        public void updateIngredients(String[] ingredients) {
            for (String s : ingredients) {
                Integer previousCount = possibleIngredients.getOrDefault(s, 0);
                possibleIngredients.put(s, ++previousCount);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Allergen allergen = (Allergen) o;
            return Objects.equals(name, allergen.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
