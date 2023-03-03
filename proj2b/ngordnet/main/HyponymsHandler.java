package ngordnet.main;

import ngordnet.ngrams.NGramMap;
import wordnet.WordnetGraph;
import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;

public class HyponymsHandler extends NgordnetQueryHandler {
    WordnetGraph graph;
    NGramMap ngm;

    public HyponymsHandler(WordnetGraph graph, NGramMap ngm) {
        this.graph = graph;
        this.ngm = ngm;
    }

    public String handle(NgordnetQuery q) {
        return graph.hyponyms(q.words(), q.startYear(), q.endYear(), q.k(), ngm);
    }
}
