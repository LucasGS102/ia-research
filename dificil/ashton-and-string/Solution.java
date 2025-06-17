import java.io.*;
import java.util.*;

class Result {
    static int[] sa, lcp;
    static String s;
    static int n;

    static void buildSA() {
        n = s.length();
        sa = new int[n];
        Integer[] saObj = new Integer[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];
        for (int i = 0; i < n; i++) {
            rank[i] = s.charAt(i);
            saObj[i] = i;
        }
        for (int k = 1; k < n; k <<= 1) {
            final int K = k;
            final int[] R = rank;
            final int N = n;
            Arrays.sort(saObj, (a, b) -> {
                if (R[a] != R[b]) return Integer.compare(R[a], R[b]);
                int ra = a + K < N ? R[a + K] : -1;
                int rb = b + K < N ? R[b + K] : -1;
                return Integer.compare(ra, rb);
            });
            tmp[saObj[0]] = 0;
            for (int i = 1; i < n; i++) {
                tmp[saObj[i]] = tmp[saObj[i - 1]] +
                    ((rank[saObj[i]] != rank[saObj[i - 1]] ||
                    ((saObj[i] + K < n ? rank[saObj[i] + K] : -1) != (saObj[i - 1] + K < n ? rank[saObj[i - 1] + K] : -1))) ? 1 : 0);
            }
            int[] t = rank; rank = tmp; tmp = t;
            if (rank[saObj[n - 1]] == n - 1) break;
        }
        for (int i = 0; i < n; i++) sa[i] = saObj[i];
    }

    static void buildLCP() {
        lcp = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
                lcp[rank[i]] = h;
                if (h > 0) h--;
            }
        }
    }

    public static char ashtonString(String str, int k) {
        s = str;
        buildSA();
        buildLCP();
        long total = 0;
        for (int i = 0; i < n; i++) {
            int len = n - sa[i];
            int cnt = len - lcp[i];
            long segLen = ((long)(len + lcp[i] + 1) * cnt) / 2;
            if (total + segLen >= k) {
                long pos = k - total;
                int low = lcp[i], high = len;
                while (low < high) {
                    int mid = (low + high) / 2;
                    long count = mid - lcp[i];
                    long lengthSum = ((long)(mid + lcp[i] + 1) * count) / 2;
                    if (lengthSum >= pos) high = mid;
                    else low = mid + 1;
                }
                pos -= ((long)(low - 1 + lcp[i] + 1) * (low - 1 - lcp[i])) / 2;
                return s.charAt(sa[i] + (int)pos - 1);
            }
            total += segLen;
        }
        return ' ';
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine().trim());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < t; i++) {
            String s = br.readLine();
            int k = Integer.parseInt(br.readLine().trim());
            bw.write(Result.ashtonString(s, k));
            bw.newLine();
        }
        bw.flush();
    }
}
