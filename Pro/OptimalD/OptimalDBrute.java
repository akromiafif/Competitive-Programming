import java.util.List;

/**
 * Find Optimal D – Brute Force
 *
 * Problem:
 *   Two arrays A and B. The "value" of an array is the total score of its
 *   elements, where an element scores 1 if it is <= D and 2 if it is > D.
 *   Find D (0 <= D <= 1e9) that MAXIMIZES (value(A) - value(B)).
 *
 * Brute Force idea:
 *   The diff only ever changes at integer thresholds, so just try EVERY
 *   integer value of D from 0 up to (maxElement + 1). For each candidate D,
 *   scan both arrays directly and compute the score difference.
 *
 * Complexity: O(maxValue * (N + M)).
 *   Fine for tiny inputs, hopeless once values/sizes grow — which is exactly
 *   why we also have the optimal solution.
 */
public class OptimalDBrute {

    /** Score of one array for a fixed D: +1 if elem <= D, else +2. */
    static long scoreOf(List<Integer> arr, int d) {
        long score = 0;
        for (int x : arr) {
            score += (x <= d) ? 1 : 2;
        }
        return score;
    }

    /** Returns the best D (smallest such D on ties). */
    static int bestD(List<Integer> a, List<Integer> b) {
        int maxVal = 0;
        for (int x : a) maxVal = Math.max(maxVal, x);
        for (int x : b) maxVal = Math.max(maxVal, x);

        long best = Long.MIN_VALUE;
        int bestD = 0;

        // Candidate D from 0 .. maxVal+1 covers every distinct behaviour.
        for (int d = 0; d <= maxVal + 1; d++) {
            long diff = scoreOf(a, d) - scoreOf(b, d);
            if (diff > best) {
                best = diff;
                bestD = d;
            }
        }
        return bestD;
    }

    /** Maximum achievable value(A) - value(B). */
    static long bestDiff(List<Integer> a, List<Integer> b) {
        return scoreOf(a, bestD(a, b)) - scoreOf(b, bestD(a, b));
    }
}
