package wordnet;

import java.util.*;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;

public class WordnetGraph {
    private DirectedGraph graph;
    private HashMap<Integer, ArrayList<String>> nodes;
    private HashMap<String, ArrayList<Integer>> nodeDict;

    public WordnetGraph(String synsetFilename, String hyponymFilename) {
        In synsets = new In(synsetFilename);
        In hyponyms = new In(hyponymFilename);

        nodes = new HashMap<>();
        nodeDict = new HashMap<>();
        graph = new DirectedGraph();

        while (!synsets.isEmpty()) {
            String[] line = synsets.readLine().split(",");
            String[] words = line[1].split(" ");
            ArrayList<String> node = new ArrayList<>();
            int id = Integer.parseInt(line[0]);

            for (String word : words) {
                node.add(word);
                if (!nodeDict.containsKey(word)) {
                    ArrayList<Integer> al = new ArrayList<>();
                    al.add(id);
                    nodeDict.put(word, al);
                } else nodeDict.get(word).add(id);
            }
            nodes.put(id, node);
        }

        while (!hyponyms.isEmpty()) {
            String[] line = hyponyms.readLine().split(",");
            int id = Integer.parseInt(line[0]);

            for (int i = 1; i < line.length; i++) {
                graph.addEdge(id, Integer.parseInt(line[i]));
            }
        }
    }

    private TreeSet<String> hyponyms(String word) {
        TreeSet<String> hyps = new TreeSet<>();
        ArrayList<Integer> nodesIn = nodeDict.get(word);
        if (nodesIn == null) return hyps;

        for (int id : nodesIn) {
            for (String synonym : nodes.get(id)) hyps.add(synonym);

            ArrayList<Integer> adjs = graph.adjacent(id);
            if (adjs == null) continue;
            while (adjs.size() > 0) {
                ArrayList<Integer> next = new ArrayList<>();
                for (int adj : adjs) {
                    for (String hyp : nodes.get(adj)) hyps.add(hyp);
                    if (graph.adjacent(adj) != null) next.addAll(graph.adjacent(adj));
                }
                adjs = next;
            }
        }

        return hyps;
    }

    private double count(String word, int startYear, int endYear, NGramMap ngm) {
        double sum = 0;
        for (double count : ngm.countHistory(word, startYear, endYear).values()) sum += count;
        return sum;
    }

    public String hyponyms(List<String> words, int startYear, int endYear, int k, NGramMap ngm) {
        TreeSet<String> intersection = new TreeSet<>();
        for (String word : words) {
            if (intersection.size() == 0) intersection = hyponyms(word);
            else intersection.retainAll(hyponyms(word));
        }

        if (k == 0) return intersection.toString();

        class popularity implements Comparator<String> {
            public int compare(String w1, String w2) {
                return (int) (count(w2, startYear, endYear, ngm) - (count(w1, startYear, endYear, ngm)));
            }
        }

        Comparator<String> c = new popularity();

        TreeSet<String> hyps = new TreeSet<>(c);
        for (String word : intersection) if (count(word, startYear, endYear, ngm) > 0) hyps.add(word);

        if (hyps.size() < k) return hyps.toString();

        TreeSet<String> limited = new TreeSet<>();
        int i = 0;
        for (String word : hyps) {
            if (i++ == k) break;
            limited.add(word);
        }

        return limited.toString();
    }
}


