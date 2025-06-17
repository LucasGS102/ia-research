import java.io.*;
import java.util.*;
import java.util.stream.*;

class Result {
    static final int MOD = 1_000_000_007;

    static class SAM {
        int[] len, link;
        Map<Character, Integer>[] next;
        int size, last;

        SAM(int n) {
            len = new int[2 * n];
            link = new int[2 * n];
            next = new HashMap[2 * n];
            for (int i = 0; i < 2 * n; i++) next[i] = new HashMap<>();
            size = 1;
            last = 0;
            link[0] = -1;
        }

        void addChar(char c) {
            int cur = size++;
            len[cur] = len[last] + 1;
            int p = last;
            while (p != -1 && !next[p].containsKey(c)) {
                next[p].put(c, cur);
                p = link[p];
            }
            if (p == -1) {
                link[cur] = 0;
            } else {
                int q = next[p].get(c);
                if (len[p] + 1 == len[q]) {
                    link[cur] = q;
                } else {
                    int clone = size++;
                    len[clone] = len[p] + 1;
                    next[clone].putAll(next[q]);
                    link[clone] = link[q];
                    while (p != -1 && next[p].get(c) == q) {
                        next[p].put(c, clone);
                        p = link[p];
                    }
                    link[q] = clone;
                    link[cur] = clone;
                }
            }
            last = cur;
        }
    }

    static long modExp(long base, long exp) {
        long res = 1;
        long b = base % MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) res = (res * b) % MOD;
            b = (b * b) % MOD;
            exp >>= 1;
        }
        return res;
    }

    public static int superFunctionalStrings(String s) {
        int n = s.length();
        SAM sam = new SAM(n);
        for (char c : s.toCharArray()) sam.addChar(c);

        int[] distinctCount = new int[sam.size];
        List<Integer>[] adj = new ArrayList[sam.size];
        for (int i = 0; i < sam.size; i++) adj[i] = new ArrayList<>();
        for (int i = 1; i < sam.size; i++) adj[sam.link[i]].add(i);

        Deque<Integer> stack = new ArrayDeque<>();
        boolean[] visited = new boolean[sam.size];
        for (int i = 0; i < sam.size; i++) distinctCount[i] = 0;

        distinctCount[0] = 0;

        // Calcular distinctCount via DFS pós-ordem
        dfsDistinct(0, adj, sam.next, distinctCount);

        long ans = 0;
        for (int i = 1; i < sam.size; i++) {
            int lengthDiff = sam.len[i] - sam.len[sam.link[i]];
            int d = distinctCount[i];
            ans += modExp(lengthDiff, d);
            ans %= MOD;
        }
        return (int) ans;
    }

    static void dfsDistinct(int u, List<Integer>[] adj, Map<Character, Integer>[] next, int[] distinctCount) {
        Set<Integer> set = new HashSet<>();
        for (Map.Entry<Character, Integer> e : next[u].entrySet()) {
            int v = e.getValue();
            if (distinctCount[v] == 0 && v != 0) dfsDistinct(v, adj, next, distinctCount);
            set.add((int) (e.getKey() - 'a'));
            for (int c = 0; c < 26; c++) {
                if (((distinctCount[v] >> c) & 1) == 1) set.add(c);
            }
        }
        int mask = 0;
        for (int c : set) mask |= 1 << c;
        distinctCount[u] = Integer.bitCount(mask);
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                String s = bufferedReader.readLine();

                int result = Result.superFunctionalStrings(s);

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
