/**
 * Cars Parking – Math (triangular numbers + parity)
 *
 * Problem:
 *   N cars on an M x M grid must all reach the same target (p, q). They move
 *   together: on drive k every car moves EXACTLY k Manhattan steps (it may
 *   zig-zag to burn distance). Find the minimum number of drives to park them
 *   all, or -1 if impossible.
 *
 * Insight to memorize:
 *   Let dist_i = |p - xi| + |q - yi|. After T drives the travel budget is
 *       budget(T) = 1 + 2 + ... + T = T(T+1)/2.
 *   A car parks exactly when  budget(T) >= dist_i  AND  budget(T) - dist_i is
 *   even (the surplus is burned by stepping out-and-back, which costs 2 each).
 *   Since all cars share T, every dist_i must have the SAME parity, else -1.
 *
 *   So: let maxDist = max dist_i.
 *     1) smallest T with budget(T) >= maxDist   (≈ sqrt(2*maxDist), then fix up)
 *     2) bump T while budget(T)'s parity != the cars' parity  (at most +2)
 *
 * Complexity: O(N).  Handles M up to ±10^17 (maxDist ~ 4*10^17) instantly.
 */
public class CarsParking {

    /**
     * @param targetRow target row p
     * @param targetCol target column q
     * @param startRows car start rows    (same length as startCols)
     * @param startCols car start columns
     * @return minimum number of drives to park all cars, or -1 if impossible
     */
    public static long minDrives(long targetRow, long targetCol, long[] startRows, long[] startCols) {
        int carCount = startRows.length;
        if (carCount == 0) return 0;

        long maxDist = 0;
        long sharedParity = -1; // parity every car must share (-1 = not seen yet)
        for (int i = 0; i < carCount; i++) {
            long dist = Math.abs(targetRow - startRows[i]) + Math.abs(targetCol - startCols[i]);
            if (sharedParity == -1) sharedParity = dist & 1;
            else if (sharedParity != (dist & 1)) return -1; // mixed parity -> impossible
            maxDist = Math.max(maxDist, dist);
        }

        // Smallest `drives` with budget(drives) = drives*(drives+1)/2 >= maxDist.
        // Estimate, then correct any sqrt rounding with O(1) steps (exact even
        // at ~4*10^17).
        long drives = (long) Math.sqrt(2.0 * maxDist);
        while (drives * (drives + 1) / 2 < maxDist) drives++;
        while (drives > 0 && (drives - 1) * drives / 2 >= maxDist) drives--;

        // Bump until the budget's parity matches the cars' parity (needs +0/+1/+2).
        while (((drives * (drives + 1) / 2) & 1) != sharedParity) drives++;
        return drives;
    }
}
