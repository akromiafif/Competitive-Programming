/**
 * Robot Garbage – Optimal Solution (DP + Convex Hull Trick)
 *
 * Problem:
 *   Each index i of an array holds some garbage value. Deploying a robot at any
 *   index costs `deployCost`. A robot cleans the garbage at its current index
 *   and may then move ONLY to i+1 (one step per unit of time). At every moment
 *   in time the running cost increases by the total amount of garbage still
 *   uncleaned. Any number of robots may be deployed at any indices. Find the
 *   minimum total cost to clean everything.
 *
 * Cost model (the key reformulation):
 *   A robot deployed at index s cleans s at time 0, s+1 at time 1, ... so a
 *   piece of garbage at index j cleaned by that robot waits (j - s) time units,
 *   each of which charges its value. Splitting the work into contiguous segments
 *   — one robot per segment starting at the segment's left end — the total cost
 *   is:
 *       sum over segments [s..e] of [ deployCost + sum_{j=s..e} (j - s)*garbage[j] ].
 *   We must partition [firstNonZero .. n-1] into such segments to minimise this.
 *   (Leading zeros need no robot; trailing/internal zeros cost nothing.)
 *
 * DP:
 *   f[i] = min cost to clean indices i..n-1 GIVEN a fresh robot is deployed at i.
 *   With prefix sums  P[k] = sum_{j<k} garbage[j]   and  Q[k] = sum_{j<k} j*garbage[j],
 *   the cost of segment [i, k-1] is  (Q[k]-Q[i]) - i*(P[k]-P[i]), so
 *       f[i] = deployCost - Q[i] + i*P[i] + min_{k>i} [ (Q[k]+f[k]) + (-P[k])*i ].
 *   Each k contributes a LINE  y = (-P[k])*x + (Q[k]+f[k])  evaluated at x = i.
 *   Processing i from high to low we add one line per index; the slopes -P[k]
 *   are non-decreasing, so a Convex Hull Trick answers every "min over lines"
 *   query, giving O(n log n) overall.
 *
 * Answer: f[firstNonZero], or 0 if the array is all zeros.
 *
 * Complexity: O(n log n) time, O(n) space — handles n up to 1,000,000 under 1s.
 */
public class RobotGarbage {

    /**
     * @param garbage    garbage value at each index (non-negative)
     * @param deployCost cost to deploy one robot
     * @return minimum total cost to clean all garbage
     */
    public static long minCost(long[] garbage, long deployCost) {
        int n = garbage.length;

        int firstNonZero = 0;
        while (firstNonZero < n && garbage[firstNonZero] == 0) firstNonZero++;
        if (firstNonZero == n) return 0; // nothing to clean

        // P[k] = sum_{j<k} garbage[j],  Q[k] = sum_{j<k} j*garbage[j].
        long[] prefixGarbage = new long[n + 1];
        long[] prefixIndexGarbage = new long[n + 1];
        for (int k = 0; k < n; k++) {
            prefixGarbage[k + 1] = prefixGarbage[k] + garbage[k];
            prefixIndexGarbage[k + 1] = prefixIndexGarbage[k] + (long) k * garbage[k];
        }

        long[] minCostFrom = new long[n + 1]; // f[i]
        minCostFrom[n] = 0;

        ConvexHull hull = new ConvexHull(n - firstNonZero + 2);
        // Line for k = n (the "no further robots" option): slope -P[n], intercept Q[n].
        hull.addLine(-prefixGarbage[n], prefixIndexGarbage[n] + minCostFrom[n]);

        for (int i = n - 1; i >= firstNonZero; i--) {
            long bestTail = hull.queryMin(i);
            minCostFrom[i] = deployCost - prefixIndexGarbage[i]
                           + (long) i * prefixGarbage[i] + bestTail;
            hull.addLine(-prefixGarbage[i], prefixIndexGarbage[i] + minCostFrom[i]);
        }

        return minCostFrom[firstNonZero];
    }
}

/**
 * Lower-envelope Convex Hull Trick for MINIMUM queries.
 * Lines are added with non-decreasing slope; queries use binary search, so they
 * may arrive in any x order. The "is the middle line redundant" test is done in
 * exact 128-bit arithmetic (via Math.multiplyHigh) to avoid any overflow.
 */
class ConvexHull {
    private final long[] slope;
    private final long[] intercept;
    private int size = 0;

    ConvexHull(int capacity) {
        slope = new long[Math.max(capacity, 1)];
        intercept = new long[Math.max(capacity, 1)];
    }

    /** Add line y = m*x + b. Slopes must be passed in non-decreasing order. */
    void addLine(long m, long b) {
        // Two lines with the same slope: keep only the lower intercept.
        if (size > 0 && slope[size - 1] == m) {
            if (intercept[size - 1] <= b) return;
            size--;
        }
        slope[size] = m;
        intercept[size] = b;
        size++;
        while (size >= 3 && isMiddleRedundant(size - 3, size - 2, size - 1)) {
            slope[size - 2] = slope[size - 1];
            intercept[size - 2] = intercept[size - 1];
            size--;
        }
    }

    /** Minimum value over all lines at x. */
    long queryMin(long x) {
        int lo = 0, hi = size - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (valueAt(mid, x) >= valueAt(mid + 1, x)) lo = mid + 1;
            else hi = mid;
        }
        return valueAt(lo, x);
    }

    private long valueAt(int i, long x) {
        return slope[i] * x + intercept[i];
    }

    /**
     * Middle line b is redundant for a MINIMUM envelope with strictly increasing
     * slopes: there the consecutive breakpoints must strictly decrease, i.e. keep
     * b only when intersection(a,b) > intersection(b,c). Equivalently b is
     * redundant when intersection(a,b) <= intersection(b,c). Since slopes
     * increase, (Sa-Sb) and (Sb-Sc) are both negative, so cross-multiplying by
     * their (positive) product preserves the direction:
     *     (Ib-Ia)*(Sb-Sc) <= (Ic-Ib)*(Sa-Sb).
     * Evaluated exactly in 128 bits to avoid overflow.
     */
    private boolean isMiddleRedundant(int a, int b, int c) {
        return productLessOrEqual(intercept[b] - intercept[a], slope[b] - slope[c],
                                  intercept[c] - intercept[b], slope[a] - slope[b]);
    }

    /** Exact signed comparison x1*x2 <= y1*y2 using 128-bit products. */
    private static boolean productLessOrEqual(long x1, long x2, long y1, long y2) {
        long xHi = Math.multiplyHigh(x1, x2);
        long xLo = x1 * x2;
        long yHi = Math.multiplyHigh(y1, y2);
        long yLo = y1 * y2;
        if (xHi != yHi) return xHi < yHi;
        return Long.compareUnsigned(xLo, yLo) <= 0;
    }
}
