import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Apple Game (Right Turn Only) – Brute Force (Dijkstra)
 *
 * Idea:
 *   Exactly the same state graph as the optimal solution — a state is
 *   (row, col, applesEatenInOrder, facingDir), going straight costs 0 turns and
 *   turning right + stepping costs 1 — but explored with a plain Dijkstra
 *   (priority queue) instead of the 0-1 BFS deque trick.
 *
 *   Dijkstra makes no use of the "weights are only 0 or 1" structure; it just
 *   relies on non-negative edge weights, which makes it an obviously-correct and
 *   independent oracle for verifying the deque-based solution.
 *
 * Complexity: O(E log V) — the log factor is exactly what the 0-1 BFS removes.
 */
public class AppleGameBrute {

    private static final int[] DROW = {0, 1, 0, -1};
    private static final int[] DCOL = {1, 0, -1, 0};

    public static int minRightTurns(int[][] grid) {
        int rows = grid.length;
        if (rows == 0) return -1;
        int cols = grid[0].length;

        int apples = 0;
        for (int[] row : grid)
            for (int value : row) apples = Math.max(apples, value);
        if (apples == 0) return 0;
        if (grid[0][0] == -1) return -1;

        int stateCount = rows * cols * (apples + 1) * 4;
        int[] dist = new int[stateCount];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int startEaten = (grid[0][0] == 1) ? 1 : 0;
        int start = stateId(0, 0, startEaten, 0, cols, apples);
        dist[start] = 0;

        // Each entry: {distance, stateId}, ordered by distance.
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.add(new int[]{0, start});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0], cur = top[1];
            if (d > dist[cur]) continue; // stale entry

            int dir = cur & 3;
            int withoutDir = cur >> 2;
            int eaten = withoutDir % (apples + 1);
            int cell = withoutDir / (apples + 1);
            int col = cell % cols;
            int row = cell / cols;

            if (eaten == apples) return d; // first goal popped is the minimum

            relax(row, col, eaten, dir, d, grid, dist, pq, rows, cols, apples);
            relax(row, col, eaten, (dir + 1) & 3, d + 1, grid, dist, pq, rows, cols, apples);
        }
        return -1;
    }

    private static void relax(int row, int col, int eaten, int dir, int newDist,
                              int[][] grid, int[] dist, PriorityQueue<int[]> pq,
                              int rows, int cols, int apples) {
        int nr = row + DROW[dir], nc = col + DCOL[dir];
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] == -1) return;
        int newEaten = eaten + (grid[nr][nc] == eaten + 1 ? 1 : 0);
        int next = stateId(nr, nc, newEaten, dir, cols, apples);
        if (newDist < dist[next]) {
            dist[next] = newDist;
            pq.add(new int[]{newDist, next});
        }
    }

    private static int stateId(int row, int col, int eaten, int dir, int cols, int apples) {
        return ((row * cols + col) * (apples + 1) + eaten) * 4 + dir;
    }
}
