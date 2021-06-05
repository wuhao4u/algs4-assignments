/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class BoggleSolver {
    private TreeSet<String> dict;
    private HashMap<String, Integer> scores;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        this.dict = new TreeSet<>();
        Collections.addAll(this.dict, dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        return Collections.emptySet();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {

        return 0;
    }

    public static void main(String[] args) {

    }
}
