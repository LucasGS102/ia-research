import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class Result {

    static List<int[]> tasks = new ArrayList<>();

    public static int taskScheduling(int d, int m) {
        tasks.add(new int[]{d, m});
        // Ordenar por prazo
        tasks.sort(Comparator.comparingInt(a -> a[0]));

        int currentTime = 0;
        int maxLate = 0;

        for (int[] task : tasks) {
            currentTime += task[1];
            int late = currentTime - task[0];
            if (late > maxLate) maxLate = late;
        }

        return maxLate > 0 ? maxLate : 0;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int d = Integer.parseInt(firstMultipleInput[0]);

                int m = Integer.parseInt(firstMultipleInput[1]);

                int result = Result.taskScheduling(d, m);

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
