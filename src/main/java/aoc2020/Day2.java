package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2020/day/2
 * 
 * @author Paul Cormier
 *
 */
public class Day2 {

    private static final String DAY2_INPUT_TXT = "Day2Input.txt";

    public static void main(String[] args) {
        part1(readFile(DAY2_INPUT_TXT));
        part2(readFile(DAY2_INPUT_TXT));
    }

    private static Stream<String> readFile(String fileName) {
        try {
            return Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private static void part1(Stream<String> lines) {

        long validPasswords = lines.map(Day2::parseLine)
                                   //.peek(p -> System.out.println(Arrays.deepToString(p)))
                                   .filter(Day2::isValid)
                                   .count();

        System.out.println("There are " + validPasswords + " valid passwords.");
    }

    private static void part2(Stream<String> lines) {

        long validPasswords = lines.map(Day2::parseLine)
                                   .filter(Day2::isValidNew)
                                   //.peek(p -> System.out.println(Arrays.deepToString(p)))
                                   .count();

        System.out.println("There are " + validPasswords + " valid passwords.");
    }

    /**
     * Split the line into the occurrences, character, password. ex: 1-3 a:
     * abcde
     * 
     * @param line
     *            The line from the input file.
     * @return A split up version of the line.
     */
    private static String[] parseLine(String line) {
        return line.split(":? ");
    }

    private static boolean isValid(String[] verification) {
        String occurrences = verification[0];
        String letter = verification[1];
        String password = verification[2];

        // Construct a regular expression to look for the occurrences of the letter.
        String regex = String.format("([^%1$s]*%1$s[^%1$s]*){%2$s}", letter, occurrences.replace('-', ','));
        return password.matches(regex);
    }

    private static boolean isValidNew(String[] verification) {
        String[] positionChars = verification[0].split("-");
        int[] positions = { Integer.valueOf(positionChars[0]) - 1, Integer.valueOf(positionChars[1]) - 1 };
        char letter = verification[1].charAt(0);
        String password = verification[2];

        // Just check the char at the positions for occurrences of the letter.
        return password.charAt(positions[0]) == letter ^ password.charAt(positions[1]) == letter;
    }
}
