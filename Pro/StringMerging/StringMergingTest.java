import java.util.Arrays;

public class StringMergingTest {

    /** Runs one case through both solutions and compares the max length. */
    private static void runTestCase(String name, String[] arr, boolean runBrute, long expected) {
        System.out.println("Running " + name + "  (n=" + arr.length + ")");

        long brute = 0, t1 = 0;
        if (runBrute) {
            long s = System.currentTimeMillis();
            brute = StringMergingBrute.maxFinalLength(arr);
            t1 = System.currentTimeMillis() - s;
        }

        long s = System.currentTimeMillis();
        long opt = StringMerging.maxFinalLength(arr);
        long t2 = System.currentTimeMillis() - s;

        if (runBrute) {
            System.out.println("  Brute Force : len=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  10x10 DP    : len=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Long.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected    : " + expected);
        }
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic strings of digits in [1, maxDigit], length 1..maxLen. */
    private static String[] gen(int n, int maxLen, int maxDigit, long seed) {
        String[] arr = new String[n];
        long st = seed;
        for (int i = 0; i < n; i++) {
            st = st * 6364136223846793005L + 1442695040888963407L;
            int len = (int) Math.floorMod(st >>> 16, maxLen) + 1;
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < len; k++) {
                st = st * 6364136223846793005L + 1442695040888963407L;
                int d = (int) Math.floorMod(st >>> 16, maxDigit) + 1; // 1..maxDigit
                sb.append((char) ('0' + d));
            }
            arr[i] = sb.toString();
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("=== Problem Examples (Brute vs DP) ===");
        // Test case 1 -> "123323321" length 9.
        runTestCase("Example 1",
                new String[]{"14", "123", "323", "321", "421", "535"}, true, 9);
        // Test case 2 -> only "22" qualifies -> 2.
        runTestCase("Example 2",
                new String[]{"14", "15", "89", "22"}, true, 2);

        System.out.println("=== Simple Test Cases (Brute vs DP) ===");
        // Single string with first==last.
        runTestCase("Single palindromic ends (121)", new String[]{"121"}, true, 3);
        // Single string first!=last -> no valid final string -> 0.
        runTestCase("Single, no match (12)", new String[]{"12"}, true, 0);
        // Two that connect into a loop: 12 then 21 -> "1221", first 1 == last 1 -> 4.
        runTestCase("Loop of two (12,21)", new String[]{"12", "21"}, true, 4);
        // "21" then "12": last of "21" (1) == first of "12" (1), merges to "2112",
        // whose first (2) == last (2) -> valid, length 4.
        runTestCase("Connect (21,12)", new String[]{"21", "12"}, true, 4);

        System.out.println("=== Complex Test Cases (Brute vs DP correctness) ===");
        // Small alphabet (digits 1..3) so chains actually form; n small for brute.
        for (int i = 1; i <= 8; i++) {
            int n = 8 + (int) Math.floorMod(7L * i, 8); // 8..15
            String[] arr = gen(n, 4, 3, 1234L * i + 7);
            runTestCase("Complex " + i + " " + Arrays.toString(arr), arr, true, Long.MIN_VALUE);
        }

        System.out.println("=== Extreme Test Case (DP only, full constraints) ===");
        // N = 1e5 strings, values up to 1e9 (digits 0..9, lengths up to 10).
        int n = 100_000;
        String[] big = new String[n];
        long st = 99999L;
        for (int i = 0; i < n; i++) {
            st = st * 6364136223846793005L + 1442695040888963407L;
            long val = Math.floorMod(st >>> 16, 1_000_000_000L) + 1; // 1..1e9
            big[i] = Long.toString(val);
        }
        System.out.println("Running Extreme (N = 100,000, values up to 1e9)");
        long s = System.currentTimeMillis();
        long ans = StringMerging.maxFinalLength(big);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  10x10 DP: len=" + ans + "  (" + dur + " ms)");
        System.out.println("  Status: " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
