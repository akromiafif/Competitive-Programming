public class AppleGameTest {

    /** Runs one case through both solutions and compares the turn count. */
    private static void runTestCase(String name, int[][] grid, boolean runBrute, int expected) {
        int rows = grid.length, cols = grid[0].length;
        System.out.println("Running " + name + "  (" + rows + "x" + cols + ")");

        int brute = 0;
        long t1 = 0;
        if (runBrute) {
            long start = System.currentTimeMillis();
            brute = AppleGameBrute.minRightTurns(grid);
            t1 = System.currentTimeMillis() - start;
        }

        long start = System.currentTimeMillis();
        int opt = AppleGame.minRightTurns(grid);
        long t2 = System.currentTimeMillis() - start;

        if (runBrute) {
            System.out.println("  Dijkstra : turns=" + brute + "  (" + t1 + " ms)");
        }
        System.out.println("  0-1 BFS  : turns=" + opt + "  (" + t2 + " ms)");

        boolean correct = true;
        if (runBrute) correct = (brute == opt);
        if (expected != Integer.MIN_VALUE) {
            correct = correct && (opt == expected);
            System.out.println("  Expected : " + expected);
        }
        System.out.println("  Status   : " + (correct ? "PASSED ✓" : "FAILED ✗"));
        System.out.println("-".repeat(50));
    }

    /** Deterministic random map: apples 1..M on distinct cells, plus a few traps. */
    private static int[][] randomMap(int n, int m, int traps, long seed) {
        int[][] grid = new int[n][n];
        long state = seed;
        // Pick distinct cells for apples 1..m (cell 0 = (0,0) is allowed).
        boolean[] used = new boolean[n * n];
        for (int apple = 1; apple <= m; apple++) {
            int cell;
            do {
                state = state * 6364136223846793005L + 1442695040888963407L;
                cell = (int) Math.floorMod(state >>> 16, (long) (n * n));
            } while (used[cell]);
            used[cell] = true;
            grid[cell / n][cell % n] = apple;
        }
        // Scatter traps on empty cells, never on (0,0).
        for (int t = 0; t < traps; t++) {
            state = state * 6364136223846793005L + 1442695040888963407L;
            int cell = (int) Math.floorMod(state >>> 16, (long) (n * n));
            if (cell != 0 && !used[cell] && grid[cell / n][cell % n] == 0) {
                grid[cell / n][cell % n] = -1;
                used[cell] = true;
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        System.out.println("=== Simple Test Cases (Dijkstra vs 0-1 BFS) ===");

        // Straight line, eat 1 then 2 going right: 0 turns.
        runTestCase("Straight line",
                new int[][]{{1, 0, 2}}, true, 0);

        // Apple 2 directly below: one right turn (face down) then go down.
        runTestCase("One turn down",
                new int[][]{
                        {1, 0, 0},
                        {0, 0, 0},
                        {2, 0, 0}}, true, 1);

        // Apple 1 boxed in by traps: cannot reach apple 2 -> -1.
        runTestCase("Boxed in (-1)",
                new int[][]{
                        {1, -1},
                        {-1, 2}}, true, -1);

        // Apple 3 sits BEFORE apple 2 on a single row: after eating 2 at the end
        // we cannot go back (no left turn / no reverse) -> -1.
        runTestCase("Out-of-reach order (-1)",
                new int[][]{{1, 3, 2}}, true, -1);

        System.out.println("=== Complex Test Cases (Dijkstra vs 0-1 BFS correctness) ===");
        int reachable = 0, impossible = 0;
        for (int i = 1; i <= 12; i++) {
            int n = 5 + (int) Math.floorMod(3L * i, 4); // 5..8
            int m = 2 + (int) Math.floorMod(2L * i, 4); // 2..5
            int traps = (int) Math.floorMod(5L * i, 6); // 0..5
            int[][] grid = randomMap(n, m, traps, 31L * i + 7);
            int opt = AppleGame.minRightTurns(grid);
            if (opt == -1) impossible++; else reachable++;
            runTestCase("Complex " + i + " (n=" + n + ", M=" + m + ")", grid, true, Integer.MIN_VALUE);
        }
        System.out.println("(reachable cases: " + reachable + ", impossible cases: " + impossible + ")");

        System.out.println("=== Extreme Test Case (0-1 BFS only, max grid) ===");
        // N = 59 (N < 60), M = 150 apples laid along a snake so they are eatable.
        int n = 59, m = 150;
        int[][] grid = new int[n][n];
        // Snake order over the grid: row 0 left->right, row 1 right->left, ...
        int placed = 0;
        for (int r = 0; r < n && placed < m; r++) {
            if (r % 2 == 0) {
                for (int c = 0; c < n && placed < m; c++) grid[r][c] = ++placed;
            } else {
                for (int c = n - 1; c >= 0 && placed < m; c--) grid[r][c] = ++placed;
            }
        }
        System.out.println("Running Extreme (N=59, M=150)");
        long s = System.currentTimeMillis();
        int turns = AppleGame.minRightTurns(grid);
        long dur = System.currentTimeMillis() - s;
        System.out.println("  0-1 BFS: turns=" + turns + "  (" + dur + " ms)");
        System.out.println("  Status : " + (dur < 1000 ? "PASSED (Under 1s)" : "WARNING: Exceeded 1s!"));
    }
}
