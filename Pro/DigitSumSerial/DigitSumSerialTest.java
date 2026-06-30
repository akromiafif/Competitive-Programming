public class DigitSumSerialTest {

    /** Runs one case through both solutions and compares the count. */
    private static void runTestCase(String name, long a, int s,
                                    boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (A=" + a + ", S=" + s + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long start = System.currentTimeMillis();
            brute = DigitSumSerialBrute.count(a, s);
            t1 = System.currentTimeMillis() - start;
        }

        long start = System.currentTimeMillis();
        long dp = DigitSumSerial.count(Long.toString(a), 0, s, true, null);
        long t2 = System.currentTimeMillis() - start;

        if (runBrute) {
            System.out.println("  Brute Force : count=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  Digit DP    : count=" + dp + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == dp);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (dp == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    public static void main(String[] args) {
        System.out.println("=== Problem Examples (Brute vs Digit DP) ===");
        runTestCase("Example 1", 101, 3, true, 4);   // 3, 12, 21, 30
        runTestCase("Example 2", 172, 3, true, 7);   // 3,12,21,30,102,111,120
        runTestCase("Example 3", 50, 4, true, 5);    // 4,13,22,31,40
        runTestCase("Example 4", 999, 500, true, 0); // max digit sum 27 < 500

        System.out.println("=== Complex Test Cases (Brute vs Digit DP correctness) ===");
        // Deterministic pseudo-random small cases.
        long state = 2024L;
        for (int i = 1; i <= 8; i++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            long a = Math.floorMod(state >>> 16, 200_000) + 1; // 1..200000
            state = state * 6364136223846793005L + 1442695040888963407L;
            int s = (int) (Math.floorMod(state >>> 16, 40) + 1); // 1..40
            runTestCase("Complex " + i, a, s, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (Digit DP only, A ~ 10^100) ===");
        // A = 100-digit number (10^99 written out), S in the achievable range.
        StringBuilder sb = new StringBuilder("9");
        for (int i = 0; i < 99; i++) sb.append('9'); // A = 100 nines (max 100-digit value)
        String bigA = sb.toString();
        int s = 450;
        System.out.println("Running Extreme (A = 100 nines, S = " + s + ")");
        long start = System.currentTimeMillis();
        long dp = DigitSumSerial.count(bigA, 0, s, true, null);
        long dur = System.currentTimeMillis() - start;
        System.out.println("  Digit DP: count=" + dp + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
