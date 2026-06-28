import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Apple Game (Right Turn Only) – Optimal Solution (0-1 BFS)
 *
 * Problem:
 *   On a grid (0 = empty, -1 = trap, positive = apple number) the player starts
 *   at (0,0) facing RIGHT and may only ever turn RIGHT (or keep going straight).
 *   Apples must be eaten strictly in order 1, 2, 3, ... (walking over a not-yet-due
 *   apple does nothing). Eat them all using the MINIMUM number of right turns, or
 *   report -1 if impossible.
 *
 * Key Insight:
 *   Model a state as (row, col, applesEatenInOrder, facingDir). From any state you
 *   may either
 *     - step one cell in the current direction  -> 0 extra turns, or
 *     - turn right and step one cell            -> 1 extra turn.
 *   Edge weights are only 0 or 1, so a 0-1 BFS with a deque (push 0-cost moves to
 *   the FRONT, 1-cost moves to the BACK) finds the minimum-turn cost to every
 *   state in O(V + E). The answer is the smallest cost among all states whose
 *   eaten-count equals the number of apples M.
 *
 *   "Eaten in order" is enforced by only incrementing the count when stepping onto
 *   the cell holding exactly (eaten + 1).
 *
 * Complexity: O(N * M_cols * M_apples * 4). For the limits (N < 60, apples <= 150)
 *   that is ~2 million states — solved well under 1s.
 */
public class AppleGame {

    // dir 0=right, 1=down, 2=left, 3=up (right turn = (dir+1)%4).
    private static final int[] DROW = {0, 1, 0, -1};
    private static final int[] DCOL = {1, 0, -1, 0};

    /**
     * @param grid rows x cols; 0 empty, -1 trap, positive = apple number
     * @return minimum number of right turns to eat all apples in order, or -1
     */
    public static int minRightTurns(int[][] grid) {
        int rows = grid.length;
        if (rows == 0) return -1;
        int cols = grid[0].length;

        int apples = 0;
        for (int[] row : grid)
            for (int value : row) apples = Math.max(apples, value);
        if (apples == 0) return 0;          // no apples to eat
        if (grid[0][0] == -1) return -1;    // cannot even stand on the start

        int stateCount = rows * cols * (apples + 1) * 4;
        int[] dist = new int[stateCount];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int startEaten = (grid[0][0] == 1) ? 1 : 0;
        int start = stateId(0, 0, startEaten, 0, cols, apples);
        dist[start] = 0;

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(start);

        while (!deque.isEmpty()) {
            int cur = deque.pollFirst();
            int dir = cur & 3;
            int withoutDir = cur >> 2;
            int eaten = withoutDir % (apples + 1);
            int cell = withoutDir / (apples + 1);
            int col = cell % cols;
            int row = cell / cols;
            int d = dist[cur];

            // Straight ahead: same number of turns.
            relax(row, col, eaten, dir, d, false, grid, dist, deque, rows, cols, apples);
            // Turn right and step: one more turn.
            relax(row, col, eaten, (dir + 1) & 3, d + 1, true, grid, dist, deque, rows, cols, apples);
        }

        int best = Integer.MAX_VALUE;
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                for (int dir = 0; dir < 4; dir++)
                    best = Math.min(best, dist[stateId(row, col, apples, dir, cols, apples)]);
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    private static void relax(int row, int col, int eaten, int dir, int newDist, boolean toBack,
                              int[][] grid, int[] dist, ArrayDeque<Integer> deque,
                              int rows, int cols, int apples) {
        int nr = row + DROW[dir], nc = col + DCOL[dir];
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] == -1) return;
        int newEaten = eaten + (grid[nr][nc] == eaten + 1 ? 1 : 0);
        int next = stateId(nr, nc, newEaten, dir, cols, apples);
        if (newDist < dist[next]) {
            dist[next] = newDist;
            if (toBack) deque.addLast(next);
            else deque.addFirst(next);
        }
    }

    private static int stateId(int row, int col, int eaten, int dir, int cols, int apples) {
        return ((row * cols + col) * (apples + 1) + eaten) * 4 + dir;
    }
}
