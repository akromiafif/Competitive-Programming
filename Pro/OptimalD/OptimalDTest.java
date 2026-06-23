import java.util.ArrayList;
import java.util.List;

public class OptimalDTest {

    /**
     * Runs a single case through both solutions.
     * Because several D values can tie for the same diff, we DON'T compare the
     * chosen D directly — we compare the achieved value(A) - value(B), which is
     * what the problem actually optimizes.
     */
    private static void runTestCase(String name, List<Integer> a, List<Integer> b,
                                    boolean runBrute, long expectedDiff) {
        System.out.println("Running " + name);

        long bruteD = -1, bruteDiff = 0, d1 = 0;
        if (runBrute) {
            long t1 = System.currentTimeMillis();
            bruteD = OptimalDBrute.bestD(a, b);
            bruteDiff = OptimalDBrute.bestDiff(a, b);
            d1 = System.currentTimeMillis() - t1;
        }

        long t2 = System.currentTimeMillis();
        int optD = OptimalD.bestD(a, b);
        long optDiff = OptimalD.bestDiff(a, b);
        long d2 = System.currentTimeMillis() - t2;

        if (runBrute) {
            System.out.println("  Brute Force : D=" + bruteD + ", diff=" + bruteDiff + "  (" + d1 + " ms)");
        }
        System.out.println("  Optimal     : D=" + optD + ", diff=" + optDiff + "  (" + d2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (bruteDiff == optDiff);
        if (expectedDiff != Long.MIN_VALUE) {
            correct = correct && (optDiff == expectedDiff);
            System.out.println("  Expected diff: " + expectedDiff);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    private static List<Integer> list(int... xs) {
        List<Integer> r = new ArrayList<>();
        for (int x : xs) r.add(x);
        return r;
    }

    private static List<Integer> randomList(int n, int maxVal, long seed) {
        // Simple deterministic LCG so runs are reproducible.
        List<Integer> r = new ArrayList<>(n);
        long s = seed;
        for (int i = 0; i < n; i++) {
            s = (s * 6364136223846793005L + 1442695040888963407L);
            int v = (int) (Math.floorMod(s >>> 16, maxVal) + 1); // 1..maxVal
            r.add(v);
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Brute vs Optimal) ===");

        // The example from the prompt: a = [1,2,3,4,5], b = [6,7,8,9,10].
        // diff(D) = cntGT_A(D) - cntGT_B(D). The best we can do is D=0, where all
        // elements are > D so both sides score 2 each -> diff = 0. Any larger D
        // only drops A's count first, making diff negative. So best diff = 0.
        runTestCase("Simple 1 (prompt example)", list(1, 2, 3, 4, 5), list(6, 7, 8, 9, 10), true, 0);

        // A all large, B all small: D=0 keeps A at score 2, B at score 2.
        runTestCase("Simple 2 (A large, B small)", list(100, 200, 300), list(1, 2, 3), true, Long.MIN_VALUE);

        // Identical arrays -> diff is 0 for the best D.
        runTestCase("Simple 3 (identical)", list(5, 5, 5), list(5, 5, 5), true, 0);

        // Single elements.
        runTestCase("Simple 4 (singletons)", list(10), list(1), true, Long.MIN_VALUE);

        // Overlapping ranges.
        runTestCase("Simple 5 (overlap)", list(1, 5, 9, 3), list(2, 8, 4), true, Long.MIN_VALUE);

        System.out.println("=== Complex Test Cases (Brute vs Optimal correctness) ===");
        // Small maxVal so brute force (O(maxVal*N)) stays fast.
        for (int i = 1; i <= 3; i++) {
            List<Integer> a = randomList(2000, 500, 11L * i);
            List<Integer> b = randomList(2000, 500, 97L * i + 1);
            runTestCase("Complex " + i + " (N=M=2000, maxVal=500)", a, b, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (Optimal only, full constraints) ===");
        // N = M = 1e5, values up to 1e8 — the upper end of the stated constraints.
        List<Integer> a = randomList(100_000, 100_000_000, 12345L);
        List<Integer> b = randomList(100_000, 100_000_000, 67890L);
        System.out.println("Running Extreme (N = M = 100,000, maxVal = 1e8)");
        long t = System.currentTimeMillis();
        int d = OptimalD.bestD(a, b);
        long diff = OptimalD.bestDiff(a, b);
        long dur = System.currentTimeMillis() - t;
        System.out.println("  Optimal: D=" + d + ", diff=" + diff + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
