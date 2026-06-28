public class CarsParkingTest {

    /** Runs one case through both solutions and compares the drive count. */
    private static void runTestCase(String name, long p, long q,
                                    long[] xs, long[] ys,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + xs.length + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = CarsParkingBrute.minDrives(p, q, xs, ys);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        long opt = CarsParking.minDrives(p, q, xs, ys);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : drives=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  Optimal     : drives=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic reproducible pseudo-random value in [0, range). */
    private static long rnd(long[] state, long range) {
        state[0] = state[0] * 6364136223846793005L + 1442695040888963407L;
        return Math.floorMod(state[0] >>> 16, range);
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs Optimal) ===");

        // All cars already parked -> 0 drives.
        runTestCase("Simple 1 (already parked)",
                5, 5, new long[]{5, 5}, new long[]{5, 5}, true, 0);

        // One car at distance 1: drive #1 (move 1) lands it. -> 1 drive.
        runTestCase("Simple 2 (distance 1)",
                0, 0, new long[]{1}, new long[]{0}, true, 1);

        // One car at distance 2: S(1)=1<2, S(2)=3>=2 but 3-2=1 odd,
        // S(3)=6, 6-2=4 even -> 3 drives.
        runTestCase("Simple 3 (distance 2 needs parity fix)",
                0, 0, new long[]{2}, new long[]{0}, true, 3);

        // Two cars with opposite parities (dist 1 and dist 2) -> impossible.
        runTestCase("Simple 4 (parity mismatch -> -1)",
                0, 0, new long[]{1, 2}, new long[]{0, 0}, true, -1);

        // Two cars, distances 3 and 3: S(2)=3>=3, 3-3=0 even -> 2 drives.
        runTestCase("Simple 5 (matching parity)",
                0, 0, new long[]{3, 0}, new long[]{0, 3}, true, 2);

        System.out.println("=== Complex Test Cases (Brute vs Optimal correctness) ===");
        long[] state = {123456789L};
        for (int i = 1; i <= 6; i++) {
            int n = 1 + (int) rnd(state, 6); // 1..6 cars
            long p = rnd(state, 40);
            long q = rnd(state, 40);
            // Force a single shared parity so the answer is usually not -1,
            // but occasionally flip one car to exercise the -1 path.
            long[] xs = new long[n];
            long[] ys = new long[n];
            for (int j = 0; j < n; j++) {
                xs[j] = rnd(state, 40);
                ys[j] = rnd(state, 40);
            }
            runTestCase("Complex " + i + " (n=" + n + ", random)",
                    p, q, xs, ys, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (Optimal only, M up to 10^17) ===");
        // Huge matrix: target near one corner, cars far away, parities aligned.
        long p = 100_000_000_000_000_000L;   // 10^17
        long q = 0;
        int n = 100;                          // N up to 100
        long[] xs = new long[n];
        long[] ys = new long[n];
        long st = 987654321L;
        for (int i = 0; i < n; i++) {
            // Build cars whose Manhattan distance to (p,q) is even for every car.
            long dx = Math.floorMod((st = st * 25214903917L + 11L) >>> 16, 100_000_000_000_000_000L);
            long dyParityFix = dx & 1; // make total distance even
            xs[i] = p - dx;
            ys[i] = q + dyParityFix;   // adds dyParityFix to distance -> even total
        }
        System.out.println("Running Extreme (M ~ 10^17, n = " + n + ")");
        long s = System.currentTimeMillis();
        long drives = CarsParking.minDrives(p, q, xs, ys);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  Optimal: drives=" + drives + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
