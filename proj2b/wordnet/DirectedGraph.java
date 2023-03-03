package wordnet;

import java.util.ArrayList;
import java.util.HashMap;

public class DirectedGraph {
    private HashMap<Integer, ArrayList<Integer>> graph;

    public DirectedGraph() {
        graph = new HashMap<>();
    }

    public void addEdge(int from, int to) {
        if (graph.get(from) == null) graph.put(from, new ArrayList<>());
        graph.get(from).add(to);
    }

    public ArrayList<Integer> adjacent(int vertex) {
        return graph.get(vertex);
    }
}