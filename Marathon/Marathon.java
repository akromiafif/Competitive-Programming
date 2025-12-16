import java.util.Scanner;

public class Marathon {
    static class Pace {
        int index;
        int time; // in seconds
        int energy;

        public Pace(int index, int time, int energy) {
            this.index = index;
            this.time = time;
            this.energy = energy;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int t = sc.nextInt();

        for (int testCase = 1; testCase <= t; testCase++) {
            int D = sc.nextInt();
            int H = sc.nextInt();

            Pace[] paces = new Pace[5];
            for (int i = 0; i < 5; i++) {
                int minv = sc.nextInt();
                int sec = sc.nextInt();
                int energy = sc.nextInt();
                paces[i] = new Pace(i, minv * 60 + sec, energy);
            }

            int minTime = (int) 1e9;
            minTime = solve(D, H, 0, paces, 0, minTime);

            int finalMin = minTime / 60;
            int finalSec = minTime % 60;
            System.out.println("#" + testCase + " " + finalMin + " " + finalSec);
        }
    }

    // Recursive solver
    // index: current pace index being considered (0 to 4)
    // distance of current pace can be from 0 to remainingD
    private static int solve(int remainingD, int remainingH, int index, Pace[] paces, int curTime, int minTime) {
        // Pruning: if current time already exceeds found minTime, stop
        if (curTime >= minTime) {
            return minTime;
        }

        // Base cases
        if (remainingD == 0) {
            return Math.min(minTime, curTime);
        }
        
        // If we ran out of paces but still have distance, invalid
        if (index >= 5) {
            return (int) 1e9;
        }

        // Iterate through possible distances for the current pace
        // From 0 to remainingD
        for (int d = 0; d <= remainingD; d++) {
            int energyUsed = paces[index].energy * d;
            
            // Check if energy constraint is met
            if (remainingH - energyUsed >= 0) {
                int timeTaken = paces[index].time * d;
                int res = solve(remainingD - d, remainingH - energyUsed, index + 1, paces, curTime + timeTaken, minTime);
                minTime = Math.min(minTime, res);
            }
        }
        return minTime;
    }
}
