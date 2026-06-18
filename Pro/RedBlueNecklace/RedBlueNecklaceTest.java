public class RedBlueNecklaceTest {

    private static void runTestCase(String name, String input, int expected) {
        System.out.println("Running " + name + " | Input: \"" + input + "\"");

        // Test Brute Force
        long t1 = System.currentTimeMillis();
        int ansBrute = RedBlueNecklaceBrute.minRemovals(input);
        long d1 = System.currentTimeMillis() - t1;

        // Test Optimal
        long t2 = System.currentTimeMillis();
        int ansOptimal = RedBlueNecklace.minRemovals(input);
        long d2 = System.currentTimeMillis() - t2;

        System.out.println("  Brute Force : " + ansBrute + "  (" + d1 + " ms)");
        System.out.println("  Optimal     : " + ansOptimal + "  (" + d2 + " ms)");

        // Verify correctness
        boolean correct = (ansBrute == ansOptimal);
        if (expected >= 0) {
            correct = correct && (ansOptimal == expected);
        }
        System.out.println("  Expected    : " + (expected >= 0 ? expected : "N/A"));
        System.out.println("  Status      : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    private static String generateRandom(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(Math.random() < 0.5 ? 'R' : 'B');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases ===");

        // From the problem example
        runTestCase("Simple 1 (problem example)", "BBRRBRBRBRBBR", 1);

        // All same color → must remove all
        runTestCase("Simple 2 (all R)", "RRRR", 4);

        // Already balanced
        runTestCase("Simple 3 (already balanced)", "RRBB", 0);

        // Single pair
        runTestCase("Simple 4 (single pair)", "RB", 0);

        // One stone
        runTestCase("Simple 5 (single stone)", "R", 1);

        // Empty-ish
        runTestCase("Simple 6 (two same)", "RR", 2);

        // Alternating
        runTestCase("Simple 7 (alternating)", "RBRBRB", 0);

        // Needs trimming from one end
        runTestCase("Simple 8 (trim right)", "RBRRR", 3);

        System.out.println("=== Complex Test Cases (Brute vs Optimal correctness) ===");

        // Random medium tests – compare brute vs optimal
        for (int i = 1; i <= 3; i++) {
            String s = generateRandom(10000);
            runTestCase("Complex " + i + " (N=10000)", s, -1);
        }

        System.out.println("=== Extreme Test Case (Optimal only, brute would be slow) ===");
        // N = 1,000,000 — brute would take ~minutes, optimal < 50ms
        String extreme = generateRandom(1_000_000);
        System.out.println("Running Extreme (N = 1,000,000)");
        long t = System.currentTimeMillis();
        int ans = RedBlueNecklace.minRemovals(extreme);
        long d = System.currentTimeMillis() - t;
        System.out.println("  Optimal answer: " + ans + "  (" + d + " ms)");
        System.out.println("  Status: " + (d < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
