public class StonesRemovalTest {

    /** Runs one case through both solutions and compares the total cost. */
    private static void runTestCase(String name, long[] costOne, long[] costTwo,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + costOne.length + ")");

        long bruteCost = 0, t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            bruteCost = StonesRemovalBrute.minRemovalCost(costOne, costTwo);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        long dpCost = StonesRemoval.minRemovalCost(costOne, costTwo);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : cost=" + bruteCost + "  (" + t1 + " ms)");
        }
        System.out.println("  DP          : cost=" + dpCost + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (bruteCost == dpCost);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (dpCost == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic reproducible random costs in 1..maxVal. */
    private static long[] randomCosts(int n, int maxVal, long seed) {
        long[] r = new long[n];
        long state = seed;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            r[i] = Math.floorMod(state >>> 16, maxVal) + 1;
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs DP) ===");

        // Single stone: it has 0 neighbours when removed -> cost 0.
        runTestCase("Simple 1 (single stone)",
                new long[]{7}, new long[]{0}, true, 0);

        // Two stones: remove one (1 neighbour), then the other (0 neighbours).
        // Cheapest is to pay the smaller of the two one-neighbour costs.
        runTestCase("Simple 2 (two stones)",
                new long[]{3, 5}, new long[]{0, 0}, true, 3);

        // Three stones, big costTwo. Strip the ends first (1 neighbour each),
        // leaving the middle with 0 neighbours. 1 + 1 + 0 = 2.
        runTestCase("Simple 3 (avoid the middle)",
                new long[]{1, 1, 1}, new long[]{0, 5, 0}, true, 2);

        // costTwo cheaper than 2*costOne would suggest, but ends still help.
        runTestCase("Simple 4 (mixed costs)",
                new long[]{4, 2, 3, 6}, new long[]{0, 9, 8, 0}, true, Long.MIN_VALUE);

        System.out.println("=== Complex Test Cases (Brute vs DP correctness) ===");
        // n small enough for n! brute force; random costs.
        for (int i = 1; i <= 4; i++) {
            int n = 8;
            long[] one = randomCosts(n, 100, 11L * i);
            long[] two = randomCosts(n, 100, 97L * i + 1);
            runTestCase("Complex " + i + " (n=8, random)", one, two, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (DP only, large n) ===");
        int n = 1_000_000;
        long[] one = randomCosts(n, 1_000_000_000, 12345L);
        long[] two = randomCosts(n, 1_000_000_000, 67890L);
        System.out.println("Running Extreme (n = 1,000,000)");
        long s = System.currentTimeMillis();
        long cost = StonesRemoval.minRemovalCost(one, two);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  DP: cost=" + cost + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
