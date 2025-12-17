import java.util.Scanner;

public class Main {
    static int N;
    static int[][] dist;
    static boolean[] visited;
    static int minCost;
    static final int START_NODE = 0; // "Office 1" is index 0

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        if (sc.hasNextInt()) {
            int T = sc.nextInt();
            
            while (T-- > 0) {
                N = sc.nextInt();
                
                dist = new int[N][N];
                visited = new boolean[N];
                minCost = Integer.MAX_VALUE;
                
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        dist[i][j] = sc.nextInt();
                    }
                }
                
                // Start DFS from node 0 (Office 1)
                // We initially mark start node as visited
                visited[0] = true;
                dfs(0, 1, 0);
                
                System.out.println(minCost);
            }
        }
        sc.close();
    }

    // current: current city index
    // count: number of visited cities
    // cost: accumulated cost so far
    static void dfs(int current, int count, int cost) {
        // Pruning: if current cost already exceeds best found so far, stop.
        if (cost >= minCost) {
            return;
        }

        // Base case: all cities visited
        if (count == N) {
            // Check if there is a path back to start
            if (dist[current][START_NODE] > 0) {
                minCost = Math.min(minCost, cost + dist[current][START_NODE]);
            }
            return;
        }

        // Try visiting next cities
        for (int i = 0; i < N; i++) {
            // if not visited and there is a path
            if (!visited[i] && dist[current][i] > 0) {
                visited[i] = true;
                dfs(i, count + 1, cost + dist[current][i]);
                visited[i] = false; // backtrack
            }
        }
    }
}
