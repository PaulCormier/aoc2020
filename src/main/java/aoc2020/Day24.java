package aoc2020;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lobby Layout
 * 
 * https://adventofcode.com/2020/day/24
 *
 * @author Paul Cormier
 * 
 */
public class Day24 {

    private static final String PUZZLE_INPUT = "Input-Day24.txt";
    private static final String TEST_PUZZLE_INPUT = "TestInput-Day24.txt";

    public static void main(String[] args) {
        Set<String> blackTiles = part1(PUZZLE_INPUT);
        part2(blackTiles);
    }

    /**
     * After all of the instructions have been followed, how many tiles are left
     * with the black side up?
     * 
     * @param puzzleInput
     * @return
     */
    private static Set<String> part1(String puzzleInput) {
        // Read the instructions
        List<String> lines = FileUtils.readFile(puzzleInput);

        Set<String> blackTiles = new HashSet<>();
        Pattern directions = Pattern.compile("(e)|(w)|(se)|(sw)|(ne)|(nw)");
        Matcher matcher;
        String direction;
        for (String line : lines) {
            int x = 0;
            int y = 0;
            int z = 0;
            matcher = directions.matcher(line);
            while (matcher.find()) {
                direction = matcher.group();
                System.out.print(direction + " ");
                // see:
                // https://math.stackexchange.com/questions/2254655/hexagon-grid-coordinate-system
                switch (direction) {
                    case "e":
                        x++;
                        y--;
                        break;
                    case "w":
                        x--;
                        y++;
                        break;
                    case "se":
                        z++;
                        y--;
                        break;
                    case "sw":
                        z++;
                        x--;
                        break;
                    case "ne":
                        z--;
                        x++;
                        break;
                    case "nw":
                        z--;
                        y++;
                        break;
                }
            }
            String coordinates = x + "," + y + "," + z;
            System.out.println("\n" + coordinates);

            if (blackTiles.contains(coordinates))
                blackTiles.remove(coordinates);
            else
                blackTiles.add(coordinates);
        }

        System.out.println("There are " + blackTiles.size() + " black tiles.");
        return blackTiles;
    }

    /**
     * How many tiles will be black after 100 days?
     * 
     * @param blackTiles
     */
    private static void part2(Set<String> blackTiles) {
    }

}
