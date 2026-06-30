import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Apple Game (Right Turn Only) – 0-1 BFS
 *
 * Problem:
 *   On a grid (0 = empty, -1 = trap, positive = apple number) the player starts
 *   at (0,0) facing RIGHT and may only ever go straight or turn RIGHT. Apples
 *   must be eaten strictly in order 1, 2, 3, ... (stepping onto a not-yet-due
 *   apple does nothing). Eat them all with the MINIMUM number of right turns,
 *   or return -1 if impossible.
 *
 * Why 0-1 BFS:
 *   State = (row, col, applesEatenInOrder, facingDir). From a state there are
 *   exactly two moves, and each steps ONE cell:
 *       turn = 0 : go straight        -> costs 0 turns
 *       turn = 1 : turn right + step  -> costs 1 turn
 *   Edge weights are only 0 or 1, so a deque does the job: push 0-cost moves to
 *   the FRONT and 1-cost moves to the BACK. That keeps the deque in
 *   non-decreasing distance order -> Dijkstra-correct in O(V + E).
 *
 *   Easy-to-remember core (the whole algorithm in one loop):
 *       for (turn = 0..1)
 *           nextDir = turn==0 ? dir : (dir+1)%4
 *           step one cell in nextDir
 *           cost    = d + turn                  // 0 stays, right turn adds 1
 *           if it improves: push FRONT when turn==0, BACK when turn==1
 */
public class AppleGame {

    private static final int INF = Integer.MAX_VALUE;

    // dir 0=right, 1=down, 2=left, 3=up; a right turn is (dir + 1) % 4.
    private static final int[] DR = {0, 1, 0, -1};
    private static final int[] DC = {1, 0, -1, 0};

    public static int minRightTurns(int[][] grid) {
        int rows = grid.length;
        if (rows == 0) return -1;
        int cols = grid[0].length;

        int apples = 0;                          // number of apples = largest value
        for (int[] row : grid)
            for (int v : row) apples = Math.max(apples, v);
        if (apples == 0) return 0;               // nothing to eat
        if (grid[0][0] == -1) return -1;         // cannot stand on the start

        // dist[row][col][eaten][dir] = fewest right turns to reach this state.
        int[][][][] dist = new int[rows][cols][apples + 1][4];
        for (int[][][] a : dist)
            for (int[][] b : a)
                for (int[] c : b) Arrays.fill(c, INF);

        int startEaten = (grid[0][0] == 1) ? 1 : 0;
        dist[0][0][startEaten][0] = 0;

        // A deque entry is just the state {row, col, eaten, dir}.
        ArrayDeque<int[]> deque = new ArrayDeque<>();
        deque.addFirst(new int[]{0, 0, startEaten, 0});

        while (!deque.isEmpty()) {
            int[] s = deque.pollFirst();
            int row = s[0], col = s[1], eaten = s[2], dir = s[3];
            int d = dist[row][col][eaten][dir];

            for (int turn = 0; turn <= 1; turn++) {
                int nextDir = (turn == 0) ? dir : (dir + 1) % 4;
                int nr = row + DR[nextDir], nc = col + DC[nextDir];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] == -1) continue;

                int nextEaten = eaten + (grid[nr][nc] == eaten + 1 ? 1 : 0);
                int cost = d + turn;             // straight adds 0, right turn adds 1
                if (cost < dist[nr][nc][nextEaten][nextDir]) {
                    dist[nr][nc][nextEaten][nextDir] = cost;
                    int[] next = {nr, nc, nextEaten, nextDir};
                    if (turn == 0) deque.addFirst(next); // 0-cost -> front
                    else deque.addLast(next);            // 1-cost -> back
                }
            }
        }

        // Best over all cells/directions once every apple is eaten.
        int best = INF;
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                for (int dir = 0; dir < 4; dir++)
                    best = Math.min(best, dist[row][col][apples][dir]);
                    
        return best == INF ? -1 : best;
    }
}
