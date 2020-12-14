package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2020/day/14
 *
 * @author Paul Cormier
 * 
 */
public class Day14 {

    private static final String INPUT_TXT = "Input-Day14.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day14.txt";
    private static final String TEST_INPUT_2_TXT = "TestInput2-Day14.txt";

    public static void main(String[] args) {
        //        part1();
        part2();
    }

    /**
     * What is the sum of all values left in memory after it completes?
     */
    private static void part1() {

        Memory shipMemory = new Memory();

        FileUtils.readFileToStream(INPUT_TXT)
                 .peek(System.out::println)
                 .map(i -> i.split(" = "))
                 .forEach(instruction -> {
                     if ("mask".equals(instruction[0]))
                         shipMemory.setMask(instruction[1]);
                     else
                         shipMemory.setValue(Integer.parseInt(StringUtils.getDigits(instruction[0])),
                                             Long.parseLong(instruction[1]));
                 });

        System.out.println("\nShip's memory: " + shipMemory);
        System.out.println("Sum of memory values: " + shipMemory.getValues().values().stream().collect(Collectors.summingLong(x -> x)));

    }

    /**
     * Using a version 2 chip, what is the sum of all values left in memory
     * after it completes?
     */
    private static void part2() {

        Memory shipMemory = new MemoryV2();

//        FileUtils.readFileToStream(TEST_INPUT_2_TXT)
        FileUtils.readFileToStream(INPUT_TXT)
                 .peek(System.out::println)
                 .map(i -> i.split(" = "))
                 .forEach(instruction -> {
                     if ("mask".equals(instruction[0]))
                         shipMemory.setMask(instruction[1]);
                     else
                         shipMemory.setValue(Integer.parseInt(StringUtils.getDigits(instruction[0])),
                                             Long.parseLong(instruction[1]));
                 });

        System.out.println("\nShip's memory: " + shipMemory);
        System.out.println("Sum of memory values: " + shipMemory.getValues().values().stream().collect(Collectors.summingLong(x -> x)));
    }

    /**
     * Representation of the ferry's memory. Version 1 decoder chip.
     */
    private static class Memory {
        protected static final char X = 'X';
        protected String mask;
        protected final Map<Long, Long> values = new HashMap<>();
        protected long onesMask = 0;
        protected long zerosMask = (long) Math.pow(2, 36);

        public void setMask(String mask) {
            this.mask = mask;

            // 1s in the mask are "or"ed and 0s are "and"ed.
            onesMask = Long.parseLong(mask.replace(X, '0'), 2);
            zerosMask = Long.parseLong(mask.replace(X, '1'), 2);
        }

        public Map<Long, Long> getValues() {
            return this.values;
        }

        /**
         * Set a value in the memory, after applying the current mask.
         * 
         * @param index
         *            The memory index to update.
         * @param value
         *            The value to bet set in memory, which will have the
         *            current mask applied.
         */
        public void setValue(long index, long value) {
            values.put(index, applyMask(value));
        }

        /**
         * Apply the current mask to the given value, and return it.
         * 
         * @param value
         *            The value to be masked.
         * @return The modified value, after applying the mask.
         */
        private long applyMask(long value) {
            // 1s in the mask are "or"ed and 0s are "and"ed.
            return (value | onesMask) & zerosMask;
        }

        @Override
        public String toString() {
            return String.format("mask = %s%n%s", mask,
                                 values.entrySet().stream()
                                       .map(e -> String.format("mem[%d] = %d%n", e.getKey(), e.getValue()))
                                       .collect(Collectors.joining()));
        }
    }

    /**
     * Representation of the ferry's memory. Version 2 decoder chip.
     */
    private static class MemoryV2 extends Memory {

        /**
         * Updates all memory addresses after applying the mask to the input
         * address.
         */
        @Override
        public void setValue(long index, long value) {
            applyMask(index).forEach(i -> values.put(i, value));
        }

        private List<Long> applyMask(long value) {
            // The onesMask is "or"ed with the value
            value |= onesMask;
            // The zerosMask has no effect.

            List<Long> maskedValues = new ArrayList<>();

            // Each 'X' bit represents both a 1 and a 0
            int xBits = StringUtils.countMatches(this.mask, X);

            // For each combination (2^xBits), replace the appropriate X
            for (int i = 0; i < Math.pow(2, xBits); i++) {
                String tempMask = mask.replace('0', '1');
                for (int j = xBits - 1; j >= 0; j--) {
                    tempMask = tempMask.replaceFirst("" + X, (i & (int) Math.pow(2, j)) == 0 ? "0" : "1");
                }
                // "and" all the 1s, "or" all the 0s...
                long tempValue = value & Long.parseLong(tempMask, 2);

                tempMask = mask.replace('1', '0');
                for (int j = xBits - 1; j >= 0; j--) {
                    tempMask = tempMask.replaceFirst("" + X, (i & (int) Math.pow(2, j)) == 0 ? "0" : "1");
                }
                maskedValues.add(tempValue | Long.parseLong(tempMask, 2));
            }

            return maskedValues;
        }
    }

}
