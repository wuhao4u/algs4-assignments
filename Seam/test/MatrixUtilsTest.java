import org.junit.jupiter.api.Test;

class MatrixUtilsTest {
    @Test
    void testTransposeMatrix() {
        int[][] m1 = new int[3][3];

        int counter = 0;
        for(int r = 0; r < m1.length; ++r) {
            for (int c = 0; c < m1[0].length; ++c) {
                m1[r][c] = counter++;
            }
        }

        m1 = MatrixUtils.transposeMatrix(m1);
    }
}