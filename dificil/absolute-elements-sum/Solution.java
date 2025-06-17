import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    public static List<Long> playingWithNumbers(List<Integer> arr, List<Integer> queries) {
        int n = arr.size();
        Collections.sort(arr);
        long[] prefixSum = new long[n];
        prefixSum[0] = arr.get(0);
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + arr.get(i);
        }

        List<Long> res = new ArrayList<>();
        long S = 0;

        for (int x : queries) {
            S += x;
            int pos = Collections.binarySearch(arr, (int) (-S));
            if (pos < 0) pos = -pos - 1;

            long sumNeg = (pos > 0 ? prefixSum[pos - 1] : 0) + (long) pos * S;
            long sumPos = (prefixSum[n - 1] - (pos > 0 ? prefixSum[pos - 1] : 0)) + (long) (n - pos) * S;

            long totalAbs = -sumNeg + sumPos;
            res.add(totalAbs);
        }

        return res;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        List<Long> result = Result.playingWithNumbers(arr, queries);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
