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
     * Navigate the ship to its final position, using a waypoint, and determine the
     * Manhattan Distance from the origin.
     */
    private static void part2() {
        Ship ferry = new Ship();
        Waypoint waypoint = new Waypoint();

        List<String> instructions = FileUtils.readFile(INPUT_TXT);
        for (String instruction : instructions) {

            int value = Integer.parseInt(instruction.substring(1));
            char instructionChar = instruction.charAt(0);

            switch (instructionChar) {
                case LEFT:
                    waypoint.turn(-value);
                    break;
                case RIGHT:
                    waypoint.turn(value);
                    break;
                case FORWARD:
                    ferry.advanceTowardWaypoint(waypoint, value);
                    break;
                // Must be a cardinal direction
                default:
                    waypoint.translate(CardinalDirection.valueOf(String.valueOf(instructionChar)), value);
                    break;
            }
        }

        System.out.println("The ship's final position is: " + ferry);
        System.out.println("The Manhattan Distance is: " + (Math.abs(ferry.positionEW) + Math.abs(ferry.positionNS)));
    }

    /**
     * Model the waypoint's position relative to the ship.
     */
    private static class Waypoint {
        int positionEW = 10;
        int positionNS = 1;

        /**
         * Translate the waypoint in the given direction by the distance given.
         * 
         * @param direction The {@link CardinalDirection} in which to translate the
         *     waypoint.
         * @param distance The distance to move the waypoint.
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

        /**
         * Rotate the waypoint around the ship by a number of degrees.
         * 
         * @param degrees A multiple of 90 degrees by which to rotate the ship. A
         *     positive value means to the right, a negative value means to the left.
         */
        public void turn(int degrees) {

            int absoluteDegrees = (degrees + 360) % 360;
            int temp;
            switch (absoluteDegrees) {
                case 90:
                    // NS = -EW, EW = NS
                    temp = -positionEW;
                    positionEW = positionNS;
                    positionNS = temp;
                    break;
                case 180:
                    // NS = -NS, EW = -EW
                    positionEW = -positionEW;
                    positionNS = -positionNS;
                    break;
                case 270:
                    // NS = EW, EW = -NS
                    temp = positionEW;
                    positionEW = -positionNS;
                    positionNS = temp;
                    break;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %d, %s %d",
                                 positionEW >= 0 ? "east" : "west", Math.abs(positionEW),
                                 positionNS >= 0 ? "north" : "south", Math.abs(positionNS));
        }
    }

    /**
     * Model the ship's position and orientation.
     */
    private static class Ship {
        CardinalDirection orientation = CardinalDirection.E;
        int positionEW = 0;
        int positionNS = 0;

        /**
         * Rotate the ship's orientation by a number of degrees.
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
         * Translate the ship in the direction of the waypoint by the given number of
         * times.
         * 
         * @param heading The direction, and distance, to move the ship.
         * @param times The number of times to move the ship towards the waypoint.
         */
        public void advanceTowardWaypoint(Waypoint heading, int times) {
            // Determine the total EW, and NS, distance to translate
            translate(CardinalDirection.E, heading.positionEW * times);
            translate(CardinalDirection.N, heading.positionNS * times);

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
