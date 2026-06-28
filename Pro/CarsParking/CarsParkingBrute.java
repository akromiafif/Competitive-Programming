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

    public static long minDrives(long targetRow, long targetCol, long[] startRows, long[] startCols) {
        int carCount = startRows.length;
        if (carCount == 0) return 0;

        // Initial distance of each car, and the largest one.
        long[] startDist = new long[carCount];
        long maxDist = 0;
        for (int i = 0; i < carCount; i++) {
            startDist[i] = Math.abs(targetRow - startRows[i]) + Math.abs(targetCol - startCols[i]);
            maxDist = Math.max(maxDist, startDist[i]);
        }

        // Upper bound on any distance value ever reachable: start + total budget.
        // We also cap the number of drives so a true -1 case terminates.
        long driveCap = 4 * (long) Math.sqrt(maxDist + 1.0) + 50; // generous
        long totalBudget = driveCap * (driveCap + 1) / 2;
        int maxReachableDist = (int) Math.min(maxDist + totalBudget, 5_000_000L); // array size guard
        if (maxReachableDist < 1) maxReachableDist = 1;

        // canReach[i][d] = car i can currently be exactly distance d from the target.
        boolean[][] canReach = new boolean[carCount][maxReachableDist + 1];
        for (int i = 0; i < carCount; i++) {
            if (startDist[i] <= maxReachableDist) canReach[i][(int) startDist[i]] = true;
        }

        if (allParked(canReach)) return 0; // every car already on the target

        for (long driveLength = 1; driveLength <= driveCap; driveLength++) {
            for (int i = 0; i < carCount; i++) {
                canReach[i] = applyDrive(canReach[i], driveLength, maxReachableDist);
            }
            if (allParked(canReach)) return driveLength;
        }
        return -1; // never aligned within the cap -> impossible
    }

    /** Expand one car's reachable-distance set by a single drive of the given length. */
    private static boolean[] applyDrive(boolean[] current, long driveLength, int maxReachableDist) {
        boolean[] next = new boolean[maxReachableDist + 1];
        for (int dist = 0; dist <= maxReachableDist; dist++) {
            if (!current[dist]) continue;
            // New distance ranges in [|dist-driveLength|, dist+driveLength]
            // with the parity of (dist + driveLength).
            long parityTarget = (dist + driveLength) & 1;
            int low = (int) Math.max(0, Math.abs(dist - driveLength));
            int high = (int) Math.min(maxReachableDist, dist + driveLength);
            // Align low up to the correct parity, then step by 2.
            if ((low & 1) != parityTarget) low++;
            for (int newDist = low; newDist <= high; newDist += 2) {
                next[newDist] = true;
            }
        }
        return next;
    }

    /** True when every car can be exactly distance 0 (parked on the target). */
    private static boolean allParked(boolean[][] canReach) {
        for (boolean[] carReach : canReach) {
            if (!carReach[0]) return false;
        }
        return true;
    }
}
