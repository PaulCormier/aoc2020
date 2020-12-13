package aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2020/day/13
 *
 * @author Paul Cormier
 * 
 */
public class Day13 {

    private static final String INPUT_TXT = "Input-Day13.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day13.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Find the next bus to take.
     */
    private static void part1() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);

        // The first line is the current timestamp
        final long timestamp = Long.parseLong(lines.get(0));

        // The second line is all the running bus numbers.
        int nextBus = Stream.of(lines.get(1).split(","))
                            .filter(StringUtils::isNumeric)
                            .map(Integer::valueOf)
                            .min((a, b) -> Long.compare(a - timestamp % a, b - timestamp % b))
                            .orElse(0);

        int waitTime = nextBus - (int) (timestamp) % nextBus;
        System.out.println("The nex bus is #" + nextBus + " in " + waitTime + " minutes.");
        System.out.println("Solution: " + waitTime * nextBus);

    }

    /**
     * Find the time when the busses depart in a particular order.
     */
    private static void part2() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);

        // The second line is all the running bus numbers.
        String[] busRoutes = lines.get(1).split(",");

        System.out.println(Arrays.toString(busRoutes));

        // Chinese remainder theorem?
        // https://www.freecodecamp.org/news/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0/
        int[] numbers = Stream.of(busRoutes)
                              .filter(StringUtils::isNumeric)
                              .mapToInt(Integer::parseInt)
                              .toArray();

        long[] remainders = new long[numbers.length];
        int j = 0;
        for (int i = 0; i < busRoutes.length; i++) {
            if (StringUtils.isNumeric(busRoutes[i]))
                remainders[j++] = (numbers[j - 1] - i) % numbers[j - 1];
        }

        System.out.println(Arrays.toString(numbers));
        System.out.println(Arrays.toString(remainders));

        // Step 1: Find the product of all the numbers in the first array.
        long product = 1;
        for (int i = 0; i < numbers.length; i++) {
            product *= numbers[i];
        }

        // Step 2: Find the partial product of each number.
        long[] partialProduct = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            partialProduct[i] = product / numbers[i];
        }

        // 3. Find the modular multiplicative inverse of number[i] modulo
        // partialProduct[i].
        long[] inverse = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            inverse[i] = computeInverse(partialProduct[i], numbers[i]);
        }

        // Step 4: Final Sum
        long sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += partialProduct[i] * inverse[i] * remainders[i];
        }

        System.out.println("Final number: " + sum % product);

    }

    public static long computeInverse(long a, long b) {
        long m = b, t, q;
        long x = 0, y = 1;
        if (b == 1)
            return 0;
        // Apply extended Euclid Algorithm
        while (a > 1) {
            // q is quotient
            q = a / b;
            t = b;
            // now proceed same as Euclid's algorithm
            b = a % b;
            a = t;
            t = x;
            x = y - q * x;
            y = t;
        } // Make x1 positive
        if (y < 0)
            y += m;
        return y;
    }

    /**
     * Find the time when the busses depart in a particular order.
     */
    private static void part2_slow() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);

        // The second line is all the running bus numbers.
        String[] busRoutes = lines.get(1).split(",");

        System.out.println(Arrays.toString(busRoutes));

        long numberOfRoutes = Stream.of(busRoutes)
                                    .filter(StringUtils::isNumeric)
                                    .count();

        // Just brute force it
        long t = 1;
        int n = 0;
        while (t < Long.MAX_VALUE && n < numberOfRoutes) {
            n = 0;
            for (int i = 0; i < busRoutes.length; i++) {
                if (StringUtils.isNumeric(busRoutes[i])) {
                    if ((t + i) % Integer.parseInt(busRoutes[i]) != 0)
                        break;
                    n++;
                }
            }
            t++;
        }

        System.out.println("The starting time is: " + t);
    }
}
