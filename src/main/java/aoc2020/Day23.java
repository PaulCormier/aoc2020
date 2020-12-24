package aoc2020;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Crab Cups
 * 
 * https://adventofcode.com/2020/day/23
 *
 * @author Paul Cormier
 * 
 */
public class Day23 {

    private static final String PUZZLE_INPUT = "872495136";
    private static final String TEST_PUZZLE_INPUT = "389125467";

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What are the labels on the cups after cup 1?
     */
    private static void part1() {
        // int move = 1;
        LinkedList<Integer> circle = TEST_PUZZLE_INPUT.chars().map(c -> c - '0').boxed()
                                                      .collect(Collectors.toCollection(LinkedList::new));
        // System.out.println(circle);

        /*int currentCupIndex = 0;
        int currentCup = 0;
        int destinationIndex;
        int destinationCup;
        List<Integer> heldCups;
        while (move <= 100) {
            System.out.println("-- move " + move + " --");
            currentCup = circle.get(currentCupIndex);
        
            System.out.println("cups: "
                               + circle.stream().limit(currentCupIndex)
                                       .map(Object::toString)
                                       .collect(Collectors.joining(" "))
                               + " (" + currentCup + ") "
                               + circle.subList(currentCupIndex + 1, 9)
                                       .stream()
                                       .map(Object::toString)
                                       .collect(Collectors.joining(" ")));
        
            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups = new ArrayList<>(circle.subList(currentCupIndex + 1, Math.min(9, currentCupIndex + 4)));
            heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 - 9))));
            circle.removeAll(heldCups);
            System.out.println("pick up: " + heldCups.stream()
                                                     .map(Object::toString)
                                                     .collect(Collectors.joining(", ")));
            // System.out.println("pick up: " +heldCups);
        
            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup - 1;
            while (destinationCup > 0 && heldCups.contains(destinationCup))
                destinationCup--;
            if (destinationCup == 0)
                destinationCup = circle.stream().mapToInt(Integer::intValue).max().getAsInt();
            System.out.println("destination: " + destinationCup);
        
            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            destinationIndex = circle.indexOf(destinationCup);
            circle.addAll(destinationIndex + 1, heldCups);
        
            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            currentCupIndex = (circle.indexOf(currentCup) + 1) % 9;
            System.out.println();
            move++;
        }*/

        playCrabCups(circle, 100L);

        System.out.println("-- final --");
        System.out.println("cups: " + circle);
        Collections.rotate(circle, -circle.indexOf(1));
        circle.remove(0);
        System.out.println(circle.stream().map(Object::toString).collect(Collectors.joining()));
    }

    /**
     * Determine which two cups will end up immediately clockwise of cup 1. What do
     * you get if you multiply their labels together?
     */
    private static void part2() {
        long totalMoves = 1000;
        // int maxNuber = 1000;
        // long totalMoves = 10_000_000;
        int maxNuber = 1_000_000;

//        long move = 1;
        LinkedList<Integer> circle = TEST_PUZZLE_INPUT.chars().map(c -> c - '0').boxed()
                                                      .collect(Collectors.toCollection(LinkedList::new));
        // Now add the rest of the numbers... to 1,000,000
        circle.addAll(IntStream.rangeClosed(10, maxNuber).boxed().collect(Collectors.toList()));

        // System.out.println("Max size: "+ maxNuber +" Circle size: "+circle.size());
        long start = System.currentTimeMillis();
        System.out.printf("Part 2... %tT.%1$tL%n", start);

        playCrabCups(circle, totalMoves);

        /*
        int currentCupIndex = 0;
        int currentCup = 0;
        int destinationIndex;
        int destinationCup;
        List<Integer> heldCups = new ArrayList<>();
        while (move <= totalMoves) {
            // System.out.println("-- move " + move + " --");
            currentCup = circle.get(currentCupIndex);
        
            // System.out.println("cups: "
            // + circle.stream().limit(currentCupIndex)
            // .map(Object::toString)
            // .collect(Collectors.joining(" "))
            // + " (" + currentCup + ") "
            // + circle.subList(currentCupIndex + 1, circle.size())
            // .stream()
            // .map(Object::toString)
            // .collect(Collectors.joining(" ")));
        
            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups.clear();
            heldCups.addAll(circle.subList(currentCupIndex + 1, Math.min(maxNuber, currentCupIndex + 4)));
            heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 - maxNuber))));
            circle.removeAll(heldCups);
            // System.out.println("pick up: " + heldCups.stream()
            // .map(Object::toString)
            // .collect(Collectors.joining(", ")));
            // System.out.println("pick up: " +heldCups);
        
            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup - 1;
            while (destinationCup > 0 && heldCups.contains(destinationCup)) {
                destinationCup--;
                if (destinationCup == 0)
                    destinationCup = maxNuber;
            }
            // destinationCup =
            // circle.stream().mapToInt(Integer::intValue).max().getAsInt();
            // System.out.println("destination: " + destinationCup);
        
            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            destinationIndex = circle.indexOf(destinationCup);
            circle.addAll(destinationIndex + 1, heldCups);
        
            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            currentCupIndex = (circle.indexOf(currentCup) + 1) % maxNuber;
            // System.out.println();
            move++;
            if (move % 1_000_000 == 0)
                System.out.println(move);
        }
        // System.out.println("-- final --");
        // System.out.println("cups: " + circle);
        // Collections.rotate(circle, -circle.indexOf(1));
        // circle.remove(0);
        // System.out.println(circle.stream().map(Object::toString).collect(Collectors.joining()));
        */
        long end = System.currentTimeMillis();
        System.out.println(Duration.ofMillis(end - start));
        // System.out.printf("%tT.%d%n",end-start);

        int indexOf1 = circle.indexOf(1);
        System.out.println(circle.get((indexOf1 + 1) % maxNuber) + " and " + circle.get((indexOf1 + 2) % maxNuber));
    }

    /**
     * Play the Crab Cups game a certain number of times. The circle of cups will be
     * shuffled a number of times.
     * 
     * @param circle The initial circle of cups.
     * @param totalMoves The number of moves to perform.
     */
    private static void playCrabCups(LinkedList<Integer> circle, long totalMoves) {

        int maxNumber = circle.size();
        int move = 1;
        int currentCupIndex = 0;
        int currentCup = 0;
        int destinationIndex;
        int destinationCup;
        List<Integer> heldCups;
        while (move <= totalMoves) {
            // System.out.println("-- move " + move + " --");
            currentCup = circle.get(currentCupIndex);

            // System.out.println("cups: "
            // + circle.stream().limit(currentCupIndex)
            // .map(Object::toString)
            // .collect(Collectors.joining(" "))
            // + " (" + currentCup + ") "
            // + circle.subList(currentCupIndex + 1, 9)
            // .stream()
            // .map(Object::toString)
            // .collect(Collectors.joining(" ")));

            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups = new ArrayList<>(circle.subList(currentCupIndex + 1, Math.min(maxNumber, currentCupIndex + 4)));
            heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 - maxNumber))));
            circle.removeAll(heldCups);
            // System.out.println("pick up: " + heldCups.stream()
            // .map(Object::toString)
            // .collect(Collectors.joining(", ")));
            // System.out.println("pick up: " +heldCups);

            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup == 1 ? maxNumber : currentCup - 1;
            while (destinationCup > 0 && heldCups.contains(destinationCup)) {
                destinationCup--;
                if (destinationCup == 0)
                    destinationCup = maxNumber;
            }
            // System.out.println("destination: " + destinationCup);

            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            destinationIndex = circle.indexOf(destinationCup);
            circle.addAll(destinationIndex + 1, heldCups);

            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            currentCupIndex = (circle.indexOf(currentCup) + 1) % maxNumber;
            // System.out.println();
            move++;
        }
    }
}
