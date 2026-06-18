import java.util.*;

public class Main {  
    static int minRemovals(String s) {
        int N = s.length();
        int maxLen = 0;
        int prefix = 0;

        HashMap<Integer, Integer> first = new HashMap<>();
        first.put(0, -1);

        for (int i = 0; i < N; i++) {
            prefix += (s.charAt(i) == 'R') ? 1 : -1;
            
            if (first.containsKey(prefix)) {
                int len = i - first.get(prefix);
                maxLen = Math.max(maxLen, len);
            } else {
                first.put(prefix, i);
            }
        }

        return N - maxLen;
    }

    public static void main(String[] args) { 
        String s = "RRRBRRRRRRR";
        System.out.println(minRemovals(s));
    }
}