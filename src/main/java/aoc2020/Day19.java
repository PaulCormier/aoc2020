package aoc2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Monster Messages
 * 
 * https://adventofcode.com/2020/day/19
 *
 * @author Paul Cormier
 * 
 */
public class Day19 {

    private static final String RULE_INPUT_TXT = "Input-Day19.txt";
    private static final String TEST_RULE_INPUT_TXT = "TestInput-Day19.txt";
    private static final String PUZZLE_INPUT_TXT = "Input2-Day19.txt";
    private static final String TEST_PUZZLE_INPUT_TXT = "TestInput2-Day19.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * How many messages completely match rule 0?
     */
    private static void part1() {
        // Parse the rules
        Map<Integer, Rule> rules = FileUtils.readFileToStream(RULE_INPUT_TXT)
                                            .map(Rule::new)
                                            .peek(System.out::println)
                                            .collect(Collectors.toMap(r -> r.id, r -> r));

        rules.values().stream().forEach(r -> System.out.println(r.getRegex(rules)));

        // Read the messages, and count how many match rule 0
        String rule0Regex = rules.get(0).getRegex(rules);
        long matching = FileUtils.readFileToStream(PUZZLE_INPUT_TXT)
                                 .filter(l -> l.matches(rule0Regex))
                                 .peek(System.out::println)
                                 .count();

        System.out.println("There are " + matching + " matching messages.");
    }

    /**
     */
    private static void part2() {
    }

    private static class Rule {
        final int id;
        final String letter;
        final List<List<Integer>> rules = new ArrayList<>();

        /**
         * Parse a rule description into a rule with one, or two, sequences of other
         * rules to follow, or a letter value.
         * 
         * 0: 4 1 5
         * 1: 2 3 | 3 2
         * 4: "a"
         * 
         * @param line The string representation of a rule.
         */
        public Rule(String line) {
            String[] ruleParts = line.split(": ");
            id = Integer.parseInt(ruleParts[0]);

            if (ruleParts[1].startsWith("\"")) {
                letter = String.valueOf(ruleParts[1].charAt(1));
            } else {
                letter = null;
                for (String rulesString : ruleParts[1].split(" \\| ")) {
                    rules.add(Stream.of(rulesString.split(" "))
                                    .map(Integer::valueOf)
                                    .collect(Collectors.toList()));
                }
            }
        }

        /**
         * Construct a regular expression for how this rule is to be applied.
         * 
         * @param allRules The entire map of rules.
         * @return A regular expression which can determine if a given string is correct
         *     for this rule.
         */
        public String getRegex(Map<Integer, Rule> allRules) {
            // If this is just a letter return it.
            if (letter != null)
                return letter;

            // Otherwise compose the string for each of the rules.
            return "(" + rules.stream()
                              .map(r1 -> r1.stream()
                                           .map(allRules::get)
                                           .map(r2 -> r2.getRegex(allRules))
                                           .collect(Collectors.joining()))
                              .collect(Collectors.joining("|"))
                   + ")";
        }

        @Override
        public String toString() {
            return String.format("%d: %s%s", id, letter == null ? "" : "\"" + letter + "\"",
                                 rules.stream()
                                      .map(r -> r.stream().map(Object::toString).collect(Collectors.joining(" ")))
                                      .collect(Collectors.joining(" | ")));
        }
    }
}
