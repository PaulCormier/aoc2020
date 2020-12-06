package aoc2020;

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
    }

}
