/**
 * String Merging – 10x10 DP
 *
 * Problem:
 *   Given strings of digits, pick a subsequence i1 < i2 < ... where each string's
 *   last digit equals the next string's first digit (so they can be concatenated).
 *   The whole merged string must also have first digit == last digit. Maximize
 *   the total length. (Values are numbers 1..1e9, so digits are 0..9, first digit
 *   1..9.)
 *
 * Insight to memorize:
 *   The only things that matter about a chain are its overall FIRST digit and
 *   LAST digit. So keep a 10x10 table:
 *       best[f][l] = longest valid chain that starts with digit f and ends with l.
 *   Sweep the array RIGHT TO LEFT. For string s with first digit f, last digit l,
 *   length len:
 *       - s alone is a chain f->l:           best[f][l] = max(best[f][l], len)
 *       - s can be PREPENDED to any chain that starts with l (its last digit
 *         matches that chain's first), extending it:
 *             for every end digit e: best[f][e] = max(best[f][e], len + best[l][e])
 *   Right-to-left guarantees those chains use only LATER indices, preserving i<j.
 *   Answer = max over d of best[d][d]  (first digit == last digit).
 *
 * Complexity: O(n * 10) time, O(100) space.
 */
public class StringMerging {

    public static long maxFinalLength(String[] arr) {
        int n = arr.length;

        // best[f][l] = longest chain starting with digit f, ending with digit l.
        // 0 means "no such chain yet" (real chains have length >= 1).
        long[][] best = new long[10][10];

        for (int i = n - 1; i >= 0; i--) {
            int first = arr[i].charAt(0) - '0';
            int last = arr[i].charAt(arr[i].length() - 1) - '0';
            int len = arr[i].length();

            // Prepend arr[i] to any later chain that STARTS with `last`.
            for (int end = 0; end <= 9; end++) {
                if (best[last][end] != 0) {
                    best[first][end] = Math.max(best[first][end], best[last][end] + len);
                }
            }
            // arr[i] standing alone.
            best[first][last] = Math.max(best[first][last], len);
        }

        long ans = 0;
        for (int d = 0; d <= 9; d++) ans = Math.max(ans, best[d][d]);
        return ans;
    }
}
