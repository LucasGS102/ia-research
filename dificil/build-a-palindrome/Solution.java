import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.*;

class Result {

    public static String buildPalindrome(String a, String b) {
        int maxLen = Math.min(a.length(), b.length());
        String ans = null;
        for (int length = maxLen; length >= 1; length--) {
            List<String> aSubs = new ArrayList<>();
            for (int i = 0; i <= a.length() - length; i++) {
                aSubs.add(a.substring(i, i + length));
            }
            Set<String> bSubs = new HashSet<>();
            for (int i = 0; i <= b.length() - length; i++) {
                bSubs.add(b.substring(i, i + length));
            }
            List<String> candidates = new ArrayList<>();
            for (String asub : aSubs) {
                for (String bsub : bSubs) {
                    String s = asub + bsub;
                    if (isPalindrome(s)) candidates.add(s);
                }
            }
            if (!candidates.isEmpty()) {
                Collections.sort(candidates);
                ans = candidates.get(0);
                break;
            }
        }
        return ans == null ? "-1" : ans;
    }

    private static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                String a = bufferedReader.readLine();

                String b = bufferedReader.readLine();

                String result = Result.buildPalindrome(a, b);

                bufferedWriter.write(result);
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
