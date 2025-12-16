import java.util.Scanner;

public class JewelMaze {
    static int N;
    static int maxJewels;
    static int[][] bestPath;
    static int[][] grid;
    static boolean[][] visited;
    
    // Direction vectors: Down, Right, Up, Left
    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int T = sc.nextInt();
        
        while (T-- > 0) {
            N = sc.nextInt();
            grid = new int[N][N];
            bestPath = new int[N][N];
            visited = new boolean[N][N];
            maxJewels = Integer.MIN_VALUE;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }

            // Start DFS from (0,0)
            // Initial jewel count: check if (0,0) has a jewel
            int startJewels = (grid[0][0] == 2) ? 1 : 0;
            visited[0][0] = true;
            
            dfs(0, 0, startJewels);
            
            // Output best path
            // Reconstruct logic: We stored the best path grid in `bestPath`
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(bestPath[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println(maxJewels);
        }
    }

    private static void dfs(int r, int c, int jewels) {
        // Reached destination (N-1, N-1)
        if (r == N - 1 && c == N - 1) {
            if (jewels >= maxJewels) {
                maxJewels = jewels;
                // Save current path to bestPath
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        // If it's part of the path (visited), mark as 3. Else copy original grid.
                        // However, 'visited' array changes during backtracking.
                        // We need to know which nodes are in THIS path.
                        // We can track path state in 'currentPath' where 3 means visited in current path.
                        
                        if (visited[i][j]) {
                            bestPath[i][j] = 3;
                        } else {
                            // If not in path, use original grid value
                            bestPath[i][j] = grid[i][j];
                        }
                    }
                }
            }
            return;
        }

        // Potential pruning? 
        // If (remaining jewels + current jewels) < maxJewels, return.
        // But finding remaining jewels is costly. N is small (10), brute force is OK.

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                if (!visited[nr][nc] && grid[nr][nc] != 1) {
                    visited[nr][nc] = true;
                    int nextJewels = jewels + (grid[nr][nc] == 2 ? 1 : 0);
                    
                    dfs(nr, nc, nextJewels);
                    
                    visited[nr][nc] = false; // Backtrack
                }
            }
        }
    }
}
