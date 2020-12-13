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
        part2_Avery();
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
     * Final number: 836024966345345
     */
    private static void part2() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);

        // The second line is all the running bus numbers.
        String[] busRoutes = lines.get(1).split(",");

        System.out.println(Arrays.toString(busRoutes));

        // Chinese remainder theorem?
        // https://www.freecodecamp.org/news/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0/
        // https://www.geeksforgeeks.org/chinese-remainder-theorem-set-2-implementation/
        int[] numbers = Stream.of(busRoutes)
                              .filter(StringUtils::isNumeric)
                              .mapToInt(Integer::parseInt)
                              .toArray();

        long[] remainders = new long[numbers.length];
        int j = 0;
        for (int i = 0; i < busRoutes.length; i++) {
            if (StringUtils.isNumeric(busRoutes[i])) {
                remainders[j] = numbers[j] - i % numbers[j];
                j++;
            }
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
    private static void part2_Avery() {
        /* @formatter:off
         * From Python:
         1   x="a"
         2   list1=[23,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,37,x,x,x,x,x,421,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,17,x,19,x,x,x,x,x,x,x,x,x,29,x,487,x,x,x,x,x,x,x,x,x,x,x,x,13]
         3   oAcum=0
         4   m1Acum=1
         5   for i in range(len(list1)):
         6     if(list1[i] != x):
         7       o2=i
         8       m2=list1[i]
         9       for i2 in range(m2):
        10         if((m1Acum*i2+o2+oAcum)%m2==0):
        11           oAcum=m1Acum*i2+oAcum
        12           m1Acum=m1Acum*m2
        13           break
        14   print(oAcum)
        
        @formatter:on */

        // A:1 This is used as an arbitrary non-numeric numeric value which replaces the
        // "x" in the input.

        // The second line is all the running bus numbers.
        // A:2
        String[] list1 = FileUtils.readFile(INPUT_TXT).get(1).split(",");

        System.out.println(Arrays.toString(list1));

        // A:3 Offset accumulator
        long oAcum = 0;
        // A:4 Modulus accumulator
        long m1Acum = 1;

        // A:5 Iterate through the array
        for (int i = 0; i < list1.length; i++) {
            // A:6 Python seems to return false when comparing a numeric value to a string
            if (StringUtils.isNumeric(list1[i])) {
                // A:7 Offset from target value (as in how far from the next multiple)
                int o2 = i;
                // A:8 Modulus value
                int m2 = Integer.parseInt(list1[i]);

                System.out.printf("oAcum: %d m1Acum: %d o2: %d m2: %d%n", oAcum, m1Acum, o2, m2);

                // A:9 Try values up to m2
                for (int i2 = 0; i2 < m2; i2++) {
                    // A:10 For each multiple of m1Acum, check if it (plus the offset o2) is also a
                    // multiple of m2
                    if ((m1Acum * i2 + o2 + oAcum) % m2 == 0) {
                        // A:11 The new offset increases by the multiple of m1Acum
                        oAcum = m1Acum * i2 + oAcum;
                        // A:12 The modulus accumulator increases by a factor of m2
                        m1Acum = m1Acum * m2;
                        // A:13 Found a solution for this modulus, continue
                        break;
                    }
                }
            }
            // The offset accumulator now has the minimum value that is a multiple of all
            // moduli at the expected offsets.
        }
        // A:14 The output accumulator is negative, for some reason.
        System.out.println("Final number: " + oAcum);
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
