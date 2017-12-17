import edu.princeton.cs.algs4.Digraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordNet {
    class Synonym {
        int id;

        // each id match to multiple nouns
        String[] synonyms;
        String definition;

        // Stores all parents' hypernyms ID
        Set<Integer> hypernyms;

        public Synonym(int id, String[] synonyms, String definition) {
            this.id = id;
            this.synonyms = synonyms;
            this.definition = definition;

            hypernyms = new HashSet<Integer>();
        }
    }

    HashMap<Integer, Synonym> synonyms;
    Digraph G;
    SAP sap;

    // constructor takes the name of the two input files
    // runtime: O(n)
    // space: O(n)
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null || synsets.length() == 0 || hypernyms.length() == 0) {
            throw new IllegalArgumentException("Wrong WordNet input.");
        }

        this.synonyms = new HashMap<Integer, Synonym>();

        // read synsets.txt
        Scanner synScanner, hyperScanner;

        // 2 CSV files
        // 1. synsets.txt : lists all noun in WordNet.
        // format: id,synset1 synset2,definition
        // vertices
        try {
            synScanner = new Scanner(new File(synsets));

            while (synScanner.hasNextLine()) {
                String line = synScanner.nextLine();
                String[] splitedLine = line.split(",");

                int id = Integer.valueOf(splitedLine[0]);
                String[] syns = splitedLine[1].split(" ");
                String definition = splitedLine[2];

                this.synonyms.put(id, new Synonym(id, syns, definition));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Synset file not found.");
        }


        // create a Digraph based on the input
        // syn for vertices, hypernyms for edges
        this.G = new Digraph(this.synonyms.size());


        // read hypernyms.txt
        // 2. hypernyms.txt : hypernym relationships
        // format: id,parent1ID,parent2ID
        // edges in the graph
        try {
            hyperScanner = new Scanner(new File(hypernyms));

            while (hyperScanner.hasNextLine()) {
                String[] splitedLine = hyperScanner.nextLine().split(",");
                int id = Integer.parseInt(splitedLine[0]);

                Synonym child = this.synonyms.get(id);
                int parentID = -1;

                for (int i = 1; i < splitedLine.length; ++i) {
                    parentID = Integer.valueOf(splitedLine[i]);
                    child.hypernyms.add(parentID);

                    G.addEdge(id, parentID);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Hyper file not found.");
        }


        this.sap = new SAP(this.G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        ArrayList<String> result = new ArrayList<String>(this.synonyms.size());

        for (Synonym s : this.synonyms.values()) {
            for (String n : s.synonyms) {
                result.add(n);
            }
        }

        return result;
    }

    // is the word a WordNet noun?
    // Runtime: O(lg n)
    public boolean isNoun(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException("Wrong isNoun input.");
        }

        return this.synonyms.containsValue(word);
    }

    // distance between nounA and nounB (defined below)
    // runtime: O(n)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || nounA.length() == 0 || nounB.length() == 0) {
            throw new IllegalArgumentException("Wrong distance input!");
        }

        // TODO, 2
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // using SAP class
    // runtime: O(n)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || nounA.length() == 0 || nounB.length() == 0) {
            throw new IllegalArgumentException("Wrong sap input!");
        }

        // TODO, test me
        int v, w;
        v = getSynID(nounA);
        w = getSynID(nounB);

        int lcsID = this.sap.ancestor(v, w);
        if (lcsID == -1) {
            return null;
        } else {
            String[] synset = this.synonyms.get(lcsID).synonyms;
            String result = String.join(" ", synset);
            return result;
        }
    }

    protected int getSynID(String noun) {
        Synonym syns;
        for (Map.Entry pair : this.synonyms.entrySet()) {
            syns = (Synonym) pair.getValue();

            for (String s : syns.synonyms) {
                if (s.equals(noun)) {
                    return (Integer) pair.getKey();
                }
            }
        }

        return -1;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        // create wordnet digraph


        WordNet wordnet = new WordNet(args[0], args[1]);

    }
}
