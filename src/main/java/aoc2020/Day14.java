package aoc2020;

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

    public static void main(String[] args) {
        part1();
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
     */
    private static void part2() {
    }

    /**
     * Representation of the ferry's memory.
     */
    private static class Memory {
        private String mask;
        private final Map<Integer, Long> values = new HashMap<>();
        private long onesMask = 0;
        private long zerosMask = 2 ^ 36;

        public void setMask(String mask) {
            this.mask = mask;

            // 1s in the mask are "or"ed and 0s are "and"ed.
            onesMask = Long.parseLong(mask.replace('X', '0'), 2);
            zerosMask = Long.parseLong(mask.replace('X', '1'), 2);
        }

        public Map<Integer, Long> getValues() {
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
        public void setValue(int index, long value) {
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
}
