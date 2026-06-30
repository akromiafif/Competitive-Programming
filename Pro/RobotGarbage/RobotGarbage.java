import java.util.Arrays;

/**
 * Robot Garbage – O(n^2) DP (memoized recurrence)
 *
 * Problem:
 *   Each index of an array holds some garbage. Deploying a robot at an index
 *   costs `deployCost`; a robot cleans its current cell then moves only to i+1
 *   (one cell per time unit). At every time unit the running cost grows by the
 *   total garbage still uncleaned. Any number of robots may be deployed. Find
 *   the minimum total cost.
 *
 * One recurrence to memorize (scan left to right):
 *   A piece of garbage cleaned by a robot deployed at `last` waited (i - last)
 *   time units, so it costs (i - last) * garbage[i]. At each index you choose:
 *       deploy a NEW robot here     -> pay deployCost, reset reference to i
 *       keep the CURRENT robot      -> pay (i - last) * garbage[i]
 *   so, with `last` = index of the last deployed robot:
 *       solve(i, last) = min( deployCost           + solve(i+1, i),
 *                             (i-last)*garbage[i]   + solve(i+1, last) )
 *       solve(n, last) = 0
 *   The first robot is forced at the first non-zero index `start`, so the
 *   answer is  deployCost + solve(start+1, start).
 *
 * Complexity: O(n^2) states (index x lastDeploy). Plenty for the real limits
 *   (small n). For huge n you'd need a Convex-Hull-Trick speedup, but that is
 *   not required here.
 */
public class RobotGarbage {

    public static long minCost(long[] garbage, long deployCost) {
        int n = garbage.length;

        int start = 0;
        while (start < n && garbage[start] == 0) start++;
        if (start == n) return 0; // nothing to clean

        long[][] memo = new long[n + 1][n + 1]; // memo[index][lastDeploy], -1 = unset
        for (long[] row : memo) Arrays.fill(row, -1);

        return deployCost + solve(start + 1, start, garbage, deployCost, memo);
    }

    private static long solve(int index, int lastDeploy, long[] garbage,
                              long deployCost, long[][] memo) {
        int n = garbage.length;
        if (index == n) return 0;
        if (memo[index][lastDeploy] != -1) return memo[index][lastDeploy];

        long deployNew = deployCost + solve(index + 1, index, garbage, deployCost, memo);
        long keepRolling = (long) (index - lastDeploy) * garbage[index]
                         + solve(index + 1, lastDeploy, garbage, deployCost, memo);

        return memo[index][lastDeploy] = Math.min(deployNew, keepRolling);
    }
}
