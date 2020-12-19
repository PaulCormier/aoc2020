package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conway Cubes
 * 
 * https://adventofcode.com/2020/day/17
 *
 * @author Paul Cormier
 * 
 */
public class Day17 {

    private static final char ACTIVE_CUBE = '#';
    private static final char INACTIVE_CUBE = '.';

    private static final String INPUT_TXT = "Input-Day17.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day17.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * How many active cubes are there after the 6th cycle?
     */
    private static void part1() {
        // Initialise the Conway space
        List<char[]> lines = FileUtils.readFileToStream(TEST_INPUT_TXT)
                                      .map(String::toCharArray)
                                      .collect(Collectors.toList());

        // Create cubes for each known input.
        Map<Coordinates, Cube> knownCubes = new HashMap<>();

        int maxY = (int) Math.ceil((lines.size() - 1) / 2.);
        int maxX = maxY;
        int y = 0;
        int z = 0;
        for (char[] line : lines) {
            maxX = (int) Math.ceil((line.length - 1) / 2.);
            for (int i = 0; i < line.length; i++) {
                int x = i;
                knownCubes.computeIfAbsent(Coordinates.of(x - maxX, y - maxY, z),
                                           c -> new Cube(c, line[x] == ACTIVE_CUBE));
            }
            y++;
        }

        // Link the known neighbours
        for (Cube cube : new ArrayList<>(knownCubes.values())) {
            for (z = -1; z <= 1; z++) {
                for (y = -1; y <= 1; y++) {
                    for (int x = -1; x <= 1; x++) {
                        Coordinates baseCoordinates = cube.getCoordinates();
                        if (x == 0 && y == 0 && z == 0)
                            continue;
                        Coordinates targetCoordinates = Coordinates.of(baseCoordinates.x + x,
                                                                       baseCoordinates.y + y,
                                                                       baseCoordinates.z + z);

                        Cube targetCube = knownCubes.computeIfAbsent(targetCoordinates, c -> new Cube(c, false));
                        targetCube.addNeighbour(cube);
                    }
                }
            }
        }

        // System.out.println(knownCubes);
        System.out.println("Before any cycles:\n");
        printCubes(knownCubes, maxX, maxY, 0);

        // Run through the cubes, and compute their next active states.
        knownCubes.values().forEach(c -> {
            int activeNeighbours = c.getActiveNeighbours();
            c.setNextActive((c.isActive() && (activeNeighbours == 2 || activeNeighbours == 3)) ||
                            (!c.isActive() && activeNeighbours == 3));
        });
        // Step them ahead
        knownCubes.values().forEach(Cube::step);

        System.out.println("After 1 cycle:\n");
        printCubes(knownCubes, 3, 3, 1);
    }

    /**
     */
    private static void part2() {
    }

    /**
     * Print out the contents of a map of cubes in a grid, with bounds: +/- maxX
     * by +/- maxY by +/- maxZ.
     * 
     * @param cubes
     *     The map of cubes to be printed.
     * @param maxX
     *     The number of cubes wide to be printed away from the centre.
     * @param maxY
     *     The number of cubes high to be printed away from the centre.
     * @param maxZ
     *     The number of layers of cubes to be printed away from the
     *     centre.
     */
    private static void printCubes(Map<Coordinates, Cube> cubes, int maxX, int maxY, int maxZ) {
        char[][][] grid = new char[maxZ * 2 + 1][maxY * 2 + 1][maxX * 2 + 1];

        for (Cube cube : cubes.values()) {
            Coordinates coordinates = cube.getCoordinates();
            if (Math.abs(coordinates.x) <= maxX && Math.abs(coordinates.y) <= maxY && Math.abs(coordinates.z) <= maxZ) {
                int x = coordinates.x + maxX;
                int y = coordinates.y + maxY;
                int z = coordinates.z + maxZ;
                grid[z][y][x] = cube.isActive() ? ACTIVE_CUBE : INACTIVE_CUBE;
            }
        }

        // Print the grid
        for (int z = 0; z < grid.length; z++) {
            System.out.println("z=" + (z - maxZ));
            for (char[] row : grid[z]) {
                System.out.println(String.valueOf(row));
            }
            System.out.println();
        }

    }

    /**
     * A Conway cell in three-dimensional space.
     */
    private static class Cube {
        private boolean active = false;
        private boolean nextActive = false;
        private final Coordinates coordinates;
        private final Set<Cube> neighbours;

        public Cube(Coordinates coordinates, boolean active) {
            this.active = active;
            this.coordinates = coordinates;
            this.neighbours = new HashSet<>();
        }

        public boolean isActive() {
            return active;
        }

        public void setNextActive(boolean active) {
            this.nextActive = active;
        }

        public void step() {
            this.active = nextActive;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public Set<Cube> getNeighbours() {
            return neighbours;
        }

        /**
         * Link two cubes as neighbours.
         * 
         * @param neighbour
         *     The neighbour to associate with this cube.
         */
        public void addNeighbour(Cube neighbour) {
            this.neighbours.add(neighbour);
            neighbour.neighbours.add(this);
        }

        public int getActiveNeighbours() {
            return (int) neighbours.stream().filter(Cube::isActive).count();
        }

        @Override
        public String toString() {
            return coordinates + " " + (active ? "Active" : "Inactive");
        }

    }

    private static class Coordinates {
        private final int x;
        private final int y;
        private final int z;

        private Coordinates(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Coordinates of(int x, int y, int z) {
            return new Coordinates(x, y, z);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coordinates other = (Coordinates) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }

    }
}
