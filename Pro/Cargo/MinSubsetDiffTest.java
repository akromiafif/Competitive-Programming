public class MinSubsetDiffTest {

    private static void runTestCase(String name, int[] arr) {
        System.out.println("Running " + name + " (N = " + arr.length + ")");
        
        // Test Brute Force
        System.out.println("--- Testing Brute Force ---");
        long startTime = System.currentTimeMillis();
        int ansBrute = MinSubsetDiffBrute.minDiff(arr);
        long endTime = System.currentTimeMillis();
        long durationBrute = endTime - startTime;
        
        System.out.println("Answer: " + ansBrute);
        System.out.println("Execution Time: " + durationBrute + " ms");
        if (durationBrute > 1000) {
            System.out.println("WARNING: Exceeded 1s time limit!");
        } else {
            System.out.println("Status: PASSED (Under 1s)");
        }

        // Test DP
        System.out.println("--- Testing DP ---");
        long startTimeDP = System.currentTimeMillis();
        int ansDP = MinSubsetDiff.minSubsetSumDifference(arr);
        long endTimeDP = System.currentTimeMillis();
        long durationDP = endTimeDP - startTimeDP;
        
        System.out.println("Answer: " + ansDP);
        System.out.println("Execution Time: " + durationDP + " ms");
        if (durationDP > 1000) {
            System.out.println("WARNING: Exceeded 1s time limit!");
        } else {
            System.out.println("Status: PASSED (Under 1s)");
        }
        
        System.out.println("=".repeat(40));
    }

    private static int[] generateRandomArray(int n, int maxVal) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int)(Math.random() * maxVal) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases ===");
        // Simple Test Case 1 (from example)
        int[] simple1 = {3, 3, 7, 3, 1};
        runTestCase("Simple Test Case 1", simple1);

        // Simple Test Case 2
        int[] simple2 = {1, 2, 3, 4};
        runTestCase("Simple Test Case 2", simple2);

        System.out.println("=== Complex Test Cases ===");
        // For O(2^N) brute force, N around 25-27 will take noticeable time but stay under 1s.
        // Complex Test Case 1
        int[] complex1 = generateRandomArray(25, 1000);
        runTestCase("Complex Test Case 1", complex1);

        // Complex Test Case 2
        int[] complex2 = generateRandomArray(26, 1000);
        runTestCase("Complex Test Case 2", complex2);

        // Complex Test Case 3
        int[] complex3 = generateRandomArray(27, 1000);
        runTestCase("Complex Test Case 3", complex3);

        System.out.println("=== Extreme Test Case (Demonstrating Brute Force Failure) ===");
        // N = 31 will do over 2 billion recursive calls for Brute Force.
        // It will take several seconds (exceeding the 1s limit), 
        // whereas the DP solution will still finish in ~4 milliseconds!
        int[] extreme = generateRandomArray(31, 1000);
        runTestCase("Extreme Test Case", extreme);
    }
}
