import java.util.ArrayList;
import java.util.List;

/**
 * Min Cost (Tree Rerooting)
 *
 * Problem:
 *   Tree with n vertices, vertex i has value a[i]. Pick a vertex v to MAXIMISE
 *       f(v) = sum over all i of  dist(v, i) * a[i]
 *   where dist is the number of edges between v and i. Print the maximum f(v).
 *
 * Rerooting in two passes (the whole trick):
 *   Root the tree at 0. Let
 *       subtreeSum[u] = sum of a[i] for i in u's subtree
 *       total         = sum of all a[i]
 *   Pass 1 (down / post-order): compute subtreeSum[u] and f(0) directly.
 *       A vertex in child c's subtree is 1 edge farther from u than from c, so
 *       f over u's subtree = sum over children c of ( f_subtree(c) + subtreeSum[c] ).
 *   Pass 2 (reroot / pre-order): slide the root from u to a neighbour c.
 *       Everything in c's subtree gets 1 CLOSER  -> -subtreeSum[c]
 *       Everything else        gets 1 FARTHER -> +(total - subtreeSum[c])
 *       =>  f[c] = f[u] + total - 2 * subtreeSum[c].
 *   Answer = max f over all vertices.
 *
 * Complexity: O(n) time and space.
 * Note: recursive; depth = tree depth (fine for shallow/balanced trees, the real
 *   constraint here). A million-deep chain would need an explicit stack.
 */
public class MinCostReroot {

    private static List<List<Integer>> adj;
    private static long[] value;
    private static long[] subtreeSum; // sum of values in each subtree (rooted at 0)
    private static long[] answer;     // answer[u] = sum_i dist(u,i) * value[i]
    private static long total;

    /**
     * @param values  value on each vertex (0-indexed)
     * @param edges   n-1 undirected edges as {u, v} pairs (0-indexed)
     * @return the maximum achievable sum_i dist(v,i)*value[i]
     */
    public static long maxWeightedDistance(long[] values, int[][] edges) {
        int n = values.length;
        value = values;

        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        total = 0;
        for (long v : values) total += v;

        subtreeSum = new long[n];
        answer = new long[n];

        answer[0] = down(0, -1); // pass 1: subtree sums + answer at the root
        reroot(0, -1);           // pass 2: slide the root to every vertex

        long best = Long.MIN_VALUE;
        for (long a : answer) best = Math.max(best, a);
        return best;
    }

    /** Post-order: fill subtreeSum[u]; return sum_i dist(u,i)*value[i] over u's subtree. */
    private static long down(int u, int parent) {
        subtreeSum[u] = value[u];
        long subtot = 0;
        for (int c : adj.get(u)) {
            if (c == parent) continue;
            long childSubtot = down(c, u);
            subtreeSum[u] += subtreeSum[c];
            subtot += childSubtot + subtreeSum[c]; // +subtreeSum[c]: one edge farther from u than c
        }
        return subtot;
    }

    /** Pre-order: f[child] = f[u] + total - 2*subtreeSum[child]. */
    private static void reroot(int u, int parent) {
        for (int c : adj.get(u)) {
            if (c == parent) continue;
            answer[c] = answer[u] + total - 2 * subtreeSum[c];
            reroot(c, u);
        }
    }
}
