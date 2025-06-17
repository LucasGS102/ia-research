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

    public static List<Integer> similarStrings(int n, String s, List<List<Integer>> queries) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Map<Character, Integer> m = new HashMap<>();
            int id = 0;
            StringBuilder key = new StringBuilder();
            for (int j = i; j < n; j++) {
                char c = s.charAt(j);
                if (!m.containsKey(c)) m.put(c, id++);
                key.append(m.get(c)).append(',');
                map.put(key.toString(), map.getOrDefault(key.toString(), 0) + 1);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (List<Integer> q : queries) {
            int l = q.get(0) - 1;
            int r = q.get(1) - 1;
            Map<Character, Integer> m = new HashMap<>();
            int id = 0;
            StringBuilder key = new StringBuilder();
            for (int i = l; i <= r; i++) {
                char c = s.charAt(i);
                if (!m.containsKey(c)) m.put(c, id++);
                key.append(m.get(c)).append(',');
            }
            res.add(map.getOrDefault(key.toString(), 0));
        }
        return res;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);
        int q = Integer.parseInt(firstMultipleInput[1]);

        String s = bufferedReader.readLine();

        List<List<Integer>> queries = new ArrayList<>();

        IntStream.range(0, q).forEach(i -> {
            try {
                queries.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        List<Integer> result = Result.similarStrings(n, s, queries);

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
