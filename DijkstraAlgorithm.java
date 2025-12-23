package com.lifeline.algorithm;

import com.lifeline.datastructures.Graph;
import com.lifeline.datastructures.Graph.Node;
import com.lifeline.datastructures.Graph.Edge;
import java.util.*;

public class DijkstraAlgorithm {

    public static List<String> findShortestPath(Graph graph, String startName, String endName) {
        if (!graph.containsNode(startName) || !graph.containsNode(endName)) {
            return Collections.emptyList();
        }

        Node start = graph.getNode(startName);
        Node end = graph.getNode(endName);

        // Reset all nodes
        for (Node node : graph.getNodes().values()) {
            node.reset();
        }

        // Initialize
        start.setDistance(0.0);

        PriorityQueue<Node> queue = new PriorityQueue<>(
                Comparator.comparingDouble(Node::getDistance)
        );
        Set<Node> visited = new HashSet<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (current.equals(end)) {
                break;
            }

            for (Edge edge : current.getEdges()) {
                Node neighbor = edge.getTarget();

                if (!visited.contains(neighbor)) {
                    double newDistance = current.getDistance() + edge.getWeight();

                    if (newDistance < neighbor.getDistance()) {
                        neighbor.setDistance(newDistance);
                        neighbor.setPrevious(current);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Build path
        return buildPath(end);
    }

    private static List<String> buildPath(Node end) {
        List<String> path = new ArrayList<>();
        Node current = end;

        if (current.getDistance() == Double.POSITIVE_INFINITY) {
            return Collections.emptyList(); // No path found
        }

        while (current != null) {
            path.add(0, current.getName());
            current = current.getPrevious();
        }

        return path;
    }

    public static double getShortestDistance(Graph graph, String startName, String endName) {
        List<String> path = findShortestPath(graph, startName, endName);

        if (path.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }

        Node endNode = graph.getNode(endName);
        return endNode.getDistance();
    }
}