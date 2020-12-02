package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2020/day/1
 * 
 * @author Paul Cormier
 *
 */
public class Day1 {

    private static final String DAY1_INPUT_TXT = "Day1Input.txt";

    public static void main(String[] args) {
        try {
            part1();
            part1Avery();
            part2();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find two numbers that sum to 2020 and print their product.
     */
    private static void part1() throws IOException, URISyntaxException {

        List<Integer> checkedNumbers = new ArrayList<>();
        // Read all lines
        List<Integer> allNumbers = Files.lines(Paths.get(ClassLoader.getSystemResource(DAY1_INPUT_TXT).toURI()))
                                        .map(Integer::valueOf)
                                        .collect(Collectors.toList());
        Optional<Integer> result;
        // Check each number
        for (Integer number : allNumbers) {
            // Check against the list of previously checked numbers
            result = checkedNumbers.stream()
                                   .filter(n -> n + number == 2020)
                                   .findAny();
            // Got a hit? Print and done.
            if (result.isPresent()) {
                System.out.println("The result is: " + result.get() * number + " (" + number + ", " + result.get() + ")");
                break;
            } else {
                // Keep track of non-hits
                checkedNumbers.add(number);
            }
        }

        /*
        // Didn't work! :(
        try {
            Files.lines(Paths.get(ClassLoader.getSystemResource(DAY1_INPUT_TXT).toURI()))
                 .mapToInt(Integer::valueOf).boxed()
                 .peek(n->System.out.println(n))
                 .map(n -> checkedNumbers.stream()
                                         .filter(p -> n + p == 2020)
                                         .findAny()
                                         .map(p -> n * p))
                 .filter(Optional::isPresent)
                 .findAny()
                 .map(Optional::get)
                 .ifPresent(r -> System.out.println("The result is: " + r));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        */

    }

    /**
     * Create a target list of values which would sum to 2020. <i>Avery's
     * algorithm.</i>
     */
    private static void part1Avery() throws IOException, URISyntaxException {
        List<Integer> allNumbers = Files.lines(Paths.get(ClassLoader.getSystemResource(DAY1_INPUT_TXT).toURI()))
                                        .map(Integer::valueOf)
                                        .collect(Collectors.toList());
        List<Integer> targetNumbers = allNumbers.stream().map(n -> 2020 - n).collect(Collectors.toList());

        allNumbers.retainAll(targetNumbers);
        int number1 = allNumbers.get(0);
        int number2 = allNumbers.get(1);

        System.out.println("The result is: " + number1 * number2 + " (" + number1 + ", " + number2 + ")");
    }

    /**
     * Find three numbers that sum to 2020 and print their product.
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    private static void part2() throws IOException, URISyntaxException {
        // Read all lines
        int[] allNumbers = Files.lines(Paths.get(ClassLoader.getSystemResource(DAY1_INPUT_TXT).toURI()))
                                .mapToInt(Integer::valueOf)
                                .toArray();

        // Just go through the array... and check everything after it.
        for (int i = 0; i < allNumbers.length; i++) {
            for (int j = i + 1; j < allNumbers.length; j++) {
                for (int k = j + 1; k < allNumbers.length; k++) {
                    if (allNumbers[i] + allNumbers[j] + allNumbers[k] == 2020)
                        System.out.println("The result is: " + allNumbers[i] * allNumbers[j] * allNumbers[k] + " (" +
                                           allNumbers[i] + ", " + allNumbers[j] + ", " + allNumbers[k] + ")");
                }
            }
        }
    }
    /*// Ugh. Too fancy... 
     private static void part2() {
    
        List<Integer> checkedNumbers = new ArrayList<>();
        List<Integer> doubleCheckedNumbers = new ArrayList<>();
        try {
            // Read all lines
            List<Integer> allNumbers = Files.lines(Paths.get(ClassLoader.getSystemResource(DAY1_INPUT_TXT).toURI()))
                                            .map(Integer::valueOf)
                                            .collect(Collectors.toList());
            // Check each number 
            Optional<Integer> secondResult;
            List<Integer> firstResults;
            for (Integer number : allNumbers) {
                // Check against the lists of previously checked numbers
                // First, any that sum to less than 2020
                firstResults = checkedNumbers.stream()
                                             .filter(n -> n + number < 2020)
                                             .collect(Collectors.toList());
    
                // Can any, that aren't the same, add to 2020?
                secondResult = Optional.empty();
                for (Integer secondNumber : firstResults) {
                    secondResult = checkedNumbers.stream()
                                                 .filter(n -> n + number + secondNumber == 2020)
                                                 .findAny();
                    if(secondResult.isPresent())
                        break;
                }
    
                // Got a hit? Print and done.
                if (secondResult.isPresent()) {
                    Integer number2 = secondResult.get();
                    System.out.println("The result is: " + number2 * number + " (" + number + ", " + number2 + ")");
                    break;
                } else {
                    // Keep track of non-hits
                    checkedNumbers.add(number);
                }
            }
        } catch (IOException | URISyntaxException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}
