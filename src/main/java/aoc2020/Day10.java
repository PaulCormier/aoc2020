package aoc2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2020/day/10
 *
 * @author Paul Cormier
 * 
 */
public class Day10 {

    private static final String INPUT_TXT = "Input-Day10.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day10.txt";
    private static final String TEST_INPUT_2_TXT = "TestInput2-Day10.txt";

    public static void main(String[] args) {
        part1();

    }

    /**
     * Count the differences between the Joltages in the list.
     */
    private static void part1() {
        List<Integer> joltages = FileUtils.readFileToStream(INPUT_TXT)
                                          .map(Integer::valueOf)
                                          .sorted()
                                          .collect(Collectors.toList());

        List<Integer> differences = new ArrayList<>();
        Integer previousJoltage = 0;
        for (Integer joltage : joltages) {
            differences.add(joltage - previousJoltage);
            previousJoltage = joltage;
        }
        Map<Object, List<Integer>> aggregateDifferences = differences.stream().collect(Collectors.groupingBy(d -> d));

        System.out.println("There are " + aggregateDifferences.get(1).size() + " 1s, and " +
                           (aggregateDifferences.get(3).size() + 1) + " 3s." +
                           " Their product is: " + (aggregateDifferences.get(1).size() * (aggregateDifferences.get(3).size() + 1)));
    }

    /**
     */
    private static void part2() {
    }

}
