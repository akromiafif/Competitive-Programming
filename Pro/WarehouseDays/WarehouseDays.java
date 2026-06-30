import java.util.Arrays;

/**
 * Warehouse Days – O(N^2) DP
 *
 * Problem:
 *   Stock starts at a[i]. Each DAY: every item grows (a[i] += b[i]), THEN you may
 *   export one item (set it to 0). Find the minimum number of days so the total
 *   stock becomes <= K.
 *
 * Insight to memorize:
 *   After D days, with no exports, total = asum + bsum*D  (every item grew D times).
 *   Exporting item j on day d removes (a[j] + b[j]*d) from that total
 *   (its initial value plus the growth it accumulated up to day d).
 *   For a fixed D you export D distinct items on days 1..D; by the rearrangement
 *   inequality the biggest-growth items belong on the latest days. So:
 *     - sort items by growth b ASCENDING,
 *     - the c-th item you export is exported on day c, contributing a[i] + b[i]*c.
 *   dp[c] = max total you can remove using exactly c exports (a knapsack):
 *       dp[c] = max(dp[c], dp[c-1] + a[i] + b[i]*c)   processed in ascending b.
 *   The smallest D with  asum + bsum*D - dp[D] <= K  is the answer (else -1).
 *
 * Complexity: O(N^2) time, O(N) space.
 */
public class WarehouseDays {

    /**
     * @param a initial stock per item
     * @param b daily growth per item
     * @param k target: minimum days until total stock <= k
     * @return minimum number of days, or -1 if impossible
     */
    public static long minDays(long[] a, long[] b, long k) {
        int n = a.length;

        long asum = 0, bsum = 0;
        for (int i = 0; i < n; i++) {
            asum += a[i];
            bsum += b[i];
        }
        if (asum <= k) return 0; // already fine before any day passes

        // Sort item indices by growth b ascending.
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (p, q) -> Long.compare(b[p], b[q]));

        // dp[c] = max total removable using exactly c exports (days 1..c).
        long[] dp = new long[n + 1];
        Arrays.fill(dp, Long.MIN_VALUE);
        dp[0] = 0;

        for (int t = 0; t < n; t++) {
            int i = order[t];
            for (int c = t + 1; c >= 1; c--) {          // c = which day this export uses
                if (dp[c - 1] != Long.MIN_VALUE) {
                    dp[c] = Math.max(dp[c], dp[c - 1] + a[i] + b[i] * c);
                }
            }
        }

        for (int days = 1; days <= n; days++) {
            if (dp[days] == Long.MIN_VALUE) continue;
            if (asum + bsum * days - dp[days] <= k) return days;
        }
        return -1;
    }
}
