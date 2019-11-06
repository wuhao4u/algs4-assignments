import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class WordNet {
    private final ArrayList<LinkedList<String>> synset; // get words with ID
    private final HashMap<String, LinkedList<Integer>> ids; // get ids with string
    private final HashSet<String> nouns; // doest the word exists?

    //    Digraph G;
    private final SAP sap;

    // constructor takes the name of the two input files
    // runtime: O(n)
    // space: O(n)
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null || synsets.length() == 0 || hypernyms.length() == 0) {
            throw new IllegalArgumentException("Wrong WordNet input.");
        }

        synset = new ArrayList<LinkedList<String>>();
        ids = new HashMap<String, LinkedList<Integer>>();

        In s = new In(synsets);

        while (s.hasNextLine()) {
            // synset: ID,synset,dictionary definition
            // synset: ASCII_character word2 word3
            // word1 separated by '_'
            StringTokenizer st = new StringTokenizer(s.readLine(), ",");

            int id = Integer.parseInt(st.nextToken());
            LinkedList<String> set = new LinkedList<String>();
            StringTokenizer sst = new StringTokenizer(st.nextToken());

            while (sst.hasMoreTokens()) {
                String noun = sst.nextToken();
                set.add(noun);

                if (ids.get(noun) == null) {
                    ids.put(noun, new LinkedList<Integer>());
                }

                ids.get(noun).add(id);
            }
            synset.add(set);
        }

        nouns = new HashSet<String>(ids.keySet());
        Digraph G = new Digraph(synset.size());

        // process hypernyms file
        In h = new In(hypernyms);
        while (h.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(h.readLine(), ",");
            int id = Integer.parseInt(st.nextToken());
            while (st.hasMoreTokens()) {
                int hyper = Integer.parseInt(st.nextToken());
                G.addEdge(id, hyper);
            }
        }

        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("Has cycle");
        }

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    // Runtime: O(lg n)
    public boolean isNoun(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException("Wrong isNoun input.");
        }

        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    // runtime: O(n)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || nounA.length() == 0 || nounB.length() == 0) {
            throw new IllegalArgumentException("Wrong distance input!");
        }

        return sap.length(ids.get(nounA), ids.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // using SAP class
    // runtime: O(n)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || nounA.length() == 0 || nounB.length() == 0) {
            throw new IllegalArgumentException("Wrong sap input!");
        }

        LinkedList<Integer> v, w;
        v = ids.get(nounA);
        w = ids.get(nounB);

        int lcsID = this.sap.ancestor(v, w);
        if (lcsID == -1) {
            return null;
        } else {
            LinkedList<String> synsWithID = this.synset.get(lcsID);
            StringBuilder sb = new StringBuilder();

            // if there are some syns
            if (!synsWithID.isEmpty()) {
                sb.append(synsWithID.get(0));

                for (int i = 1; i < synsWithID.size(); ++i) {
                    sb.append(' ');
                    sb.append(synsWithID.get(i));
                }
            }


            return sb.toString();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // create wordnet digraph
        WordNet wn = new WordNet("wordnetTests/synsets100-subgraph.txt",
                "wordnetTests/hypernyms100-subgraph.txt");
//        WordNet wn = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
    }
}
