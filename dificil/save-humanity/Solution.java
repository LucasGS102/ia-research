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

class Result {
    public static void virusIndices(String p, String v) {
        int n = p.length();
        int m = v.length();
        char[] pa = p.toCharArray();
        char[] va = v.toCharArray();
        StringBuilder sb = new StringBuilder();

        int mismatch = 0;
        for (int j = 0; j < m; j++) {
            if (pa[j] != va[j]) mismatch++;
        }
        if (mismatch <= 1) sb.append(0).append(" ");

        for (int i = 1; i <= n - m; i++) {
            if (pa[i - 1] != va[0]) mismatch--;
            if (pa[i + m - 1] != va[m - 1]) mismatch++;
            mismatch = 0;
            for (int j = 0; j < m; j++) {
                if (pa[i + j] != va[j]) {
                    mismatch++;
                    if (mismatch > 1) break;
                }
            }
            if (mismatch <= 1) sb.append(i).append(" ");
        }

        if (sb.length() == 0) {
            System.out.println("No Match!");
        } else {
            System.out.println(sb.toString().trim());
        }
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                String p = firstMultipleInput[0];

                String v = firstMultipleInput[1];

                Result.virusIndices(p, v);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}
