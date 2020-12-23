package aoc2020;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Crab Combat
 * 
 * https://adventofcode.com/2020/day/22
 *
 * @author Paul Cormier
 * 
 */
public class Day22 {

    private static final int[] PLAYER_1 = { 50, 14, 10, 17, 38, 40, 3, 46, 39, 25, 18, 2, 41, 45, 7, 47, 36, 1, 30, 32, 8, 31, 12, 5, 28 };
    private static final int[] PLAYER_2 = { 9, 6, 37, 42, 22, 4, 21, 15, 44, 16, 29, 43, 19, 11, 13, 24, 48, 35, 26, 23, 27, 33, 20, 49,
                                            34 };

    private static final int[] TEST_PLAYER_1 = { 9, 2, 6, 3, 1 };
    private static final int[] TEST_PLAYER_2 = { 5, 8, 4, 7, 10 };

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What is the winning player's score?
     */
    private static void part1() {
        // Setup the players' hands
        Queue<Integer> player1Hand = new ArrayDeque<>(Arrays.stream(PLAYER_1).boxed().collect(Collectors.toList()));
        Queue<Integer> player2Hand = new ArrayDeque<>(Arrays.stream(PLAYER_2).boxed().collect(Collectors.toList()));

        // Play until a player runs out of cards.
        int round = 1;
        while (player1Hand.size() > 0 && player2Hand.size() > 0) {
            System.out.printf("-- Round %d --%n", round++);
            System.out.printf("Player 1's deck: %s%nPlayer 2's deck: %s%n", player1Hand, player2Hand);
            System.out.printf("Player 1 plays: %d%nPlayer 2 plays: %d%n", player1Hand.peek(), player2Hand.peek());

            if (player1Hand.peek() > player2Hand.peek()) {
                System.out.println("Player 1 wins the round!\n");
                player1Hand.add(player1Hand.remove());
                player1Hand.add(player2Hand.remove());
            } else if (player1Hand.peek() < player2Hand.peek()) {
                System.out.println("Player 2 wins the round!\n");
                player2Hand.add(player2Hand.remove());
                player2Hand.add(player1Hand.remove());
            } else {
                System.out.println("It's a tie!\n");
                player1Hand.add(player1Hand.remove());
                player2Hand.add(player2Hand.remove());
            }
        }

        System.out.println("== Post-game results ==");
        System.out.println("Player 1's deck: " + player1Hand);
        System.out.println("Player 2's deck: " + player2Hand);

        System.out.println("Player " + (player1Hand.isEmpty() ? 2 : 1) + " wins!\n");

        // Calculate score
        Collection<Integer> winningHand = player1Hand.isEmpty() ? player2Hand : player1Hand;
        int score = 0;
        int multiplier = winningHand.size();
        for (int card : winningHand) {
            score += card * multiplier--;
        }

        System.out.println("Final score: " + score);
    }

    /**
     */
    private static void part2() {
    }
}
