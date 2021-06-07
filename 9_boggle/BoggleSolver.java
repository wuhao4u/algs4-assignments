/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {
    private TreeSet<String> dict;
    private TreeSet<String> validWords;
    // private TreeMap<String, Integer> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dict = new TreeSet<>();
        Collections.addAll(this.dict, dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> combinations = this.getAllCombinations(board);

        return Collections.emptySet();
    }

    public Set<String> getAllCombinations(BoggleBoard board) {
        Set<String> combinations = new HashSet<>();

        // DFS to get all combinations
        for (int r = 0; r < board.rows(); ++r) {
            for (int c = 0; c < board.cols(); ++c) {
                this.combination(board, r, c, new StringBuilder(), combinations);
            }
        }

        // TODO: filter combination with words in the dictionary

        return combinations;
    }

    private void combination(BoggleBoard board, int curRow, int curCol, StringBuilder soFar, Set<String> results) {
        if (curRow < 0 || curRow >= board.rows() || curCol < 0 || curCol >= board.cols()) {
            return;
        }

        // add this cell to result
        char curChar = board.getLetter(curRow, curCol);
        // append char in-place
        soFar.append(curChar);
        results.add(soFar.toString());

        // left, curCol - 1
        this.combination(board, curRow, curCol - 1, soFar, results);

        // right, curCol + 1
        this.combination(board, curRow, curCol + 1, soFar, results);

        // up, curRow - 1
        this.combination(board, curRow - 1, curCol, soFar, results);

        // down, curRow + 1
        this.combination(board, curRow + 1, curCol, soFar, results);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (this.dict.contains(word)) {
            if (word.length() < 3) {
                return 0;
            } else if (word.length() > 7) {
                return 11;
            } else {
                switch (word.length()) {
                    case 3:
                        return 1;
                    case 4:
                        return 1;
                    case 5:
                        return 2;
                    case 6:
                        return 3;
                    case 7:
                        return 5;
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.toString());
        sb.append('h');
        System.out.println(sb.toString());
    }
}
