import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Find Optimal D – Sort + Binary Search
 *
 * Problem:
 *   Two arrays A and B. An element scores 1 if it is <= D and 2 if it is > D;
 *   value(array) = sum of scores. Find D in [0, 1e9] maximizing value(A)-value(B).
 *
 * Insight to memorize:
 *   value(arr) = size + (#elements > D), so
 *       value(A) - value(B) = (|A| - |B|) + (countAbove_A(D) - countAbove_B(D)).
 *   The (|A|-|B|) part is constant, so we just maximize the count difference.
 *   That difference only changes when D crosses an element value, so the best D
 *   is ALWAYS either 0 or one of the element values — no need to test anything
 *   else. Try each candidate, counting "elements > D" with a binary search on
 *   the sorted arrays.
 *
 * Complexity: O((N + M) log(N + M)).
 */
public class OptimalD {

    /** Elements strictly greater than threshold in a SORTED list (binary search). */
    static int countAbove(List<Integer> sorted, int threshold) {
        int low = 0, high = sorted.size();
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (sorted.get(mid) <= threshold) low = mid + 1;
            else high = mid;
        }
        return sorted.size() - low; // everything from `low` onward is > threshold
    }

    /** The D that maximizes value(A) - value(B). */
    static int bestD(List<Integer> a, List<Integer> b) {
        List<Integer> sortedA = new ArrayList<>(a);
        List<Integer> sortedB = new ArrayList<>(b);
        Collections.sort(sortedA);
        Collections.sort(sortedB);

        // Candidates: 0 and every element value (where the count difference jumps).
        List<Integer> candidates = new ArrayList<>();
        candidates.add(0);
        candidates.addAll(a);
        candidates.addAll(b);

        int chosenD = 0;
        long best = Long.MIN_VALUE;
        for (int D : candidates) {
            long diff = (long) countAbove(sortedA, D) - countAbove(sortedB, D);
            if (diff > best) {
                best = diff;
                chosenD = D;
            }
        }
        return chosenD;
    }

    /** Maximum achievable value(A) - value(B). */
    static long bestDiff(List<Integer> a, List<Integer> b) {
        int D = bestD(a, b);
        return scoreOf(a, D) - scoreOf(b, D);
    }

    /** Score of an array for a fixed D: +1 if elem <= D, else +2. */
    static long scoreOf(List<Integer> arr, int D) {
        long score = 0;
        for (int x : arr) score += (x <= D) ? 1 : 2;
        return score;
    }
}
