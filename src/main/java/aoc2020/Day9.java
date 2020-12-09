package aoc2020;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
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
        int bracketSize = 25;
        List<Long> numbers = FileUtils.readFileToStream(INPUT_TXT)
                                      .map(Long::valueOf)
                                      .collect(Collectors.toList());

        long invalidNumber = part1(numbers, bracketSize);
        System.out.println(invalidNumber + " is not the sum of the preceding " + bracketSize + " numbers.");

        List<Long> sequence = part2(numbers, invalidNumber);
        LongSummaryStatistics summary = sequence.stream().collect(Collectors.summarizingLong(l -> l));
        System.out.println("The sequence: " + sequence + " sums to " + invalidNumber +
                           " and has a maximum of: " + summary.getMax() +
                           ", and a minimum of: " + summary.getMin());
        System.out.println("The encryption weakness in your XMAS-encrypted list of numbers is: " +
                           (summary.getMax() + summary.getMin()));
    }

    /**
     * Find the first number that isn't the sum of two different preceding
     * numbers.
     * 
     * @param numbers
     *            The sequence of numbers to test.
     * @param bracketSize
     *            The size of the bracket to look for numbers which sum to the
     *            current number.
     * 
     * @return The first number that isn't the sum of two different preceding
     *         numbers.
     */
    private static long part1(List<Long> numbers, int bracketSize) {

        // The numbers in the sequence must be the sum of two numbers in this queue
        List<Long> numbersToCheck = new ArrayList<>(numbers.subList(0, bracketSize));

        for (Long number : numbers.subList(bracketSize, numbers.size())) {
            if (!findAllSums(numbersToCheck).contains(number)) {
                return number;
            }
            numbersToCheck.remove(0);
            numbersToCheck.add(number);
        }

        return 0;
    }

    /**
     * Using the invalid number from part 1, find a sequence which sums to it,
     * and return it.
     * 
     * @param numbers
     *            The sequence of numbers to test.
     * @param invalidNumber
     *            The number from part 1 to which a sequence of numbers will
     *            sum.
     * @return The sequence of numbers which sum to the invalid numbers.
     */
    private static List<Long> part2(List<Long> numbers, long invalidNumber) {
        // Just try everything
        for (int i = 0; i < numbers.size(); i++) {
            long sum = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                sum += numbers.get(j);
                if (sum == invalidNumber)
                    return numbers.subList(i, j + 1);
                if (sum > invalidNumber)
                    break;
            }
        }

        return Collections.emptyList();
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
