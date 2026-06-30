/**
 * Minimum Subset Sum Difference – Subset-Sum DP (1-D)
 *
 * Problem:
 *   Split the array into two groups so the gap between their sums is as small
 *   as possible; return that minimum gap.
 *
 * Insight to memorize:
 *   If one group sums to s, the other sums to (total - s), so the gap is
 *   |total - 2s|. We only need to know WHICH sums s are achievable by some
 *   subset — the classic subset-sum DP:
 *       reachable[s] = "some subset sums exactly to s"
 *       reachable[0] = true; for each value x, mark reachable[s] |= reachable[s-x]
 *   (sweep s high -> low so each x is used at most once). Then pick the
 *   achievable s closest to total/2.
 *
 * Complexity: O(n * total) time, O(total) space.
 */
public class MinSubsetDiff {

    static int minSubsetSumDifference(int[] arr) {
        int total = 0;
        for (int x : arr) total += x;

        boolean[] reachable = new boolean[total + 1];
        reachable[0] = true;                       // the empty subset sums to 0
        for (int x : arr)
            for (int s = total; s >= x; s--)       // high -> low: use x only once
                if (reachable[s - x]) reachable[s] = true;

        int best = Integer.MAX_VALUE;
        for (int s = 0; s <= total / 2; s++)       // s <= total-s, so gap = total - 2s
            if (reachable[s]) best = Math.min(best, total - 2 * s);
        return best;
    }
}
