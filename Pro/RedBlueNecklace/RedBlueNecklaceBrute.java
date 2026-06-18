/**
 * Red-Blue Necklace – Brute Force O(N^2)
 *
 * Try every possible contiguous subarray, check if it has equal R and B.
 * Track the longest one. Answer = N - longestLen.
 */
public class RedBlueNecklaceBrute {

    static int minRemovals(String s) {
        int n = s.length();
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int rCount = 0, bCount = 0;
            for (int j = i; j < n; j++) {
                if (s.charAt(j) == 'R') rCount++;
                else bCount++;

                if (rCount == bCount) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return n - maxLen;
    }
}
