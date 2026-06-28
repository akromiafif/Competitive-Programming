/**
 * Stones Removal – Optimal Solution (Dynamic Programming)
 *
 * Problem:
 *   Stones sit in a row (positions 0..n-1). You remove them ONE AT A TIME in
 *   any order. The cost of removing a stone depends on how many of its ORIGINAL
 *   neighbours are still present at that moment:
 *       - 0 neighbours present -> cost 0
 *       - 1 neighbour  present -> costOne[i]
 *       - 2 neighbours present -> costTwo[i]
 *   (End stones only ever have 1 neighbour, so they never pay costTwo.)
 *   Find the removal order with the minimum total cost.
 *
 * Key Insight (approach by vgtcross, CF blog 117311):
 *   Whether stone i pays for its LEFT neighbour depends only on the order of i
 *   vs i-1; whether it pays for its RIGHT neighbour depends only on i vs i+1.
 *   So we can scan left to right and, at each step, only remember one bit:
 *   "is my right neighbour removed before or after me?".
 *
 *   dp[i][0] = best cost for stones 0..i  when stone i+1 is removed BEFORE i
 *              (i.e. i's right side is already empty when i is removed)
 *   dp[i][1] = best cost for stones 0..i  when stone i+1 is removed AFTER  i
 *              (i.e. i's right neighbour is still present when i is removed)
 *
 *   Coming from the left, dp[i-1][0] means i's LEFT neighbour is still present
 *   when i is removed, and dp[i-1][1] means it is already gone. Combining the
 *   left status (from the previous state) with the right status (the new state)
 *   gives the neighbour count, hence the cost:
 *
 *     dp[i][0] = min( dp[i-1][0] + costOne[i],   // left present, right gone  -> 1
 *                     dp[i-1][1] + 0          )  // left gone,    right gone  -> 0
 *     dp[i][1] = min( dp[i-1][0] + costTwo[i],   // left present, right present-> 2
 *                     dp[i-1][1] + costOne[i] )  // left gone,    right present-> 1
 *
 *   Stone 0 has no left neighbour; stone n-1 has no right neighbour, so the
 *   answer is dp[n-1][0] (force "right side empty").
 *
 * Complexity: O(n) time, O(1) extra space (rolling the two states).
 */
public class StonesRemoval {

    /**
     * @param costOne cost to remove each stone when it has 1 neighbour
     * @param costTwo cost to remove each stone when it has 2 neighbours
     * @return minimum total cost to remove all stones
     */
    public static long minRemovalCost(long[] costOne, long[] costTwo) {
        int n = costOne.length;
        if (n == 0) return 0;

        // Base case: stone 0 (no left neighbour).
        long rightGone = 0;            // dp[0][0]: right neighbour removed first -> 0 neighbours
        long rightPresent = costOne[0]; // dp[0][1]: right neighbour still there  -> 1 neighbour

        for (int i = 1; i < n; i++) {
            long newRightGone = Math.min(rightGone + costOne[i], // left present, right gone -> 1
                                         rightPresent + 0);       // left gone,    right gone -> 0
            long newRightPresent = Math.min(rightGone + costTwo[i],      // both present -> 2
                                            rightPresent + costOne[i]);  // only right   -> 1
            rightGone = newRightGone;
            rightPresent = newRightPresent;
        }

        // Stone n-1 has no real right neighbour, so its right side is "empty".
        return rightGone;
    }
}
