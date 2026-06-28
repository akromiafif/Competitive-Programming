/**
 * Cars Parking – Brute Force (drive-by-drive reachability simulation)
 *
 * Idea:
 *   Make NO use of the triangular-number / parity formula. Instead literally
 *   simulate the drives. For one car we track the SET of Manhattan distances it
 *   could currently be from the target. A single drive of length k takes a car
 *   that is distance s away to any distance s' with
 *         |s - k| <= s' <= s + k   and   s' ≡ (s + k) (mod 2),
 *   because the car can head toward the target, away from it, or zig-zag — as
 *   long as it walks exactly k units total and never leaves a (here unbounded)
 *   board, so it always has room to burn surplus distance.
 *
 *   We grow the reachable-distance set one drive at a time for every car. After
 *   drive T, a car is parked exactly when distance 0 is in its set. The answer
 *   is the first T at which ALL cars simultaneously contain 0.
 *
 * This just plays out reality, which is exactly why it is trustworthy for
 * checking the O(1) formula on small inputs.
 *
 * Complexity: pseudo-polynomial — the distance sets grow with the budget, so it
 *   is only meant for small coordinates / small answers (it bails out via a
 *   drive cap and reports -1, matching a genuine impossibility).
 */
public class CarsParkingBrute {

    public static long minDrives(long p, long q, long[] xs, long[] ys) {
        int n = xs.length;
        if (n == 0) return 0;

        // Initial distance of each car, and the largest one.
        long[] dist = new long[n];
        long maxDist = 0;
        for (int i = 0; i < n; i++) {
            dist[i] = Math.abs(p - xs[i]) + Math.abs(q - ys[i]);
            maxDist = Math.max(maxDist, dist[i]);
        }

        // Upper bound on any distance value ever reachable: start + total budget.
        // We also cap the number of drives so a true -1 case terminates.
        long driveCap = 4 * (long) Math.sqrt(maxDist + 1.0) + 50; // generous
        long budget = driveCap * (driveCap + 1) / 2;
        int bound = (int) Math.min(maxDist + budget, 5_000_000L); // array size guard
        if (bound < 1) bound = 1;

        // reachable[i][s] = car i can currently be exactly distance s from target.
        boolean[][] reachable = new boolean[n][bound + 1];
        for (int i = 0; i < n; i++) {
            if (dist[i] <= bound) reachable[i][(int) dist[i]] = true;
        }

        if (allParked(reachable)) return 0; // every car already on the target

        for (long k = 1; k <= driveCap; k++) {
            for (int i = 0; i < n; i++) {
                reachable[i] = applyDrive(reachable[i], k, bound);
            }
            if (allParked(reachable)) return k;
        }
        return -1; // never aligned within the cap -> impossible
    }

    /** Expand one car's reachable-distance set by a single drive of length k. */
    private static boolean[] applyDrive(boolean[] cur, long k, int bound) {
        boolean[] next = new boolean[bound + 1];
        for (int s = 0; s <= bound; s++) {
            if (!cur[s]) continue;
            // New distance s' ranges in [|s-k|, s+k] with parity of (s+k).
            long loL = Math.abs(s - k);
            long hiL = s + k;
            long parityTarget = (s + k) & 1;
            int lo = (int) Math.max(0, loL);
            int hi = (int) Math.min(bound, hiL);
            // Align lo up to the correct parity, then step by 2.
            if ((lo & 1) != parityTarget) lo++;
            for (int sp = lo; sp <= hi; sp += 2) {
                next[sp] = true;
            }
        }
        return next;
    }

    /** True when every car can be exactly distance 0 (parked on the target). */
    private static boolean allParked(boolean[][] reachable) {
        for (boolean[] r : reachable) {
            if (!r[0]) return false;
        }
        return true;
    }
}
