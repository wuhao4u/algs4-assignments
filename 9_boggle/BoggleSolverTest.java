import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
class BoggleSolverTest {
    private BoggleBoard boardPts0, boardPts1, boardPts2, boardPts3, boardPts4, boardPts5,
            boardPts100, boardPts200, boardPts777, boardPts1000, boardPts1500, boardPts2000,
            boardPts4540, boardPts13464, boardPts26539;
    private BoggleSolver boggleSolverCommon, boggleSolverTwl06, boggleSolverZ;
    private HashSet<String> dictCommon, dictTwl06, dictZ;

    @BeforeEach
    void setUp() {
        this.boardPts0 = new BoggleBoard("board-points0.txt");
        this.boardPts1 = new BoggleBoard("board-points1.txt");
        this.boardPts5 = new BoggleBoard("board-points5.txt");
        this.boardPts100 = new BoggleBoard("board-points100.txt");
        this.boardPts777 = new BoggleBoard("board-points777.txt");
        this.boardPts2000 = new BoggleBoard("board-points2000.txt");
        this.boardPts4540 = new BoggleBoard("board-points4540.txt");
        this.boardPts26539 = new BoggleBoard("board-points26539.txt");

        this.dictCommon = new HashSet<>();
        this.dictTwl06 = new HashSet<>();
        this.dictZ = new HashSet<>();

        String[] dictCommonStrs = this.makeDictionary("dictionary-common.txt", this.dictCommon);
        String[] dictTwl06 = this.makeDictionary("dictionary-twl06.txt", this.dictZ);
        String[] dictZingarelli = this.makeDictionary("dictionary-zingarelli2005.txt", this.dictZ);
        int x = 0;
        this.boggleSolverCommon = new BoggleSolver(dictCommonStrs);
        this.boggleSolverTwl06 = new BoggleSolver(dictTwl06);
        this.boggleSolverZ = new BoggleSolver(dictZingarelli);
    }

    String[] makeDictionary(String pathToFile, Set<String> dict) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(pathToFile));
            String[] results = new String[lines.size()];

            for (int i = 0; i < lines.size(); ++i) {
                dict.add(lines.get(i));
                results[i] = lines.get(i);
            }

            return results;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return new String[0];
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllValidWords() {
        int counter = 0;
        int score = 0;
        Iterable<String> vw0 = this.boggleSolverCommon.getAllValidWords(this.boardPts0);
        for (String vw : vw0) {
            assertTrue(this.dictCommon.contains(vw));
            ++counter;
            score += this.boggleSolverCommon.scoreOf(vw);
        }
        System.out.println("valid words count: " + counter);
        System.out.println("points0: " + score);

        counter = 0;
        score = 0;
        Iterable<String> vwZ = this.boggleSolverZ.getAllValidWords(this.boardPts5);
        for (String vw : vwZ) {
            assertTrue(this.dictZ.contains(vw));
            ++counter;
            score += this.boggleSolverZ.scoreOf(vw);
        }
        System.out.println("valid words count: " + counter);
        System.out.println("points0: " + score);

        counter = 0;
        score = 0;
        Iterable<String> vwZ100 = this.boggleSolverZ.getAllValidWords(this.boardPts100);
        for (String vw : vwZ100) {
            assertTrue(this.dictZ.contains(vw));
            ++counter;
            score += this.boggleSolverZ.scoreOf(vw);
        }
        System.out.println("valid words count: " + counter);
        System.out.println("points0: " + score);
    }

    @Test
    void getAllCombinations() {
        Set<String> c0 = this.boggleSolverCommon.getAllCombinations(this.boardPts0);
        assertNotEquals(0, c0.size());
    }

    @Test
    void scoreOf() {
    }
}