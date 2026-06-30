/**
 * String Merging – Brute Force (try every subsequence)
 *
 * Idea:
 *   Enumerate all 2^n subsets of indices. Reading the chosen indices in
 *   increasing order, a subset is a valid "final" string iff every chosen
 *   string's first digit equals the previous chosen string's last digit, AND the
 *   overall first digit equals the overall last digit. Track the max total
 *   length. No DP, just checks every possibility — a trustworthy oracle.
 *
 * Complexity: O(2^n * n). Only usable for small n (~<= 18).
 */
public class StringMergingBrute {

    public static long maxFinalLength(String[] arr) {
        int n = arr.length;
        long best = 0;

        for (int mask = 1; mask < (1 << n); mask++) {
            int firstDigit = -1;
            int prevLast = -1;
            int lastDigit = -1;
            long totalLen = 0;
            boolean ok = true;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) continue;
                int f = arr[i].charAt(0) - '0';
                int l = arr[i].charAt(arr[i].length() - 1) - '0';
                if (firstDigit == -1) {
                    firstDigit = f;                 // first chosen string
                } else if (f != prevLast) {
                    ok = false;                     // cannot connect to the previous one
                    break;
                }
                prevLast = l;
                lastDigit = l;
                totalLen += arr[i].length();
            }

            if (ok && firstDigit == lastDigit) {
                best = Math.max(best, totalLen);
            }
        }
        return best;
    }
}
