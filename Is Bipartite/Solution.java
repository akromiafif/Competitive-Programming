class Solution {
    class Pieces {
        int node;
        int color;

        public Pieces(int node, int color) {
            this.node = node;
            this.color = color;
        }
    }
    
    public boolean bfs(int[][] adjList, boolean[] visited, Queue<Pieces> queue, int[] color, int now) {
        queue.add(new Pieces(now, -1));
        visited[now] = true;
        color[now] = -1;

        while (!queue.isEmpty()) {
            Pieces current = queue.poll();

            for (int neighbor: adjList[current.node]) {
                if (!visited[neighbor]) {
                    if (color[neighbor] == 0) {
                        color[neighbor] = -1 * current.color;
                        visited[neighbor] = true;                      
                        queue.add(new Pieces(neighbor, -1 * current.color));
                    } 
                } else if (color[neighbor] == current.color) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isBipartite(int[][] graph) {
        int N = graph.length;

        Queue<Pieces> queue = new LinkedList<>();
        boolean[] visited = new boolean[N];
        int[] color = new int[N];

        for (int i=0; i<N; i++) {
            if (color[i] == 0) {
                if (!bfs(graph, visited, queue, color, i)) {
                    return false;
                }
            }
        }

        return true;
    }
}