/**
 * Tiles Selection – Brute Force (try every K-subset)
 *
 * Idea:
 *   Directly follow the definition: enumerate every subset of K tiles, compute
 *   its maximum pairwise difference (max over pairs of max(|dx|, |dy|)), and keep
 *   the smallest such maximum. No "box" insight, no binary search — it just
 *   measures every choice, which makes it a trustworthy oracle on small inputs.
 *
 * Complexity: O(C(n, k) * k^2). Only usable for small n.
 */
public class TilesSelectionBrute {

    public static int minMaxDifference(int[][] tiles, int k) {
        if (k <= 1) return 0;
        int[] best = {Integer.MAX_VALUE};
        choose(tiles, k, 0, new int[k], 0, best);
        return best[0];
    }

    private static void choose(int[][] tiles, int k, int start, int[] pick, int count, int[] best) {
        if (count == k) {
            int maxDiff = 0;
            for (int a = 0; a < k; a++) {
                for (int b = a + 1; b < k; b++) {
                    int dx = Math.abs(tiles[pick[a]][0] - tiles[pick[b]][0]);
                    int dy = Math.abs(tiles[pick[a]][1] - tiles[pick[b]][1]);
                    maxDiff = Math.max(maxDiff, Math.max(dx, dy));
                }
            }
            best[0] = Math.min(best[0], maxDiff);
            return;
        }
        for (int i = start; i < tiles.length; i++) {
            pick[count] = i;
            choose(tiles, k, i + 1, pick, count + 1, best);
        }
    }
}
