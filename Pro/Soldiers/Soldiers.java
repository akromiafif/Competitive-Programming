import java.util.ArrayList;
import java.util.List;

/**
 * Soldiers – Tree Balancing (simple recursive DFS)
 *
 * Problem:
 *   Each tree node is a unit holding some soldiers. Balance the tree so all
 *   children of the same parent have the SAME subtree sum, by only DELETING
 *   soldiers, removing as few as possible. Return the soldiers remaining.
 *
 * One-formula insight (everything you need to memorize):
 *   Solve bottom-up. For a node u with children c1..ck, each child reports its
 *   balanced subtree sum. Siblings must become equal and we can only shrink, so
 *   the common value is the SMALLEST child sum. Hence
 *       remaining(leaf) = soldiers[leaf]
 *       remaining(u)    = soldiers[u] + min(remaining(child)) * childCount
 *   The answer is remaining(root).
 *
 * Complexity: O(N). Recursion depth = tree depth (<= 100 by the constraints).
 */
public class Soldiers {

    /**
     * @param parent   parent[i] = parent index of node i, or -1 for the root
     * @param soldiers soldiers[i] = soldiers in node i (non-negative)
     * @return total soldiers remaining after balancing
     */
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

    /** Remaining soldiers in u's subtree after balancing. */
    private static long dfs(int u, List<List<Integer>> children, int[] soldiers) {
        List<Integer> kids = children.get(u);
        if (kids.isEmpty()) return soldiers[u];          // leaf: nothing to balance

        long minChild = Long.MAX_VALUE;
        for (int c : kids)
            minChild = Math.min(minChild, dfs(c, children, soldiers));

        return soldiers[u] + minChild * kids.size();     // shrink every child to the smallest
    }
}
