package aoc2020;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

//        playCrabCups(circle, 100L);
        playCrabCups(new Circle<>(circle),circle.get(0), 100L);

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

        // long move = 1;
        // ArrayList<Integer> circle = TEST_PUZZLE_INPUT.chars().map(c -> c -
        // '0').boxed()
        // .collect(Collectors.toCollection(ArrayList::new));
        // // Now add the rest of the numbers... to 1,000,000
        // circle.addAll(IntStream.rangeClosed(10,
        // maxNuber).boxed().collect(Collectors.toList()));

        List<Integer> cups = TEST_PUZZLE_INPUT.chars().map(c -> c - '0').boxed()
                                              .collect(Collectors.toList());
        // Now add the rest of the numbers... to 1,000,000
        cups.addAll(IntStream.rangeClosed(10, maxNuber).boxed().collect(Collectors.toList()));
        Circle<Integer> circle = new Circle<>(cups);

        // System.out.println("Max size: "+ maxNuber +" Circle size: "+circle.size());
        long start = System.currentTimeMillis();
        System.out.printf("\nPart 2... %tT.%1$tL%n", start);

        playCrabCups(circle, cups.get(0), totalMoves);

        long end = System.currentTimeMillis();
        System.out.println("This run: " + Duration.ofMillis(end - start));
        System.out.println("Estimate for 10,000,000 runs: "
                           + Duration.ofMillis((end - start) * 10_000_000L / totalMoves));
        // System.out.printf("%tT.%d%n",end-start);

        Circle.Node cup1 = circle.get(1);
        System.out.println(cup1.next.item + " and " + cup1.next.next.item);
        // int indexOf1 = circle.indexOf(1);
        // System.out.println(circle.get((indexOf1 + 1) % maxNuber) + " and " +
        // circle.get((indexOf1 + 2) % maxNuber));
    }

    /**
     * Play the Crab Cups game a certain number of times. The circle of cups will be
     * shuffled a number of times.
     * 
     * @param circle The initial circle of cups.
     * @param totalMoves The number of moves to perform.
     * @param startingValue
     */
    private static void playCrabCups(Circle<Integer> circle, int startingValue, long totalMoves) {
        boolean debug = false;

        int maxNumber = circle.size();
        int move = 1;
        // int currentCupIndex = 0;
        Circle.Node currentCup = circle.get(startingValue);
        // int destinationIndex;
        int destinationCup;
        // List<Integer> heldCups=new ArrayList<>();
        Circle.Node heldCups;
        while (move <= totalMoves) {
            if (debug)
                System.out.println("-- move " + move + " --");
            // currentCup = circle.remove(0);
            // circle.add(currentCup);

            if (debug) {
                List<Integer> tempCircle = new LinkedList<>();
                Circle.Node tempCup = currentCup;
                do {
                    tempCircle.add(tempCup.item);
                    tempCup = tempCup.next;
                } while (tempCup != currentCup);
                System.out.println("cups: " + tempCircle.stream()
                                                        .map(Object::toString)
                                                        .collect(Collectors.joining(" ")));
            }

            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups = circle.removeNext(currentCup, 3);
            // heldCups[0] = circle.remove(0);
            // heldCups[1] = circle.remove(0);
            // heldCups[2] = circle.remove(0);
            // heldCups.clear();
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups = new ArrayList<>(circle.subList(currentCupIndex + 1,
            // Math.min(maxNumber, currentCupIndex + 4)));
            // heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 -
            // maxNumber))));
            // circle.removeAll(heldCups);
            if (debug)
                // System.out.println("pick up: " + heldCups.stream()
                System.out.println("pick up: " + Stream.of(heldCups.item, heldCups.next.item, heldCups.next.next.item)
                                                       .map(Object::toString)
                                                       .collect(Collectors.joining(", ")));

            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup.item == 1 ? maxNumber : currentCup.item - 1;
            // while (destinationCup > 0 && heldCups.contains(destinationCup)) {
            while (destinationCup > 0 && (heldCups.item == destinationCup || heldCups.next.item == destinationCup
                                          || heldCups.next.next.item == destinationCup)) {
                destinationCup--;
                if (destinationCup == 0)
                    destinationCup = maxNumber;
            }
            if (debug)
                System.out.println("destination: " + destinationCup);

            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            circle.addAfter(circle.get(destinationCup), heldCups);
            // destinationIndex = circle.indexOf(destinationCup) + 1;
            // circle.addAll(destinationIndex, Arrays.asList(heldCups));
            // circle.add(destinationIndex, heldCups[2]);
            // circle.add(destinationIndex, heldCups[1]);
            // circle.add(destinationIndex, heldCups[0]);

            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            // currentCupIndex = (circle.indexOf(currentCup) + 1) % maxNumber;
            currentCup = currentCup.next;
            move++;
            if (debug)
                System.out.println();
        }
    }

    /**
     * Play the Crab Cups game a certain number of times. The circle of cups will be
     * shuffled a number of times.
     * 
     * @param circle The initial circle of cups.
     * @param totalMoves The number of moves to perform.
     */
    private static void playCrabCups(List<Integer> circle, long totalMoves) {
        boolean debug = false;

        int maxNumber = circle.size();
        int move = 1;
        int currentCupIndex = 0;
        Integer currentCup;
        int destinationIndex;
        int destinationCup;
        // List<Integer> heldCups=new ArrayList<>();
        Integer[] heldCups = new Integer[3];
        while (move <= totalMoves) {
            if (debug)
                System.out.println("-- move " + move + " --");
            // currentCup = circle.remove(0);
            // circle.add(currentCup);
            currentCup = circle.get(currentCupIndex);

            if (debug)
                System.out.println("cups: " + circle.stream()
                                                    .map(Object::toString)
                                                    .collect(Collectors.joining(" ")));

            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups[0] = circle.remove(currentCupIndex + 1);
            heldCups[1] = circle.remove(currentCupIndex + 1);
            heldCups[2] = circle.remove(currentCupIndex + 1);
            // heldCups[0] = circle.remove(0);
            // heldCups[1] = circle.remove(0);
            // heldCups[2] = circle.remove(0);
            // heldCups.clear();
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups = new ArrayList<>(circle.subList(currentCupIndex + 1,
            // Math.min(maxNumber, currentCupIndex + 4)));
            // heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 -
            // maxNumber))));
            // circle.removeAll(heldCups);
            if (debug)
                // System.out.println("pick up: " + heldCups.stream()
                System.out.println("pick up: " + Stream.of(heldCups)
                                                       .map(Object::toString)
                                                       .collect(Collectors.joining(", ")));

            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup == 1 ? maxNumber : currentCup - 1;
            // while (destinationCup > 0 && heldCups.contains(destinationCup)) {
            while (destinationCup > 0 && (heldCups[0] == destinationCup || heldCups[1] == destinationCup
                                          || heldCups[2] == destinationCup)) {
                destinationCup--;
                if (destinationCup == 0)
                    destinationCup = maxNumber;
            }
            if (debug)
                System.out.println("destination: " + destinationCup);

            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            destinationIndex = circle.indexOf(destinationCup) + 1;
            circle.addAll(destinationIndex, Arrays.asList(heldCups));
            // circle.add(destinationIndex, heldCups[2]);
            // circle.add(destinationIndex, heldCups[1]);
            // circle.add(destinationIndex, heldCups[0]);

            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            currentCupIndex = (circle.indexOf(currentCup) + 1) % maxNumber;
            move++;
            if (debug)
                System.out.println();
        }
    }

    /**
     * Play the Crab Cups game a certain number of times. The circle of cups will be
     * shuffled a number of times.
     * 
     * @param circle The initial circle of cups.
     * @param totalMoves The number of moves to perform.
     */
    private static void playCrabCups(LinkedList<Integer> circle, long totalMoves) {
        boolean debug = false;

        int maxNumber = circle.size();
        int move = 1;
        // int currentCupIndex = 0;
        Integer currentCup;
        int destinationIndex;
        int destinationCup;
        // List<Integer> heldCups=new ArrayList<>();
        Integer[] heldCups = new Integer[3];
        while (move <= totalMoves) {
            if (debug)
                System.out.println("-- move " + move + " --");
            currentCup = circle.poll();
            circle.addLast(currentCup);

            if (debug)
                System.out.println("cups: " + circle.stream()
                                                    .map(Object::toString)
                                                    .collect(Collectors.joining(" ")));

            // The crab picks up the three cups that are immediately clockwise of the
            // current cup. They are removed from the circle; cup spacing is adjusted as
            // necessary to maintain the circle.
            heldCups[0] = circle.poll();
            heldCups[1] = circle.poll();
            heldCups[2] = circle.poll();
            // heldCups.clear();
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups.add(circle.poll());
            // heldCups = new ArrayList<>(circle.subList(currentCupIndex + 1,
            // Math.min(maxNumber, currentCupIndex + 4)));
            // heldCups.addAll(circle.subList(0, Math.max(0, (currentCupIndex + 4 -
            // maxNumber))));
            // circle.removeAll(heldCups);
            if (debug)
                // System.out.println("pick up: " + heldCups.stream()
                System.out.println("pick up: " + Stream.of(heldCups)
                                                       .map(Object::toString)
                                                       .collect(Collectors.joining(", ")));

            // The crab selects a destination cup: the cup with a label equal to the current
            // cup's label minus one. If this would select one of the cups that was just
            // picked up, the crab will keep subtracting one until it finds a cup that
            // wasn't just picked up. If at any point in this process the value goes below
            // the lowest value on any cup's label, it wraps around to the highest value on
            // any cup's label instead.
            destinationCup = currentCup == 1 ? maxNumber : currentCup - 1;
            // while (destinationCup > 0 && heldCups.contains(destinationCup)) {
            while (destinationCup > 0 && (heldCups[0] == destinationCup || heldCups[1] == destinationCup
                                          || heldCups[2] == destinationCup)) {
                destinationCup--;
                if (destinationCup == 0)
                    destinationCup = maxNumber;
            }
            if (debug)
                System.out.println("destination: " + destinationCup);

            // The crab places the cups it just picked up so that they are immediately
            // clockwise of the destination cup. They keep the same order as when they were
            // picked up.
            destinationIndex = circle.indexOf(destinationCup) + 1;
            // circle.addAll(destinationIndex, heldCups);
            circle.add(destinationIndex, heldCups[2]);
            circle.add(destinationIndex, heldCups[1]);
            circle.add(destinationIndex, heldCups[0]);

            // The crab selects a new current cup: the cup which is immediately clockwise of
            // the current cup.
            // currentCupIndex = (circle.indexOf(currentCup) + 1) % maxNumber;
            move++;
            if (debug)
                System.out.println();
        }
    }

    /**
     * Play the Crab Cups game a certain number of times. The circle of cups will be
     * shuffled a number of times.
     * 
     * @param circle The initial circle of cups.
     * @param totalMoves The number of moves to perform.
     */
    private static void playCrabCups_old(LinkedList<Integer> circle, long totalMoves) {

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

    private static class Circle<E> {
        /**
         * A uni-directional linked list node.
         *
         * @param <E> The value of the node.
         */
        protected static class Node {
            int item;
            Node next;

            Node(int element, Node next) {
                this.item = element;
                this.next = next;
            }

        }

        /**
         * To improve performance of the indexOf operation, all nodes are stored in a
         * set.
         */
        private final Node[] index;

        public Circle(List<Integer> otherList) {
            index = new Node[otherList.size()];

            Node lastElement = new Node(otherList.get(otherList.size() - 1), null);
            index[lastElement.item-1] = lastElement;
            Node nextElement = lastElement;
            for (int i = otherList.size() - 2; i > 0; i--) {
                nextElement = new Node(otherList.get(i), nextElement);
                index[nextElement.item-1] = nextElement;
            }
            Node firstElement = new Node(otherList.get(0), nextElement);
            index[firstElement.item-1] = firstElement;
            lastElement.next = firstElement;
        }

        public void addAfter(Node node, Node heldCups) {
            Node nextNode = node.next;
            node.next = heldCups;
            Node lastNode = nextNode;
            while (lastNode.next != null)
                lastNode = lastNode.next;
            lastNode.next = nextNode;
        }

        public Node removeNext(Node node, int number) {
            Node nextNode = node.next;
            Node lastNode = nextNode;
            while (--number > 0) {
                lastNode = lastNode.next;
            }
            node.next = lastNode.next;
            lastNode.next = null;
            return nextNode;
        }

        public int size() {
            return index.length;
        }

        public Node get(int value) {
            return index[value-1];
        }

    }
    /*private static class Circle<E> {
        *//**
            * A uni-directional linked list node.
            *
            * @param <E> The value of the node.
            */
    /*
    protected static class Node<E> {
     E item;
     Node<E> next;
    
     Node(E element, Node<E> next) {
         this.item = element;
         this.next = next;
     }
    
     @Override
     public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((item == null) ? 0 : item.hashCode());
         return result;
     }
    
     @Override
     public boolean equals(Object obj) {
         if (this == obj)
             return true;
         if (obj == null)
             return false;
         if (getClass() != obj.getClass())
             return false;
         Node other = (Node) obj;
         if (item == null) {
             if (other.item != null)
                 return false;
         } else if (!item.equals(other.item))
             return false;
         return true;
     }
    
    }
    
    *//**
        * To improve performance of the indexOf operation, all nodes are stored in a
        * set.
        *//*
          private final Map<E, Node<E>> index;
          
          public Circle() {
           this.index = new TreeMap<>();
          }
          
          public void addAfter(Node<E> node, Node<E> heldCups) {
           Node<E> nextNode = node.next;
           node.next = heldCups;
           Node<E> lastNode = nextNode;
           while (lastNode.next != null)
               lastNode = lastNode.next;
           lastNode.next = nextNode;
          }
          
          public Node<E> removeNext(Node<E> node, int number) {
           Node<E> nextNode = node.next;
           Node<E> lastNode = nextNode;
           while (--number > 0) {
               lastNode = lastNode.next;
           }
           node.next = lastNode.next;
           lastNode.next = null;
           return nextNode;
          }
          
          public int size() {
           return index.size();
          }
          
          public Node<E> get(E value) {
           return index.get(value);
          }
          
          public Circle(List<E> otherList) {
           this();
          
           Node<E> lastElement = new Node<>(otherList.get(otherList.size() - 1), null);
           index.put(lastElement.item, lastElement);
           Node<E> nextElement = lastElement;
           for (int i = otherList.size() - 2; i > 0; i--) {
               nextElement = new Node<>(otherList.get(i), nextElement);
               index.put(nextElement.item, nextElement);
           }
           Node<E> firstElement = new Node<>(otherList.get(0), nextElement);
           index.put(firstElement.item, firstElement);
           lastElement.next = firstElement;
          }
          }*/

}
