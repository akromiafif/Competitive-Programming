# Mr. Lee's Travel Problem - Simply Explained

Imagine Mr. Lee is a **delivery driver** who lives at **Location #1** (his company).
He has a list of other locations (offices) he *must* visit to deliver packages.

## The Rules
1.  **Start at Home**: He starts at Location #1.
2.  **Visit Everyone Once**: He must go to *every* other location exactly one time. He cannot skip any, and he cannot go to the same place twice.
3.  **Return Home**: After he has visited the last location, he must fly back to Location #1 to finish his trip.
4.  **Save Money**: Every flight between two locations costs a certain amount of money. He wants to pick the order of visits so that the **total cost is as low as possible**.

---

## Understanding the "Map" (The Input Matrix)
The problem gives you a grid of numbers. This is a "lookup table" for flight prices.

**Example Grid (N=3 spots):**
```
   1   2   3  (To)
1  0  10  50
2  10  0  20
3  50 20   0
(From)
```
*   **Row 1 represents flying FROM Location 1.**
    *   To Location 2 costs $10.
    *   To Location 3 costs $50.
*   **Row 2 represents flying FROM Location 2.**
    *   To Location 1 costs $10.
    *   To Location 3 costs $20.

**Zeros represent current location or impossible paths.** You can't fly from 1 to 1. Sometimes a `0` means "No flight exists", so you *can't* take that path.

---

## Walkthrough Example
Let's say there are 3 locations: **Home(1)**, **A(2)**, and **B(3)**.

Mr. Lee is at **Home(1)**.
He needs to visit **A(2)** and **B(3)** and then come back to **Home(1)**.

### Option 1: Go to A first
1.  Home(1) -> A(2) : Cost $10
2.  Now he is at A(2). He hasn't visited B(3) yet. So he MUST go to B(3).
3.  A(2) -> B(3) : Cost $20
4.  Now he has visited everyone (Home, A, B). He must return Home.
5.  B(3) -> Home(1) : Cost $50
**Total Cost:** $10 + $20 + $50 = **$80**

### Option 2: Go to B first
1.  Home(1) -> B(3) : Cost $50
2.  Now he is at B(3). He hasn't visited A(2) yet. So he MUST go to A(2).
3.  B(3) -> A(2) : Cost $20 (Assuming symmetric cost for this example)
4.  Now he has visited everyone. He must return Home.
5.  A(2) -> Home(1) : Cost $10
**Total Cost:** $50 + $20 + $10 = **$80**

In this simple example, both are $80. But usually, one way is cheaper!

### Why is this hard?
If there are 4 cities, he has more choices:
*   1 -> 2 -> 3 -> 4 -> 1
*   1 -> 2 -> 4 -> 3 -> 1
*   1 -> 3 -> 2 -> 4 -> 1
*   ...and so on.

The computer must check these valid combinations to find the cheapest one. We use "Depth First Search" (DFS) to simulate him trying every possible valid path and remembering the cheapest one found.

---

## Summary of constraints
*   **Input N**: Number of cities (e.g., 5).
*   **1 -> 2**: Flight cost from city 1 to city 2.
*   **0 cost**: Usually means no flight exists (cannot go that way).
*   **Goal**: Find the number representing the minimum money spent.
