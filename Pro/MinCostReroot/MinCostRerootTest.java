public class MinCostRerootTest {

    /** Runs one case through both solutions and compares the maximum. */
    private static void runTestCase(String name, long[] values, int[][] edges,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + values.length + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = MinCostRerootBrute.maxWeightedDistance(values, edges);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        long opt = MinCostReroot.maxWeightedDistance(values, edges);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : max=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  Rerooting   : max=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic random tree: vertex i (>=1) attaches to a random earlier vertex. */
    private static int[][] randomTree(int n, long seed) {
        int[][] edges = new int[n - 1][2];
        long state = seed;
        for (int i = 1; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            int parent = (int) Math.floorMod(state >>> 16, (long) i); // 0..i-1
            edges[i - 1][0] = parent;
            edges[i - 1][1] = i;
        }
        return edges;
    }

    private static long[] randomValues(int n, int maxVal, long seed) {
        long[] v = new long[n];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            v[i] = Math.floorMod(state >>> 16, maxVal + 1L);
        }
        return v;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs Rerooting) ===");

        // Single vertex: distance to itself is 0 -> answer 0.
        runTestCase("Single vertex", new long[]{5}, new int[0][0], true, 0);

        // Path 0-1-2, all values 1: endpoints give 0+1+2 = 3.
        runTestCase("Path of 3 (values 1)",
                new long[]{1, 1, 1}, new int[][]{{0, 1}, {1, 2}}, true, 3);

        // Path 0-1-2-3, all values 1: endpoints give 0+1+2+3 = 6.
        runTestCase("Path of 4 (values 1)",
                new long[]{1, 1, 1, 1}, new int[][]{{0, 1}, {1, 2}, {2, 3}}, true, 6);

        // Star: center 0, leaves 1,2,3, all values 1. A leaf is best: 1+2+2 = 5.
        runTestCase("Star of 4 (values 1)",
                new long[]{1, 1, 1, 1}, new int[][]{{0, 1}, {0, 2}, {0, 3}}, true, 5);

        // Path 0-1-2 with weight only on vertex 0: farthest vertex (2) wins, 2*5 = 10.
        runTestCase("Weighted path (5,0,0)",
                new long[]{5, 0, 0}, new int[][]{{0, 1}, {1, 2}}, true, 10);

        System.out.println("=== Complex Test Cases (Brute vs Rerooting correctness) ===");
        for (int i = 1; i <= 8; i++) {
            int n = 20 + (int) Math.floorMod(13L * i, 80); // 20..99
            int[][] edges = randomTree(n, 31L * i + 5);
            long[] values = randomValues(n, 100, 71L * i + 3);
            runTestCase("Complex " + i + " (random tree)", values, edges, true, Long.MIN_VALUE);
        }

        // One larger brute-checked case.
        int[][] midEdges = randomTree(2000, 4242L);
        long[] midVals = randomValues(2000, 1000, 555L);
        runTestCase("Medium (n=2000, random)", midVals, midEdges, true, Long.MIN_VALUE);

        System.out.println("=== Extreme Test Case (Rerooting only, large n) ===");
        // Random tree of 200,000 vertices (shallow, so recursion is safe).
        int n = 200_000;
        int[][] edges = randomTree(n, 24680L);
        long[] values = randomValues(n, 1000, 13579L);
        System.out.println("Running Extreme (n = 200,000)");
        long s = System.currentTimeMillis();
        long ans = MinCostReroot.maxWeightedDistance(values, edges);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  Rerooting: max=" + ans + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
