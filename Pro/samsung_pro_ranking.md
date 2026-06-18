# Samsung Pro Coding Questions – Difficulty Ranking (Easiest → Hardest)

> [!NOTE]
> Ranked by analyzing problem complexity, number of concepts required, and implementation difficulty. Problems marked with 🟢 are good warm-ups, 🟡 require solid DP/algo skills, and 🔴 are the hardest.

---

## Ranking Overview

| Rank | Problem | File | Category | Difficulty |
|------|---------|------|----------|------------|
| 1 | 🟢 Red-Blue Necklace | `3_RB.cpp` | Prefix Sum / HashMap | ⭐ |
| 2 | 🟢 Scores (Find Optimal D) | `5_2_scores.cpp` | Sorting + Binary Search | ⭐⭐ |
| 3 | 🟢 Min Subset Diff (Cargo) | `Min_Subset_Diff(2025_Test_2).cpp` | DP (Subset Sum) / Bitset | ⭐⭐ |
| 4 | 🟢 Stones Removal | `4_3_stones.cpp` | Linear DP | ⭐⭐ |
| 5 | 🟡 Segments (Points on Path) | `1_segments.cpp` | Geometry + Interval Merging | ⭐⭐⭐ |
| 6 | 🟡 Digit Sum Serial Numbers | `7_2_digit_sum.cpp` | Digit DP | ⭐⭐⭐ |
| 7 | 🟡 Robot Garbage Cleaning | `7_1_robot.cpp` | DP (Memoization) | ⭐⭐⭐ |
| 8 | 🟡 Tiles Selection | `4_2_tiles.cpp` | 2D Prefix Sum + Binary Search | ⭐⭐⭐ |
| 9 | 🟡 Warehouse Days | `2_days.cpp` | Backtracking / Greedy + DP | ⭐⭐⭐⭐ |
| 10 | 🟡 String Merging | `5_1_strings.cpp` | DP on Sequences | ⭐⭐⭐⭐ |
| 11 | 🟡 Soldiers (Tree Balancing) | `soldiers.cpp` | Tree DP (DFS) | ⭐⭐⭐⭐ |
| 12 | 🔴 Cars Parking | `6_1_cars.cpp` | Math + Parity + Binary Search | ⭐⭐⭐⭐ |
| 13 | 🔴 Min Cost (Tree Rerooting) | `min_cost.cpp` | Rerooting DP on Trees | ⭐⭐⭐⭐⭐ |
| 14 | 🔴 Logging Trees (Robot) | `logging_trees.cpp` | Complex DP + Greedy | ⭐⭐⭐⭐⭐ |
| 15 | 🔴 Apple Game | `apples.cpp` | 0-1 BFS + Multi-dim DP | ⭐⭐⭐⭐⭐ |
| 16 | 🔴 Warehouse Truck | `4_1_warehouse.cpp` | BFS + Bitmask DP | ⭐⭐⭐⭐⭐ |

---

## Detailed Breakdown

### 🟢 Rank 1 – Red-Blue Necklace (`3_RB.cpp`) ⭐
**Problem**: Given a necklace of R and B stones, remove minimum stones from ends to make R count = B count.
**Key Insight**: Find the longest subarray with equal R and B → answer is `N - longestLen`. Classic prefix sum + hashmap trick.
**Concepts**: Prefix sum, HashMap
**Why easiest**: Single-pass O(N), no DP needed, well-known pattern.

---

### 🟢 Rank 2 – Scores (`5_2_scores.cpp`) ⭐⭐
**Problem**: Given arrays A and B, find threshold D to maximize `score(A) - score(B)` where score gives 1 for elements ≤ D and 2 for elements > D.
**Key Insight**: Only candidate D values matter (the actual element values). Sort both arrays, then for each candidate D use binary search to count elements.
**Concepts**: Sorting, Binary Search, Sweep
**Why easy**: Straightforward greedy/sweep approach, no DP.

---

### 🟢 Rank 3 – Min Subset Diff / Cargo (`Min_Subset_Diff(2025_Test_2).cpp`) ⭐⭐
**Problem**: Divide N items into two groups minimizing `|sum(A) - sum(B)|`.
**Key Insight**: Classic subset-sum DP. Use bitset for O(N × Sum) or memoized recursion.
**Concepts**: Subset Sum DP, Bitset optimization
**Why easy**: Textbook DP problem, well-documented pattern.

---

### 🟢 Rank 4 – Stones Removal (`4_3_stones.cpp`) ⭐⭐
**Problem**: Remove stones in a line with costs depending on neighbor count. Find minimum total cost.
**Key Insight**: Linear DP with 2 states per stone (removed before or after its neighbor).
**Concepts**: Linear DP, State design
**Why easy**: Only 2 states, O(N) time, clean transitions.

---

### 🟡 Rank 5 – Segments / Points on Path (`1_segments.cpp`) ⭐⭐⭐
**Problem**: Given a 2D lattice path and N query points, count how many points lie on the path.
**Key Insight**: Decompose path into horizontal/vertical segments, merge overlapping ones, then binary search for each query point.
**Concepts**: Geometry, Interval merging, Binary search on sets
**Why medium**: Multiple steps (segment decomposition + merging + lookup), but no DP.

---

### 🟡 Rank 6 – Digit Sum Serial Numbers (`7_2_digit_sum.cpp`) ⭐⭐⭐
**Problem**: Count numbers from 1 to A whose digit sum equals S. A can be up to 10^100.
**Key Insight**: Classic Digit DP with states `(position, remaining_sum, tight_bound)`.
**Concepts**: Digit DP, Memoization, Modular arithmetic
**Why medium**: Digit DP is a known pattern but requires careful implementation of the tight constraint.

---

### 🟡 Rank 7 – Robot Garbage Cleaning (`7_1_robot.cpp`) ⭐⭐⭐
**Problem**: Deploy robots to clean garbage array. Robot costs `m` to deploy, can move right. At each step, cost = remaining uncleaned garbage. Minimize total cost.
**Key Insight**: DP with states `(index, last_robot_position)`. At each index, decide whether to deploy a new robot or continue with current.
**Concepts**: DP with memoization, Cost optimization
**Why medium**: State space is manageable, but formulating the cost correctly is tricky.

---

### 🟡 Rank 8 – Tiles Selection (`4_2_tiles.cpp`) ⭐⭐⭐
**Problem**: Select K tiles from N, minimizing the maximum difference (defined as max of height diff and width diff between any two selected tiles).
**Key Insight**: Binary search on the answer + 2D prefix sum to check if K tiles fit in a square window of size `mid`.
**Concepts**: Binary Search on Answer, 2D Prefix Sum
**Why medium**: Combining binary search with 2D prefix sums requires solid understanding of both.

---

### 🟡 Rank 9 – Warehouse Days (`2_days.cpp`) ⭐⭐⭐⭐
**Problem**: Warehouse with stocks A[i] that grow by B[i] daily. Each day you can export one item (set it to 0). Find min days to get total stock ≤ K.
**Key Insight**: Brute-force backtracking (choosing which items to export on which days). Can be optimized with sorting by growth rate.
**Concepts**: Backtracking / Greedy + DP
**Why harder**: Exponential brute force without clever pruning; needs careful optimization to pass within time limits.

---

### 🟡 Rank 10 – String Merging (`5_1_strings.cpp`) ⭐⭐⭐⭐
**Problem**: Merge strings in order where last char of previous = first char of next. Final string must have first char = last char. Maximize length.
**Key Insight**: DP with states `(index, start_digit, end_digit)`. Track which digit the chain started with and currently ends with.
**Concepts**: DP on sequences, String manipulation, State compression (10×10 digit pairs)
**Why harder**: 3D DP state space with non-trivial transition logic.

---

### 🟡 Rank 11 – Soldiers / Tree Balancing (`soldiers.cpp`) ⭐⭐⭐⭐
**Problem**: Balance a tree so all siblings have equal subtree sums by only removing soldiers. Minimize removals.
**Key Insight**: DFS from leaves up. At each parent, set all children's subtree sums to the minimum among them.
**Concepts**: Tree DP, DFS, Greedy on trees
**Why harder**: Requires understanding tree recursion and the constraint that you can only delete (not add).

---

### 🔴 Rank 12 – Cars Parking (`6_1_cars.cpp`) ⭐⭐⭐⭐
**Problem**: Move N cars simultaneously to a target point. Drive k moves k distance on turn k. Find minimum drives.
**Key Insight**: Manhattan distances + parity analysis. All cars must have the same parity of distance. Use triangular number formula to find minimum turns, then adjust by parity.
**Concepts**: Math (triangular numbers), Parity, Binary search
**Why hard**: The parity trick is non-obvious and requires mathematical reasoning.

---

### 🔴 Rank 13 – Min Cost / Tree Rerooting (`min_cost.cpp`) ⭐⭐⭐⭐⭐
**Problem**: Choose vertex v in a weighted tree to maximize `Σ(d_i × a_i)` where d_i is distance from v to vertex i.
**Key Insight**: Rerooting technique. First DFS to compute answer rooted at node 1, then re-root to every other node in O(1) per transition.
**Concepts**: Tree DP, Rerooting DP, DFS
**Why very hard**: Rerooting DP is an advanced technique. The state transitions during re-rooting are error-prone.

---

### 🔴 Rank 14 – Logging Trees (`logging_trees.cpp`) ⭐⭐⭐⭐⭐
**Problem**: Robot moves along a road chopping trees (sorted by height, must load in non-increasing order). Minimize total time (movement + chopping).
**Key Insight**: Group trees by height. For each height level, decide whether to approach from the left or right end. DP with states `(height_level, current_position)`.
**Concepts**: Complex DP, Greedy grouping, Interval processing
**Why very hard**: The ordering constraint (non-increasing height) combined with bidirectional movement makes the state space complex. Long problem statement with many rules.

---

### 🔴 Rank 15 – Apple Game (`apples.cpp`) ⭐⭐⭐⭐⭐
**Problem**: Navigate an N×N grid eating apples in order (1, 2, 3...) with only right turns allowed. Minimize right turns.
**Key Insight**: 0-1 BFS (deque-based) with 4D state `(x, y, apples_eaten, direction)`. Moving forward costs 0, turning right costs 1.
**Concepts**: 0-1 BFS, Multi-dimensional DP/BFS, State space exploration
**Why very hard**: 4D state space (`60 × 60 × 150 × 4`), combining grid traversal with sequential constraints and restricted movement. The 0-1 BFS optimization is essential.

---

### 🔴 Rank 16 – Warehouse Truck (`4_1_warehouse.cpp`) ⭐⭐⭐⭐⭐
**Problem**: Truck navigates a grid from garage → warehouses → airport. Cost depends on distance × (1 + goods carried). Maximize goods delivered within budget C.
**Key Insight**: BFS from garage and airport to precompute distances. Then use bitmask DP over up to 13 warehouses to find optimal visiting order.
**Concepts**: BFS, Bitmask DP, Multi-source shortest path
**Why hardest**: Combines BFS graph traversal with bitmask DP (2^13 states). The cost function depends on the number of goods currently carried, adding another dimension.
