package aoc2020;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * https://adventofcode.com/2020/day/9
 *
 * @author Paul Cormier
 * 
 */
public class Day9 {

    private static final String INPUT_TXT = "Input-Day9.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day9.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Find the first number that isn't the sum of two different preceding
     * numbers.
     */
    private static void part1() {
        List<Long> numbers = FileUtils.readFileToStream(INPUT_TXT)
                                      .map(Long::valueOf)
                                      .collect(Collectors.toList());

        int bracketSize = 25;
        // The numbers in the sequence must be the sum of two numbers in this queue
        List<Long> numbersToCheck = new ArrayList<>(numbers.subList(0, bracketSize));

        for (Long number : numbers.subList(bracketSize, numbers.size())) {
            if (!findAllSums(numbersToCheck).contains(number)) {
                System.out.println(number + " is not the sum of the preceding " + bracketSize + " numbers.");
                break;
            }
            numbersToCheck.remove(0);
            numbersToCheck.add(number);
        }
    }

    /**
     */
    private static void part2() {
    }

    /**
     * Find all the sums of the combinations of the given numbers.
     * 
     * @param numbersToSum
     *            A list of number to be summed with each other.
     * @return A list of all the sums of the combinations of the given numbers.
     */
    private static List<Long> findAllSums(List<Long> numbersToSum) {
        List<Long> sums = new ArrayList<>();
        Iterator<int[]> combinations = CombinatoricsUtils.combinationsIterator(numbersToSum.size(), 2);
        while (combinations.hasNext()) {
            final int[] combination = combinations.next();
            sums.add(numbersToSum.get(combination[0]) + numbersToSum.get(combination[1]));
        }
        return sums;
    }

}
