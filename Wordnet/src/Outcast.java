import edu.princeton.cs.algs4.Digraph;

import java.math.BigInteger;

public class Outcast {
    WordNet wordnet;
//    Digraph G;
//    SAP sap;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet){
        this.wordnet = wordnet;
    }

    private int outcastDistance(WordNet wordnet, String noun) {
        // 1. noun to id
        int vid = wordnet.getSynID(noun);

        // 2. get distances for this id to every noun in the wordnet
        if (vid == -1) {
            return -1;
        }

        int totalDist = 0;
        for (Integer wid : wordnet.synonyms.keySet()) {
            if (wid == vid) {
                // skip itself, self to self should be 0
                continue;
            }

            int vwDist = wordnet.sap.length(vid, wid);
            if (vwDist != -1) {
                totalDist += vwDist;
            }
        }
        // 3. sum them up

        return totalDist;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = -1;
        int cuurentDist = -1;
        String outcast = null;

        for (String noun : nouns) {
            // TODO: something wrong, so slow here
            int x = 0;
            cuurentDist = outcastDistance(wordnet, noun);
            x = 1;
            if (cuurentDist > maxDist) {
                maxDist = cuurentDist;
                outcast = noun;
            }
        }

        return outcast;
    }


    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
