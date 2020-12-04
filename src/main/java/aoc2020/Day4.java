package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://adventofcode.com/2020/day/4
 * 
 * @author Paul Cormier
 *
 */
public class Day4 {

    private static final String COUNTRY_ID = "cid";
    private static final String INPUT_TXT = "Day4Input.txt";
    private static final String TEST_INPUT_TXT = "Day4TestInput.txt";

    private static Stream<String> readFileToStream(String fileName) {
        try {
            return Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    /**
     * Clean the input: Replace newlines with spaces, then split along double
     * spaces.
     * 
     * @param lines
     *            The stream of lines from the input file. The data may be
     *            separated by newlines, records are separated by two newlines.
     * @return A stream of complete records.
     */
    private static Stream<String> cleanInput(Stream<String> lines) {
        return lines.collect(Collectors.collectingAndThen(Collectors.joining(" "),
                                                          s -> Stream.of(s.split("  "))));
    }

    public static void main(String[] args) {
        part1(cleanInput(readFileToStream(INPUT_TXT)));

    }

    private static void part1(Stream<String> list) {
        long validRecords = list.peek(System.out::println)
                                .map(r -> {
                                    Map<String, String> recordMap = parseRecord(r);
                                    // Ignore Country ID
                                    recordMap.remove(COUNTRY_ID);
                                    return recordMap;
                                })
                                .filter(m -> m.keySet().size() == 7)
                                .count();
        System.out.println("There are " + validRecords + " valid records.");
    }

    private static Map<String, String> parseRecord(String record) {
        return Stream.of(record.split(" "))
                     .map(r -> r.split(":"))
                     .collect(Collectors.toMap(r -> r[0], r -> r[1]));
    }
}
