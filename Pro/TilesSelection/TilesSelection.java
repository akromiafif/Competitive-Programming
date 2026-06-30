/**
 * Tiles Selection – Binary Search on Answer + 2D Prefix Sum
 *
 * Problem:
 *   N tiles, each with two coordinates (x, y) = (height, width). Choose K of them
 *   minimizing the MAXIMUM pairwise difference, where the difference of two tiles
 *   is max(|x1-x2|, |y1-y2|) (Chebyshev distance). Coordinates are small (<= 400).
 *
 * Insight to memorize:
 *   The max pairwise Chebyshev distance of a chosen set equals
 *       max( xRange, yRange )   where xRange = maxX-minX, yRange = maxY-minY.
 *   So minimizing it = fitting K tiles inside the smallest axis-aligned SQUARE.
 *   Binary-search the side `d`; feasible if SOME d-by-d box holds >= K tiles.
 *   Count tiles in any box in O(1) with a 2D prefix sum over the coordinate grid.
 *
 * Complexity: O(maxC^2) to build the grid + O(maxC^2 * log maxC) for the search.
 *   Independent of N once the grid is built — so it needs SMALL coordinates
 *   (the 2D grid is (maxC+1)^2). For large coordinates use a sort + two-pointer
 *   variant instead.
 */
public class TilesSelection {

    public static int minMaxDifference(int[][] tiles, int k) {
        if (k <= 1) return 0; // 0 or 1 tile -> no pair -> difference 0

        int maxC = 0;
        for (int[] t : tiles) maxC = Math.max(maxC, Math.max(t[0], t[1]));
        int size = maxC + 1;

        // prefix[i][j] = number of tiles with x < i and y < j.
        long[][] prefix = new long[size + 1][size + 1];
        for (int[] t : tiles) prefix[t[0] + 1][t[1] + 1]++;
        for (int i = 1; i <= size; i++)
            for (int j = 1; j <= size; j++)
                prefix[i][j] += prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1];

        // Smallest box side `d` such that some [x..x+d] x [y..y+d] holds >= k tiles.
        int lo = 0, hi = maxC, ans = maxC;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (someBoxHoldsK(prefix, size, mid, k)) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }

    /** Is there a box spanning `side` in each coordinate that contains >= k tiles? */
    private static boolean someBoxHoldsK(long[][] prefix, int size, int side, int k) {
        for (int x = 0; x + side < size; x++) {
            for (int y = 0; y + side < size; y++) {
                // count tiles in [x .. x+side] x [y .. y+side]
                int x2 = x + side + 1, y2 = y + side + 1;
                long count = prefix[x2][y2] - prefix[x][y2] - prefix[x2][y] + prefix[x][y];
                if (count >= k) return true;
            }
        }
        return false;
    }
}
