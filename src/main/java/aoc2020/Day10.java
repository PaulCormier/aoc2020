package aoc2020;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

/**
 * https://adventofcode.com/2020/day/10
 *
 * @author Paul Cormier
 * 
 */
public class Day10 {

    private static final String INPUT_TXT = "Input-Day10.txt";
    private static final String TEST_INPUT_TXT = "TestInput-Day10.txt";
    private static final String TEST_INPUT_2_TXT = "TestInput2-Day10.txt";

    public static void main(String[] args) {
        //        part1();
        part2();
    }

    /**
     * Count the differences between the Joltages in the list.
     */
    private static void part1() {
        List<Integer> joltages = FileUtils.readFileToStream(TEST_INPUT_TXT)
                                          .map(Integer::valueOf)
                                          .sorted()
                                          .collect(Collectors.toList());

        List<Integer> differences = new ArrayList<>();
        Integer previousJoltage = 0;
        for (Integer joltage : joltages) {
            differences.add(joltage - previousJoltage);
            previousJoltage = joltage;
        }
        Map<Object, List<Integer>> aggregateDifferences = differences.stream().collect(Collectors.groupingBy(d -> d));

        System.out.println("There are " + aggregateDifferences.get(1).size() + " 1s, and " +
                           (aggregateDifferences.get(3).size() + 1) + " 3s." +
                           " Their product is: " + (aggregateDifferences.get(1).size() * (aggregateDifferences.get(3).size() + 1)));
    }

    /*
     * Didn't really pan out...
     */
    /*    private static void part2() {
    
        List<Integer> joltages = FileUtils.readFileToStream(TEST_INPUT_TXT)
                                          .map(Integer::valueOf)
                                          .sorted()
                                          .collect(Collectors.toList());
    
        List<Integer> differences = new ArrayList<>();
        Integer previousJoltage = 0;
        for (Integer joltage : joltages) {
            differences.add(joltage - previousJoltage);
            previousJoltage = joltage;
        }
        Map<Object, List<Integer>> aggregateDifferences = differences.stream().collect(Collectors.groupingBy(d -> d));
    
        // Maximum joltage (+3)
        int maxJoltage = joltages.get(joltages.size() - 1) + 3;
    
        // There are only differences of 1 and 3...
        // NOPE! 
        int diff1 = aggregateDifferences.get(1).size();
        int diff3 = aggregateDifferences.get(3).size() + 1;
    
        // So the total difference from 0 to max+3 must be: 1x + 3y = maxJoltage
        // But the total number of combinations is xC(diff1) + yC(diff3), for each solution.
    
        System.out.printf("There are %d 1s, and %d 3s. They must sum to %d jolts.%n", diff1, diff3, maxJoltage);
    
        long totalSolutions = 0;
        for (int x = 0; x <= diff1; x++) {
            for (int y = 0; y <= diff3; y++) {
                if (x + (3 * y) == maxJoltage) {
                    long combinations = CombinatoricsUtils.binomialCoefficient(diff1, x) * CombinatoricsUtils.binomialCoefficient(diff3, y);
                    System.out.printf("%d 1s (%d), %d 3s (%d)%n", x, y,
                                      CombinatoricsUtils.binomialCoefficient(diff1, x),
                                      CombinatoricsUtils.binomialCoefficient(diff3, y));
                    totalSolutions += combinations;
                }
            }
        }
    
        System.out.println("There are " + totalSolutions + " total solutions.");
    }
    */

    /*
    * Not quite working...
     */
    private static void part2() {

        // List of graph edges.
        List<Integer> joltages = FileUtils.readFileToStream(INPUT_TXT)
                                          .map(Integer::valueOf)
                                          .sorted()
                                          .collect(Collectors.toList());
        // Add implicit 0, and max+3
        joltages.add(0, 0);
        int maxJoltage = joltages.get(joltages.size() - 1) + 3;
        joltages.add(maxJoltage);

        System.out.println(joltages);

        List<Edge> edges = new ArrayList<>();

        // Look for all potential joltages for an edge
        Integer[] joltagesArray = joltages.toArray(Integer[]::new);
        for (int i = 0; i < joltagesArray.length; i++) {
            for (int j = 1; j <= 3 && j + i < joltagesArray.length; j++) {
                if (joltagesArray[i + j] - joltagesArray[i] <= 3) {
                    edges.add(new Edge(i, i + j));
                    //                    edges.add(new Edge(joltagesArray[i], joltagesArray[i+j]));
                }
            }
        }

        System.out.println(edges);

        // Number of vertices in the graph
        final int N = joltagesArray.length;

        System.out.println("Number of vertices: " + N);
        System.out.println("Max Joltage: " + maxJoltage);

        // construct graph
        Day10.Graph g = new Day10.Graph(edges, N);

        System.out.println(g.adjList);

        int src = 0;
        //                int dest = maxJoltage;
        int dest = joltagesArray.length - 1;
        //        int numberOfEdges = joltagesArray.length-1;
        int maxDepth = joltagesArray.length - 1;

        // Do modified BFS traversal from source vertex src
        long totalPaths = IntStream.rangeClosed(1, maxDepth)
                                   .mapToLong(i -> modifiedBFS(g, src, dest, i))
                                   .sum();
        System.out.println(totalPaths);
        //        System.out.println(modifiedBFS(g, src, dest, maxDepth));
    }

    //    https://www.techiedelight.com/total-paths-in-digraph-from-source-to-destination-m-edges/
    // Perform BFS on graph g starting from vertex v
    public static int modifiedBFS(Day10.Graph g, int src, int dest, int m) {
        // create a queue used to do BFS
        Queue<Node> q = new ArrayDeque<>();

        // push source vertex into the queue
        q.add(new Node(src, 0));

        // stores number of paths from source to destination
        // having exactly m edges
        int count = 0;

        // loop till queue is empty
        while (!q.isEmpty()) {
            // pop front node from queue
            Node node = q.poll();

            int v = node.vertex;
            int depth = node.depth;

            // if destination is reached and BFS depth is equal to m
            // update count
            if (v == dest && depth == m)
                count++;

            // don't consider nodes having BFS depth more than m.
            // This check will result in optimized code and also
            // handle cycles in the graph (else loop will never break)
            if (depth > m)
                break;

            // do for every adjacent vertex u of v
            for (int u : g.adjList.get(v)) {
                // push every vertex (discovered or undiscovered) into
                // the queue
                q.add(new Node(u, depth + 1));
            }
        }

        // return number of paths from source to destination
        return count;
    }

    // data structure to store graph edges
    static class Edge {
        int source, dest;

        public Edge(int source, int dest) {
            this.source = source;
            this.dest = dest;
        }

        @Override
        public String toString() {
            return source + "->" + dest;
        }
    }

    // BFS Node
    static class Node {
        // stores current vertex number and current depth of
        // BFS (how far away current node is from the source?)
        int vertex, depth;

        public Node(int vertex, int depth) {
            this.vertex = vertex;
            this.depth = depth;
        }
    }

    // class to represent a graph object
    static class Graph {
        // A List of Lists to represent an adjacency list
        //        Map<Integer, List<Integer>> adjList = null;
        List<List<Integer>> adjList = null;

        // Constructor
        Graph(List<Edge> edges, int N) {
            //            adjList = new HashMap<>();
            adjList = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                adjList.add(new ArrayList<>());
            }

            // add edges to the directed graph
            for (Edge edge : edges) {
                int src = edge.source;
                int dest = edge.dest;

                //                adjList.computeIfAbsent(src, ArrayList::new).add(dest);
                adjList.get(src).add(dest);
            }
        }
    }

    /**
     * What is the total number of distinct ways you can arrange the adapters to
     * connect the charging outlet to your device?
     */
    private static void part2_slow() {

        // List of graph edges.
        List<Integer> joltages = FileUtils.readFileToStream(INPUT_TXT)
                                          .map(Integer::valueOf)
                                          .sorted()
                                          .collect(Collectors.toList());
        // Add implicit 0, and max+3
        joltages.add(0, 0);
        int maxJoltage = joltages.get(joltages.size() - 1) + 3;
        joltages.add(maxJoltage);

        //        System.out.println(joltages);

        // Create a graph of the combinations
        org.jgrapht.Graph<Integer, DefaultEdge> g = new SimpleDirectedGraph<>(DefaultEdge.class);

        // Add the vertices
        joltages.forEach(g::addVertex);

        // Add the edges
        Integer[] joltagesArray = joltages.toArray(Integer[]::new);
        for (int i = 0; i < joltagesArray.length; i++) {
            for (int j = 1; j <= 3 && j + i < joltagesArray.length; j++) {
                if (joltagesArray[i + j] - joltagesArray[i] <= 3) {
                    g.addEdge(joltagesArray[i], joltagesArray[i + j]);
                }
            }
        }

        System.out.println(g);

        List<GraphPath<Integer, DefaultEdge>> allPaths = new AllDirectedPaths<>(g).getAllPaths(0, maxJoltage, true, null);
        //        System.out.println(allPaths);

        System.out.println("There are " + allPaths.size() + " combinations.");
    }
}
