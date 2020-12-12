package aoc2020;

import java.util.List;

/**
 * https://adventofcode.com/2020/day/12
 *
 * @author Paul Cormier
 * 
 */
public class Day12 {

    private static final String INPUT_TXT = "Input-Day12.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day12.txt";

    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';
    private static final char FORWARD = 'F';

    private static enum CardinalDirection {
        N, E, S, W;
    }

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Navigate the ship to its final position, and determine the Manhattan Distance
     * from the origin.
     */
    private static void part1() {
        Ship ferry = new Ship();
        List<String> instructions = FileUtils.readFile(INPUT_TXT);
        for (String instruction : instructions) {

            int value = Integer.parseInt(instruction.substring(1));
            char instructionChar = instruction.charAt(0);

            switch (instructionChar) {
                case LEFT:
                    ferry.turn(-value);
                    break;
                case RIGHT:
                    ferry.turn(value);
                    break;
                case FORWARD:
                    ferry.advance(value);
                    break;
                // Must be a cardinal direction
                default:
                    ferry.translate(CardinalDirection.valueOf(String.valueOf(instructionChar)), value);
                    break;
            }
        }

        System.out.println("The ship's final position is: " + ferry);
        System.out.println("The Manhattan Distance is: " + (Math.abs(ferry.positionEW) + Math.abs(ferry.positionNS)));
    }

    /**
     */
    private static void part2() {
    }

    /**
     * Model the ship's position and orientation.
     */
    private static class Ship {
        CardinalDirection orientation = CardinalDirection.E;
        int positionEW = 0;
        int positionNS = 0;

        /**
         * Rotate the ship's orientation be a number of degrees.
         * 
         * @param degrees A multiple of 90 degrees by which to rotate the ship. A
         *     positive value means to the right, a negative value means to the left.
         */
        public void turn(int degrees) {
            int offset = (degrees / 90 + orientation.ordinal() + CardinalDirection.values().length)
                    % CardinalDirection.values().length;
            orientation = CardinalDirection.values()[offset];
        }

        /**
         * Translate the ship in the direction it is facing by the given number of
         * units.
         * 
         * @param distance The distance to move the ship, in the direction it's facing.
         */
        public void advance(int distance) {
            translate(orientation, distance);
        }

        /**
         * Translate the ship in the given direction by the distance given.
         * 
         * @param direction The {@link CardinalDirection} in which to translate the
         *     ship.
         * @param distance The distance to move the ship.
         */
        public void translate(CardinalDirection direction, int distance) {
            switch (direction) {
                case N:
                    positionNS += distance;
                    break;
                case S:
                    positionNS -= distance;
                    break;
                case E:
                    positionEW += distance;
                    break;
                case W:
                    positionEW -= distance;
                    break;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %d, %s %d (%s)",
                                 positionEW >= 0 ? "east" : "west", Math.abs(positionEW),
                                 positionNS >= 0 ? "north" : "south", Math.abs(positionNS),
                                 orientation);
        }
    }
}
