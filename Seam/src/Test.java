import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        int[] A = new int[10];
        int[] A1 = new int[9];

        for (int i = 0; i < 10; ++i) {
            A[i] = i;
        }

        int r = 5;
        System.out.println(Arrays.toString(A));
        System.out.println(Arrays.toString(A1));
        System.arraycopy(A, 0, A1, 0, r);
        System.out.println(Arrays.toString(A));
        System.out.println(Arrays.toString(A1));

        System.arraycopy(A, r + 1, A1, r, A.length - r - 1);
        System.out.println(Arrays.toString(A));
        System.out.println(Arrays.toString(A1));
    }
}
