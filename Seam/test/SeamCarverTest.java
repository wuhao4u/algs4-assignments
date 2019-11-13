import edu.princeton.cs.algs4.Picture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeamCarverTest {
    Picture p11, p37, p73, p1010;
    SeamCarver sc11, sc37, sc73, sc1010;

    @BeforeEach
    void setUp() {
        p11 = new Picture("resources/1x1.png");
        p37 = new Picture("resources/3x7.png");
        p73 = new Picture("resources/7x3.png");
        p1010 = new Picture("resources/10x10.png");

        sc11 = new SeamCarver(p11);
        sc37 = new SeamCarver(p37);
        sc73 = new SeamCarver(p73);
        sc1010 = new SeamCarver(p1010);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void picture() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SeamCarver(null);
        });
    }

    @Test
    void width() {
        assertEquals(1, p11.width());
        assertEquals(10, p1010.width());
    }

    @Test
    void height() {
    }

    @Test
    void energy() {
    }

    @Test
    void findVerticalSeam() {
    }

    @Test
    void findHorizontalSeam() {
    }

    @Test
    void removeVerticalSeam() {
        int[] invalidSeam1 = {7, 8, 8, 10, 8, 8, 9, 9, 9, 8};
        assertThrows(IllegalArgumentException.class, () -> {
            sc1010.removeVerticalSeam(invalidSeam1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sc11.removeVerticalSeam(new int[]{0});
        });

        int[] is2 = { 2, 3, 2, 1, 0, 0, 1 };
        assertThrows(IllegalArgumentException.class, () -> {
            sc37.removeVerticalSeam(is2);
        });

    }

    @Test
    void removeHorizontalSeam() {
        int[] invalidSeam1 = {-1, 0, 0, 0, 1, 2, 1, 0, 0, 0};
        assertThrows(IllegalArgumentException.class, () -> {
            sc1010.removeHorizontalSeam(invalidSeam1);
        });

        int[] invalidSeam2 = {-1, -2, 0};
        assertThrows(IllegalArgumentException.class, () -> {
            sc37.removeHorizontalSeam(invalidSeam2);
        });

        int[] invalidSeam3 = {0, 1, 2, 1, 0, 0, -1};
        assertThrows(IllegalArgumentException.class, () -> {
            sc73.removeHorizontalSeam(invalidSeam3);
        });
    }
}