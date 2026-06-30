import java.util.Arrays;

/**
 * Digit Sum Serial – Digit DP (single function)
 *
 * Problem:
 *   Count how many integers in [1, A] have digit sum EXACTLY S, where A may have
 *   up to ~100 digits (A < 10^100) and 1 <= S <= 1000. Answer modulo 1e9+7.
 *   (Since S >= 1, counting [0, A] gives the same result and is simpler.)
 *
 * Insight to memorize:
 *   Build the number left to right tracking:
 *     - remainingSum : digit-sum budget still left (starts at S, must hit 0)
 *     - isTight      : is the prefix so far still equal to A's prefix? While
 *                      tight the next digit is capped at A's digit; once you go
 *                      under, you're FREE and later digits can be 0..9.
 *   Valid iff remainingSum == 0 at the end. Only FREE states repeat (the tight
 *   path is a single chain), so memoize on (position, remainingSum) and skip
 *   caching while tight.
 *
 * Call it as:  count(digits, 0, S, true, null)
 *   The first call (memo == null) does the bounds check + allocates the memo,
 *   then the same function recurses with the shared table.
 *
 * Complexity: O(len(A) * S * 10) time, O(len(A) * S) memo.
 */
public class DigitSumSerial {

    private static final int MOD = 1_000_000_007;

    /**
     * @param digits       A as a decimal string (no sign, no leading '+')
     * @param position     current digit index (start at 0)
     * @param remainingSum digit-sum budget left (start at S)
     * @param isTight      whether the prefix still equals A's prefix (start true)
     * @param memo         pass null on the first call; it is created and reused
     * @return count of integers in [1, A] with digit sum == S, modulo 1e9+7
     */
    public static long count(String digits, int position, int remainingSum,
                             boolean isTight, long[][] memo) {
        if (memo == null) { // first call: validate and build the table
            int numDigits = digits.length();     // remainingSum == S here
            if (remainingSum < 0 || remainingSum > 9 * numDigits) return 0;
            memo = new long[numDigits][remainingSum + 1];
            for (long[] row : memo) Arrays.fill(row, -1);
        }

        if (remainingSum < 0) return 0;                 // overshot the budget
        if (position == digits.length()) return remainingSum == 0 ? 1 : 0;

        if (!isTight && memo[position][remainingSum] != -1)
            return memo[position][remainingSum];        // reuse a free state

        int maxDigit = isTight ? (digits.charAt(position) - '0') : 9;
        long ways = 0;
        for (int digit = 0; digit <= maxDigit; digit++) {
            ways = (ways + count(digits, position + 1, remainingSum - digit,
                                 isTight && (digit == maxDigit), memo)) % MOD;
        }

        if (!isTight) memo[position][remainingSum] = ways; // cache only free states
        return ways;
    }
}
