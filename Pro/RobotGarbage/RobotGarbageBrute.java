/**
 * Robot Garbage – Brute Force (try every subset of deploy positions)
 *
 * Idea:
 *   The first robot is forced at the first non-zero index `start`. For every
 *   later index we independently decide "deploy a robot here or not", i.e. we
 *   enumerate all 2^(positions after start) subsets. For each choice we just add
 *   up the real cost:
 *       - deployCost for every robot deployed, plus
 *       - (index - lastDeployedBefore it) * garbage[index] for every index that
 *         is NOT a deploy point (the wait charged to its serving robot).
 *   Take the cheapest over all subsets.
 *
 * No DP, no cleverness — it literally prices every possible deployment plan —
 * which is exactly why it is a trustworthy oracle for the DP on small inputs.
 *
 * Complexity: O(2^n * n). Only usable for small n (~<= 18).
 */
public class RobotGarbageBrute {

    public static long minCost(long[] garbage, long deployCost) {
        int n = garbage.length;

        int start = 0;
        while (start < n && garbage[start] == 0) start++;
        if (start == n) return 0; // nothing to clean

        int optional = n - 1 - start;          // indices start+1 .. n-1 each toggle
        long best = Long.MAX_VALUE;

        for (int mask = 0; mask < (1 << optional); mask++) {
            long cost = 0;
            int lastDeploy = start;            // first robot is at start
            int robots = 1;

            for (int i = start + 1; i < n; i++) {
                int bit = i - (start + 1);
                if ((mask & (1 << bit)) != 0) { // deploy a robot at i
                    lastDeploy = i;
                    robots++;
                } else {                        // current robot rolls over i
                    cost += (long) (i - lastDeploy) * garbage[i];
                }
            }
            cost += (long) robots * deployCost;
            best = Math.min(best, cost);
        }
        return best;
    }
}
