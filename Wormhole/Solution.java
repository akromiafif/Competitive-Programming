package Wormhole;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution {
    static int ANS = Integer.MAX_VALUE;
    static int n;
    static int[][] w = new int[35][5];
    static boolean[] mask = new boolean[35];

    static int abs(int i) {
        return (i >= 0) ? i : -i;
    }

    static int dist(int sX, int sY, int tX, int tY) {
        return abs(sX - tX) + abs(sY - tY);
    }

    static void solve(int sX, int sY, int tX, int tY, int value) {
        // Optimization: Pruning
        // If current cost is already greater than the best answer found so far, return.
        // Also check if current direct path is better.
        
        int directCost = dist(sX, sY, tX, tY);
        if (value + directCost < ANS) {
            ANS = value + directCost;
        }
        
        // If current value is already worse than or equal to ANS, no need to explore deeper
        // unless we can find a "negative edge" equivalent, but costs are positive here.
        if (value >= ANS) return;

        for (int i = 0; i < n; i++) {
            if (!mask[i]) {
                mask[i] = true;

                // Option 1: Enter at (x1, y1), Exit at (x2, y2)
                int costToEnter1 = dist(sX, sY, w[i][0], w[i][1]);
                // Only proceed if this path is potentially better
                if (value + costToEnter1 + w[i][4] < ANS) {
                    solve(w[i][2], w[i][3], tX, tY, value + costToEnter1 + w[i][4]);
                }

                // Option 2: Enter at (x2, y2), Exit at (x1, y1)
                int costToEnter2 = dist(sX, sY, w[i][2], w[i][3]);
                if (value + costToEnter2 + w[i][4] < ANS) {
                    solve(w[i][0], w[i][1], tX, tY, value + costToEnter2 + w[i][4]);
                }

                mask[i] = false; // Backtrack
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        if (!st.hasMoreTokens()) return;
        int T = Integer.parseInt(st.nextToken());
        
        long totalStartTime = System.currentTimeMillis();

        for (int t = 1; t <= T; t++) {
            ANS = Integer.MAX_VALUE;
            
            // Handle potentially empty lines or formatting issues
            String line = br.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = br.readLine();
            }
            if (line == null) break;
            
            st = new StringTokenizer(line);
            n = Integer.parseInt(st.nextToken());
            
            st = new StringTokenizer(br.readLine());
            int sX = Integer.parseInt(st.nextToken());
            int sY = Integer.parseInt(st.nextToken());
            int tX = Integer.parseInt(st.nextToken());
            int tY = Integer.parseInt(st.nextToken());
            
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                w[i][0] = Integer.parseInt(st.nextToken()); // x1
                w[i][1] = Integer.parseInt(st.nextToken()); // y1
                w[i][2] = Integer.parseInt(st.nextToken()); // x2
                w[i][3] = Integer.parseInt(st.nextToken()); // y2
                w[i][4] = Integer.parseInt(st.nextToken()); // cost
                mask[i] = false;
            }
            
            long startTime = System.nanoTime();
            solve(sX, sY, tX, tY, 0);
            long endTime = System.nanoTime();
            
            System.out.println("Case #" + t + ": " + ANS);
            // System.out.println("Time: " + (endTime - startTime) / 1e6 + " ms");
        }
        
        long totalEndTime = System.currentTimeMillis();
        // System.out.println("Total Execution Time: " + (totalEndTime - totalStartTime) + " ms");
    }
}
