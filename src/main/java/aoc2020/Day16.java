package aoc2020;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;

/**
 * https://adventofcode.com/2020/day/16
 *
 * @author Paul Cormier
 * 
 */
public class Day16 {

    private static final int[] YOUR_TICKET = { 191, 89, 73, 139, 71, 103, 109, 53, 97, 179, 59, 67, 79, 101, 113, 157, 61, 107, 181, 137 };
    private static final int[] TEST_TICKET = { 7, 1, 14 };
    private static final int[] TEST2_TICKET = { 11, 12, 13 };

    // Ticket rules
    private static final String INPUT_TXT = "Input-Day16.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day16.txt";
    private static final String TEST2_INPUT_TXT = "Test2Input-Day16.txt";
    // Tickets
    private static final String INPUT_2_TXT = "Input2-Day16.txt";
    private static final String TEST_INPUT_2_TXT = "TestInput2-Day16.txt";
    private static final String TEST2_INPUT_2_TXT = "Test2Input2-Day16.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What is your ticket scanning error rate?
     */
    private static void part1() {
        // Parse the rules
        List<TicketRule> rules = FileUtils.readFileToStream(INPUT_TXT)
                                          .map(TicketRule::new)
                                          .collect(Collectors.toList());

        // Check the nearby tickets
        List<List<Integer>> nearbyTickets = FileUtils.readFileToStream(INPUT_2_TXT)
                                                     .map(l -> Arrays.stream(l.split(","))
                                                                     .map(Integer::valueOf)
                                                                     .collect(Collectors.toList()))
                                                     .collect(Collectors.toList());

        Integer errorScanningRate = nearbyTickets.stream()
                                                 .flatMap(Collection::stream)
                                                 .filter(i -> rules.stream().noneMatch(r -> r.fieldIsValid(i)))
                                                 .collect(Collectors.summingInt(x -> x));

        System.out.println("The ticket error scannig rate is: " + errorScanningRate);

    }

    /**
     */
    private static void part2() {
        // Parse the rules
        List<TicketRule> rules = FileUtils.readFileToStream(TEST2_INPUT_TXT)
                                          .map(TicketRule::new)
                                          .collect(Collectors.toList());

        // Check the nearby tickets, and only keep valid ones.
        List<List<Integer>> nearbyTickets = FileUtils.readFileToStream(TEST2_INPUT_2_TXT)
                                                     .map(line -> Arrays.stream(line.split(","))
                                                                        .map(Integer::valueOf)
                                                                        .collect(Collectors.toList()))
                                                     .filter(list -> list.stream()
                                                                         .allMatch(field -> rules.stream()
                                                                                                 .anyMatch(rule -> rule.fieldIsValid(field))))
                                                     .collect(Collectors.toList());

        System.out.println("The ticket error scannig rate is: ");

    }

    private static class TicketRule {
        String field;
        final Range<Integer> range1;
        final Range<Integer> range2;

        /**
         * Parse the given rule string into a TicketRule.
         * 
         * @param rule
         *            ex: class: 1-3 or 5-7
         */
        public TicketRule(String rule) {
            this.field = rule.split(": ")[0];

            String[] ranges = rule.split(": ")[1].split(" or ");

            Integer low = Integer.valueOf(ranges[0].split("-")[0]);
            Integer high = Integer.valueOf(ranges[0].split("-")[1]);
            this.range1 = Range.between(low, high);

            low = Integer.valueOf(ranges[1].split("-")[0]);
            high = Integer.valueOf(ranges[1].split("-")[1]);
            this.range2 = Range.between(low, high);
        }

        public boolean fieldIsValid(int field) {
            return range1.contains(field) || range2.contains(field);
        }
    }
}
