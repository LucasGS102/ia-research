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

    public static int insertionSort(List<Integer> arr) {
        int max = 10000001; // máximo possível valor de arr[i] + 1
        int[] bit = new int[max];
        int shiftCount = 0;

        for (int i = arr.size() - 1; i >= 0; i--) {
            int current = arr.get(i);
            shiftCount += query(bit, current - 1); // quantos menores vieram depois
            update(bit, current, 1);               // registra este número na árvore
        }

        return shiftCount;
    }

    // Retorna a soma de 1 a i na BIT
    private static int query(int[] bit, int i) {
        int sum = 0;
        while (i > 0) {
            sum += bit[i];
            i -= i & -i;
        }
        return sum;
    }

    // Incrementa o valor na posição i da BIT
    private static void update(int[] bit, int i, int delta) {
        while (i < bit.length) {
            bit[i] += delta;
            i += i & -i;
        }
    }
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                    .collect(toList());

                int result = Result.insertionSort(arr);

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
