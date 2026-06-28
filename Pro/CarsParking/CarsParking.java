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
 *   Let d_i = Manhattan distance |p - xi| + |q - yi| of car i to the target.
 *   After T drives a car has spent a total budget
 *         S(T) = 1 + 2 + ... + T = T(T+1)/2.
 *   A single car lands exactly on the target after T drives  <=>
 *         (1) S(T) >= d_i            (enough total travel to cover the gap)
 *         (2) (S(T) - d_i) is even   (the surplus is burned moving out-and-back,
 *                                     which always costs an even amount)
 *
 *   Because every car shares the same T (they move together), condition (2)
 *   forces S(T) ≡ d_i (mod 2) for ALL i simultaneously. That can only hold if
 *   every d_i already has the SAME parity; otherwise the answer is -1.
 *
 *   When the parities agree, let D = max d_i. We need the smallest T with
 *   S(T) >= D and S(T) ≡ D (mod 2). Find the smallest T0 with S(T0) >= D, then
 *   nudge T0 up by 0, 1, or 2 to fix the parity. (Triangular numbers have
 *   parity pattern 0,1,1,0 repeating with period 4, so any three consecutive
 *   T values contain both parities — at most +2 is ever needed.)
 *
 * Complexity: O(N) to scan the cars + O(log D) binary search for T0. Constant
 *   work afterwards, so it answers M up to ±10^17 (D up to ~4*10^17) instantly.
 *
 * Overflow note: D <= 4*10^17 and the matching T is ~9*10^8, so S(T) <= ~4*10^17
 *   stays well within signed 64-bit range. The binary search caps T at 2*10^9,
 *   where T(T+1)/2 ~ 2*10^18 still fits in a long.
 */
public class CarsParking {

    /**
     * @param p  target row
     * @param q  target column
     * @param xs car start rows    (xs.length == ys.length == N)
     * @param ys car start columns
     * @return minimum number of drives to park all cars on (p, q), or -1 if impossible
     */
    public static long minDrives(long p, long q, long[] xs, long[] ys) {
        int n = xs.length;
        if (n == 0) return 0;

        long maxDist = 0;
        long parity = -1; // parity shared by all cars (-1 = not yet seen)

        for (int i = 0; i < n; i++) {
            long d = Math.abs(p - xs[i]) + Math.abs(q - ys[i]);
            long par = d & 1;
            if (parity == -1) {
                parity = par;
            } else if (parity != par) {
                return -1; // two cars need opposite parities -> never aligned
            }
            if (d > maxDist) maxDist = d;
        }

        // Smallest T0 with S(T0) = T0(T0+1)/2 >= maxDist.
        long t0 = smallestDrivesToCover(maxDist);

        // Nudge up to fix S(T) parity to match the (shared) car parity.
        for (long add = 0; add <= 2; add++) {
            long t = t0 + add;
            long s = t * (t + 1) / 2;
            if (s >= maxDist && (s & 1) == parity) {
                return t;
            }
        }
        // Unreachable in practice (a parity-fixing T always exists within +2).
        throw new IllegalStateException("no valid drive count found");
    }

    /** Smallest T with T(T+1)/2 >= target, via binary search (no floating point). */
    private static long smallestDrivesToCover(long target) {
        if (target <= 0) return 0;
        long lo = 0, hi = 2_000_000_000L; // S(hi) ~ 2*10^18 covers any reachable target
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid * (mid + 1) / 2 >= target) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }
}
