public class MinSubsetDiffBrute {

    static int total;

    /**
     * Pure recursion (no memoization) -- this is the O(2^n) brute force.
     *
     * i    = index of the element we are deciding right now
     * sumA = sum already assigned to group A
     *
     * At each element we branch two ways: put it in A, or leave it out
     * (which puts it in B). When every element is placed, we measure the gap.
     */
    static int dfs(int i, int sumA, int[] arr) {
        if (i == arr.length) {            // every element has been placed
            int sumB = total - sumA;
            return Math.abs(sumA - sumB); // this split's gap
        }

        int put  = dfs(i + 1, sumA + arr[i], arr); // arr[i] -> group A
        int skip = dfs(i + 1, sumA,          arr); // arr[i] -> group B

        return Math.min(put, skip);                // keep the better split
    }

    static int minDiff(int[] arr) {
        total = 0;
        for (int x : arr) total += x;
        return dfs(0, 0, arr);
    }
}
