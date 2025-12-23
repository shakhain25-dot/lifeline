package com.lifeline.datastructures;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(String name) {
        nodes.putIfAbsent(name, new Node(name));
    }

    public void addEdge(String from, String to, double weight) {
        Node fromNode = nodes.get(from);
        Node toNode = nodes.get(to);

        if (fromNode != null && toNode != null) {
            fromNode.addEdge(new Edge(toNode, weight));
            toNode.addEdge(new Edge(fromNode, weight)); // Undirected graph
        }
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public boolean containsNode(String name) {
        return nodes.containsKey(name);
    }

    // Public Node class
    public static class Node {
        private String name;
        private List<Edge> edges;
        private double distance;
        private Node previous;

        public Node(String name) {
            this.name = name;
            this.edges = new ArrayList<>();
            this.distance = Double.POSITIVE_INFINITY;
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public String getName() {
            return name;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public void reset() {
            this.distance = Double.POSITIVE_INFINITY;
            this.previous = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Public Edge class
    public static class Edge {
        private Node target;
        private double weight;

        public Edge(Node target, double weight) {
            this.target = target;
            this.weight = weight;
        }

        public Node getTarget() {
            return target;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Edge{target=" + target.getName() + ", weight=" + weight + "}";
        }
    }
}