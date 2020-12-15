package aoc2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2020/day/15
 *
 * @author Paul Cormier
 * 
 */
public class Day15 {

    private static final int[][] PUZZLE_INPUT = { { 8, 0, 17, 4, 1, 12 } };
    private static final int[][] TEST_INPUT = { { 0, 3, 6 },
                                                { 1, 3, 2 },
                                                { 2, 1, 3 },
                                                { 1, 2, 3 },
                                                { 2, 3, 1 },
                                                { 3, 2, 1 },
                                                { 3, 1, 2 } };
    private static final int[] TEST_OUTPUT = { 436, 1, 10, 27, 78, 438, 1836 };

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What is the 2020th number in the sequence?
     */
    private static void part1() {

        // Run through all of the test cases
        for (int[] startingNumbers : PUZZLE_INPUT) {
            //        int[] startingNumbers = TEST_INPUT[0];

            // The sequence of numbers
            int[] spokenNumbers = new int[2020];

            // Fill it with the starting numbers
            System.arraycopy(startingNumbers, 0, spokenNumbers, 0, startingNumbers.length);

            // Fill in the rest of the sequence
            for (int i = startingNumbers.length; i < spokenNumbers.length; i++) {
                int targetNumber = spokenNumbers[i - 1];
                int age = 0;
                // Starting with the last number, search backwards for its previous occurrence
                for (int j = i - 2; j >= 0; j--) {
                    if (spokenNumbers[j] == targetNumber) {
                        age = i - j - 1;
                        break;
                    }
                }
                spokenNumbers[i] = age;
            }
            //        System.out.println(Arrays.toString(spokenNumbers));
            System.out.println("Given the starting numbers " + Arrays.toString(startingNumbers) +
                               ", the 2020th number spoken is " + spokenNumbers[spokenNumbers.length - 1] + ".");

        }
    }

    /**
     * What is the 30,000,000th number in the sequence?
     */
    private static void part2() {
        int maxNumber = 30_000_000;
        // int maxNumber = 2020;

        // Run through all of the test cases
        for (int[] startingNumbers : PUZZLE_INPUT) {
            // int[] startingNumbers = TEST_INPUT[0];

            // Obviously, can't just keep the whole collection.
            // Only the previous instance of each number needs to be kept.
            Map<Integer, Integer> spokenNumbers = new HashMap<>();

            // Fill it with the starting numbers
            for (int i = 0; i < startingNumbers.length; i++) {
                spokenNumbers.put(startingNumbers[i], i + 1);
            }

            Integer previouslySpoken = null;
            int lastSpoken = startingNumbers[startingNumbers.length - 1];

            // Fill in the rest of the sequence
            for (int turn = startingNumbers.length + 1; turn <= maxNumber; turn++) {
                int nextSpoken;
                if (previouslySpoken == null)
                    nextSpoken = 0;
                else
                    nextSpoken = turn - 1 - previouslySpoken;

                previouslySpoken = spokenNumbers.put(nextSpoken, turn);

                lastSpoken = nextSpoken;
            }

            // System.out.println(spokenNumbers);
            System.out.println("Given the starting numbers " + Arrays.toString(startingNumbers) +
                               ", the " + maxNumber + "th number spoken is " + lastSpoken + ".");

        }
    }

}
