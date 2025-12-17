import java.util.*;

class Solution {
    
    // We use this to print the path once a cycle is found
    private void printCycle(int curr, int ancestor, int[] parent) {
        List<Integer> cyclePath = new ArrayList<>();
        
        // 1. Start from the current node and walk up parents until we hit the ancestor
        int temp = curr;
        while (temp != ancestor && temp != -1) {
            cyclePath.add(temp);
            temp = parent[temp];
        }
        cyclePath.add(ancestor);
        cyclePath.add(curr); // Add start node again to close the loop visually
        
        // 2. The path is currently backwards (Child -> Ancestor), so reverse it
        Collections.reverse(cyclePath);
        
        System.out.println("Cycle detected: " + cyclePath.toString());
    }

    private boolean dfs(int node, int par, ArrayList<ArrayList<Integer>> adjList, boolean[] visited, int[] parent) {
        visited[node] = true;
        parent[node] = par; // Record who brought us here
        
        for (Integer neighbor : adjList.get(node)) {
            if (neighbor == par) continue; // Don't go back to immediate parent
            
            if (visited[neighbor]) {
                // FOUND CYCLE: 'neighbor' is visited and is NOT our immediate parent.
                // Since it's DFS, 'neighbor' must be our ancestor.
                // printCycle(node, neighbor, parent);
                return true;
            }
            
            if (dfs(neighbor, node, adjList, visited, parent)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCycle(int V, int[][] edges) {
        boolean[] visited = new boolean[V];
        int[] parent = new int[V]; 
        Arrays.fill(parent, -1);
        
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < V; i++) adjList.add(new ArrayList<>());
        
        for (int i = 0; i < edges.length; i++) {
            adjList.get(edges[i][0]).add(edges[i][1]);
            adjList.get(edges[i][1]).add(edges[i][0]);
        }
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                // Start DFS from unvisited nodes
                if (dfs(i, -1, adjList, visited, parent)) {
                    return true;
                }
            }
        }
        return false;
    }
}