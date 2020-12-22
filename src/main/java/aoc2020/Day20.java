package aoc2020;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
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
    private static final String TEST_2_PUZZLE_INPUT_TXT = "TestInput2-Day20.txt";

    private static final String TEST_TILE = "Tile 1951:\n" +
                                            "#.##...##.\n" +
                                            "#.####...#\n" +
                                            ".....#..##\n" +
                                            "#...######\n" +
                                            ".##.#....#\n" +
                                            ".###.#####\n" +
                                            "###.##.##.\n" +
                                            ".###....#.\n" +
                                            "..#.#..#.#\n" +
                                            "#...##.#..";
    private static final String TEST_TILE_2 = "Tile 3527:\n" +
                                              "........##\n" +
                                              ".........#\n" +
                                              "........#.\n" +
                                              "...#..##.#\n" +
                                              "......##..\n" +
                                              "###.#..#.#\n" +
                                              "...#....#.\n" +
                                              "#.......##\n" +
                                              "..........\n" +
                                              ".#....###.";

    public static void main(String[] args) {
        //        part1();
        part2();
        //        Tile testTile = new Tile(TEST_TILE);
        //        for (int i = 1; i <= 4; i++) {
        //            System.out.println(testTile);
        //            testTile.rotate(1);
        //        }
        //        testTile.flip();
        //        for (int i = 1; i <= 4; i++) {
        //            System.out.println(testTile);
        //            testTile.rotate(1);
        //        }

        //        Tile testTile2 = new Tile(TEST_TILE_2);
        //        testTile2.flipVertical();
        //        System.out.println(testTile2.tileToString());
        //        System.out.println();
        //        System.out.println(testTile2);
        //        System.out.println();
        //        testTile2.rotate(3);
        //        System.out.println(testTile2.tileToString());
        //        System.out.println();
        //        System.out.println(testTile2);
        //        System.out.println();

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
     * How many # are not part of a sea monster?
     */
    private static void part2() {
        Map<Integer, List<Tile>> sideMap = new HashMap<>();

        // Parse the input
        List<Tile> tiles = FileUtils.cleanInput(FileUtils.readFileToStream(PUZZLE_INPUT_TXT))
                                    .map(Tile::new)
                                    //                                    .peek(System.out::println)
                                    .peek(t -> {
                                        t.sides.forEach(s -> sideMap.computeIfAbsent(s, ArrayList::new).add(t));
                                        t.sidesFlipped.forEach(s -> sideMap.computeIfAbsent(s, ArrayList::new).add(t));
                                    })
                                    .collect(Collectors.toList());

        //        System.out.println(tiles.size());

        // Reassemble the image
        int size = (int) Math.sqrt(tiles.size());
        Tile[][] arrangement = new Tile[size][size];

        // Pick a top-left corner
        Tile corner = tiles.stream()
                           .filter(t -> t.getSides().stream().filter(s -> sideMap.get(s).size() == 1).count() == 2)
                           .findAny()
                           .get();
        arrangement[0][0] = corner;

//        corner.flipVertical();
        //        System.out.println("Top-left corner: \n" + corner);

        // Orient it correctly
        // Which sides are missing?
        List<Boolean> matchingSides = corner.getSides()
                                            .stream()
                                            .map(s -> sideMap.get(s).size() == 1)
                                            //                                            .peek(System.out::println)
                                            .collect(Collectors.toList());

        boolean first = matchingSides.get(0);
        boolean second = matchingSides.get(1);
        int rotate = 0;
        if (!first && second)
            rotate = 2;
        else if (first && second)
            rotate = 3;
        else if (first && !second)
            rotate = 1;
        else if (!first && !second)
            rotate = 0;

        corner.rotate(rotate);
        //        System.out.println("Top-left corner: \n" + corner);
        int row = 0;
        do {
            // Fill in the row
            for (int i = 1; i < size; i++) {
                // Find the tile which goes to the right
                Tile leftTile = arrangement[row][i - 1];
                Integer matchingSide = leftTile.getSides().get(1);
                Tile rightTile = sideMap.get(matchingSide).stream().filter(t -> t != leftTile).findAny().get();
                arrangement[row][i] = rightTile;
                // Orient it correctly
                // Get the matching side on the flip side
                if (rightTile.getSides().contains(matchingSide)) {
                    rightTile.flipVertical();
                }
                rightTile.rotate(3 + rightTile.getSidesFlipped().indexOf(matchingSide));

            }

            //        System.out.println(ArrayUtils.toString(arrangement[0]));

            // Get the next left side piece
            Tile lastLeftSide = arrangement[row][0];
            Integer topSide = lastLeftSide.getSides().get(2);
            Tile nextLeftSide = sideMap.get(topSide).stream().filter(t -> t != lastLeftSide).findAny().orElse(null);
            if (nextLeftSide != null) {
                // Orient it so that the topSide is on top, and the unmatched side is on the left
                if (nextLeftSide.getSides().contains(topSide)) {
                    nextLeftSide.flipVertical();
                }
                nextLeftSide.rotate(nextLeftSide.getSidesFlipped().indexOf(topSide));
                arrangement[row + 1][0] = nextLeftSide;
            }
        } while (++row < size);

        // Assemble the map
        String[] mapRows = new String[size * 8];

        for (int i = 0; i < size * 8; i++) {
            for (int j = 0; j < size; j++)
                mapRows[i] = StringUtils.defaultString(mapRows[i]) + new String(arrangement[i / 8][j].getData()[i % 8]);
        }

        // Show the tiles for troubleshooting
        String[] tileRows = new String[size * 10];
        for (int i = 0; i < size * 10; i++) {
            for (int j = 0; j < size; j++)
                tileRows[i] = StringUtils.defaultString(tileRows[i]) + new String(arrangement[i / 10][j].getFullData()[i % 10]);
        }

        int rowNum = 0;
        for (String tileRow : tileRows) {
            if (rowNum++ % 10 == 0) {
                System.out.println(Stream.of(arrangement[rowNum / 10]).map(t -> t.getId() + ":      ").collect(Collectors.joining()));
            }
            System.out.println(tileRow.replaceAll("(.{10})", "$1 "));
        }
        // Look for sea monsters
        int foundSeaMonsters = 0;
        String body1Regex = "#(....)##(....)##(....)###";
        String body2Regex = "(.)#(..)#(..)#(..)#(..)#(..)#";
        Pattern body1 = Pattern.compile(body1Regex);
        Pattern body2 = Pattern.compile(body2Regex);
        String bodyReplace1 = "O$1OO$2OO$3OOO";
        String bodyReplace2 = "$1O$2O$3O$4O$5O$6O";

        long startingHashes = Stream.of(mapRows).collect(Collectors.joining("\n")).chars().filter(c -> c == '#').count();
        System.out.println("There are " + startingHashes + " \"#\"s to start with.");

        //        ArrayUtils.reverse(mapRows);
        //        mapRows = rotate(mapRows);
        //        mapRows = rotate(mapRows);
        //        mapRows = rotate(mapRows);
        search: for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                for (int i = 1; i < mapRows.length - 1; i++) {
                    // Look for the rows of the body
                    Matcher row1Matcher = body1.matcher(mapRows[i]);
                    Matcher row2Matcher = body2.matcher(mapRows[i + 1]);
                    while (row1Matcher.find()) {
                        int row1Start = row1Matcher.start();
                        while (row2Matcher.find()) {
                            int row2Start = row2Matcher.start();
                            if (row1Start == row2Start && mapRows[i - 1].charAt(row1Start + 18) == '#') {
                                foundSeaMonsters++;
                                mapRows[i - 1] = mapRows[i - 1].substring(0, row1Start + 18) + "O" +
                                                 mapRows[i - 1].substring(row1Start + 19);

                                mapRows[i] = mapRows[i].substring(0, row1Start) +
                                             mapRows[i].substring(row1Start, row1Start + 20).replaceAll(body1Regex, bodyReplace1) +
                                             mapRows[i].substring(row1Start + 20);
                                mapRows[i + 1] = mapRows[i + 1].substring(0, row2Start) +
                                                 mapRows[i + 1].substring(row2Start, row2Start + 17).replaceAll(body2Regex, bodyReplace2) +
                                                 mapRows[i + 1].substring(row2Start + 17);

                                //                                StringBuffer replacement = new StringBuffer();
                                //                                mapRows[i] = row1Matcher.appendReplacement(replacement, bodyReplace1)
                                //                                                        .appendTail(replacement).toString();
                                //                           row1Matcher.replaceAll(bodyReplace1);
                                //                                replacement = new StringBuffer();
                                //                                mapRows[i + 1] = row2Matcher.appendReplacement(replacement, bodyReplace2)
                                //                                                            .appendTail(replacement).toString();
                                //                                mapRows[i + 1] = row2Matcher.replaceAll(bodyReplace2);
                            }
                            row2Matcher.region(row2Start + 1, mapRows[i].length());
                        }
                        row2Matcher.reset();
                        row1Matcher.region(row1Start + 1, mapRows[i].length());
                    }
                }

                if (foundSeaMonsters > 0)
                    break search;

                // Rotate and try again
                mapRows = rotate(mapRows);
                tileRows = rotate(tileRows);
            }
            // Flip the map and try again
            ArrayUtils.reverse(mapRows);
            ArrayUtils.reverse(tileRows);
        }
        System.out.println("Found sea monsters: " + foundSeaMonsters);
        String map = Stream.of(mapRows).collect(Collectors.joining("\n"));
        System.out.println(map);

        // Count remaining '#'s
        long remainingHashes = map.chars().filter(c -> c == '#').count();

        System.out.println("There are " + remainingHashes + " remaining \"#\"s.");

        //         rowNum = 0;
        //        for (String tileRow : tileRows) {
        //            if (rowNum++ % 10 == 0) {
        //                System.out.println(Stream.of(arrangement[rowNum / 10]).map(t->t.getId()+":      ").collect(Collectors.joining()));
        //            }
        //            System.out.println(tileRow.replaceAll("(.{10})", "$1 "));
        //        }
        System.out.println(Stream.of(tileRows).collect(Collectors.joining("\n")));
    }

    /**
     * Rotate an array of strings.
     * 
     * @param lines
     *            The lines of the map.
     * @return An array of strings based on the rotation of the input.
     */
    private static String[] rotate(String[] lines) {
        char[][] matrix = Stream.of(lines).map(String::toCharArray).collect(Collectors.toList()).toArray(new char[0][0]);

        int size = matrix.length;
        char[][] ret = new char[size][size];

        // Rotate the matrix 1, or 3 times.
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                ret[i][j] = matrix[size - j - 1][i];

        return Stream.of(ret).map(String::new).collect(Collectors.toList()).toArray(new String[0]);
    }

    private static class Tile {
        private List<Integer> sides;
        private List<Integer> sidesFlipped;

        private char[][] data;
        private char[][] fullData;
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
            // Get the id, then discard the header
            this.id = Integer.parseInt(content.substring(5, 9));
            content = content.substring(11).replace(' ', '\n');

            // Compute the numerical codes for the sides.
            sides = new ArrayList<>();
            sidesFlipped = new ArrayList<>();

            // Go around clockwise
            String top = content.substring(0, 10);

            String right = Stream.of(content.split("\n"))
                                 .map(l -> l.substring(9))
                                 .collect(Collectors.joining());

            String bottom = content.substring(99, 109);
            String left = Stream.of(content.split("\n"))
                                .map(l -> l.substring(0, 1))
                                .collect(Collectors.joining());

            sides.add(parseSide(top));
            sides.add(parseSide(right));
            sides.add(parseSide(StringUtils.reverse(bottom)));
            sides.add(parseSide(StringUtils.reverse(left)));
            sidesFlipped.add(parseSide(StringUtils.reverse(top)));
            sidesFlipped.add(parseSide(left));
            sidesFlipped.add(parseSide(bottom));
            sidesFlipped.add(parseSide(StringUtils.reverse(right)));

            // The sides are not part of the data.
            this.data = Stream.of(content.substring(11, 99).replaceAll(".(.{8}).\n", "$1\n").split("\n"))
                              .map(String::toCharArray)
                              .collect(Collectors.toList())
                              .toArray(new char[0][0]);

            // Keep the full data for troubleshooting.
            this.fullData = Stream.of(content.split("\n"))
                                  .map(String::toCharArray)
                                  .collect(Collectors.toList())
                                  .toArray(new char[0][0]);
        }

        public int getId() {
            return id;
        }

        public char[][] getData() {
            return data;
        }

        public char[][] getFullData() {
            return fullData;
        }

        public List<Integer> getSides() {
            return sides;
        }

        public List<Integer> getSidesFlipped() {
            return sidesFlipped;
        }

        /**
         * Rotate the data, and sides, clockwise a number of times.
         * 
         * @param times
         *            The number of times to rotate the tile.
         */
        public void rotate(int times) {
            Collections.rotate(sides, times);
            Collections.rotate(sidesFlipped, -times);

            //            if (times % 4 == 2) {
            //                Stream.of(data).forEach(ArrayUtils::reverse);
            //                ArrayUtils.reverse(data);
            //                Stream.of(fullData).forEach(ArrayUtils::reverse);
            //                ArrayUtils.reverse(fullData);
            //            } else if (times % 2 == 1) {
            int size = data.length;

            // Rotate the matrix.
            for (int time = 0; time < times % 4; time++) {
                char[][] ret = new char[size][size];
                for (int i = 0; i < size; ++i)
                    for (int j = 0; j < size; ++j)
                        ret[i][j] = data[size - j - 1][i];
                data = ret;
            }
            int size2 = fullData.length;

            // Rotate the matrix.
            for (int time = 0; time < times % 4; time++) {
                char[][] ret2 = new char[size2][size2];
                for (int i = 0; i < size2; ++i)
                    for (int j = 0; j < size2; ++j)
                        ret2[i][j] = fullData[size2 - j - 1][i];
                fullData = ret2;
            }
            //            }
        }

        /**
         * Invert the tile along a vertical axis.
         */
        public void flipVertical() {
            List<Integer> temp = sides;
            sides = sidesFlipped;
            sidesFlipped = temp;

            // Reverse each line
            for (char[] row : data) {
                ArrayUtils.reverse(row);
            }
            // Reverse each line
            for (char[] row : fullData) {
                ArrayUtils.reverse(row);
            }
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
            String dataString = Stream.of(data).map(String::new).collect(Collectors.joining("\n"));
            return "Tile " + this.id + ":\n" + dataString + "\n" + sides + "\n" + sidesFlipped + "\n";
        }

        public String tileToString() {
            return Stream.of(fullData).map(String::new).collect(Collectors.joining("\n"));
        }
    }
}
