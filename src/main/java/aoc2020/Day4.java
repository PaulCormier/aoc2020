package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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

    private static final String INPUT_TXT = "Day4Input.txt";
    private static final String TEST_INPUT_TXT = "Day4TestInput.txt";
    private static final String TEST2_INPUT_TXT = "Day4TestInput2.txt";

    /** Country id is removed, as it is not relevant to validation. */
    private static final String COUNTRY_ID = "cid";

    /** Valid eye colours. */
    private static final List<String> EYE_COLOURS = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

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

        part2(cleanInput(readFileToStream(INPUT_TXT)));
    }

    private static void part1(Stream<String> list) {
        long validRecords = list.peek(System.out::println)
                                .map(Day4::parseRecord)
                                .filter(m -> m.size() == 7)
                                .count();
        System.out.println("There are " + validRecords + " valid records.");
    }

    private static void part2(Stream<String> list) {
        long validRecords = list.map(Day4::parseRecord)
                                .peek(System.out::println)
                                .filter(Day4::isValid)
                                .count();
        System.out.println("There are " + validRecords + " valid records.");
    }

    private static Map<String, String> parseRecord(String record) {
        Map<String, String> passport = Stream.of(record.split(" "))
                                             .map(r -> r.split(":"))
                                             .collect(Collectors.toMap(r -> r[0], r -> r[1]));
        passport.remove(COUNTRY_ID);
        return passport;
    }

    /**
     * <ul>
     * <li>byr (Birth Year) - four digits; at least 1920 and at most 2002.</li>
     * <li>iyr (Issue Year) - four digits; at least 2010 and at most 2020.</li>
     * <li>eyr (Expiration Year) - four digits; at least 2020 and at most
     * 2030.</li>
     * <li>hgt (Height) - a number followed by either cm or in:</li>
     * <li>If cm, the number must be at least 150 and at most 193.</li>
     * <li>If in, the number must be at least 59 and at most 76.</li>
     * <li>hcl (Hair Color) - a # followed by exactly six characters 0-9 or
     * a-f.</li>
     * <li>ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.</li>
     * <li>pid (Passport ID) - a nine-digit number, including leading
     * zeroes.</li>
     * <li>cid (Country ID) - ignored, missing or not.</li>
     * </ul>
     * 
     * @param passport
     * @return
     */
    private static boolean isValid(Map<String, String> passport) {
        boolean valid = true;

        // Has enough values (otherwise stop right now)
        if (passport.size() != 7)
            return false;

        //byr (Birth Year) - four digits; at least 1920 and at most 2002.
        try {
            int byr = Integer.parseInt(passport.getOrDefault("byr", "0"));
            valid = valid && byr >= 1920 && byr <= 2002;
        } catch (NumberFormatException e) {
            valid = false;
        }

        //iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        try {
            int iyr = Integer.parseInt(passport.getOrDefault("iyr", "0"));
            valid = valid && iyr >= 2010 && iyr <= 2020;
        } catch (NumberFormatException e) {
            valid = false;
        }

        //eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        try {
            int eyr = Integer.parseInt(passport.getOrDefault("eyr", "0"));
            valid = valid && eyr >= 2020 && eyr <= 2030;
        } catch (NumberFormatException e) {
            valid = false;
        }

        //hgt (Height) - a number followed by either cm or in:
        //If cm, the number must be at least 150 and at most 193.
        //If in, the number must be at least 59 and at most 76.
        try {
            int hgt = Integer.parseInt(passport.get("hgt").replaceAll("in|cm", ""));
            String unit = passport.get("hgt").replaceAll("[0-9]+", "");
            switch (unit) {
                case "cm":
                    valid = valid && hgt >= 150 && hgt <= 193;
                    break;
                case "in":
                    valid = valid && hgt >= 59 && hgt <= 76;
                    break;
                default:
                    valid = false;
            }
        } catch (NumberFormatException e) {
            valid = false;
        }

        //hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        valid = valid && passport.get("hcl").matches("#[0-9a-f]{6}");

        //ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        valid = valid && EYE_COLOURS.contains(passport.get("ecl"));

        //pid (Passport ID) - a nine-digit number, including leading zeroes.
        valid = valid && passport.get("pid").matches("[0-9]{9}");

        //cid (Country ID) - ignored, missing or not.
        // Already removed

        return valid;
    }
}
