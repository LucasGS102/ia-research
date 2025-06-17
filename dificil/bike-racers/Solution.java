import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    public static long bikeRacers(List<List<Integer>> bikers, List<List<Integer>> bikes, int K) {
        int N = bikers.size();
        int M = bikes.size();

        long maxDist = 0;
        long[][] distMatrix = new long[N][M];

        for (int i = 0; i < N; i++) {
            List<Integer> bkr = bikers.get(i);
            int bx = bkr.get(0);
            int by = bkr.get(1);
            for (int j = 0; j < M; j++) {
                List<Integer> bk = bikes.get(j);
                int kx = bk.get(0);
                int ky = bk.get(1);
                long dx = bx - kx;
                long dy = by - ky;
                long dist = dx * dx + dy * dy;
                distMatrix[i][j] = dist;
                if (dist > maxDist) maxDist = dist;
            }
        }

        long left = 0, right = maxDist;
        long ans = maxDist;

        while (left <= right) {
            long mid = left + (right - left) / 2;

            // Construir grafo bipartido: de bikers (0..N-1) para bikes (0..M-1) se dist <= mid
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                List<Integer> edges = new ArrayList<>();
                for (int j = 0; j < M; j++) {
                    if (distMatrix[i][j] <= mid) {
                        edges.add(j);
                    }
                }
                graph.add(edges);
            }

            if (maxBipartiteMatching(graph, N, M) >= K) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return ans;
    }

    private static boolean bpm(int u, boolean[] seen, int[] matchR, List<List<Integer>> graph) {
        for (int v : graph.get(u)) {
            if (!seen[v]) {
                seen[v] = true;
                if (matchR[v] == -1 || bpm(matchR[v], seen, matchR, graph)) {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    private static int maxBipartiteMatching(List<List<Integer>> graph, int n, int m) {
        int[] matchR = new int[m];
        Arrays.fill(matchR, -1);
        int result = 0;

        for (int u = 0; u < n; u++) {
            boolean[] seen = new boolean[m];
            if (bpm(u, seen, matchR, graph)) {
                result++;
            }
        }
        return result;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] firstMultipleInput = bufferedReader.readLine().trim().split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);
        int m = Integer.parseInt(firstMultipleInput[1]);
        int k = Integer.parseInt(firstMultipleInput[2]);

        List<List<Integer>> bikers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            bikers.add(
                Arrays.stream(bufferedReader.readLine().trim().split(" "))
                .map(Integer::parseInt)
                .collect(toList())
            );
        }

        List<List<Integer>> bikes = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            bikes.add(
                Arrays.stream(bufferedReader.readLine().trim().split(" "))
                .map(Integer::parseInt)
                .collect(toList())
            );
        }

        long result = Result.bikeRacers(bikers, bikes, k);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
