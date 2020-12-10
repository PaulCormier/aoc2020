package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.CombinatoricsUtils;

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
        //        part1();
        part2();
    }

    /**
     * Count the differences between the Joltages in the list.
     */
    private static void part1() {
        List<Integer> joltages = FileUtils.readFileToStream(TEST_INPUT_TXT)
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
     * What is the total number of distinct ways you can arrange the adapters to
     * connect the charging outlet to your device?
     */
    private static void part2() {

        List<Integer> joltages = FileUtils.readFileToStream(TEST_INPUT_TXT)
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

        // Maximum joltage (+3)
        int maxJoltage = joltages.get(joltages.size() - 1) + 3;

        // There are only differences of 1 and 3...
        // NOPE! 
        int diff1 = aggregateDifferences.get(1).size();
        int diff3 = aggregateDifferences.get(3).size() + 1;

        // So the total difference from 0 to max+3 must be: 1x + 3y = maxJoltage
        // But the total number of combinations is xC(diff1) + yC(diff3), for each solution.

        System.out.printf("There are %d 1s, and %d 3s. They must sum to %d jolts.%n", diff1, diff3, maxJoltage);

        long totalSolutions = 0;
        for (int x = 0; x <= diff1; x++) {
            for (int y = 0; y <= diff3; y++) {
                if (x + (3 * y) == maxJoltage) {
                    long combinations = CombinatoricsUtils.binomialCoefficient(diff1, x) * CombinatoricsUtils.binomialCoefficient(diff3, y);
                    System.out.printf("%d 1s (%d), %d 3s (%d)%n", x, y,
                                      CombinatoricsUtils.binomialCoefficient(diff1, x),
                                      CombinatoricsUtils.binomialCoefficient(diff3, y));
                    totalSolutions += combinations;
                }
            }
        }

        System.out.println("There are " + totalSolutions + " total solutions.");
    }

    /**
     * Function to count total number of possible solutions of a linear equation
     * of k variables
     * 
     * @see https://www.techiedelight.com/total-possible-solutions-linear-equation-k-variables/
     * 
     * @param coeff
     *            The coefficients of the variables of the equation.
     * @param k
     *            The index of the last coefficient to solve for.
     * @param rhs
     *            The constant value which the equation is equal to.
     * @return The number of solutions to the equation with the given
     *         parameters.
     */
    public static long count(int[] coeff, int k, int rhs) {
        // if rhs becomes 0, solution is found
        if (rhs == 0)
            return 1;

        // return 0 if rhs becomes negative or no coefficient is left
        if (rhs < 0 || k < 0)
            return 0;

        // Case 1. include current coefficient coeff[k] in solution and
        // recur with remaining value (rhs - coeff[k])
        long include = count(coeff, k, rhs - coeff[k]);

        // Case 2. exclude current coefficient coeff[k] from solution and
        // recur for remaining coefficients (k - 1)
        long exclude = count(coeff, k - 1, rhs);

        // return total ways by including or excluding current coefficient
        return include + exclude;
    }
}
