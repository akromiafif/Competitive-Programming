public class WarehouseDaysTest {

    /** Runs one case through both solutions and compares the min days. */
    private static void runTestCase(String name, long[] a, long[] b, long k,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + a.length + ", K=" + k + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = WarehouseDaysBrute.minDays(a, b, k);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        long opt = WarehouseDays.minDays(a, b, k);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : days=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  N^2 DP      : days=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic arrays: a in [1, maxA], b in [1, maxB] (growth kept smaller). */
    private static long[][] randomCase(int n, int maxA, int maxB, long seed) {
        long[] a = new long[n], b = new long[n];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            a[i] = Math.floorMod(state >>> 16, maxA) + 1;
            state = state * 6364136223846793005L + 1442695040888963407L;
            b[i] = Math.floorMod(state >>> 16, maxB) + 1;
        }
        return new long[][]{a, b};
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs N^2 DP) ===");

        // Already under K -> 0 days.
        runTestCase("Already ok", new long[]{10}, new long[]{1}, 10, true, 0);

        // One item, must export once on day 1: 10+5 - (10+5) = 0 <= 0.
        runTestCase("One export", new long[]{10}, new long[]{5}, 0, true, 1);

        // Big single item, K=50: day 1 clears it to 0 <= 50.
        runTestCase("Single big", new long[]{100}, new long[]{1}, 50, true, 1);

        // Needs two days: D=1 leaves 11 > 5, D=2 leaves 1 <= 5.
        runTestCase("Needs two days", new long[]{10, 10}, new long[]{1, 1}, 5, true, 2);

        // Impossible: growth outpaces exporting, can't reach 0.
        runTestCase("Impossible (-1)", new long[]{1, 1}, new long[]{10, 10}, 0, true, -1);

        System.out.println("=== Complex Test Cases (Brute vs N^2 DP correctness) ===");
        for (int i = 1; i <= 10; i++) {
            int n = 8 + (int) Math.floorMod(7L * i, 8); // 8..15
            long[][] ab = randomCase(n, 50, 4, 31L * i + 7); // small growth -> mixed answers
            long[] a = ab[0], b = ab[1];
            long asum = 0;
            for (long x : a) asum += x;
            long k = asum / 2; // moderate target
            runTestCase("Complex " + i, a, b, k, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (N^2 DP only, large n) ===");
        int n = 2000;
        long[][] ab = randomCase(n, 1000, 3, 24680L);
        long[] a = ab[0], b = ab[1];
        long asum = 0;
        for (long x : a) asum += x;
        long k = asum / 2;
        System.out.println("Running Extreme (n = 2000)");
        long s = System.currentTimeMillis();
        long ans = WarehouseDays.minDays(a, b, k);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  N^2 DP: days=" + ans + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
