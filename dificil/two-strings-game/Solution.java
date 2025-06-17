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

    static class SuffixAutomaton {
        static final int ALPHABET = 26;
        int[] link, length;
        int[][] next;
        int size, last;

        SuffixAutomaton(int maxLen) {
            link = new int[maxLen * 2];
            length = new int[maxLen * 2];
            next = new int[maxLen * 2][ALPHABET];
            Arrays.fill(link, -1);
            for (int i = 0; i < maxLen * 2; i++) Arrays.fill(next[i], -1);
            size = 1;
            last = 0;
        }

        void extend(char c) {
            int cur = size++;
            length[cur] = length[last] + 1;
            int cIndex = c - 'a';
            int p = last;
            while (p != -1 && next[p][cIndex] == -1) {
                next[p][cIndex] = cur;
                p = link[p];
            }
            if (p == -1) {
                link[cur] = 0;
            } else {
                int q = next[p][cIndex];
                if (length[p] + 1 == length[q]) {
                    link[cur] = q;
                } else {
                    int clone = size++;
                    length[clone] = length[p] + 1;
                    System.arraycopy(next[q], 0, next[clone], 0, ALPHABET);
                    link[clone] = link[q];
                    while (p != -1 && next[p][cIndex] == q) {
                        next[p][cIndex] = clone;
                        p = link[p];
                    }
                    link[q] = clone;
                    link[cur] = clone;
                }
            }
            last = cur;
        }
    }

    static SuffixAutomaton samA, samB;
    static Boolean[][] win;
    static Long[][] dp;
    static final long INF = (long)1e18;

    static boolean canWin(int u, int v) {
        if (win[u][v] != null) return win[u][v];
        for (int c = 0; c < 26; c++) {
            int na = samA.next[u][c];
            int nb = samB.next[v][c];
            if (na == -1 && nb == -1) continue;
            int nu = na == -1 ? u : na;
            int nv = nb == -1 ? v : nb;
            if (!canWin(nu, nv)) {
                win[u][v] = true;
                return true;
            }
        }
        win[u][v] = false;
        return false;
    }

    static long countPositions(int u, int v) {
        if (dp[u][v] != null) return dp[u][v];
        long res = 1;
        for (int c = 0; c < 26; c++) {
            int na = samA.next[u][c];
            int nb = samB.next[v][c];
            if (na == -1 && nb == -1) continue;
            int nu = na == -1 ? u : na;
            int nv = nb == -1 ? v : nb;
            res += countPositions(nu, nv);
            if (res > INF) res = INF;
        }
        dp[u][v] = res;
        return res;
    }

    static List<String> buildKth(long k) {
        StringBuilder Ares = new StringBuilder();
        StringBuilder Bres = new StringBuilder();
        int u = 0, v = 0;
        if (k == 1) return Arrays.asList("", "");
        k--;
        while (k > 0) {
            boolean moved = false;
            for (int c = 0; c < 26; c++) {
                int na = samA.next[u][c];
                int nb = samB.next[v][c];
                if (na == -1 && nb == -1) continue;
                int nu = na == -1 ? u : na;
                int nv = nb == -1 ? v : nb;
                if (!canWin(nu, nv)) continue;
                long cnt = countPositions(nu, nv);
                if (cnt >= k) {
                    if (na != -1) Ares.append((char)(c + 'a'));
                    if (nb != -1) Bres.append((char)(c + 'a'));
                    u = nu;
                    v = nv;
                    k--;
                    moved = true;
                    break;
                } else {
                    k -= cnt;
                }
            }
            if (!moved) break;
        }
        return Arrays.asList(Ares.toString(), Bres.toString());
    }

    public static List<String> twoStrings(long k, String a, String b) {
        samA = new SuffixAutomaton(a.length());
        samB = new SuffixAutomaton(b.length());
        for (char c : a.toCharArray()) samA.extend(c);
        for (char c : b.toCharArray()) samB.extend(c);

        win = new Boolean[samA.size][samB.size];
        dp = new Long[samA.size][samB.size];

        if (!canWin(0, 0)) {
            return Arrays.asList("no solution");
        }

        long total = countPositions(0, 0);
        if (total < k) return Arrays.asList("no solution");

        return buildKth(k);
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        long k = Long.parseLong(firstMultipleInput[2]);

        String a = bufferedReader.readLine();

        String b = bufferedReader.readLine();

        List<String> result = Result.twoStrings(k, a, b);

        bufferedWriter.write(
            result.stream()
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
