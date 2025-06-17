import java.io.*;
import java.util.*;

public class Solution {
    static final long MOD = 1000000007L;
    static final long BASE = 131L;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            String[] parts = br.readLine().split(" ");
            int N = Integer.parseInt(parts[0]);
            int A = Integer.parseInt(parts[1]);
            int B = Integer.parseInt(parts[2]);

            String S = br.readLine();

            long[] power = new long[N + 1];
            long[] hash = new long[N + 1];
            power[0] = 1;
            for (int i = 1; i <= N; i++) {
                power[i] = (power[i - 1] * BASE) % MOD;
            }
            for (int i = 0; i < N; i++) {
                hash[i + 1] = (hash[i] * BASE + (S.charAt(i) - 'a' + 1)) % MOD;
            }

            int[] dp = new int[N + 1];
            Arrays.fill(dp, Integer.MAX_VALUE);
            dp[0] = 0;

            HashMap<Long, List<Integer>> substrHashes = new HashMap<>();

            for (int i = 0; i < N; i++) {
                dp[i + 1] = Math.min(dp[i + 1], dp[i] + A);

                int low = 1, high = N - i;
                int best = 0;

                while (low <= high) {
                    int mid = (low + high) >> 1;
                    long curHash = getHash(hash, power, i, i + mid - 1);
                    List<Integer> starts = substrHashes.get(curHash);
                    boolean found = false;
                    if (starts != null) {
                        for (int pos : starts) {
                            if (pos < i) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) {
                        best = mid;
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                if (best > 0) {
                    dp[i + best] = Math.min(dp[i + best], dp[i] + B);
                }

                for (int length = 1; length <= i + 1; length++) {
                    long h = getHash(hash, power, i + 1 - length, i);
                    substrHashes.computeIfAbsent(h, k -> new ArrayList<>()).add(i + 1 - length);
                }
            }
            System.out.println(dp[N]);
        }
    }

    static long getHash(long[] hash, long[] power, int l, int r) {
        long res = hash[r + 1] - (hash[l] * power[r - l + 1] % MOD);
        if (res < 0) res += MOD;
        return res;
    }
}
