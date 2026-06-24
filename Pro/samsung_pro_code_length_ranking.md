# Samsung Pro Questions – Ranked by Code Simplicity
*(From Shortest/Least Typing to Longest/Heaviest Implementation)*

> [!NOTE]
> This ranking **ignores conceptual difficulty**. A problem ranked "Shortest Code" might take hours to figure out the math, but only 5 minutes to type. Problems at the bottom require heavy typing (BFS, graph structures, massive arrays).

---

## ⌨️ Level 1: Minimalist (Under 20 Lines of Core Logic)

| Rank | Problem | File | Why the code is so short |
|------|---------|------|--------------------------|
| 1 | **Red-Blue Necklace** | `3_RB.cpp` | Single `for` loop updating a HashMap. Pure prefix sum. |
| 2 | **Cars Parking** | `6_1_cars.cpp` | Just a math formula (square roots) and a parity check. Almost zero logic structures. |
| 3 | **Digit Sum Serial** | `7_2_digit_sum.cpp` | A single, clean 10-line recursive function. (Though finding the logic is hard!) |
| 4 | **Scores (Optimal D)** | `5_2_scores.cpp` | Sort two arrays, run `upper_bound` binary search. Done. |
| 5 | **Min Subset Diff** | `Min_Subset_Diff(2025_Test_2).cpp` | Extremely compact using C++ `bitset` shifts or a 15-line recursion. |

---

## 💻 Level 2: Standard (20 - 40 Lines of Core Logic)

| Rank | Problem | File | Why it takes a bit more typing |
|------|---------|------|--------------------------------|
| 6 | **Robot Garbage** | `7_1_robot.cpp` | Standard 2D memoized recursion. Just a few `if/else` checks. |
| 7 | **Stones Removal** | `4_3_stones.cpp` | Simple linear loop, but has a few specific state transitions to write out. |
| 8 | **Soldiers** | `soldiers.cpp` | A single Tree DFS function, but requires iterating over children and calculating minimums. |
| 9 | **Min Cost (Rerooting)** | `min_cost.cpp` | Two separate DFS functions (`precalc` and `reroot`). |
| 10 | **String Merging** | `5_1_strings.cpp` | Parsing strings, extracting first/last chars, and updating a 3D DP array. |

---

## 🛠️ Level 3: Heavy (40 - 70 Lines of Core Logic)

| Rank | Problem | File | Why it requires heavy typing |
|------|---------|------|------------------------------|
| 11 | **Segments** | `1_segments.cpp` | You have to write logic to separate horizontal/vertical lines, merge overlapping intervals, and binary search points. |
| 12 | **Tiles Selection** | `4_2_tiles.cpp` | You must build a 2D Prefix Sum array manually, then write a binary search over 2D window sizes. |
| 13 | **Warehouse Days** | `2_days.cpp` | Full recursive backtracking function with state management (tracking exports over days). |
| 14 | **Apple Game** | `apples.cpp` | Requires setting up a `deque` for 0-1 BFS, a 4-dimensional state array (`dp[x][y][apples][dir]`), and bounds checking. |

---

## 🏗️ Level 4: Enterprise (80+ Lines of Core Logic)

| Rank | Problem | File | Why it's a typing marathon |
|------|---------|------|---------------------------|
| 15 | **Logging Trees** | `logging_trees.cpp` | Requires grouping items into `map<int, vector<int>>`, sorting each group, calculating distances, and a complex bi-directional recursive DP. |
| 16 | **Warehouse Truck** | `4_1_warehouse.cpp` | You must write a BFS to precompute shortest paths on a grid, AND write a Bitmask DP to test all permutations of visiting 13 warehouses. Easily 100+ lines. |
