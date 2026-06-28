/**
 * Stones Removal – Brute Force
 *
 * Idea:
 *   Literally try EVERY possible removal order (every permutation of the
 *   stones). For each chosen next stone we look at the CURRENT board, count how
 *   many of its original neighbours are still present, charge the matching cost
 *   (0 / costOne / costTwo), mark it removed, and recurse. Keep the minimum.
 *
 * This makes no clever observation at all — it just simulates reality — which is
 * exactly why it's trustworthy for verifying the DP on small inputs.
 *
 * Complexity: O(n! * n). Fine for n up to ~9, hopeless beyond that.
 */
public class StonesRemovalBrute {

    public static long minRemovalCost(long[] costOne, long[] costTwo) {
        int n = costOne.length;
        if (n == 0) return 0;
        boolean[] removed = new boolean[n];
        return search(costOne, costTwo, removed, n);
    }

    /** Try removing each still-present stone next; return the cheapest completion. */
    private static long search(long[] costOne, long[] costTwo, boolean[] removed, int remaining) {
        if (remaining == 0) return 0;

        int n = removed.length;
        long best = Long.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            if (removed[i]) continue;

            // Count original neighbours still on the board right now.
            int neighbours = 0;
            if (i > 0 && !removed[i - 1]) neighbours++;
            if (i < n - 1 && !removed[i + 1]) neighbours++;

            long cost = (neighbours == 0) ? 0
                      : (neighbours == 1) ? costOne[i]
                                          : costTwo[i];

            removed[i] = true;
            long rest = search(costOne, costTwo, removed, remaining - 1);
            removed[i] = false; // backtrack

            best = Math.min(best, cost + rest);
        }
        return best;
    }
}
