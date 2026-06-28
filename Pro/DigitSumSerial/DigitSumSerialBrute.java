/**
 * Digit Sum Serial – Brute Force
 *
 * Idea:
 *   Walk every integer from 1 to A, add up its decimal digits, and tally the
 *   ones whose digit sum equals S. No DP, no states, no cleverness — it just
 *   checks reality, which is exactly what makes it a trustworthy oracle for the
 *   digit DP on small inputs.
 *
 * Complexity: O(A * digits(A)). Linear in the VALUE of A, so it is only usable
 *   when A fits comfortably in a long and is small (up to ~10^7 in a second).
 *   The real constraint A < 10^100 is hopeless here — that is what the DP is for.
 */
public class DigitSumSerialBrute {

    /**
     * @param upperBound A (must be small enough to iterate)
     * @param targetSum  required digit sum S
     * @return count of integers in [1, A] with digit sum == S, modulo 1e9+7
     */
    public static long count(long upperBound, int targetSum) {
        long validCount = 0;
        for (long serial = 1; serial <= upperBound; serial++) {
            if (digitSum(serial) == targetSum) validCount++;
        }
        return validCount % 1_000_000_007L;
    }

    private static int digitSum(long value) {
        int sum = 0;
        while (value > 0) {
            sum += (int) (value % 10);
            value /= 10;
        }
        return sum;
    }
}
