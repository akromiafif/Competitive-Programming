import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

public class Endoscope {
    
    static class Point {
        int r, c, dist;
        public Point(int r, int c, int dist) {
            this.r = r;
            this.c = c;
            this.dist = dist;
        }
    }

    /*
     * Pipe connectivity definition:
     * Index 0: Up
     * Index 1: Down
     * Index 2: Left
     * Index 3: Right
     */
    static boolean[][] pipeTypes = {
        {false, false, false, false}, // 0: None
        {true, true, true, true},     // 1: All
        {true, true, false, false},   // 2: Up, Down
        {false, false, true, true},   // 3: Left, Right
        {true, false, false, true},   // 4: Up, Right
        {false, true, false, true},   // 5: Down, Right
        {false, true, true, false},   // 6: Down, Left
        {true, false, true, false}    // 7: Up, Left
    };

    // Directions: Up, Down, Left, Right
    // Corresponds to the pipeTypes indices
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    // Index mapping to check compatibility
    // If I move Up (index 0), neighbor must have Down (index 1)
    // If I move Down (index 1), neighbor must have Up (index 0)
    // If I move Left (index 2), neighbor must have Right (index 3)
    // If I move Right (index 3), neighbor must have Left (index 2)
    static int[] reverseDir = {1, 0, 3, 2};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int T = sc.nextInt();
        int caseNum = 1;

        while (T-- > 0) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            int R = sc.nextInt();
            int C = sc.nextInt();
            int L = sc.nextInt();

            int[][] grid = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }
            
            System.out.println("Case #" + caseNum);
            caseNum++;

            // Edge case: Pipe at start is 0
            if (grid[R][C] == 0) {
                System.out.println(0);
                continue;
            }

            System.out.println(BFS(N, M, R, C, L, grid));
        }
    }

    private static int BFS(int N, int M, int R, int C, int L, int[][] grid) {
        if (L == 0) return 0; 
        // Or 1 if it counts standing still? Problem usually implies L=1 is 1 pipe.
        // "If L=1, explore only one water pipe". So return 1 if L >= 1.
        // Code handles this naturally if loop runs once? No, dist starts at 1.

        boolean[][] visited = new boolean[N][M];
        Queue<Point> queue = new LinkedList<>();

        queue.add(new Point(R, C, 1));
        visited[R][C] = true;
        
        int count = 0;

        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            count++;

            if (curr.dist >= L) continue;

            int currPipe = grid[curr.r][curr.c];

            for (int i = 0; i < 4; i++) {
                // Check if current pipe has opening in this direction
                if (pipeTypes[currPipe][i]) {
                    int nr = curr.r + dr[i];
                    int nc = curr.c + dc[i];

                    // Check bounds
                    if (nr >= 0 && nr < N && nc >= 0 && nc < M) {
                        int nextPipe = grid[nr][nc];
                        
                        // Check if not visited, not empty, AND neighbor has compatible opening
                        if (!visited[nr][nc] && nextPipe != 0) {
                            int requiredOpening = reverseDir[i];
                            if (pipeTypes[nextPipe][requiredOpening]) {
                                visited[nr][nc] = true;
                                q.add(new Point(nr, nc, curr.dist + 1));
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
