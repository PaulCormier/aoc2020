package aoc2020;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        } while (instructionPointer < instructions.size() && executedLines.add(instructionPointer));

        System.out.println("The accumulator = " + accumulator);
    }

    /**
     * Fix the program, what is the accumulator value at the end?
     */
    private static void part2() {
        List<String> instructions = FileUtils.readFile(INPUT_TXT);
        int accumulator = 0;
        Set<Integer> executedLines = new HashSet<>();

        // Try changing each "nop"/"jmp" until something works
        int operationToChange = 0;

        // There are only so many "nop"/"jmp" instructions
        long totalOperations = instructions.stream().filter(i -> !i.startsWith("acc")).count();
        while (++operationToChange < totalOperations) {
            // Reset, re-run
            accumulator = 0;
            executedLines.clear();

            int instructionPointer = 0;
            String instruction;
            int currentOperation = 0;
            // Run the instructions until a duplicate is found
            do {
                instruction = instructions.get(instructionPointer);

                String operation = instruction.substring(0, 3);
                // If this is the n-th "nop"/"jmp" operation, switch it.
                if (operationToChange > currentOperation && ("nop".equals(operation) || "jmp".equals(operation))) {
                    currentOperation++;
                    if (operationToChange == currentOperation)
                        operation = "nop".equals(operation) ? "jmp" : "nop";
                }

                // Execute the operation
                switch (operation) {
                    case "acc":
                        accumulator += Integer.parseInt(instruction.substring(4));
                    case "nop":
                        instructionPointer++;
                        break;
                    case "jmp":
                        instructionPointer += Integer.parseInt(instruction.substring(4));
                        break;
                }
            } while (instructionPointer < instructions.size() && executedLines.add(instructionPointer));
            if (instructionPointer == instructions.size()) {
                System.out.println("The program terminated successfully!");
                System.out.println("Changed instruction: " + instructions.stream()
                                                                         .filter(i -> !i.startsWith("acc"))
                                                                         .collect(Collectors.toList())
                                                                         .get(operationToChange));
                break;
            }
        }
        System.out.println("The accumulator = " + accumulator);
    }

}
