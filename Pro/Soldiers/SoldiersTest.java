public class SoldiersTest {

    /** Runs one case through both solutions and compares the remaining total. */
    private static void runTestCase(String name, int[] parent, int[] soldiers,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + parent.length + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long start = System.currentTimeMillis();
            brute = SoldiersBrute.minRemaining(parent, soldiers);
            t1 = System.currentTimeMillis() - start;
        }

        long start = System.currentTimeMillis();
        long opt = Soldiers.minRemaining(parent, soldiers);
        long t2 = System.currentTimeMillis() - start;

        if (runBrute) {
            System.out.println("  Brute Force : remaining=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  Iterative   : remaining=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic random rooted tree: node i>=1 attaches to a random earlier node. */
    private static int[][] randomTree(int n, int maxSoldiers, long seed) {
        int[] parent = new int[n];
        int[] soldiers = new int[n];
        long state = seed;
        parent[0] = -1;
        for (int i = 0; i < n; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            soldiers[i] = (int) Math.floorMod(state >>> 16, maxSoldiers + 1L);
            if (i >= 1) {
                state = state * 6364136223846793005L + 1442695040888963407L;
                parent[i] = (int) Math.floorMod(state >>> 16, (long) i); // 0..i-1
            }
        }
        return new int[][]{parent, soldiers};
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs Iterative) ===");

        // Single node: nothing to balance.
        runTestCase("Single node",
                new int[]{-1}, new int[]{10}, true, 10);

        // Root with two leaf children 3 and 5: reduce 5 -> 3, remaining 0 + 3*2 = 6.
        runTestCase("Two leaves (3,5)",
                new int[]{-1, 0, 0}, new int[]{0, 3, 5}, true, 6);

        // Three leaves 4,4,4 already balanced: remaining 0 + 4*3 = 12.
        runTestCase("Already balanced",
                new int[]{-1, 0, 0, 0}, new int[]{0, 4, 4, 4}, true, 12);

        // Two levels. Node1 has leaves {3,4} -> 6; node2 is a leaf {5}.
        // Root reduces children to min(6,5)=5 -> remaining 0 + 5*2 = 10.
        // (Reference model: the deeper {3,4}->6 subtree is treated as reducible to 5.)
        runTestCase("Two levels",
                new int[]{-1, 0, 0, 1, 1}, new int[]{0, 0, 5, 3, 4}, true, 10);

        System.out.println("=== Complex Test Cases (Brute vs Iterative correctness) ===");
        for (int i = 1; i <= 8; i++) {
            int n = 20 + (int) Math.floorMod(7L * i, 80); // 20..99
            int[][] tree = randomTree(n, 100, 909L * i + 13);
            runTestCase("Complex " + i + " (random tree)", tree[0], tree[1], true, Long.MIN_VALUE);
        }

        // Larger brute-checked random tree.
        int[][] mid = randomTree(2000, 100, 4242L);
        runTestCase("Medium (n=2000, random)", mid[0], mid[1], true, Long.MIN_VALUE);

        System.out.println("=== Extreme Test Cases (large N, shallow recursion) ===");
        // The real constraints are tiny (N <= 500, depth <= 100), so the simple
        // recursive DFS is plenty. These big cases just show it stays fast. They
        // are kept SHALLOW on purpose — a million-deep chain would overflow the
        // call stack, which is the one trade-off of the recursive version.

        // Wide star: one root with N-1 leaf children (depth 1, recursion-safe).
        int starN = 1_000_000;
        int[] starParent = new int[starN];
        int[] starSoldiers = new int[starN];
        starParent[0] = -1;
        long st = 13579L;
        for (int i = 0; i < starN; i++) {
            st = st * 6364136223846793005L + 1442695040888963407L;
            starSoldiers[i] = (int) Math.floorMod(st >>> 16, 101L);
            if (i >= 1) starParent[i] = 0;
        }
        long s1 = System.currentTimeMillis();
        long starRem = Soldiers.minRemaining(starParent, starSoldiers);
        long d1 = System.currentTimeMillis() - s1;
        System.out.println("Star (n=1,000,000): remaining=" + starRem + "  (" + d1 + " ms)  "
                + (d1 < 1000 ? "PASSED (Under 1s)" : "WARNING: >1s!"));

        // Large random tree: parent[i] in [0,i-1] gives expected depth ~ log(N),
        // so recursion stays shallow.
        int randN = 300_000;
        int[][] big = randomTree(randN, 100, 24680L);
        long s2 = System.currentTimeMillis();
        long bigRem = Soldiers.minRemaining(big[0], big[1]);
        long d2 = System.currentTimeMillis() - s2;
        System.out.println("Random (n=300,000): remaining=" + bigRem + "  (" + d2 + " ms)  "
                + (d2 < 1000 ? "PASSED (Under 1s)" : "WARNING: >1s!"));
    }
}
