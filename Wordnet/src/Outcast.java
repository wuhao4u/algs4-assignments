
public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet){
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = -1;
        int curentDist = -1;
        String outcast = null;


        for (int i = 0; i < nouns.length; ++i) {
            curentDist = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if (j == i) continue;

                curentDist += wordnet.distance(nouns[i], nouns[j]);
            }


            if (curentDist > maxDist) {
                maxDist = curentDist;
                outcast = nouns[i];
            }
        }

        return outcast;
    }


    // see test client below
    public static void main(String[] args) {
        /*
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        */
    }
}
