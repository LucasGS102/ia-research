import java.io.*;
import java.util.*;

public class Solution {
    static class SAM {
        int maxLen, last;
        int[] len, link, cnt;
        ArrayList<Integer>[] adj;

        public SAM(int n) {
            maxLen = 0;
            last = 0;
            len = new int[2 * n];
            link = new int[2 * n];
            cnt = new int[2 * n];
            adj = new ArrayList[2 * n];
            for (int i = 0; i < 2 * n; i++) adj[i] = new ArrayList<>();
            Arrays.fill(link, -1);
        }

        int addChar(int c) {
            int cur = ++maxLen;
            len[cur] = len[last] + 1;
            cnt[cur] = 1;
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
                    int clone = ++maxLen;
                    len[clone] = len[p] + 1;
                    link[clone] = link[q];
                    next[clone] = new HashMap<>(next[q]);
                    cnt[clone] = 0;
                    while (p != -1 && next[p].get(c) == q) {
                        next[p].put(c, clone);
                        p = link[p];
                    }
                    link[q] = clone;
                    link[cur] = clone;
                }
            }
            last = cur;
            return cur;
        }

        HashMap<Integer, Integer>[] next;

        void build(String s) {
            int n = s.length();
            next = new HashMap[2 * n];
            for (int i = 0; i < 2 * n; i++) next[i] = new HashMap<>();
            maxLen = 0;
            last = 0;
            len[0] = 0;
            link[0] = -1;
            cnt[0] = 0;
            for (int i = 0; i < n; i++) addChar(s.charAt(i) - 'a');
        }

        void dfs(int u) {
            for (int v : adj[u]) {
                dfs(v);
                cnt[u] += cnt[v];
            }
        }

        void prepare() {
            for (int i = 1; i <= maxLen; i++) {
                adj[link[i]].add(i);
            }
            dfs(0);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String t = br.readLine();

        SAM sam = new SAM(t.length());
        sam.build(t);
        sam.prepare();

        long ans = 0;
        for (int i = 1; i <= sam.maxLen; i++) {
            long val = (long) sam.len[i] * sam.cnt[i];
            if (val > ans) ans = val;
        }

        System.out.println(ans);
    }
}
