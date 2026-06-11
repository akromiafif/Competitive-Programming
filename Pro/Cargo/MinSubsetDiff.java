import java.util.Arrays;

/**
 * Minimum Subset Sum Difference (memoized DP).
 *
 * Split the array into two groups so the difference between their
 * sums is as small as possible. Print that minimum difference.
 *
 * Core idea: if one group sums to s, the other sums to (total - s).
 * So we just need to know WHICH sums s are achievable by some subset,
 * then pick the s that makes |total - 2s| smallest. Finding achievable
 * subset sums is the classic subset-sum DP.
 *
 * This is the brute-force include/exclude recursion (see MinSubsetDiffBrute)
 * plus a cache on the (ind, target) state, which collapses the 2^n tree
 * into an O(n * totalSum) table.
 */
public class MinSubsetDiff {

    /**
     * Returns true if some subset of arr[0..ind] sums exactly to `target`.
     *
     * dp[ind][target] is a memo cache:
     *   -1 = not computed yet
     *    0 = false (not achievable)
     *    1 = true  (achievable)
     */
    static boolean subsetSum(int ind, int target, int[] arr, int[][] dp) {
        // Base case 1: a target of 0 is always reachable (pick nothing).
        if (target == 0) {
            dp[ind][target] = 1;
            return true;
        }

        // Base case 2: only the first element is left to decide on.
        if (ind == 0) {
            boolean ok = (arr[0] == target);
            dp[ind][target] = ok ? 1 : 0;
            return ok;
        }

        // Already solved this exact (ind, target) state -> reuse it.
        if (dp[ind][target] != -1) {
            return dp[ind][target] == 1;
        }

        // Choice 1: skip arr[ind].
        boolean notTaken = subsetSum(ind - 1, target, arr, dp);

        // Choice 2: take arr[ind] (only if it doesn't overshoot the target).
        boolean taken = false;
        if (arr[ind] <= target) {
            taken = subsetSum(ind - 1, target - arr[ind], arr, dp);
        }

        boolean result = notTaken || taken;
        dp[ind][target] = result ? 1 : 0;
        return result;
    }

    static int minSubsetSumDifference(int[] arr) {
        int n = arr.length;

        int totalSum = 0;
        for (int x : arr) totalSum += x;

        // dp has n rows (elements) and totalSum + 1 columns (every target 0..totalSum).
        int[][] dp = new int[n][totalSum + 1];
        for (int[] row : dp) Arrays.fill(row, -1);

        // Compute, for every possible sum s, whether some subset reaches it.
        for (int s = 0; s <= totalSum; s++) {
            subsetSum(n - 1, s, arr, dp);
        }

        // Among all achievable subset sums s, minimise |s - (totalSum - s)|.
        int best = Integer.MAX_VALUE;
        for (int s = 0; s <= totalSum; s++) {
            if (dp[n - 1][s] == 1) {
                int diff = Math.abs(s - (totalSum - s));
                best = Math.min(best, diff);
            }
        }
        return best;
    }
}
