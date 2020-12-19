package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        // part1();
        part2();
    }

    /**
     * How many active cubes are there after the 6th cycle?
     */
    private static void part1() {
        // Initialise the Conway space
        List<char[]> lines = FileUtils.readFileToStream(INPUT_TXT)
                                      .map(String::toCharArray)
                                      .collect(Collectors.toList());

        // Create cubes for each known input.
        Map<Coordinates, Cube> knownCubes = new HashMap<>();

        int maxY = (int) Math.ceil((lines.size() - 1) / 2.);
        int maxX = maxY;
        int maxZ = 0;
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
        printCubes(knownCubes, maxX, maxY, maxZ);

        for (int cycle = 1; cycle <= 6; cycle++) {
            // Run through the cubes, and compute their next active states.
            knownCubes.values().forEach(c -> {
                int activeNeighbours = c.getActiveNeighbours();
                c.setNextActive((c.isActive() && (activeNeighbours == 2 || activeNeighbours == 3)) ||
                                (!c.isActive() && activeNeighbours == 3));
            });
            // Step them ahead
            knownCubes.values().forEach(Cube::step);

            // Link the new neighbours
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

            // System.out.println("After " + cycle + " cycle(s):\n");
            // printCubes(knownCubes, ++maxX, ++maxY, ++maxZ);
        }

        long activeCubes = knownCubes.values().stream().filter(Cube::isActive).count();
        System.out.println("There are " + activeCubes + " active cubes.");
    }

    /**
     * How many active cubes are there after the 6th cycle?
     */
    private static void part2() {// Initialise the Conway space
        List<char[]> lines = FileUtils.readFileToStream(INPUT_TXT)
                                      .map(String::toCharArray)
                                      .collect(Collectors.toList());

        // Create cubes for each known input.
        Map<Coordinates4D, HyperCube> knownCubes = new HashMap<>();

        int maxY = (int) Math.ceil((lines.size() - 1) / 2.);
        int maxX = maxY;
        int maxZ = 0;
        int maxW = 0;
        int y = 0;
        int z = 0;
        int w = 0;
        for (char[] line : lines) {
            maxX = (int) Math.ceil((line.length - 1) / 2.);
            for (int i = 0; i < line.length; i++) {
                int x = i;
                knownCubes.computeIfAbsent(Coordinates4D.of(x - maxX, y - maxY, z, w),
                                           c -> new HyperCube(c, line[x] == ACTIVE_CUBE));
            }
            y++;
        }

        // Link the known neighbours
        for (HyperCube cube : new ArrayList<>(knownCubes.values())) {
            for (w = -1; w <= 1; w++) {
                for (z = -1; z <= 1; z++) {
                    for (y = -1; y <= 1; y++) {
                        for (int x = -1; x <= 1; x++) {
                            Coordinates4D baseCoordinates = cube.getCoordinates();
                            if (x == 0 && y == 0 && z == 0 && w == 0)
                                continue;
                            Coordinates4D targetCoordinates = Coordinates4D.of(baseCoordinates.x + x,
                                                                               baseCoordinates.y + y,
                                                                               baseCoordinates.z + z,
                                                                               baseCoordinates.w + w);

                            HyperCube targetCube = knownCubes.computeIfAbsent(targetCoordinates,
                                                                              c -> new HyperCube(c, false));
                            targetCube.addNeighbour(cube);
                        }
                    }
                }
            }
        }

        // System.out.println(knownCubes);
//        System.out.println("Before any cycles:\n");
//        printHyperCubes(knownCubes, maxX, maxY, maxZ, maxW);

        for (int cycle = 1; cycle <= 6; cycle++) {
            // Run through the cubes, and compute their next active states.
            knownCubes.values().forEach(c -> {
                int activeNeighbours = c.getActiveNeighbours();
                c.setNextActive((c.isActive() && (activeNeighbours == 2 || activeNeighbours == 3)) ||
                                (!c.isActive() && activeNeighbours == 3));
            });
            // Step them ahead
            knownCubes.values().forEach(Cube::step);

            // Link the new neighbours
            for (HyperCube cube : new ArrayList<>(knownCubes.values())) {
                for (w = -1; w <= 1; w++) {
                    for (z = -1; z <= 1; z++) {
                        for (y = -1; y <= 1; y++) {
                            for (int x = -1; x <= 1; x++) {
                                Coordinates4D baseCoordinates = cube.getCoordinates();
                                if (x == 0 && y == 0 && z == 0 && w == 0)
                                    continue;
                                Coordinates4D targetCoordinates = Coordinates4D.of(baseCoordinates.x + x,
                                                                                   baseCoordinates.y + y,
                                                                                   baseCoordinates.z + z,
                                                                                   baseCoordinates.w + w);

                                HyperCube targetCube = knownCubes.computeIfAbsent(targetCoordinates,
                                                                                  c -> new HyperCube(c, false));
                                targetCube.addNeighbour(cube);
                            }
                        }
                    }
                }
            }

//            System.out.println("After " + cycle + " cycle(s):\n");
//            printHyperCubes(knownCubes, ++maxX, ++maxY, ++maxZ, ++maxW);
        }

        long activeCubes = knownCubes.values().stream().filter(Cube::isActive).count();
        System.out.println("There are " + activeCubes + " active cubes.");
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

    private static void printHyperCubes(Map<Coordinates4D, HyperCube> cubes, int maxX, int maxY, int maxZ, int maxW) {
        char[][][][] grid = new char[maxW * 2 + 1][maxZ * 2 + 1][maxY * 2 + 1][maxX * 2 + 1];

        for (HyperCube cube : cubes.values()) {
            Coordinates4D coordinates = cube.getCoordinates();
            if (Math.abs(coordinates.x) <= maxX && Math.abs(coordinates.y) <= maxY && Math.abs(coordinates.z) <= maxZ
                && Math.abs(coordinates.w) <= maxW) {
                int x = coordinates.x + maxX;
                int y = coordinates.y + maxY;
                int z = coordinates.z + maxZ;
                int w = coordinates.w + maxW;
                grid[w][z][y][x] = cube.isActive() ? ACTIVE_CUBE : INACTIVE_CUBE;
            }
        }

        // Print the grid
        for (int w = 0; w < grid.length; w++) {
            for (int z = 0; z < grid[w].length; z++) {
                System.out.println("z=" + (z - maxZ) + ", w=" + (w - maxW));
                for (char[] row : grid[w][z]) {
                    System.out.println(String.valueOf(row));
                }
                System.out.println();
            }
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

    private static class HyperCube extends Cube {

        public HyperCube(Coordinates4D coordinates, boolean active) {
            super(coordinates, active);
        }

        @Override
        public Coordinates4D getCoordinates() {
            return (Coordinates4D) super.getCoordinates();
        }
    }

    private static class Coordinates {
        protected final int x;
        protected final int y;
        protected final int z;

        protected Coordinates(int x, int y, int z) {
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

    private static class Coordinates4D extends Coordinates {
        final int w;

        private Coordinates4D(int x, int y, int z, int w) {
            super(x, y, z);
            this.w = w;
        }

        public static Coordinates4D of(int x, int y, int z, int w) {
            return new Coordinates4D(x, y, z, w);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + w;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coordinates4D other = (Coordinates4D) obj;
            if (w != other.w)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d,%d)", x, y, z, w);
        }

    }
}
