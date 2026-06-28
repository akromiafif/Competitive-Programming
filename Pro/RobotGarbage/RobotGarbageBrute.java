import java.util.Arrays;

/**
 * Robot Garbage – Brute Force (memoized recurrence)
 *
 * Idea (the same recurrence as the reference C++):
 *   Scan indices left to right. At each index we either:
 *     - deploy a NEW robot here  -> pay deployCost, and this index becomes the
 *       new reference point, or
 *     - let the CURRENT robot (last deployed at `lastDeploy`) keep rolling -> the
 *       garbage here has waited (index - lastDeploy) time units, costing
 *       (index - lastDeploy) * garbage[index].
 *   The first robot is forced at the first non-zero index. We memoize on
 *   (index, lastDeploy) and take the minimum.
 *
 * This makes no clever observation (no prefix sums, no hull) — it just tries
 * both choices at every step — which is exactly why it is a trustworthy oracle
 * for the Convex-Hull DP on small inputs.
 *
 * Complexity: O(n^2) states. Fine for n up to a couple thousand, hopeless beyond.
 */
public class RobotGarbageBrute {

    public static long minCost(long[] garbage, long deployCost) {
        int n = garbage.length;

        int firstNonZero = 0;
        while (firstNonZero < n && garbage[firstNonZero] == 0) firstNonZero++;
        if (firstNonZero == n) return 0; // nothing to clean

        // memo[index][lastDeploy], sentinel = "not computed".
        long[][] memo = new long[n + 1][n + 1];
        for (long[] row : memo) Arrays.fill(row, Long.MIN_VALUE);

        return deployCost + solve(firstNonZero + 1, firstNonZero, garbage, deployCost, memo);
    }

    private static long solve(int index, int lastDeploy, long[] garbage,
                              long deployCost, long[][] memo) {
        int n = garbage.length;
        if (index == n) return 0;
        if (memo[index][lastDeploy] != Long.MIN_VALUE) return memo[index][lastDeploy];

        long deployNew = deployCost + solve(index + 1, index, garbage, deployCost, memo);
        long keepRolling = (long) (index - lastDeploy) * garbage[index]
                         + solve(index + 1, lastDeploy, garbage, deployCost, memo);

        return memo[index][lastDeploy] = Math.min(deployNew, keepRolling);
    }
}
