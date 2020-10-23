# FasterInvTrig

Faster implementation of [Inverse trigonometric functions](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions) 
than [Math.*](https://docs.oracle.com/javase/7/docs/api/java/lang/Math.html#acos(double)) or [FastMath.*](https://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/util/FastMath.html#acos(double)) with the caveat of accuracy.

Benchmark for great circle distance for 2m random points with [N-Vector](https://en.wikipedia.org/wiki/N-vector#Example:_Great_circle_distance)
shows gains from 5.7-15 times other methods.

Implementation calculates and stores the maclaurin series co-efficients for asin, 
with identity functions for faster convergence (see code comments). 
All functions use this series.

### BenchMark Results

```
Benchmark                            (num_points)  Mode  Cnt    Score   Error  Units
FastInvTrigBenchmark.acosBM               2000000  avgt    2   44.351          ms/op
FastInvTrigBenchmark.fastMathAcosBM       2000000  avgt    2  251.697          ms/op
FastInvTrigBenchmark.haversineBM          2000000  avgt    2  513.882          ms/op
FastInvTrigBenchmark.mathAcosBM           2000000  avgt    2  668.539          ms/op

Benchmark                            (num_points)  Mode  Cnt    Score   Error  Units
FastInvTrigBenchmark.acosBM               2000000  avgt    2   44.676          ms/op
FastInvTrigBenchmark.fastMathAcosBM       2000000  avgt    2  252.712          ms/op
FastInvTrigBenchmark.haversineBM          2000000  avgt    2  496.516          ms/op
FastInvTrigBenchmark.mathAcosBM           2000000  avgt    2  686.048          ms/op
```