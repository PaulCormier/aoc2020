package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2020/day/6
 * 
 * @author Paul Cormier
 * 
 */
public class Day6 {

    private static final String INPUT_TXT = "Day6Input.txt";
    private static final String TEST_INPUT_TXT = "Day6TestInput.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What is the sum of those counts? (Any person answered yes)
     */
    private static void part1() {
        long sumOfAnswers = FileUtils.cleanInput(FileUtils.readFileToStream(INPUT_TXT))
                                     .map(l -> l.replace(" ", ""))
                                     // .peek(System.out::print)
                                     .map(l -> l.chars().distinct().count())
                                     // .peek(System.out::println)
                                     .collect(Collectors.summingLong(c -> c));

        System.out.println("The sum of the counts is: " + sumOfAnswers);
    }

    /**
     * What is the sum of those counts? (Every person answered yes)
     */
    private static void part2() {
        int sumOfAnswers = FileUtils.cleanInput(FileUtils.readFileToStream(INPUT_TXT))
                                    .map(l -> l.split(" "))
                                    // .peek(a -> System.out.print(Arrays.toString(a) + " "))
                                    .map(Day6::countAllYes)
                                    // .peek(System.out::println)
                                    .collect(Collectors.summingInt(c -> c));

        System.out.println("The sum of the counts is: " + sumOfAnswers);
    }

    /**
     * For each group of answers count the number of characters which appear in all
     * answers.
     * 
     * @param answers An array of characters representing the "yes" answers.
     * @return A count of characters which appear in all answers.
     */
    private static int countAllYes(String[] answers) {
        List<String> characters = new ArrayList<>(Arrays.asList(answers[0].split("")));
        for (String answer : answers) {
            characters.retainAll(Arrays.asList(answer.split("")));
        }
        return characters.size();
    }

}
