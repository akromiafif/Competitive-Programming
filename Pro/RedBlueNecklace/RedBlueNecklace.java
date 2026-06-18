import java.util.HashMap;

/**
 * Red-Blue Necklace – Optimal Solution (Prefix Sum + HashMap)
 *
 * Problem:
 *   Given a necklace of R (red) and B (blue) stones, remove the minimum
 *   number of stones from either the left or right end so that the
 *   remaining necklace has an equal count of R and B.
 *
 * Key Insight:
 *   "Removing from ends" means keeping a contiguous subarray in the middle.
 *   So we need the LONGEST subarray with equal R and B counts.
 *   Answer = N - longestBalancedSubarrayLength.
 *
 * Technique:
 *   Treat R as +1 and B as -1. Build a running prefix sum.
 *   If prefix[i] == prefix[j], the subarray (i..j] has equal R and B.
 *   Store the FIRST occurrence of each prefix value → longest subarray.
 *
 * Complexity: O(N) time, O(N) space.
 */
public class RedBlueNecklace {

    static int minRemovals(String s) {
        int n = s.length();
        int maxLen = 0;
        int prefix = 0;

        // Map: prefix value -> first index where this prefix was seen
        HashMap<Integer, Integer> first = new HashMap<>();
        first.put(0, -1); // prefix 0 is seen "before" the string starts

        for (int i = 0; i < n; i++) {
            prefix += (s.charAt(i) == 'R') ? 1 : -1;

            if (first.containsKey(prefix)) {
                int len = i - first.get(prefix);
                maxLen = Math.max(maxLen, len);
            } else {
                first.put(prefix, i);
            }
        }

        return n - maxLen;
    }
}
