package org.github.danrosher.benchmark;


import org.apache.commons.math3.util.FastMath;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.github.danrosher.FasterMath;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class FasterMathBenchmark {

    static final Random r = new Random();

    /**
     * @param lat1 The y coordinate of the first point, in radians
     * @param lon1 The x coordinate of the first point, in radians
     * @param lat2 The y coordinate of the second point, in radians
     * @param lon2 The x coordinate of the second point, in radians
     * @return The distance between the two points, as determined by the Haversine
     * formula, in radians.
     */
    public static double distHaversineRAD(double lat1, double lon1, double lat2, double lon2) {
        // TODO investigate slightly different formula using asin() and min()
        // http://www.movable-type.co.uk/scripts/gis-faq-5.1.html

        // Check for same position
        if (lat1 == lat2 && lon1 == lon2)
            return 0.0;
        double hsinX = Math.sin((lon1 - lon2) * 0.5);
        double hsinY = Math.sin((lat1 - lat2) * 0.5);
        double h = hsinY * hsinY + (Math.cos(lat1) * Math.cos(lat2) * hsinX * hsinX);
        if (h > 1)// numeric robustness issue. If we didn't check, the answer would be NaN!
            h = 1;
        return 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
    }

    public static double[] latLongToNVector(double lat, double lon) {
        double x = Math.cos(lat) * Math.cos(lon);
        double y = Math.cos(lat) * Math.sin(lon);
        double z = Math.sin(lat);
        return new double[]{x, y, z};
    }

    private final static double pi2 = 2 * Math.PI;

    public static double deg2rad(double deg) {
        return deg * (pi2 / 360);
    }

    public static double[] generateRandomPoint() {
        double u = r.nextDouble();
        double v = r.nextDouble();

        double latitude = deg2rad(Math.toDegrees(Math.acos(u * 2 - 1)) - 90);
        double longitude = deg2rad(360 * v - 180);
        return new double[]{latitude, longitude};
    }

    @Param({"2000000"})
    private int num_points;

    double[][] NVectorPoints;
    double[] q;
    double[] q_nvec;
    double[][] points;

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
            .include(FasterMathBenchmark.class.getSimpleName())
            .forks(1)
            .build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        points = new double[num_points][2];
        NVectorPoints = new double[num_points][3];
        for (int i = 0; i < num_points; i++) {
            double[] p = generateRandomPoint();
            points[i] = p;
            NVectorPoints[i] = latLongToNVector(p[0], p[1]);
        }
        q = generateRandomPoint();
        q_nvec = latLongToNVector(q[0], q[1]);
    }

    @Benchmark
    public void haversineBH(Blackhole bh){
        for (int i = 0; i < num_points; i++) {
            double[] p = points[i];
            bh.consume(distHaversineRAD(p[0], p[1], q[0], q[1]));
        }
    }

    @Benchmark
    public void acosBM(Blackhole bh) {
        for (int i = 0; i < num_points; i++) {
            double[] p = NVectorPoints[i];
            bh.consume(FasterMath.acos(p[0] * q_nvec[0] + p[1] * q_nvec[1] + p[2] * q_nvec[2]));
        }
    }

    @Benchmark
    public void mathAcosBH(Blackhole bh){
        for (int i = 0; i < num_points; i++) {
            double[] p = NVectorPoints[i];
            bh.consume(Math.acos(p[0] * q_nvec[0] + p[1] * q_nvec[1] + p[2] * q_nvec[2]));
        }
    }

    @Benchmark
    public void fastMathAcosBH(Blackhole bh){
        for (int i = 0; i < num_points; i++) {
            double[] p = NVectorPoints[i];
            bh.consume(FastMath.acos(p[0] * q_nvec[0] + p[1] * q_nvec[1] + p[2] * q_nvec[2]));
        }
    }

}

