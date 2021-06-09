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
    private BoggleBoard boardPts0, boardPts1;
    private BoggleSolver boggleSolver;
    private HashSet<String> dictCommon;

    @BeforeEach
    void setUp() {
        this.boardPts0 = new BoggleBoard("board-points0.txt");
        this.boardPts1 = new BoggleBoard("board-points1.txt");

        this.dictCommon = new HashSet<>();

        String[] dictCommonStrs = this.makeDictionary("dictionary-common.txt", this.dictCommon);
        this.boggleSolver = new BoggleSolver(dictCommonStrs);
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
        // try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(pathToFile))) {
        /*
        try (Path path = Paths.get(pathToFile))) {
            List<String> lines = Files.readAllLines(path);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return new String[0];
        }

         */
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllValidWords() {
        Iterable<String> vw0 = this.boggleSolver.getAllValidWords(this.boardPts0);
        for (String vw : vw0) {
            assertTrue(this.dictCommon.contains(vw));
        }
    }

    @Test
    void getAllCombinations() {
        Set<String> c0 = this.boggleSolver.getAllCombinations(this.boardPts0);
        assertNotEquals(0, c0.size());
    }

    @Test
    void scoreOf() {
    }
}