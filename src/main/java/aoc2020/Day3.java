package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2020/day/3
 * 
 * @author Paul Cormier
 *
 */
public class Day3 {

    private static final String INPUT_TXT = "Day3Input.txt";
    private static final int LINE_LENGTH = 31;
    //    private static final String INPUT_TXT = "Day3TestInput.txt";
    //    private static final int LINE_LENGTH = 11;

    public static void main(String[] args) {
        part1(readFile(INPUT_TXT));
        part2(readFile(INPUT_TXT).toArray(new String[] {}));
    }

    private static List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Count trees encountered following slope: right 3, down 1.
     */
    private static void part1(List<String> list) {
        System.out.println(list.get(0));
        int column = 0;
        int trees = 0;
        for (String line : list.subList(1, list.size())) {
            column += 3;
            column %= LINE_LENGTH;
            if (line.charAt(column) == '#') {
                trees++;
                System.out.println(line.substring(0, column) + "X" + line.substring(column + 1));
            } else {
                System.out.println(line.substring(0, column) + "O" + line.substring(column + 1));
            }

        }
        System.out.println(trees + " trees are encountered.");
    }

    /**
     * Count trees encountered following slopes:
     * <ul>
     * <li>Right 1, down 1</li>
     * <li>Right 3, down 1</li>
     * <li>Right 5, down 1</li>
     * <li>Right 7, down 1</li>
     * <li>Right 1, down 2</li>
     * </ul>
     */
    private static void part2(String[] list) {

        int[][] slopes = { { 1, 1 },
                           { 3, 1 },
                           { 5, 1 },
                           { 7, 1 },
                           { 1, 2 } };

        List<Long> trees = new ArrayList<>();

        for (int[] slope : slopes) {

            int column = 0;
            int count = 0;

            for (int i = 0; i < list.length; i += slope[1]) {
                String line = list[i];

                if (line.charAt(column) == '#') {
                    count++;
                    System.out.println(line.substring(0, column) + "X" + line.substring(column + 1));
                } else {
                    System.out.println(line.substring(0, column) + "O" + line.substring(column + 1));
                }

                column += slope[0];
                column %= LINE_LENGTH;
            }
            trees.add((long) count);
            System.out.println(count + " trees are encountered.");
        }
        long product = trees.stream().collect(Collectors.reducing(1L, Math::multiplyExact)).longValue();
        System.out.println(product + " is the product of the numbers of trees encountered: " + trees);
    }
}
