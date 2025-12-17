# Piller Pipe Problem - Simply Explained

Imagine you are a construction worker with a pile of **steel pipes** of different lengths.
You want to weld them together to build **two pillars** standing side-by-side.

## The Goal
1.  **Twin Towers**: The two pillars must be exactly the **Same Height**.
2.  **Max Height**: You want these twin pillars to be **as tall as possible**.
3.  **Leftovers Allowed**: You don't have to use every single pipe. You can throw some away if they don't fit your plan.

---

## Example
You have pipes with lengths: **1, 2, 3, 4, 6**.

### Attempt 1: Make height 6?
*   **Pillar A**: Use pipe **6**. (Height = 6)
*   **Pillar B**: Use pipes **2 + 4**. (Height = 6)
*   **Result**: Success! We made twin pillars of height 6. (Leftover: 1, 3)

### Attempt 2: Can we go higher? Make height 8?
*   **Pillar A**: Use pipes **6 + 2**. (Height = 8)
*   **Pillar B**: Use pipes **4 + 3 + 1**. (Height = 8)
*   **Result**: Success! We made twin pillars of height 8. (Leftover: None)

Since **8** is taller than 6, the best answer is **8**.

---

## Why is this tricky?
If you have 50 pipes, checking every possible combination like "Put this in Pile A, or Pile B, or Trash" is impossible for a human (and even slow for a computer if done blindly).

**The Computer's Trick (Dynamic Programming):**
Instead of remembering exactly how tall both pillars are (which can be huge numbers), the computer only remembers the **Difference** in height between them.
*   "If I use these pipes, I can have Pillar A be 5 units taller than Pillar B."
*   "If I add a 5-unit pipe to Pillar B, now the difference is 0!" (Success condition).

We systematically check every pipe to see if it brings the difference closer to 0 while maximizing the height.
