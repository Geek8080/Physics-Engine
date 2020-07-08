public class ImpulseMath {
    public static final double PI = (float) StrictMath.PI;
    public static final double EPSILON = 0.0001d;
    public static final double EPSILON_SQ = EPSILON * EPSILON;
    public static final double BIAS_RELATIVE = 0.95d;
    public static final double BIAS_ABSOLUTE = 0.01d;
    public static final double DT = 0.016d;// 1.0d / 60.0d;(calculated for 1 frame per 16ms)
    public static final Vector GRAVITY = new Vector(new Point(0.0d, 50.0d));
    public static final double RESTING = Vector.scalarProduct(DT, GRAVITY).getMagnitude() + EPSILON;
    public static final double PENETRATION_ALLOWANCE = 0.05d;
    public static final double PENETRATION_CORRETION = 0.4d;

    public static boolean equal(double a, double b) {
        return StrictMath.abs(a - b) <= EPSILON;
    }

    public static double clamp(double min, double max, double a) {
        return (a < min ? min : (a > max ? max : a));
    }

    public static int round(double a) {
        return (int) (a + 0.5f);
    }

    public static double random(double min, double max) {
        return (double) ((max - min) * Math.random() + min);
    }

    public static int random(int min, int max) {
        return (int) ((max - min + 1) * Math.random() + min);
    }

    public static boolean gt(double a, double b) {
        return a >= b * BIAS_RELATIVE + a * BIAS_ABSOLUTE;
    }
}