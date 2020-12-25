package aoc2020;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.map.HashedMap;

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
        Set<Coordinates> blackTiles = part1(PUZZLE_INPUT);
        part2(blackTiles);
    }

    /**
     * After all of the instructions have been followed, how many tiles are left
     * with the black side up?
     * 
     * @param puzzleInput
     * @return
     */
    private static Set<Coordinates> part1(String puzzleInput) {
        // Read the instructions
        List<String> lines = FileUtils.readFile(puzzleInput);

        Set<Coordinates> blackTiles = new HashSet<>();
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
                // System.out.print(direction + " ");
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
            Coordinates coordinates = Coordinates.of(x, y, z);
            // System.out.println("\n" + coordinates);

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
    private static void part2(Set<Coordinates> blackTiles) {
        Set<Coordinates> nextDayBlackTiles = new HashSet<>(blackTiles);
        Map<Coordinates, Integer> whiteTiles = new HashedMap<>();

        int day = 0;
        while (day <= 100) {
            for (Coordinates tile : blackTiles) {
                // Any black tile with zero or more than 2 black tiles immediately adjacent to
                // it is flipped to white.
                Set<Coordinates> adjacentTiles = Set.of(Coordinates.of(tile.x + 1, tile.y, tile.z - 1),
                                                        Coordinates.of(tile.x + 1, tile.y - 1, tile.z),
                                                        Coordinates.of(tile.x - 1, tile.y + 1, tile.z),
                                                        Coordinates.of(tile.x - 1, tile.y, tile.z + 1),
                                                        Coordinates.of(tile.x, tile.y + 1, tile.z - 1),
                                                        Coordinates.of(tile.x, tile.y - 1, tile.z + 1));
                int adjacentBlackTiles = SetUtils.intersection(blackTiles, adjacentTiles).size();
                if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2)
                    nextDayBlackTiles.remove(tile);

                // Any white tile with exactly 2 black tiles immediately adjacent to it is
                // flipped to black.
                SetUtils.disjunction(adjacentTiles, blackTiles)
                        .stream()
                        .forEach(t -> whiteTiles.merge(t, 1, Math::addExact));
            }

            nextDayBlackTiles.addAll(whiteTiles.entrySet()
                                               .stream()
                                               .filter(e -> e.getValue() == 2)
                                               .map(Entry::getKey)
                                               .collect(Collectors.toList()));

            if (day % 10 == 0)
                System.out.println("Day " + day + ": " + blackTiles.size());

            whiteTiles.clear();
            blackTiles = nextDayBlackTiles;
            nextDayBlackTiles = new HashSet<>(blackTiles);
            day++;
        }
    }

    /**
     * Day 17's Coordinates.
     */
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

}
