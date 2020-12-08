package aoc2020;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://adventofcode.com/2020/day/8
 *
 * @author Paul Cormier
 * 
 */
public class Day8 {

    private static final String INPUT_TXT = "Input-Day8.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day8.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Immediately before any instruction is executed a second time, what value
     * is in the accumulator?
     */
    private static void part1() {
        List<String> instructions = FileUtils.readFile(INPUT_TXT);
        int instructionPointer = 0;
        int accumulator = 0;
        Set<Integer> executedLines = new HashSet<>();

        // Run the instructions until a duplicate is found
        String instruction;
        do {
            instruction = instructions.get(instructionPointer);
            switch (instruction.substring(0, 3)) {
                case "acc":
                    accumulator += Integer.parseInt(instruction.substring(4));
                case "nop":
                    instructionPointer++;
                    break;
                case "jmp":
                    instructionPointer += Integer.parseInt(instruction.substring(4));
                    break;
            }
        } while (executedLines.add(instructionPointer));

        System.out.println("The accumulator = " + accumulator);
    }

    /**
     */
    private static void part2() {
    }

}
