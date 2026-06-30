import java.util.Arrays;

/**
 * Warehouse Days – Brute Force (the reference 2^n backtracking)
 *
 * Idea (exactly the given C++):
 *   Sort items by growth b ascending. Recurse over items deciding export-or-not.
 *   `days` = how many exported so far; an exported item is assigned the next day
 *   (days+1), so in ascending-b order the biggest-growth items get the latest
 *   days (the optimal assignment for any chosen subset). At the leaf, the total
 *   stock is asum + bsum*days - (removed), and we keep the minimum feasible days.
 *
 * No pruning, no DP — it tries every subset of exports — a trustworthy oracle.
 *
 * Complexity: O(2^n). Only usable for small n.
 */
public class WarehouseDaysBrute {

    public static long minDays(long[] a, long[] b, long k) {
        int n = a.length;

        long asum = 0, bsum = 0;
        for (int i = 0; i < n; i++) {
            asum += a[i];
            bsum += b[i];
        }
        if (asum <= k) return 0;

        // Reorder into ascending growth so the in-recursion day assignment is optimal.
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (p, q) -> Long.compare(b[p], b[q]));
        long[] A = new long[n], B = new long[n];
        for (int t = 0; t < n; t++) {
            A[t] = a[order[t]];
            B[t] = b[order[t]];
        }

        long[] best = {Long.MAX_VALUE};
        dfs(0, 0, 0, 0, A, B, asum, bsum, k, best);
        return best[0] == Long.MAX_VALUE ? -1 : best[0];
    }

    private static void dfs(int ind, long days, long removedA, long removedB,
                            long[] A, long[] B, long asum, long bsum, long k, long[] best) {
        if (ind == A.length) {
            long total = asum + bsum * days - removedA - removedB;
            if (total <= k) best[0] = Math.min(best[0], days);
            return;
        }
        // Export item `ind` on day (days+1).
        dfs(ind + 1, days + 1, removedA + A[ind], removedB + B[ind] * (days + 1),
                A, B, asum, bsum, k, best);
        // Skip it.
        dfs(ind + 1, days, removedA, removedB, A, B, asum, bsum, k, best);
    }
}
