public final class PrintUtil {
    public static final String SEPARATOR = "--------------------------------------------------------------------------";

    public static void printIntMatrix(int[][] matrix) {
        for (int r = 0; r < matrix.length; ++r) {
            for (int c = 0; c < matrix[0].length; ++c) {
                System.out.print(matrix[r][c]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static void printDoubleMatrix(double[][] matrix) {
        for (int r = 0; r < matrix.length; ++r) {
            for (int c = 0; c < matrix[0].length; ++c) {
                System.out.print(String.format("%.2f", matrix[r][c]));
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static <T> void printMatrix(T[][] matrix) {
        for (int r = 0; r < matrix.length; ++r) {
            for (int c = 0; c < matrix[0].length; ++c) {
                System.out.print(matrix[r][c]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
