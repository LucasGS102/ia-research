import java.io.*;
import java.util.*;

class Result {

    public static int palindromicBorder(String s) {
        int n = s.length();
        int mod = 1_000_000_007;

        class Eertree {
            int[] length, link, diff, seriesLink;
            int size, last, pos;
            int[][] to;
            char[] str;
            long[] dp, ans;

            Eertree(int maxLen) {
                length = new int[maxLen + 3];
                link = new int[maxLen + 3];
                diff = new int[maxLen + 3];
                seriesLink = new int[maxLen + 3];
                to = new int[maxLen + 3][8];
                for (int i = 0; i < maxLen + 3; i++) Arrays.fill(to[i], 0);
                size = 2;
                last = 2;
                length[1] = -1;
                link[1] = 1;
                length[2] = 0;
                link[2] = 1;
                pos = 0;
                str = new char[maxLen + 2];
                dp = new long[maxLen + 2];
                ans = new long[maxLen + 3];
            }

            int getLink(int v) {
                while (pos - length[v] - 1 < 0 || str[pos - length[v] - 1] != str[pos]) v = link[v];
                return v;
            }

            void addChar(char c) {
                str[++pos] = c;
                int cur = getLink(last);
                int cIndex = c - 'a';
                if (to[cur][cIndex] == 0) {
                    int now = ++size;
                    length[now] = length[cur] + 2;
                    int linkNow = getLink(link[cur]);
                    if (linkNow == 0) linkNow = 2;
                    link[now] = to[linkNow][cIndex];
                    if (link[now] == 0) link[now] = 2;
                    to[cur][cIndex] = now;
                    diff[now] = length[now] - length[link[now]];
                    seriesLink[now] = (diff[now] == diff[link[now]]) ? seriesLink[link[now]] : link[now];
                }
                last = to[cur][cIndex];
                ans[last]++;
            }

            int solve() {
                long res = 0;
                dp[0] = 0;
                for (int i = 1; i <= pos; i++) {
                    int cur = last;
                    while (cur > 2) {
                        int sl = seriesLink[cur];
                        int diffVal = diff[cur];
                        int idx = i - (length[sl] + diffVal);
                        long val = (idx >= 0) ? dp[idx] : 0;
                        if (diffVal == diff[link[cur]]) val = (val + ans[link[cur]]) % mod;
                        ans[cur] = val % mod;
                        dp[i] = (dp[i] + ans[cur]) % mod;
                        cur = sl;
                    }
                    res = (res + dp[i]) % mod;
                    if (i < pos) {
                        int nextChar = str[i + 1] - 'a';
                        int curLink = getLink(last);
                        if (to[curLink][nextChar] != 0) last = to[curLink][nextChar];
                    }
                }
                return (int) res;
            }
        }

        Eertree tree = new Eertree(n);
        for (char c : s.toCharArray()) tree.addChar(c);
        return tree.solve();
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String s = bufferedReader.readLine();

        int result = Result.palindromicBorder(s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
