import java.io.*;
import java.util.*;

public class Solution {

    static int[] manacher(String s) {
        int n = s.length();
        int[] p = new int[n];
        int c = 0, r = 0;
        for (int i = 0; i < n; i++) {
            int mirr = 2 * c - i;
            if (i < r) p[i] = Math.min(r - i, p[mirr]);
            int a = i + 1 + p[i];
            int b = i - 1 - p[i];
            while (a < n && b >= 0 && s.charAt(a) == s.charAt(b)) {
                p[i]++;
                a++;
                b--;
            }
            if (i + p[i] > r) {
                c = i;
                r = i + p[i];
            }
        }
        return p;
    }

    static class Interval {
        int start, end, length;
        Interval(int s, int e) { start = s; end = e; length = e - s + 1; }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        String S = br.readLine();
        String doubled = S + S;

        StringBuilder sb = new StringBuilder();
        sb.append('#');
        for (int i = 0; i < doubled.length(); i++) {
            sb.append(doubled.charAt(i));
            sb.append('#');
        }
        String T = sb.toString();

        int[] p = manacher(T);
        int lenT = T.length();
        int lenS = S.length();

        List<Interval> palindromes = new ArrayList<>();

        for (int i = 0; i < lenT; i++) {
            int radius = p[i];
            if (radius == 0) continue;
            int left = i - radius;
            int right = i + radius;
            int start = (left + 1) / 2;
            int end = (right - 1) / 2;
            if (start < 0 || end >= 2 * lenS) continue;
            palindromes.add(new Interval(start, end));
        }

        int size = 2 * lenS;
        int[] maxLenStart = new int[size];
        int[] maxLenEnd = new int[size];
        Arrays.fill(maxLenStart, 0);
        Arrays.fill(maxLenEnd, 0);

        for (Interval iv : palindromes) {
            int len = iv.length;
            if (maxLenStart[iv.start] < len) maxLenStart[iv.start] = len;
            if (maxLenEnd[iv.end] < len) maxLenEnd[iv.end] = len;
        }

        SegmentTree segStart = new SegmentTree(maxLenStart);
        SegmentTree segEnd = new SegmentTree(maxLenEnd);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        for (int k = 0; k < lenS; k++) {
            int l = k;
            int r = k + lenS - 1;
            int res = 1;
            int maxStart = segStart.query(l, r);
            int maxEnd = segEnd.query(l, r);
            res = Math.max(res, maxStart);
            res = Math.max(res, maxEnd);
            bw.write(res + "\n");
        }

        bw.flush();
    }

    static class SegmentTree {
        int n;
        int[] tree;

        SegmentTree(int[] arr) {
            n = arr.length;
            int size = 1;
            while (size < n) size <<= 1;
            tree = new int[2 * size];
            Arrays.fill(tree, 0);
            System.arraycopy(arr, 0, tree, size, n);
            for (int i = size - 1; i > 0; i--) {
                tree[i] = Math.max(tree[i << 1], tree[i << 1 | 1]);
            }
        }

        int query(int l, int r) {
            l += tree.length / 2;
            r += tree.length / 2;
            int res = 0;
            while (l <= r) {
                if ((l & 1) == 1) res = Math.max(res, tree[l++]);
                if ((r & 1) == 0) res = Math.max(res, tree[r--]);
                l >>= 1;
                r >>= 1;
            }
            return res;
        }
    }
}
