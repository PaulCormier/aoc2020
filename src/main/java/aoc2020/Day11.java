package aoc2020;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2020/day/11
 *
 * @author Paul Cormier
 * 
 */
public class Day11 {

    private static final String INPUT_TXT = "Input-Day11.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day11.txt";

    private static final char EMPTY_SEAT = 'L';
    private static final char OCCUPIED_SEAT = '#';
    private static final char FLOOR = '.';

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Once the system stabilises, how many seats are occupied?
     */
    private static void part1() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);
        char[][] grid = new char[lines.size()][];

        // Load the grid
        for (int i = 0; i < grid.length; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        while (step(grid)) {
            // for (char[] line : grid)
            // System.out.println(String.valueOf(line));
            // System.out.println();
        }

        int occupiedSeats = 0;
        for (char[] line : grid) {
            String lineString = String.valueOf(line);
            occupiedSeats += StringUtils.countMatches(lineString, OCCUPIED_SEAT);
            System.out.println(lineString);
        }

        System.out.println("There are " + occupiedSeats + " occupied seats in the end.");
    }

    /**
     * Once the system stabilises, how many seats are occupied?
     */
    private static void part2() {

        List<String> lines = FileUtils.readFile(INPUT_TXT);
        char[][] grid = new char[lines.size()][];

        // Load the grid
        for (int i = 0; i < grid.length; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        while (step2(grid)) {
            for (char[] line : grid)
                System.out.println(String.valueOf(line));
            System.out.println();
        }

        int occupiedSeats = 0;
        for (char[] line : grid) {
            String lineString = String.valueOf(line);
            occupiedSeats += StringUtils.countMatches(lineString, OCCUPIED_SEAT);
            System.out.println(lineString);
        }

        System.out.println("There are " + occupiedSeats + " occupied seats in the end.");
    }

    /**
     * Run the given grid through the rules. Update the grid. Return whether and
     * cells changed.
     * 
     * @param grid The seating grid to examine.
     * @return true if the step resulting in any modifications to the grid.
     */
    private static boolean step(char[][] grid) {
        // Make a copy of the grid to examine
        char[][] copyGrid = new char[grid.length][];

        // Load the grid
        for (int i = 0; i < grid.length; i++) {
            copyGrid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }

        // If there were any changes
        boolean changed = false;

        // If a cell is a seat,
        // If it is empty, and there are no neighbours, fill it
        // If it is occupied, and there are 4 or more neighbours, empty it.
        for (int i = 0; i < copyGrid.length; i++) {
            for (int j = 0; j < copyGrid[i].length; j++) {
                switch (copyGrid[i][j]) {
                case FLOOR:
                    continue;
                case OCCUPIED_SEAT:
                    if (checkNeighbours(i, j, copyGrid, OCCUPIED_SEAT) >= 4) {
                        grid[i][j] = EMPTY_SEAT;
                        changed = true;
                    }
                    break;
                case EMPTY_SEAT:
                    if (checkNeighbours(i, j, copyGrid, OCCUPIED_SEAT) == 0) {
                        grid[i][j] = OCCUPIED_SEAT;
                        changed = true;
                    }
                    break;
                }
            }
        }

        return changed;
    }

    /**
     * Run the given grid through the rules. Update the grid. Return whether and
     * cells changed.
     * 
     * @param grid The seating grid to examine.
     * @return true if the step resulting in any modifications to the grid.
     */
    private static boolean step2(char[][] grid) {
        // Make a copy of the grid to examine
        char[][] copyGrid = new char[grid.length][];

        // Load the grid
        for (int i = 0; i < grid.length; i++) {
            copyGrid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }

        // If there were any changes
        boolean changed = false;

        // If a cell is a seat,
        // If it is empty, and there are no neighbours, fill it
        // If it is occupied, and there are 4 or more neighbours, empty it.
        for (int i = 0; i < copyGrid.length; i++) {
            for (int j = 0; j < copyGrid[i].length; j++) {
                switch (copyGrid[i][j]) {
                case FLOOR:
                    continue;
                case OCCUPIED_SEAT:
                    if (checkNeighbours2(i, j, copyGrid, OCCUPIED_SEAT) >= 5) {
                        grid[i][j] = EMPTY_SEAT;
                        changed = true;
                    }
                    break;
                case EMPTY_SEAT:
                    if (checkNeighbours2(i, j, copyGrid, OCCUPIED_SEAT) == 0) {
                        grid[i][j] = OCCUPIED_SEAT;
                        changed = true;
                    }
                    break;
                }
            }
        }

        return changed;
    }

    /**
     * Check the neighbours of the cell (i,j) in the grid for the presence of the
     * value statusToCheck.
     * 
     * @param i The starting index of the outer array in the grid.
     * @param j The starting index of the inner array in the grid.
     * @param grid The grid of cells to check in.
     * @param statusToCheck The value to be looked for.
     * @return The number of neighbours with the given value.
     */
    private static int checkNeighbours(int i, int j, char[][] grid, char statusToCheck) {
        int foundNeighbours = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;
                if (x + i >= 0 && x + i < grid.length && y + j >= 0 && y + j < grid[x + i].length)
                    foundNeighbours += grid[x + i][y + j] == statusToCheck ? 1 : 0;
            }
        }
        return foundNeighbours;
    }

    /**
     * Check the visible cells from the cell (i,j) in the grid for the presence of
     * the
     * value statusToCheck.
     * 
     * @param i The starting index of the outer array in the grid.
     * @param j The starting index of the inner array in the grid.
     * @param grid The grid of cells to check in.
     * @param statusToCheck The value to be looked for.
     * @return The number of neighbours with the given value.
     */
    private static int checkNeighbours2(int i, int j, char[][] grid, char statusToCheck) {
        int foundNeighbours = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;

                Optional<Character> cell = safeGridAccess(x + i, y + j, grid);

                // If there is no seat, move the check out
                int x2 = x;
                int y2 = y;
                while (cell.isPresent() && cell.get() == FLOOR) {
                    x2 += Integer.compare(x, 0);
                    y2 += Integer.compare(y, 0);
                    cell = safeGridAccess(x2 + i, y2 + j, grid);
                }

                if (cell.isEmpty())
                    continue;

                foundNeighbours += cell.get() == statusToCheck ? 1 : 0;
            }
        }
        return foundNeighbours;
    }

    private static Optional<Character> safeGridAccess(int i, int j, char[][] grid) {
        if (i >= 0 & i < grid.length && j >= 0 && j < grid[i].length) {
            return Optional.of(grid[i][j]);
        }
        return Optional.empty();
    }
}
