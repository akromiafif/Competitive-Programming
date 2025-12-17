# Wormhole Problem

## Description
There is one spaceship at a source coordinate $(S_x, S_y)$. The destination is at $(D_x, D_y)$.
There are $N$ wormholes using coordinates $(x1, y1)$ and $(x2, y2)$.
- Wormholes are **bi-directional**. You can enter at $(x1, y1)$ and exit at $(x2, y2)$, or enter at $(x2, y2)$ and exit at $(x1, y1)$.
- Passing through a wormhole costs a specific value $C$.
- Traveling between any two points $(x_a, y_a)$ and $(x_b, y_b)$ without a wormhole costs the Manhattan distance: $|x_a - x_b| + |y_a - y_b|$.

## Goal
Find the minimum cost (time/distance) to reach the destination from the source.
You can use any number of wormholes (or none).

## Input Format
1. $T$ (Number of test cases)
2. For each test case:
    - $N$ (Number of wormholes)
    - Source and Destination: $S_x, S_y, D_x, D_y$
    - $N$ lines, each with 5 integers: $x1, y1, x2, y2, Cost$

## Constraints
- Time Limit: 1 second
- $N$ is usually small (around 10-12) to allow backtracking solutions.
