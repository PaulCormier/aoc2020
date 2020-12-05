package aoc2020;

/**
 * https://adventofcode.com/2020/day/5
 * 
 * @author Paul Cormier
 * 
 */
public class Day5 {

    private static final String INPUT_TXT = "Day5Input.txt";
    private static final String TEST_INPUT_TXT = "Day5TestInput.txt";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What is the highest seat ID on a boarding pass?
     */
    private static void part1() {
        int maxSeatId = FileUtils.readFileToStream(INPUT_TXT)
                                 .map(Day5::parseSeatId)
                                 .peek(System.out::println)
                                 .max(Integer::compare)
                                 .get();
        System.out.println("The highest seat id is: " + maxSeatId);
    }

    /**
     * What is the missing seat id?
     */
    private static void part2() {
        int maxSeatId = FileUtils.readFileToStream(INPUT_TXT)
                                 .map(Day5::parseSeatId)
                                 .sorted()
//                                 .peek(System.out::println)
                                 .min(Integer::compare)
                                 .get();
        System.out.println("The lowest seat id is: " + maxSeatId);
    }

    /**
     * Use binary space partitioning to decode the seat id.
     * 
     * @param seatCode The F/B L/R code to be decoded into a seat id.
     * @return The unique id of the seat represented in the code.
     */
    private static int parseSeatId(String seatCode) {
//        System.out.print(seatCode + ": ");

        // Row id 0-127
        // Each of the first 7 characters are used in binary search for row
        String binaryRowString = seatCode.substring(0, 7).replace("F", "0").replace("B", "1");
//        System.out.print(binaryString);

        int rowId = Integer.parseInt(binaryRowString, 2);
//        System.out.print(rowId + ", ");

        // Column id
        String binaryColumnString = seatCode.substring(7).replace("L", "0").replace("R", "1");
        int columnId = Integer.parseInt(binaryColumnString, 2);

//        System.out.print(columnId + ", ");

        // Unique id
        return rowId * 8 + columnId;
    }
}
