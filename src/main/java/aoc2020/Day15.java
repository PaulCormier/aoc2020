package aoc2020;

import java.util.Arrays;

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
     */
    private static void part2() {

    }

}
