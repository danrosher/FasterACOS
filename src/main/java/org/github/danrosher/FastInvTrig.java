package org.github.danrosher;

public class FastInvTrig {

    final static double pip2 = Math.PI / 2.0;
    static final double SQRT2 = 1.0 / Math.sqrt(2.0);

    final static double[] TABLE;
    final static int MAX_TERMS = 10;

    static {
        TABLE = new double[MAX_TERMS];
        double factor = 1.0;
        double divisor = 1.0;
        for (int n = 0; n < MAX_TERMS; n++) {
            TABLE[n] = factor / divisor;
            divisor += 2;
            factor *= (2 * n + 1.0) / ((n + 1) * 2);
        }
    }

    private static double asin2(double x) {
        double acc = x;
        double tempExp = x;
        double x2 = x * x;
        for (int n = 1; n < MAX_TERMS; n++) {
            tempExp *= x2;
            acc += TABLE[n] * tempExp;
        }
        return acc;
    }

    public static double asin(double x) {
        if (x > SQRT2)
            return pip2 - asin2(Math.sqrt(1 - (x * x)));
        else if (Math.abs(x) <= SQRT2)
            return asin2(x);
        return asin2(Math.sqrt(1 - (x * x))) - pip2;
    }

    public static double acos(double x) {
        return pip2 - asin(x);
    }

    //Following for completion for Inverse trigonometric functions
    public static double atan(double x) {
        return asin(x/Math.sqrt(1 + x*x));
    }

    public static double acot(double x) {
        return pip2 - atan(x);
    }

    public static double asec(double x) {
        return acos(1/x);
    }

    public static double acsc(double x) {
        return pip2 - asec(x);
    }


}