package aoc2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2020/day/7
 *
 * @author Paul Cormier
 * 
 */
public class Day7 {

    private static final String INPUT_TXT = "Input-Day7.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day7.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * How many bag colours can eventually contain at least one shiny gold bag?
     */
    private static void part1() {
        // Parse the rules into a Map of bags, by colour.
        List<String> rules = FileUtils.readFile(TEST_INPUT_TXT);
        Map<String, Bag> bagMap = mapRules(rules);

        // Build a map of the containers?
        Map<Bag, Bag> containers = new HashMap<>();

        // Collect each level of potential containers for the target bag colour.
        Bag targetColour = bagMap.get("shiny gold");
        

    }

    /**
     * Parse the rules into a map of bags, indexed by colour.
     * 
     * @param rules
     *            The list of rules indicating what colours of bags (and how
     *            many) can be in each type of bag.
     * @return A map of bags, indexed by colour.
     */
    private static Map<String, Bag> mapRules(List<String> rules) {
        Map<String, Bag> bagMap = new HashMap<>();
        for (String rule : rules) {
            // light red bags contain 1 bright white bag, 2 muted yellow bags.
            // (colour) bags contain ([0-9] (colour) bags?[,.])*
            String colour = rule.split(" bags contain ")[0];
            // Check for the bag in the map
            Bag bag = bagMap.computeIfAbsent(colour, Bag::new);
            // Add the bags it may contain
            String[] contents = rule.split(" bags contain ")[1].split(" bags?[,.] ?");
            for (String content : contents) {
                String number = StringUtils.getDigits(content);
                if (StringUtils.isBlank(number))
                    break;
                colour = content.substring(2);
                Bag containedBag = bagMap.computeIfAbsent(colour, Bag::new);
                bag.getContents().put(containedBag, Integer.valueOf(number));
            }
        }

        return bagMap;
    }

    /*  private static Map<String, Bag> mapRules(List<String> rules) {
        Map<String, Bag> bagMap = new HashMap<>();
        Pattern rulePattern = Pattern.compile("(.*) bags contain(?: ([0-9]) (.*) bags?[,.])*+");
        Matcher ruleMatcher;
        for (String rule : rules) {
            // light red bags contain 1 bright white bag, 2 muted yellow bags.
            // (colour) bags contain ([0-9] (colour) bags?[,.])*
            ruleMatcher = rulePattern.matcher(rule);
            ruleMatcher.find();
            String colour = ruleMatcher.group(1);
            // Check for the bag in the map
            Bag bag = bagMap.computeIfAbsent(colour, Bag::new);
            // Add the bags it may contain
            while (!ruleMatcher.hitEnd()) {
                String number = ruleMatcher.group();
                colour = ruleMatcher.group();
                Bag containedBag = bagMap.computeIfAbsent(colour, Bag::new);
                bag.getContents().put(containedBag, Integer.valueOf(number));
            }
        }
    
        return bagMap;
    }*/

    /**
     */
    private static void part2() {

    }

    /**
     * A luggage bag, which has a descriptive colour, and may contain a certain
     * number of other bags.
     *
     */
    private static class Bag {
        private final String colour;
        private final Map<Bag, Integer> contents;

        public Bag(String colour) {
            this.colour = colour;
            this.contents = new HashMap<>();
        }

        public String getColour() {
            return colour;
        }

        public Map<Bag, Integer> getContents() {
            return contents;
        }

        @Override
        public String toString() {
            return "Bag [colour=" + colour + ", contents=" + contents + "]";
        }

    }

}
