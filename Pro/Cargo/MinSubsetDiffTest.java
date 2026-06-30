public class MinSubsetDiffTest {

    /** Runs one case through both solutions and compares the gap. */
    private static void runTestCase(String name, int[] arr, boolean runBrute, int expected) {
        System.out.println("Running " + name + " (N=" + arr.length + ")");

        int brute = 0;
        long t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = MinSubsetDiffBrute.minDiff(arr);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        int dp = MinSubsetDiff.minSubsetSumDifference(arr);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : diff=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  Subset DP   : diff=" + dp + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == dp);
        if (expected != Integer.MIN_VALUE) {
            correct = correct && (dp == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic reproducible values in [1, maxVal]. */
    private static int[] randomArray(int n, int maxVal, long seed) {
        int[] arr = new int[n];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            arr[i] = (int) Math.floorMod(state >>> 16, maxVal) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs Subset DP) ===");
        // {3,3,7,3,1} total 17 (odd): best split 9 vs 8 -> gap 1.
        runTestCase("Simple 1 (3 3 7 3 1)", new int[]{3, 3, 7, 3, 1}, true, 1);
        // {1,2,3,4} total 10: 5 vs 5 -> gap 0.
        runTestCase("Simple 2 (1 2 3 4)", new int[]{1, 2, 3, 4}, true, 0);
        // Single element: one group is empty -> gap is that element.
        runTestCase("Simple 3 (single)", new int[]{10}, true, 10);
        // Equal pair: perfectly balanced.
        runTestCase("Simple 4 (equal pair)", new int[]{8, 8}, true, 0);

        System.out.println("=== Complex Test Cases (Brute vs Subset DP correctness) ===");
        // N kept <= 19 so the O(2^n) brute oracle stays fast.
        for (int i = 1; i <= 6; i++) {
            int n = 16 + (i % 4);                  // 16..19
            int[] arr = randomArray(n, 1000, 1234L * i + 7);
            runTestCase("Complex " + i, arr, true, Integer.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (Subset DP only, large sum) ===");
        // N = 200, values up to 1000 -> total ~ 2*10^5. DP is O(N*total) ~ 4*10^7,
        // which is instant; the O(2^N) brute would be utterly hopeless here.
        int[] big = randomArray(200, 1000, 999L);
        System.out.println("Running Extreme (N=200, sum up to ~200,000)");
        long s = System.currentTimeMillis();
        int dp = MinSubsetDiff.minSubsetSumDifference(big);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  Subset DP: diff=" + dp + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
