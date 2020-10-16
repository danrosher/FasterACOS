package org.github.danrosher;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FasterMathTest {

    final static int num_points = 100;
    final static double pi2 = 2 * Math.PI;
    final static double EPSILON = 0.0001;
    static final Random r = new Random();

    static double[][] points = new double[num_points][2];

    public static double[] generateRandomPoint() {
        double u = r.nextDouble();
        double v = r.nextDouble();

        double latitude = deg2rad(Math.toDegrees(Math.acos(u * 2 - 1)) - 90);
        double longitude = deg2rad(360 * v - 180);
        return new double[]{latitude, longitude};
    }

    public static double deg2rad(double deg) {
        return deg * (pi2 / 360);
    }

    @BeforeAll
    static void initAll() {
        for (int i = 0; i < num_points; i++) {
            points[i] =  generateRandomPoint();
        }
    }

    @Test
    void asin() {
        for(double i=0;i<=1;i = i + 0.00001){
            assertTrue(FasterMath.asin(i) - Math.asin(i) <= EPSILON);
        }
    }

    @Test
    void acos() {
        for(double i=0;i<=1;i = i + 0.00001){
            assertTrue(FasterMath.acos(i) - Math.acos(i) <= EPSILON);
        }
    }

    @Test
    void distanceAccuracy(){

    }
}