package aoc2020;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Operation Order
 * 
 * https://adventofcode.com/2020/day/18
 *
 * @author Paul Cormier
 * 
 */
public class Day18 {

    private static final String INPUT_TXT = "Input-Day18.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day18.txt";
    private static final String TEST_INPUT_2_TXT = "TestInput2-Day18.txt";

    public static void main(String[] args) {
        // part1();
        part2();
    }

    /**
     * What is the sum of the resulting values?
     */
    private static void part1() {

        // Get the questions
        // Parse the questions
        // Find the sum of the solutions
        long sum = FileUtils.readFileToStream(INPUT_TXT)
                            // .peek(q -> System.out.print(q + " = "))
                            .mapToLong(Day18::parseString)
                            // .peek(System.out::println)
                            .sum();
        System.out.println("The sum is: " + sum);
    }

    /**
     * What is the sum of the resulting values?
     */
    private static void part2() {

        // Get the questions
        // Parse the questions
        // Find the sum of the solutions
        long sum = FileUtils.readFileToStream(TEST_INPUT_TXT)
                            .peek(System.out::println)
                            // .peek(q -> System.out.print(q + " = "))
                            .map(Day18::addParentheses)
                            .mapToLong(Day18::parseString)
                            .sum();
        System.out.println("The sum is: " + sum);
    }

    /**
     * Add parentheses around the addition operations to enforce operator order.
     * 
     * @param line The arithmetic expression.
     * @return The new expression, with parentheses around the addition operations.
     */
    private static String addParentheses(String line) {
        StringBuilder output = new StringBuilder();

        int parensAdded = 0;
        String value = null;

        char[] characters = line.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            // Check for parentheses
            if (characters[i] == '(') {
                // Find the end, evaluate the substring, and set the resulting value
                int j = i;
                for (int parens = 1; j < characters.length; j++) {
                    if (characters[j] == '(')
                        parens++;
                    else if (characters[j] == ')') {
                        if (--parens == 1)
                            break;
                    }
                }
                output.append("(")
                      .append(addParentheses(new String(characters, i + 1, j - i - 1)))
                      .append(")");
                if (parensAdded > 0) {
                    output.append(')');
                    parensAdded--;
                }
                value = null;
                i = j;
            }

            // Number
            if (NumberUtils.isCreatable(String.valueOf(characters[i]))) {
                value = String.valueOf(characters[i]);
            }

            // Operator
            if (characters[i] == '*') {
                if (value != null)
                    output.append(value);
                if (parensAdded > 0) {
                    output.append(')');
                    parensAdded--;
                }
                output.append('*');
            }
            if (characters[i] == '+') {
                
                if (value != null) {
                        output.append('(').append(value);
                    parensAdded++;
                }
                output.append('+');
            }
        }
        if (value != null) {
            output.append(value);
        }
        if (parensAdded > 0) {
            output.append(')');
            parensAdded--;
        }

        return output.toString();
    }

    /**
     * Parse a string containing an arithmetic expression. Brackets are evaluated
     * first, then operators are evaluated in left-to-right order.
     * 
     * @param line The arithmetic expression to be evaluated.
     * @return The numerical result of the expression.
     */
    private static long parseString(String line) {
        //

        long total = 0;
        long value;
        char operator = '+';

        char[] characters = line.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            // Check for parentheses
            if (characters[i] == '(') {
                // Find the end, evaluate the substring, and set the resulting value
                int j = i;
                for (int parens = 1; j < characters.length; j++) {
                    if (characters[j] == '(')
                        parens++;
                    else if (characters[j] == ')') {
                        if (--parens == 1)
                            break;
                    }
                }
                value = parseString(" " + new String(characters, i + 1, j - i - 1));
                i = j;
                total = operator == '+' ? total + value : total * value;
            }
            // Number
            if (NumberUtils.isCreatable(String.valueOf(characters[i]))) {
                value = Long.parseLong(String.valueOf(characters[i]));
                total = operator == '+' ? total + value : total * value;
            }
            // Operator
            if (characters[i] == '+' || characters[i] == '*') {
                operator = characters[i];
            }

        }
        System.out.println(line + " = " + total);
        return total;
    }

}
