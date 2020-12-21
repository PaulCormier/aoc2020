package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Jurassic Jigsaw
 * 
 * https://adventofcode.com/2020/day/20
 *
 * @author Paul Cormier
 * 
 */
public class Day20 {

    private static final String PUZZLE_INPUT_TXT = "Input-Day20.txt";
    private static final String TEST_PUZZLE_INPUT_TXT = "TestInput-Day20.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What do you get if you multiply together the IDs of the four corner
     * tiles?
     */
    private static void part1() {
        Map<Integer, List<Tile>> sideMap = new HashMap<>();

        // Parse the input
        List<Tile> tiles = FileUtils.cleanInput(FileUtils.readFileToStream(PUZZLE_INPUT_TXT))
                                    .map(Tile::new)
                                    //                                    .peek(System.out::println)
                                    .peek(t -> {
                                        t.getSides().forEach(s -> sideMap.computeIfAbsent(s, ArrayList::new).add(t));
                                        t.getSidesFlipped().forEach(s -> sideMap.computeIfAbsent(s, ArrayList::new).add(t));
                                    })
                                    .collect(Collectors.toList());

        // Find the corners
        // Could be the four tiles which only have two sides which match other tiles?
        long product = tiles.stream()
                            .filter(t -> t.getSides().stream().filter(s -> sideMap.get(s).size() == 1).count() == 2)
                            .peek(System.out::println)
                            .mapToLong(Tile::getId)
                            .reduce(Math::multiplyExact)
                            .getAsLong();

        System.out.println("The product of the ids of the corner tiles is: " + product);
    }

    /**
     */
    private static void part2() {
    }

    private static class Tile {
        private final List<Integer> sides;
        private final List<Integer> sidesFlipped;

        private final String data;
        private final int id;

        /**
         * Parse a string representation of the tile into it's id, data, and
         * representations of the sides.
         * 
         * The first part of the string is the tile id, followed by the tile
         * data. Spaces represent newlines.
         * 
         * @param content
         *            The string representing the tile.
         */
        public Tile(String content) {
            this.id = Integer.parseInt(content.substring(5, 9));
            this.data = content.substring(11).replace(' ', '\n');

            // Compute the numerical codes for the sides.
            sides = new ArrayList<>();
            sidesFlipped = new ArrayList<>();

            // Go around clockwise
            String top = this.data.substring(0, 10);
            sides.add(parseSide(top));
            sidesFlipped.add(parseSide(StringUtils.reverse(top)));

            String bottom = this.data.substring(99, 109);
            sides.add(parseSide(StringUtils.reverse(bottom)));
            sidesFlipped.add(parseSide(bottom));

            String right = Stream.of(this.data.split("\n"))
                                 .map(l -> l.substring(9))
                                 .collect(Collectors.joining());
            sides.add(parseSide(right));
            sidesFlipped.add(parseSide(StringUtils.reverse(right)));

            String left = Stream.of(this.data.split("\n"))
                                .map(l -> l.substring(0, 1))
                                .collect(Collectors.joining());
            sides.add(parseSide(StringUtils.reverse(left)));
            sidesFlipped.add(parseSide(left));

        }

        public List<Integer> getSides() {
            return sides;
        }

        public List<Integer> getSidesFlipped() {
            return sidesFlipped;
        }

        public String getData() {
            return data;
        }

        public int getId() {
            return id;
        }

        /**
         * Convert a string representation of a side into a numerical value.
         * 
         * @param side
         *            The characters of the side.
         * @return A numerical value representing the pattern.
         */
        private static int parseSide(String side) {
            return Integer.parseInt(side.replace('#', '1').replace('.', '0'), 2);
        }

        @Override
        public String toString() {
            return "Tile " + this.id + ":\n" + data + "\n" + sides + "\n" + sidesFlipped + "\n";
        }
    }
}
