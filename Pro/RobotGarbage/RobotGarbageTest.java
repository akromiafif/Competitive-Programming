public class RobotGarbageTest {

    /** Runs one case through both solutions and compares the cost. */
    private static void runTestCase(String name, long[] garbage, long deployCost,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + garbage.length + ", m=" + deployCost + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long start = System.currentTimeMillis();
            brute = RobotGarbageBrute.minCost(garbage, deployCost);
            t1 = System.currentTimeMillis() - start;
        }

        long start = System.currentTimeMillis();
        long opt = RobotGarbage.minCost(garbage, deployCost);
        long t2 = System.currentTimeMillis() - start;

        if (runBrute) {
            System.out.println("  Brute Force : cost=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  CHT DP      : cost=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic reproducible garbage values in [0, maxVal]. */
    private static long[] randomGarbage(int n, int maxVal, long seed) {
        long[] g = new long[n];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            g[i] = Math.floorMod(state >>> 16, maxVal + 1L);
        }
        return g;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs CHT DP) ===");

        // The reference example: 3 1 4 1 5, m = 2 -> 8.
        runTestCase("Example (3 1 4 1 5, m=2)",
                new long[]{3, 1, 4, 1, 5}, 2, true, 8);

        // All zeros -> nothing to clean -> 0.
        runTestCase("All zeros",
                new long[]{0, 0, 0, 0}, 7, true, 0);

        // Single piece of garbage, leading zeros skipped: one robot, no waiting.
        runTestCase("Single piece (deploy once)",
                new long[]{0, 0, 5}, 10, true, 10);

        // Deployment is free -> deploy at every index, zero waiting cost -> 0.
        runTestCase("Free deployment",
                new long[]{3, 1, 4, 1, 5}, 0, true, 0);

        // Deployment hugely expensive -> a single robot is best:
        // 1000 + (0*3 + 1*1 + 2*4 + 3*1 + 4*5) = 1000 + 32 = 1032.
        runTestCase("Expensive deployment (one robot)",
                new long[]{3, 1, 4, 1, 5}, 1000, true, 1032);

        System.out.println("=== Complex Test Cases (Brute vs CHT DP correctness) ===");
        for (int i = 1; i <= 8; i++) {
            int n = 40 + (int) Math.floorMod(11L * i, 60); // 40..99
            long[] g = randomGarbage(n, 30, 1234L * i + 7);
            long m = Math.floorMod(97L * i, 50) + 1;        // 1..50
            runTestCase("Complex " + i + " (random)", g, m, true, Long.MIN_VALUE);
        }

        // A larger brute-checked case.
        runTestCase("Medium (n=1500, random)",
                randomGarbage(1500, 1000, 555L), 200, true, Long.MIN_VALUE);

        System.out.println("=== Extreme Test Case (CHT DP only, large n) ===");
        int n = 1_000_000;
        long[] g = randomGarbage(n, 5, 24680L); // small values keep all sums exact
        long m = 1000;
        System.out.println("Running Extreme (n = 1,000,000)");
        long start = System.currentTimeMillis();
        long cost = RobotGarbage.minCost(g, m);
        long dur = System.currentTimeMillis() - start;
        System.out.println("  CHT DP: cost=" + cost + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
