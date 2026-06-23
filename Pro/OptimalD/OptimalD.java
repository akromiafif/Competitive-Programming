import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Find Optimal D – Optimal Solution (Sort + Binary Search)
 *
 * Problem:
 *   Two arrays A and B. An element scores 1 if it is <= D and 2 if it is > D.
 *   value(array) = sum of element scores. Find D in [0, 1e9] maximizing
 *   value(A) - value(B).
 *
 * Key Insight:
 *   For an array of size n,  value = n + (#elements > D)
 *   because each element contributes a base 1, plus an extra 1 when it is > D.
 *   So
 *       value(A) - value(B) = (n - m) + countAbove_A(D) - countAbove_B(D)
 *   The (n - m) term is constant, so we just maximize
 *       countAbove_A(D) - countAbove_B(D).
 *
 *   countAbove only changes as D crosses an actual element value, so the optimum
 *   is attained at one of a small set of candidate thresholds:
 *     - (each distinct element value) - 1   (D just below an element)
 *     - each distinct element value
 *     - the allowed extremes (0 and 1e9)
 *
 * Technique:
 *   Sort both arrays once. For each candidate D, get countAbove via binary
 *   search (n - upperBound(D)). Keep the D with the largest difference.
 *
 * Complexity: O((N + M) log(N + M)) time, O(N + M) space.
 */
public class OptimalD {

    static final int MAX_D = 1_000_000_000;

    /** How many elements in a SORTED list are strictly greater than threshold. */
    static int countAbove(List<Integer> sortedArr, int threshold) {
        // Binary search for the first index whose value is > threshold.
        int low = 0, high = sortedArr.size();
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (sortedArr.get(mid) <= threshold) low = mid + 1;
            else high = mid;
        }
        // Everything from that index to the end is greater than threshold.
        return sortedArr.size() - low;
    }

    /** Returns the best D (smallest such D on ties), clamped to [0, 1e9]. */
    static int bestD(List<Integer> arrA, List<Integer> arrB) {
        List<Integer> sortedA = new ArrayList<>(arrA);
        List<Integer> sortedB = new ArrayList<>(arrB);
        Collections.sort(sortedA);
        Collections.sort(sortedB);

        // Build the set of candidate thresholds worth trying.
        TreeSet<Integer> candidateThresholds = new TreeSet<>();
        candidateThresholds.add(0);      // smallest allowed D
        candidateThresholds.add(MAX_D);  // largest allowed D
        for (int value : sortedA) {
            addClamped(candidateThresholds, value - 1); // D just below this value
            addClamped(candidateThresholds, value);     // D equal to this value
        }
        for (int value : sortedB) {
            addClamped(candidateThresholds, value - 1);
            addClamped(candidateThresholds, value);
        }

        long maxDifference = Long.MIN_VALUE;
        int chosenD = 0;
        for (int threshold : candidateThresholds) {
            long difference = (long) countAbove(sortedA, threshold)
                                   - countAbove(sortedB, threshold);
            if (difference > maxDifference) { // strictly greater -> keeps smallest D on ties
                maxDifference = difference;
                chosenD = threshold;
            }
        }
        return chosenD;
    }

    /** Maximum achievable value(A) - value(B). */
    static long bestDiff(List<Integer> arrA, List<Integer> arrB) {
        int threshold = bestD(arrA, arrB);
        long valueA = arrA.size() + countAboveUnsorted(arrA, threshold);
        long valueB = arrB.size() + countAboveUnsorted(arrB, threshold);
        return valueA - valueB;
    }

    /** Adds a threshold to the set, clamped into the allowed [0, 1e9] range. */
    private static void addClamped(TreeSet<Integer> thresholds, int threshold) {
        if (threshold < 0) threshold = 0;
        if (threshold > MAX_D) threshold = MAX_D;
        thresholds.add(threshold);
    }

    /** Count elements greater than threshold in an UNSORTED list (simple scan). */
    private static int countAboveUnsorted(List<Integer> arr, int threshold) {
        int count = 0;
        for (int value : arr) if (value > threshold) count++;
        return count;
    }
}
