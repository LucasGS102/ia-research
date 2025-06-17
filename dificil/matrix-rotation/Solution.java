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

    /*
     * Complete the 'matrixRotation' function below.
     *
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY matrix
     *  2. INTEGER r
     */

    public static void matrixRotation(List<List<Integer>> matrix, int r) {
    int m = matrix.size();
    int n = matrix.get(0).size();
    int layers = Math.min(m, n) / 2;

    for (int l = 0; l < layers; l++) {
        List<Integer> layer = new ArrayList<>();

        // Top edge
        for (int j = l; j < n - l; j++)
            layer.add(matrix.get(l).get(j));

        // Right edge
        for (int i = l + 1; i < m - l - 1; i++)
            layer.add(matrix.get(i).get(n - l - 1));

        // Bottom edge
        for (int j = n - l - 1; j >= l; j--)
            layer.add(matrix.get(m - l - 1).get(j));

        // Left edge
        for (int i = m - l - 2; i > l; i--)
            layer.add(matrix.get(i).get(l));

        int size = layer.size();
        int rot = r % size;

        List<Integer> rotated = new ArrayList<>();
        rotated.addAll(layer.subList(rot, size));
        rotated.addAll(layer.subList(0, rot));

        // Put back the rotated layer
        int idx = 0;

        // Top edge
        for (int j = l; j < n - l; j++)
            matrix.get(l).set(j, rotated.get(idx++));

        // Right edge
        for (int i = l + 1; i < m - l - 1; i++)
            matrix.get(i).set(n - l - 1, rotated.get(idx++));

        // Bottom edge
        for (int j = n - l - 1; j >= l; j--)
            matrix.get(m - l - 1).set(j, rotated.get(idx++));

        // Left edge
        for (int i = m - l - 2; i > l; i--)
            matrix.get(i).set(l, rotated.get(idx++));
    }

    // Print the final matrix
    for (List<Integer> row : matrix) {
        System.out.println(row.stream()
                              .map(Object::toString)
                              .collect(Collectors.joining(" ")));
    }
}


}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int m = Integer.parseInt(firstMultipleInput[0]);

        int n = Integer.parseInt(firstMultipleInput[1]);

        int r = Integer.parseInt(firstMultipleInput[2]);

        List<List<Integer>> matrix = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                matrix.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Result.matrixRotation(matrix, r);

        bufferedReader.close();
    }
}