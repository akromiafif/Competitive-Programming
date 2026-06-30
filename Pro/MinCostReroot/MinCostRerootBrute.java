import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Min Cost (Tree Rerooting) – Brute Force
 *
 * Idea:
 *   Try EVERY vertex as the chosen vertex. For each one, BFS the whole tree to
 *   get the distance to every other vertex, then sum dist(v,i)*value[i]. Keep
 *   the maximum. No rerooting, no cleverness — it just measures every choice
 *   directly, which makes it a trustworthy oracle on small trees.
 *
 * Complexity: O(n^2) (a full BFS from each of the n vertices).
 */
public class MinCostRerootBrute {

    public static long maxWeightedDistance(long[] values, int[][] edges) {
        int n = values.length;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        long best = Long.MIN_VALUE;
        for (int source = 0; source < n; source++) {
            long[] dist = new long[n];
            Arrays.fill(dist, -1);
            dist[source] = 0;

            ArrayDeque<Integer> queue = new ArrayDeque<>();
            queue.add(source);
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int w : adj.get(u)) {
                    if (dist[w] == -1) {
                        dist[w] = dist[u] + 1;
                        queue.add(w);
                    }
                }
            }

            long sum = 0;
            for (int i = 0; i < n; i++) sum += dist[i] * values[i];
            best = Math.max(best, sum);
        }
        return best;
    }
}
