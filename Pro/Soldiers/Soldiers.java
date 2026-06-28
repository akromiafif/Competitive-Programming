import java.util.Arrays;

/**
 * Soldiers – Optimal Solution (single O(N) iterative tree DFS)
 *
 * Problem:
 *   Each tree node is a military unit holding some soldiers. Balance the tree so
 *   that all children of the same parent have the SAME subtree sum, removing the
 *   minimum total number of soldiers (we may only DELETE soldiers). Return the
 *   total soldiers remaining after balancing.
 *
 * Key Insight:
 *   Balance bottom-up. For a node u with children c1..ck, each child contributes
 *   its already-balanced subtree sum r(ci). Siblings must become equal and we can
 *   only shrink, so the common value can be at most min(r(ci)) — and matching it
 *   to exactly that minimum removes the fewest soldiers. Hence
 *       r(u) = soldiers[u] + (k == 0 ? 0 : min(r(ci)) * k).
 *   The answer is r(root). (This is the model of the reference solution: a parent
 *   reduces every child down to the smallest sibling subtree sum.)
 *
 *   The reference computes that minimum with an O(deg^2) double loop; it is just
 *   min(childSums), so a single pass per node gives O(N) overall. We also run the
 *   DFS ITERATIVELY (explicit stack, reverse-preorder accumulation) so a chain of
 *   a million nodes cannot overflow the call stack.
 *
 * Complexity: O(N) time, O(N) space — handles N up to 1,000,000 under 1s.
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

        // Children count per node + locate the root.
        int root = -1;
        int[] childCount = new int[n];
        for (int i = 0; i < n; i++) {
            if (parent[i] == -1) root = i;
            else childCount[parent[i]]++;
        }

        // Compressed adjacency (CSR): childList[childStart[u] .. childStart[u+1]).
        int[] childStart = new int[n + 1];
        for (int i = 0; i < n; i++) childStart[i + 1] = childStart[i] + childCount[i];
        int[] childList = new int[childStart[n]];
        int[] cursor = new int[n];
        for (int i = 0; i < n; i++) {
            int p = parent[i];
            if (p != -1) childList[childStart[p] + cursor[p]++] = i;
        }

        // Preorder traversal with an explicit stack (parent appears before children).
        int[] preorder = new int[n];
        int[] stack = new int[n];
        int top = 0, written = 0;
        stack[top++] = root;
        while (top > 0) {
            int u = stack[--top];
            preorder[written++] = u;
            for (int e = childStart[u]; e < childStart[u + 1]; e++) stack[top++] = childList[e];
        }

        // Walk preorder backwards: every node's children are handled before it.
        long[] remaining = new long[n];
        long[] minChild = new long[n];
        Arrays.fill(minChild, Long.MAX_VALUE);
        for (int idx = n - 1; idx >= 0; idx--) {
            int u = preorder[idx];
            long r = soldiers[u] + (childCount[u] == 0 ? 0L : minChild[u] * childCount[u]);
            remaining[u] = r;
            int p = parent[u];
            if (p != -1 && r < minChild[p]) minChild[p] = r;
        }

        return remaining[root];
    }
}
