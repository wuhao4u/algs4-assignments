package io.github.wuhao4u;

/**
 * Created by wuhao on 2017-06-10.
 */

public class TestPoint {
    Point origin = new Point(0, 0);
    Point p01 = new Point(0, 1), p10 = new Point(1, 0), p11 = new Point(1, 1);

    public void testCompareTo() {

        assert origin.compareTo(origin) == 0;
        assert origin.compareTo(p01) < 0;
        assert origin.compareTo(p10) < 0;
        assert origin.compareTo(p11) < 0;

        assert p01.compareTo(p01) == 0;
        assert p01.compareTo(p10) > 0;
        assert p01.compareTo(p11) < 0;

        assert p10.compareTo(p11) < 0;
        assert p10.compareTo(p01) < 0;
        assert p10.compareTo(p11) < 0;

        assert p11.compareTo(origin) > 0;
        assert p11.compareTo(p01) > 0;
        assert p11.compareTo(p10) > 0;

        System.out.println("Horayyy!!!!");
    }

    public void testSlopeTo() {
        assert p11.slopeTo(p01) == +0.0;
        assert p11.slopeTo(p10) == Double.POSITIVE_INFINITY;
        assert p11.slopeTo(p11) == Double.NEGATIVE_INFINITY;

        System.out.printf("(1,1) to (0,0): %f\n", p11.slopeTo(origin));

        Point p21 = new Point(2, 3);
        System.out.printf("(2,3) to (1,1): %f\n", p21.slopeTo(p11));
    }

    public static void main(String[] args) {
        TestPoint tp = new TestPoint();
        tp.testCompareTo();
        tp.testSlopeTo();
    }
}
