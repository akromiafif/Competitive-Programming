import java.util.Scanner;
import java.util.Arrays;

public class Main {
    static int N;
    static int[] pipes;
    static int[][] memo;
    static int TOTAL_SUM = 0;
    static final int INF = 1000000000; // Use a large number effectively representing infinity

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            int T = sc.nextInt();
            while (T-- > 0) {
                N = sc.nextInt();
                pipes = new int[N];
                TOTAL_SUM = 0;
                for (int i = 0; i < N; i++) {
                    pipes[i] = sc.nextInt();
                    TOTAL_SUM += pipes[i];
                }

                // memo[index][diff]
                // index: 0 to N
                // diff: 0 to TOTAL_SUM
                memo = new int[N + 1][TOTAL_SUM + 1];
                for (int[] row : memo) {
                    Arrays.fill(row, -1);
                }

                // We start with pipe index 0 and difference 0.
                // The function returns the Max Height of the TALLER pillar achievable.
                // If diff becomes 0 at the end, Taller == Shorter == Answer.
                int result = dfs(0, 0);

                // If result is negative, it means no valid configuration was found.
                if (result <= 0) {
                    System.out.println(0);
                } else {
                    System.out.println(result);
                }
            }
        }
        sc.close();
    }

    // dfs returns the Maximum Height of the TALLER pillar given the current state.
    // idx: Current pipe index we are considering.
    // diff: The current difference between Pillar 1 and Pillar 2 (P1 - P2). 
    //       We always assume P1 is the taller one for the state, so diff >= 0.
    public static int dfs(int idx, int diff) {
        // Base case: If we have considered all pipes
        if (idx == N) {
            // If difference is 0, valid configuration. 
            // Return 0 because we don't add any MORE height from this point.
            if (diff == 0) return 0;
            // If difference is not 0, invalid configuration.
            return -INF;
        }

        // Return cached value if exists
        if (memo[idx][diff] != -1) {
            return memo[idx][diff];
        }

        int h = pipes[idx];

        // Option 1: Skip this pipe
        // Height of taller pillar doesn't increase from this pipe.
        int skip = dfs(idx + 1, diff);

        // Option 2: Add to Pillar 1 (The currently taller one)
        // P1 increases by h. Diff increases by h.
        // We add 'h' to the result because P1 (taller) grew by h.
        int addToP1 = h + dfs(idx + 1, diff + h);

        // Option 3: Add to Pillar 2 (The currently shorter one)
        // This is the tricky part. Two cases:
        // Case A: h <= diff. P2 is still shorter (or equal).
        //         P1 (taller) height does NOT change immediately.
        //         Diff decreases to (diff - h).
        //         Result = 0 + dfs(...)
        // Case B: h > diff. P2 becomes the NEW taller one.
        //         Old P1 height was H. New P2 height is (H - diff) + h.
        //         The new taller pillar is P2.
        //         The increase in "Max Taller Height" is (new P2) - (old P1)
        //         = (H - diff + h) - H = h - diff.
        //         New diff is h - diff.
        //         Result = (h - diff) + dfs(...)
        
        int addToP2;
        if (h <= diff) {
            // P1 remains taller
            int res = dfs(idx + 1, diff - h);
            if (res == -INF) addToP2 = -INF;
            else addToP2 = res; // No immediate height added to P1
        } else {
            // P2 becomes taller
            int res = dfs(idx + 1, h - diff);
            if (res == -INF) addToP2 = -INF;
            else addToP2 = (h - diff) + res; // Add the potential gain
        }

        // Take the maximum of all valid choices
        int max = Math.max(skip, Math.max(addToP1, addToP2));

        // Memoize and return
        memo[idx][diff] = max;
        return max;
    }
}
