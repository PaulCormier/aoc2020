package aoc2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

/**
 * Allergen Assessment
 * 
 * https://adventofcode.com/2020/day/21
 *
 * @author Paul Cormier
 * 
 */
public class Day21 {

    private static final String PUZZLE_INPUT_TXT = "Input-Day21.txt";
    private static final String TEST_PUZZLE_INPUT_TXT = "TestInput-Day21.txt";

    public static void main(String[] args) {
        part1();
        part2();

    }

    /**
     * Determine which ingredients cannot possibly contain any of the allergens
     * in your list. How many times do any of those ingredients appear?
     */
    private static void part1() {
        Set<String> ingredients = new HashSet<>();
        Set<String> allergens = new HashSet<>();

        List<Food> allFood = FileUtils.readFileToStream(PUZZLE_INPUT_TXT)
                                      .map(Food::new)
                                      //                                   .peek(System.out::println)
                                      .peek(f -> ingredients.addAll(f.getIngredients()))
                                      .peek(f -> allergens.addAll(f.getAllergens()))
                                      .collect(Collectors.toList());

        // The final mapping of ingredients to allergens.
        Map<String, String> allergenIngredients = new HashMap<>();

        /* Not quite. Worked for the example, but not the real input.
        // Find two sets of ingredients which have only one ingredient and one allergen in common
        int i = 0;
        for (Food food1 : allFood) {
            System.out.printf("%d: ", i++);
            int j = 0;
            for (Food food2 : allFood) {
                // Find common elements
                List<String> commonIngredients = ListUtils.intersection(food1.getIngredients(), food2.getIngredients());
                List<String> commonAllergens = ListUtils.intersection(food1.getAllergens(), food2.getAllergens());
                // Remove known values
                commonIngredients.removeAll(allergenIngredients.keySet());
                commonAllergens.removeAll(allergenIngredients.values());
                if (commonIngredients.size() == 1 && commonAllergens.size() == 1) {
                    allergenIngredients.put(commonIngredients.get(0), commonAllergens.get(0));
                } else {
                    System.out.printf("%d(%d,%d) ", j++, commonIngredients.size(), commonAllergens.size());
                }
            }
            System.out.println();
        }*/

        // For each allergen, find an ingredient which is listed in every food with that allergen.
        // Should be able to map out all allergens
        while (allergens.size() > 0) {
            for (String allergen : allergens) {
                allFood.stream()
                       .filter(f -> f.getAllergens().contains(allergen))
                       .map(Food::getIngredients)
                       .reduce(ListUtils::intersection)
                       .ifPresent(i -> {
                           if (CollectionUtils.removeAll(i, allergenIngredients.keySet()).size() == 1)
                               allergenIngredients.put(CollectionUtils.removeAll(i, allergenIngredients.keySet()).iterator().next(),
                                                       allergen);
                       });
            }
            allergens.removeAll(allergenIngredients.values());
        }

        System.out.println(allergenIngredients);

        // Leftover ingredients
        List<String> leftoverIngredients = ListUtils.removeAll(ingredients, allergenIngredients.keySet());
        System.out.println(leftoverIngredients);

        // Count how many times they appear in food
        int solution = allFood.stream()
                              .mapToInt(f -> CollectionUtils.intersection(f.getIngredients(), leftoverIngredients).size())
                              .sum();
        System.out.println("The ingredients appear " + solution + " times.");
    }

    /**
     */
    private static void part2() {
    }

    private static class Food {
        private final List<String> ingredients;
        private final List<String> allergens;

        /**
         * Parse a line describing the ingredients and allergens in this food.
         * 
         * @param line
         *            The ingredients, followed by the allergens.
         */
        public Food(String line) {
            this.ingredients = Arrays.asList(line.split(" \\(contains ")[0].split(" "));
            this.allergens = Arrays.asList(line.substring(0, line.length() - 1).split(" \\(contains ")[1].split(", "));
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public List<String> getAllergens() {
            return allergens;
        }

        @Override
        public String toString() {
            return this.ingredients.stream().collect(Collectors.joining(" ")) +
                   " (contains " +
                   this.allergens.stream().collect(Collectors.joining(", ")) +
                   ")";
        }
    }
}
