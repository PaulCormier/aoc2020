package aoc2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final String TEST_INPUT2_TXT = "TestInput2-Day7.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * How many bag colours can eventually contain at least one shiny gold bag?
     */
    private static void part1() {
        // Parse the rules into a Map of bags, by colour.
        List<String> rules = FileUtils.readFile(INPUT_TXT);
        Map<String, Bag> bagMap = mapRules(rules);

        // Collect each level of potential containers for the target bag colour.
        Bag targetColour = bagMap.get("shiny gold");

        // Find what can contain this colour
        Set<Bag> containers = new HashSet<>();

        List<Bag> tempContainers = bagMap.values().stream()
                                         .filter(b -> b.contents.containsKey(targetColour))
                                         .collect(Collectors.toList());
        while (containers.addAll(tempContainers)) {
            tempContainers = tempContainers.stream()
                                           .flatMap(bag -> bagMap.values()
                                                                 .stream()
                                                                 .filter(b -> b.contents.containsKey(bag)))
                                           .distinct()
                                           .collect(Collectors.toList());
        }
        System.out.println(containers);
        System.out.println("There are " + containers.size() + " which can hold that bag.");
    }

    /**
     * Parse the rules into a map of bags, indexed by colour.
     * 
     * @param rules
     *     The list of rules indicating what colours of bags (and how
     *     many) can be in each type of bag.
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
     * How many bags are in the target bag?
     */
    private static void part2() {
        // Parse the rules into a Map of bags, by colour.
        List<String> rules = FileUtils.readFile(INPUT_TXT);
        Map<String, Bag> bagMap = mapRules(rules);

        // Recursively count the number of bags in each bag.
        Bag targetColour = bagMap.get("shiny gold");
        int totalBags = countBags(targetColour);
        System.out.println("Total numbers of bags: " + totalBags);
    }

    /**
     * Count how many bags are in this bag. WARNING! This is a recursive call.
     * 
     * @param bag The parent bag which may contain other bags.
     * @return The total number of bags.
     */
    private static int countBags(Bag bag) {
        System.out.println(bag);
        return bag.getContents().entrySet()
                  .stream()
                  .mapToInt(e -> e.getValue() * (countBags(e.getKey()) + 1))
                  .sum();
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
            return colour + " bags contain " +
                    contents.entrySet().stream()
                            .map(e -> e.getValue() + " " + e.getKey().getColour() + " bag"
                                    + (e.getValue() > 1 ? "s" : ""))
                            .collect(Collectors.joining(", "))
                    + ".";
        }

    }

}
