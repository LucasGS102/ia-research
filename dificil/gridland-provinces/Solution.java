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
     * Complete the 'gridlandProvinces' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. STRING s1
     *  2. STRING s2
     */

    public static int gridlandProvinces(String s1, String s2) {
    int n = s1.length();
    Set<String> seen = new HashSet<>();
    boolean[][] visited = new boolean[2][n];

    int total = 2 * n;

    Deque<PathState> stack = new ArrayDeque<>();

    for (int row = 0; row < 2; row++) {
        for (int col = 0; col < n; col++) {
            visited[row][col] = true;
            StringBuilder sb = new StringBuilder();
            sb.append(row == 0 ? s1.charAt(col) : s2.charAt(col));
            stack.push(new PathState(row, col, 1, sb, copyVisited(visited)));
            visited[row][col] = false;
        }
    }

    int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    while (!stack.isEmpty()) {
        PathState cur = stack.pop();
        if (cur.count == total) {
            seen.add(cur.sb.toString());
            continue;
        }
        for (int[] d : dir) {
            int nr = cur.r + d[0];
            int nc = cur.c + d[1];
            if (nr >= 0 && nr < 2 && nc >= 0 && nc < n && !cur.vis[nr][nc]) {
                cur.vis[nr][nc] = true;
                char ch = (nr == 0 ? s1.charAt(nc) : s2.charAt(nc));
                StringBuilder nextSb = new StringBuilder(cur.sb);
                nextSb.append(ch);
                stack.push(new PathState(nr, nc, cur.count + 1, nextSb, copyVisited(cur.vis)));
                cur.vis[nr][nc] = false;
            }
        }
    }

    return seen.size();
}

static class PathState {
    int r, c, count;
    StringBuilder sb;
    boolean[][] vis;

    PathState(int r, int c, int count, StringBuilder sb, boolean[][] vis) {
        this.r = r;
        this.c = c;
        this.count = count;
        this.sb = sb;
        this.vis = vis;
    }
}

static boolean[][] copyVisited(boolean[][] v) {
    return new boolean[][] {
        Arrays.copyOf(v[0], v[0].length),
        Arrays.copyOf(v[1], v[1].length)
    };
}


}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int p = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, p).forEach(pItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                String s1 = bufferedReader.readLine();

                String s2 = bufferedReader.readLine();

                int result = Result.gridlandProvinces(s1, s2);

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
