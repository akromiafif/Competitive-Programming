/**
 * Cars Parking – Optimal Solution (math: triangular numbers + parity)
 *
 * Problem:
 *   An M x M matrix holds N cars at starting cells (xi, yi). Every car must end
 *   up parked at the SAME target cell (p, q). All cars move SIMULTANEOUSLY: on
 *   drive #1 each car moves EXACTLY 1 unit of Manhattan distance, on drive #2
 *   EXACTLY 2 units, ... on drive #k EXACTLY k units. Paths may overlap and a
 *   car may walk back and forth to "burn" distance. Find the minimum number of
 *   drives T after which EVERY car sits on (p, q), or -1 if impossible.
 *
 * Key Insight:
 *   Let dist_i = Manhattan distance |p - xi| + |q - yi| of car i to the target.
 *   After T drives a car has spent a total budget
 *         budget(T) = 1 + 2 + ... + T = T(T+1)/2.
 *   A single car lands exactly on the target after T drives  <=>
 *         (1) budget(T) >= dist_i           (enough total travel to cover the gap)
 *         (2) (budget(T) - dist_i) is even  (the surplus is burned moving
 *                                            out-and-back, which costs an even
 *                                            amount)
 *
 *   Because every car shares the same T (they move together), condition (2)
 *   forces budget(T) ≡ dist_i (mod 2) for ALL i simultaneously. That can only
 *   hold if every dist_i already has the SAME parity; otherwise the answer is -1.
 *
 *   When the parities agree, let maxDist = max dist_i. We need the smallest T
 *   with budget(T) >= maxDist and budget(T) ≡ maxDist (mod 2). Find the smallest
 *   T0 with budget(T0) >= maxDist, then nudge T0 up by 0, 1, or 2 to fix the
 *   parity. (Triangular numbers have parity pattern 0,1,1,0 repeating with
 *   period 4, so any three consecutive T values contain both parities — at most
 *   +2 is ever needed.)
 *
 * Complexity: O(N) to scan the cars + O(log maxDist) binary search for T0.
 *   Constant work afterwards, so it answers M up to ±10^17 (maxDist up to
 *   ~4*10^17) instantly.
 *
 * Overflow note: maxDist <= 4*10^17 and the matching T is ~9*10^8, so budget(T)
 *   <= ~4*10^17 stays well within signed 64-bit range. The binary search caps T
 *   at 2*10^9, where T(T+1)/2 ~ 2*10^18 still fits in a long.
 */
public class CarsParking {

    /**
     * @param targetRow  target cell row  (p)
     * @param targetCol  target cell column (q)
     * @param startRows  car start rows    (startRows.length == startCols.length == N)
     * @param startCols  car start columns
     * @return minimum number of drives to park all cars on the target, or -1 if impossible
     */
    public static long minDrives(long targetRow, long targetCol, long[] startRows, long[] startCols) {
        int carCount = startRows.length;
        if (carCount == 0) return 0;

        long maxDist = 0;
        long sharedParity = -1; // parity shared by all cars (-1 = not yet seen)

        for (int i = 0; i < carCount; i++) {
            long dist = Math.abs(targetRow - startRows[i]) + Math.abs(targetCol - startCols[i]);
            long parity = dist & 1;
            if (sharedParity == -1) {
                sharedParity = parity;
            } else if (sharedParity != parity) {
                return -1; // two cars need opposite parities -> never aligned
            }
            if (dist > maxDist) maxDist = dist;
        }

        // Smallest baseDrives with budget(baseDrives) = baseDrives(baseDrives+1)/2 >= maxDist.
        long baseDrives = smallestDrivesToCover(maxDist);

        // Nudge up to fix budget(T) parity to match the (shared) car parity.
        for (long extra = 0; extra <= 2; extra++) {
            long drives = baseDrives + extra;
            long budget = drives * (drives + 1) / 2;
            if (budget >= maxDist && (budget & 1) == sharedParity) {
                return drives;
            }
        }
        // Unreachable in practice (a parity-fixing T always exists within +2).
        throw new IllegalStateException("no valid drive count found");
    }

    /** Smallest drives with drives(drives+1)/2 >= target, via binary search (no floating point). */
    private static long smallestDrivesToCover(long target) {
        if (target <= 0) return 0;
        long low = 0, high = 2_000_000_000L; // budget(high) ~ 2*10^18 covers any reachable target
        while (low < high) {
            long mid = low + (high - low) / 2;
            if (mid * (mid + 1) / 2 >= target) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
}
