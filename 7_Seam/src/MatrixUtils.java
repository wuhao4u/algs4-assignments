import java.awt.Color;

public class MatrixUtils {
    public static Color[][] transposeMatrix(Color[][] m0) {
        if (m0 == null || m0.length == 0) {
            return m0;
        }

        final int height = m0.length;
        final int width = m0[0].length;

        Color[][] m1 = new Color[m0[0].length][m0.length];

        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                m1[c][r] = m0[r][c];
            }
        }

        return m1;
    }

    public static double[][] transposeMatrix(double[][] m0) {
        if (m0 == null || m0.length == 0) {
            return m0;
        }

        final int height = m0.length;
        final int width = m0[0].length;

        double[][] m1 = new double[m0[0].length][m0.length];

        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                m1[c][r] = m0[r][c];
            }
        }

        return m1;
    }

    public static int[][] transposeMatrix(int[][] m0) {
        if (m0 == null || m0.length == 0) {
            return m0;
        }

        final int height = m0.length;
        final int width = m0[0].length;

        int[][] m1 = new int[m0[0].length][m0.length];

        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                m1[c][r] = m0[r][c];
            }
        }

        return m1;
    }
}
