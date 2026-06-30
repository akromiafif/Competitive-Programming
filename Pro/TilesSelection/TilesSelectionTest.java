public class TilesSelectionTest {

    /** Runs one case through both solutions and compares the answer. */
    private static void runTestCase(String name, int[][] tiles, int k,
                                    boolean runBrute, int expected) {
        System.out.println("Running " + name + "  (n=" + tiles.length + ", k=" + k + ")");

        int brute = 0;
        long t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = TilesSelectionBrute.minMaxDifference(tiles, k);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        int opt = TilesSelection.minMaxDifference(tiles, k);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : ans=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  BinSearch+PS: ans=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Integer.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic tiles with coordinates in [0, maxC]. */
    private static int[][] randomTiles(int n, int maxC, long seed) {
        int[][] tiles = new int[n][2];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            tiles[i][0] = (int) Math.floorMod(state >>> 16, maxC + 1L);
            state = state * 6364136223846793005L + 1442695040888963407L;
            tiles[i][1] = (int) Math.floorMod(state >>> 16, maxC + 1L);
        }
        return tiles;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs BinSearch+PrefixSum) ===");

        // k=1 -> no pair -> 0.
        runTestCase("Single pick", new int[][]{{3, 7}, {1, 1}}, 1, true, 0);

        // Closest two of a diagonal: difference 1.
        runTestCase("Closest pair",
                new int[][]{{1, 1}, {2, 2}, {3, 3}, {10, 10}}, 2, true, 1);

        // Three forming an L: ranges 3 and 3 -> 3.
        runTestCase("L-shape, k=3",
                new int[][]{{0, 0}, {0, 3}, {3, 0}}, 3, true, 3);

        // Cluster of 3 near origin vs a far tile; pick the cluster -> 1.
        runTestCase("Cluster of 3",
                new int[][]{{0, 0}, {0, 1}, {1, 0}, {50, 50}}, 3, true, 1);

        System.out.println("=== Complex Test Cases (Brute vs BinSearch+PrefixSum) ===");
        for (int i = 1; i <= 8; i++) {
            int n = 10 + (int) Math.floorMod(5L * i, 6); // 10..15
            int k = 2 + (int) Math.floorMod(3L * i, 4);  // 2..5
            int[][] tiles = randomTiles(n, 20, 31L * i + 7);
            runTestCase("Complex " + i, tiles, k, true, Integer.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (BinSearch+PrefixSum only) ===");
        // N = 100,000 tiles, coordinates in [0,400] (the small-coordinate regime).
        int n = 100_000, maxC = 400, k = 500;
        int[][] tiles = randomTiles(n, maxC, 24680L);
        System.out.println("Running Extreme (N=100,000, coords<=400, k=500)");
        long s = System.currentTimeMillis();
        int ans = TilesSelection.minMaxDifference(tiles, k);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  BinSearch+PS: ans=" + ans + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
