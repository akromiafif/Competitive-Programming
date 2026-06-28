import java.util.ArrayList;
import java.util.List;

/**
 * Soldiers – Brute Force (faithful recursive reference DFS)
 *
 * Idea (exactly the reference C++ recurrence):
 *   Recurse to every child to get its balanced subtree sum. Then TRY each child's
 *   sum as the common "target" value: a target is feasible only if no sibling is
 *   smaller than it (we cannot add soldiers), and its cost is the sum of the
 *   per-sibling reductions. Keep the cheapest feasible target. The node's
 *   remaining total is its own soldiers plus target * childCount.
 *
 * It makes no shortcut (the inner double loop is O(deg^2)) — it just plays out the
 * stated balancing rule — which is why it is a trustworthy oracle for the O(N)
 * solution on small trees. (The only feasible target is always the minimum child
 * sum, which is exactly what the optimal exploits.)
 *
 * Complexity: O(sum of deg^2) and recursive — fine for small N, but a wide or
 *   deep tree will blow up or overflow the stack, hence "brute".
 */
public class SoldiersBrute {

    public static long minRemaining(int[] parent, int[] soldiers) {
        int n = parent.length;
        if (n == 0) return 0;

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) children.add(new ArrayList<>());
        int root = -1;
        for (int i = 0; i < n; i++) {
            if (parent[i] == -1) root = i;
            else children.get(parent[i]).add(i);
        }
        return dfs(root, children, soldiers);
    }

    private static long dfs(int u, List<List<Integer>> children, int[] soldiers) {
        List<Integer> kids = children.get(u);
        if (kids.isEmpty()) return soldiers[u];

        long[] childSums = new long[kids.size()];
        for (int i = 0; i < kids.size(); i++) {
            childSums[i] = dfs(kids.get(i), children, soldiers);
        }

        long bestCost = Long.MAX_VALUE;
        long keptChildren = 0;
        for (long target : childSums) {
            long cost = 0;
            boolean feasible = true;
            for (long s : childSums) {
                if (s < target) { feasible = false; break; } // cannot increase a sibling
                cost += s - target;
            }
            if (feasible && cost < bestCost) {
                bestCost = cost;
                keptChildren = target * kids.size();
            }
        }
        return soldiers[u] + keptChildren;
    }
}
