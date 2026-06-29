import java.util.*;

public class Main {  
    private static final int INF = Integer.MAX_VALUE;
    private static final int[] DR = {0, 1, 0, -1};
    private static final int[] DC = {1, 0, -1, 0};

    static int BFS(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int apples = 0;
        for (int[] row: grid)
            for (int v: row) apples = Math.max(apples, v);

        if (apples == 0) return 0;
        if (grid[0][0] == -1) return -1;

        int[][][][] dist = new int[rows][cols][apples + 1][4];
        for (int[][][] a: dist)
            for (int[][] b: a)
                for (int[] c: b) Arrays.fill(c, INF);

        int startEaten = (grid[0][0] == 1) ? 1 : 0;
        dist[0][0][startEaten][0] = 0;

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
                int cost = d + turn;
                if (cost < dist[nr][nc][nextEaten][nextDir]) {
                    dist[nr][nc][nextEaten][nextDir] = cost;
                    int[] next = {nr, nc, nextEaten, nextDir};
                    if (turn == 0) deque.addFirst(next);
                    else deque.addLast(next);
                }
            }
        }

        int best = INF;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int dir = 0; dir < 4; dir++) {
                    best = Math.min(best, dist[row][col][apples][dir]);
                }
            }
        }

        return best == INF ? -1 : best;
        
    }

    public static void main(String[] args) {
        int[][] grid = {
            {1, 0, 0},
            {0, 0, 0},
            {2, 0, 0}
        };
        System.out.println(BFS(grid)); // expect 1 (turn right once, then go down)
    }
}