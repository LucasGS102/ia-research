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

    public static long minimumPasses(long m, long w, long p, long n) {
        long passes = 0, candies = 0, minPasses = (n + m * w - 1) / (m * w);

        while (candies < n) {
            if ((long) m * w >= n - candies) {
                minPasses = Math.min(minPasses, passes + 1);
                break;
            }

            if (candies < p) {
                long needed = p - candies;
                long step = (needed + m * w - 1) / (m * w);
                passes += step;
                candies += step * m * w;
                if ((long) m * w >= n - candies) {
                    minPasses = Math.min(minPasses, passes + 1);
                    break;
                }
            }

            long maxPurchase = candies / p;
            long total = m + w + maxPurchase;
            long half = total / 2;

            if (m > w) {
                m = Math.max(m, half);
                w = total - m;
            } else {
                w = Math.max(w, half);
                m = total - w;
            }

            candies -= maxPurchase * p;
            passes += 1;
            candies += m * w;
            long remain = (n - candies + m * w - 1) / (m * w);
            minPasses = Math.min(minPasses, passes + remain);
        }

        return minPasses;
    }
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        long m = Long.parseLong(firstMultipleInput[0]);

        long w = Long.parseLong(firstMultipleInput[1]);

        long p = Long.parseLong(firstMultipleInput[2]);

        long n = Long.parseLong(firstMultipleInput[3]);

        long result = Result.minimumPasses(m, w, p, n);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
