# Wormhole Problem Breakdown

## 1. The Core Objective
You are a spaceship captain in a 2D grid universe.
- **Start**: You begin at coordinates $(S_x, S_y)$.
- **End**: You want to reach $(D_x, D_y)$.
- **Goal**: Minimize the total cost (time/distance) of the trip.

## 2. Movement Rules
There are two ways to move:
1. **Regular Flight (Manhattan Distance)**:
   - Moving from $(x_1, y_1)$ to $(x_2, y_2)$ costs $|x_1 - x_2| + |y_1 - y_2|$.
   - *Example*: Moving from $(0,0)$ to $(3,4)$ costs $|0-3| + |0-4| = 3 + 4 = 7$.

2. **Wormholes (Shortcuts)**:
   - There are $N$ wormholes available.
   - Each wormhole connects two points: Entrance $(e_x, e_y)$ and Exit $(o_x, o_y)$.
   - **Cost**: Passing through the wormhole costs a fixed amount $C$.
   - **Bi-directional**: You can enter at the "Exit" and come out at the "Entrance" for the same cost.
   - **Usage**: You must first "Fly" to the wormhole entrance, then pay the wormhole cost, then you appear at the exit.

## 3. Strategy
The problem is about finding the optimal sequence of wormholes to use (or none at all).

### Decision Making
At your current position, you always have $2N + 1$ choices:
1. **Go straight to Destination**: Cost = `Dist(Current, Dest)`.
2. **Go to Wormhole 1 (End A)**: Cost = `Dist(Current, W1_A) + W1_Cost`. New Pos: `W1_B`.
3. **Go to Wormhole 1 (End B)**: Cost = `Dist(Current, W1_B) + W1_Cost`. New Pos: `W1_A`.
...
4. **Go to Wormhole N (End A)**: Cost = `Dist(Current, WN_A) + WN_Cost`. New Pos: `WN_B`.

Since $N$ is small (usually around 10), we can use **Recursion (Backtracking)** to try all relevant paths.

### Correctness Check
- **Why Recursion works**: We need to find the *global* minimum. A greedy approach (always taking the closest wormhole) might fail if that wormhole leads you further away or to a dead end. Backtracking explores all meaningful combinations.
- **Pruning**: If the cost of the current path already exceeds the best cost found so far, we stop exploring that branch (`if (current_cost >= min_cost) return`).

## 4. Visual Example
Imagine:
- Source: $(0,0)$
- Destination: $(100, 100)$
- **Direct Path**: $|100-0| + |100-0| = 200$.

Now add a Wormhole:
- Ends: $(10, 10)$ and $(90, 90)$
- Cost: $5$

**Option A (Direct)**: Cost = 200.
**Option B (Using Wormhole)**:
1. Fly S$(0,0) \to (10,10)$: Cost $10+10 = 20$.
2. Take Wormhole $(10,10) \to (90,90)$: Cost $5$.
3. Fly $(90,90) \to$ End$(100,100)$: Cost $10+10 = 20$.
   
**Total Cost**: $20 + 5 + 20 = 45$.
**Winner**: Option B.

## 5. Constraints & Complexity
- **Time Limit**: 1 Second.
- **N**: Small (~10).
- **Complexity**: roughly $O(N! \cdot 2^N)$.
  - For $N=10$, this is high but manageable with pruning because most paths are obviously bad.
  - The number of visited wormholes in an optimal path is rarely the full set.
