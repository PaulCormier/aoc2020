package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        //        part2(readFile(INPUT_TXT));
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
     * <li>right 1, down 1</li>
     * <li>right 3, down 1</li>
     * <li>Right 5, down 1</li>
     * <li>Right 7, down 1</li>
     * <li>Right 1, down 2</li>
     * </ul>
     */
    private static void part2(List<String> list) {
        
        int column = 0;
        int row = 0;
        int count = 0;
        List<Integer> trees = new ArrayList<>();
        
        System.out.println(list.get(row++));
        for (String line : list.subList(row, list.size())) {
            column += 3;
            column %= LINE_LENGTH;
            if (line.charAt(column) == '#') {
                count++;
                System.out.println(line.substring(0, column) + "X" + line.substring(column + 1));
            } else {
                System.out.println(line.substring(0, column) + "O" + line.substring(column + 1));
            }

        }
        System.out.println(trees + " trees are encountered.");
    }
}
