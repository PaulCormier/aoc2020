package aoc2020;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2020/day/13
 *
 * @author Paul Cormier
 * 
 */
public class Day13 {

    private static final String INPUT_TXT = "Input-Day13.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day13.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * Find the next bus to take.
     */
    private static void part1() {
        List<String> lines = FileUtils.readFile(INPUT_TXT);

        // The first line is the current timestamp
        final long timestamp = Long.parseLong(lines.get(0));

        // The second line is all the running bus numbers.
        int nextBus = Stream.of(lines.get(1).split(","))
                            .filter(StringUtils::isNumeric)
                            .map(Integer::valueOf)
                            .min((a, b) -> Long.compare(a - timestamp % a, b - timestamp % b))
                            .orElse(0);

        int waitTime = nextBus - (int) (timestamp) % nextBus;
        System.out.println("The nex bus is #" + nextBus + " in " + waitTime + " minutes.");
        System.out.println("Solution: " + waitTime * nextBus);
    }

    /**
     * Find the time when the busses depart in a particular order.
     */
    private static void part2() {
    }
}
