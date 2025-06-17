import java.io.*;
import java.util.*;

public class Solution {

    static int n, k;
    static ArrayList<Integer>[] tree;
    static Fenwick fenw;
    static long result = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] first = br.readLine().split(" ");
        n = Integer.parseInt(first[0]);
        k = Integer.parseInt(first[1]);

        tree = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) tree[i] = new ArrayList<>();

        boolean[] hasParent = new boolean[n + 1];

        for (int i = 0; i < n - 1; i++) {
            String[] edge = br.readLine().split(" ");
            int p = Integer.parseInt(edge[0]);
            int c = Integer.parseInt(edge[1]);
            tree[p].add(c);
            hasParent[c] = true;
        }

        int root = 1;
        for (int i = 1; i <= n; i++) {
            if (!hasParent[i]) {
                root = i;
                break;
            }
        }

        fenw = new Fenwick(n);

        dfs(root);

        System.out.println(result);
    }

    static void dfs(int node) {
        int left = Math.max(1, node - k);
        int right = Math.min(n, node + k);
        // Conta ancestrais com valores no intervalo
        long count = fenw.sum(right) - fenw.sum(left - 1);
        result += count;
        fenw.update(node, 1);
        for (int child : tree[node]) {
            dfs(child);
        }
        fenw.update(node, -1);
    }

    static class Fenwick {
        int[] bit;
        int size;

        Fenwick(int n) {
            size = n;
            bit = new int[n + 1];
        }

        void update(int i, int delta) {
            while (i <= size) {
                bit[i] += delta;
                i += i & (-i);
            }
        }

        int sum(int i) {
            int s = 0;
            while (i > 0) {
                s += bit[i];
                i -= i & (-i);
            }
            return s;
        }
    }
}
