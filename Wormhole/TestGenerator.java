package Wormhole;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class TestGenerator {
    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("c:\\Users\\Avifcii\\Desktop\\CP\\Wormhole\\input.txt"));
        Random rand = new Random();

        int simpleCases = 3;
        int complexCases = 10;
        int totalCases = simpleCases + complexCases;

        out.println(totalCases);

        // --- Simple Cases ---
        
        // Case 1: Start (0,0) -> End (10,10). Dist=20.
        // 1 Wormhole (2,2)->(8,8) cost 100. (Useless)
        // Expected: 20
        out.println(1);
        out.println("0 0 10 10");
        out.println("2 2 8 8 100");

        // Case 2: Start (0,0) -> End (20,0). Dist=20.
        // 1 Wormhole (0,0)->(20,0) cost 5. (Useful)
        // Expected: 5
        out.println(1);
        out.println("0 0 20 0");
        out.println("0 0 20 0 5");

        // Case 3: Start (0,0) -> End (100,100). Dist=200.
        // W1: (10,10)->(50,50) cost 10.
        // W2: (60,60)->(90,90) cost 10.
        // Path: (0,0)->(10,10)[20] + W1[10] + (50,50)->(60,60)[20] + W2[10] + (90,90)->(100,100)[20] 
        // Total = 20 + 10 + 20 + 10 + 20 = 80.
        // Direct: 200.
        // Expected: 80.
        out.println(2);
        out.println("0 0 100 100");
        out.println("10 10 50 50 10");
        out.println("60 60 90 90 10");

        // --- Complex Cases ---
        // N up to 12 to test time limit.
        for (int i = 0; i < complexCases; i++) {
            int N = 10 + rand.nextInt(3); // 10, 11, or 12
            out.println(N);
            
            int MAXX = 2000;
            int MAXY = 2000;
            
            int sX = rand.nextInt(MAXX);
            int sY = rand.nextInt(MAXY);
            int tX = rand.nextInt(MAXX);
            int tY = rand.nextInt(MAXY);
            
            out.println(sX + " " + sY + " " + tX + " " + tY);
            
            for (int j = 0; j < N; j++) {
                int x1 = rand.nextInt(MAXX);
                int y1 = rand.nextInt(MAXY);
                int x2 = rand.nextInt(MAXX);
                int y2 = rand.nextInt(MAXY);
                // Make costs competitive to encourage branching
                int dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                int cost = rand.nextInt((int)(dist * 0.8) + 1); // Cost is 0 to ~80% of dist
                
                out.println(x1 + " " + y1 + " " + x2 + " " + y2 + " " + cost);
            }
        }

        out.close();
        System.out.println("Generated input.txt");
    }
}
