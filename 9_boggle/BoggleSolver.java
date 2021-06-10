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
    // private TreeSet<String> validWords;
    // private TreeMap<String, Integer> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dict = new TreeSet<>();
        Collections.addAll(this.dict, dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> validWords = new TreeSet<>();
        Set<String> combinations = this.getAllCombinations(board);

        // TODO: filter combination with words in the dictionary
        for (String combination : combinations) {
            int x = 0;
            if (this.dict.contains(combination)) {
                validWords.add(combination);
            }
        }

        return validWords;
    }

    public Set<String> getAllCombinations(BoggleBoard board) {
        Set<String> combinations = new HashSet<>();
        boolean[][] visited;

        // DFS to get all combinations
        for (int r = 0; r < board.rows(); ++r) {
            for (int c = 0; c < board.cols(); ++c) {
                // passing in a 2D array to avoid double visiting
                visited = new boolean[board.rows()][board.cols()];
                this.combination(board, r, c, new StringBuilder(), visited, combinations);
            }
        }

        return combinations;
    }

    private void combination(BoggleBoard board, int curRow, int curCol, StringBuilder soFar,
                             boolean[][] visited, Set<String> results) {
        if (curRow < 0 || curRow >= board.rows()
                || curCol < 0 || curCol >= board.cols()
                || visited[curRow][curCol]
        ) {
            return;
        }

        // add this cell to result
        char curChar = board.getLetter(curRow, curCol);
        // append char in-place
        soFar.append(curChar);
        visited[curRow][curCol] = true;

        if (soFar.length() > 1) {
            results.add(soFar.toString());
        }

        // left, curCol - 1
        this.combination(board, curRow, curCol - 1, soFar, visited, results);

        // right, curCol + 1
        this.combination(board, curRow, curCol + 1, soFar, visited, results);

        // up, curRow - 1
        this.combination(board, curRow - 1, curCol, soFar, visited, results);

        // down, curRow + 1
        this.combination(board, curRow + 1, curCol, soFar, visited, results);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int x = 0;
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
