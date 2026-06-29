/**
 * Digit Sum Serial – Optimal Solution (Digit DP)
 *
 * Problem:
 *   Serial numbers are plain non-negative integers. Count how many serials in
 *   the range [1, A] have a digit sum EXACTLY equal to S, where A may have up to
 *   ~100 digits (A < 10^100) and 1 <= S <= 1000. Return the count modulo 1e9+7.
 *   (Since S >= 1, the number 0 never qualifies, so counting [0, A] gives the
 *   same result and is simpler to express as a digit DP.)
 *
 * Key Insight (classic digit DP):
 *   Build the number left to right, digit by digit, tracking two things:
 *     - remainingSum : how much digit-sum budget is still left (starts at S,
 *                      must reach exactly 0)
 *     - isTight      : whether the prefix chosen so far equals A's prefix
 *                      exactly. While tight, the next digit may not exceed A's
 *                      digit at this position; once we place something smaller,
 *                      tightness drops and every later digit is free to be 0..9.
 *   At the end (all positions placed) the number is valid iff remainingSum == 0.
 *
 *   States (position, remainingSum, isTight) are memoized. Numbers with leading
 *   zeros are simply the same as shorter numbers (e.g. "007" == 7) and are
 *   counted naturally, which is exactly what we want for the [0, A] range.
 *
 * Complexity: O(len(A) * S * 10) time, O(len(A) * S * 2) memo. For 100 digits
 *   and S = 1000 that is ~1e6 transitions — answered instantly.
 */
public class DigitSumSerial {

    private static final int MOD = 1_000_000_007;

    /**
     * @param upperBound A as a decimal string (no sign, no leading '+')
     * @param targetSum  required digit sum S
     * @return count of integers in [1, A] with digit sum == S, modulo 1e9+7
     */
    public static long count(String digits, int targetSum) {
        if (targetSum < 0) return 0;
        int numDigits = digits.length();
        // Largest achievable digit sum is 9 * (number of digits).
        if (targetSum > 9 * numDigits) return 0;

        // memo[position][remainingSum][isTight] = number of ways to fill the
        // remaining positions. The sentinel -1 means "not computed yet".
        long[][][] memo = new long[numDigits][targetSum + 1][2];
        for (long[][] plane : memo)
            for (long[] row : plane) {
                row[0] = -1;
                row[1] = -1;
            }
        return countFrom(digits, 0, targetSum, true, memo);
    }

    private static long countFrom(String digits, int position, int remainingSum,
                                  boolean isTight, long[][][] memo) {
        if (remainingSum < 0) return 0;                 // overshot the digit-sum budget
        if (position == digits.length()) return remainingSum == 0 ? 1 : 0;

        int tightIndex = isTight ? 1 : 0;
        if (memo[position][remainingSum][tightIndex] != -1)
            return memo[position][remainingSum][tightIndex];

        int maxDigit = isTight ? (digits.charAt(position) - '0') : 9;
        long ways = 0;
        for (int digit = 0; digit <= maxDigit; digit++) {
            ways = (ways + countFrom(digits, position + 1, remainingSum - digit, isTight && (digit == maxDigit), memo)) % MOD;
        }

        return memo[position][remainingSum][tightIndex] = ways;
    }
}
