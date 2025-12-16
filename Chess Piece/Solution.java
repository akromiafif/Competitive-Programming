import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.*;

public class Solution {

    static class Point {
        int r, c, dist;

        public Point(int r, int c, int dist) {
            this.r = r;
            this.c = c;
            this.dist = dist;
        }
    }

    // The 8 possible moves for a Knight
    static int[][] moves = {
        {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, 
        {2, -1}, {1, -2}, {-1, -2}, {-2, -1}
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        if (!sc.hasNextInt()) return;
        int T = sc.nextInt();

        for (int t = 1; t <= T; t++) {
            int N = sc.nextInt();
            int M = sc.nextInt();

            int r1 = sc.nextInt();
            int c1 = sc.nextInt();
            int r2 = sc.nextInt();
            int c2 = sc.nextInt();

            int result = bfs(N, M, r1, c1, r2, c2);

            System.out.println("Case #" + t);
            System.out.println(result);
        }
    }

    private static int bfs(int N, int M, int startR, int startC, int targetR, int targetC) {
        if (startR == targetR && startC == targetC) return 0;

        // BFS Setup
        // Visited array
        boolean[][] visited = new boolean[N + 1][M + 1];
        Queue<Point> queue = new LinkedList<>();
        
        queue.add(new Point(startR, startC, 0));
        visited[startR][startC] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (int i = 0; i < 8; i++) {
                int nextR = current.r + moves[i][0];
                int nextC = current.c + moves[i][1];

                if (nextR >= 1 && nextR <= N && nextC >= 1 && nextC <= M && !visited[nextR][nextC]) {
                    if (nextR == targetR && nextC == targetC) {
                        return current.dist + 1;
                    }
                    visited[nextR][nextC] = true;
                    queue.add(new Point(nextR, nextC, current.dist + 1));
                }
            }
        }

        return -1;
    }
}
