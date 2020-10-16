package org.github.danrosher;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FastInvTrigTest {

    final static int num_points = 100;
    final static double pi2 = 2 * Math.PI;
    final static double EPSILON = 0.0001;
    static final Random r = new Random();

    static double[][] points = new double[num_points][2];

    public static double[] latLongToNVector(double lat, double lon) {
        lat = deg2rad(lat);
        lon = deg2rad(lon);
        double x = Math.cos(lat) * Math.cos(lon);
        double y = Math.cos(lat) * Math.sin(lon);
        double z = Math.sin(lat);
        return new double[]{x, y, z};
    }

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
            assertTrue(FastInvTrig.asin(i) - Math.asin(i) <= EPSILON);
        }
    }

    @Test
    void acos() {
        for(double i=0;i<=1;i = i + 0.00001){
            assertTrue(FastInvTrig.acos(i) - Math.acos(i) <= EPSILON);
        }
    }

    @Test
    void distanceAccuracy(){
        final double R =   6371008.8;//meters google:standard mean earth radius

        //52.024534,-0.490683
        //52.027135,-0.490281
        //290.72 m (accurate from https://www.movable-type.co.uk/scripts/latlong-vincenty.html)
        double[] a = latLongToNVector(52.024534, -0.490683);
        double[] b = latLongToNVector(52.027135,-0.490281);
        double d = R * FastInvTrig.acos(a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
        assertTrue(d - 290.72 < 10); //less than 10m accuracy

        //52.024534,-0.490683
        //51.480331,-0.198885
        //63814.266 m (accurate from https://www.movable-type.co.uk/scripts/latlong-vincenty.html)
        a = latLongToNVector(52.024534,-0.490683);
        b = latLongToNVector(51.480331,-0.198885);
        d = R * FastInvTrig.acos(a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
        assertTrue(d - 63814.266 < 10);  //less than 10m accuracy

    }
}