import java.util.*;

public class Solution {
    static final int MOD = 1000000007;
    static final int MAX = 300005;
    static long[] fact = new long[MAX];
    static long[] inv = new long[MAX];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        TreeSet<Integer> rem = new TreeSet<>();
        for (int i = 1; i <= n; i++) rem.add(i);
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            if (a[i] != 0) rem.remove(a[i]);
        }
        pre();
        BIT bit = new BIT(n + 2);
        int k = rem.size();
        long res = 0;
        Integer[] free = rem.toArray(new Integer[0]);
        for (int i = 0; i < n; i++) {
            if (a[i] == 0) {
                int less = lower(free, bit);
                long ways = fact[k - 1] * less % MOD * inv[k] % MOD;
                res = (res + ways * fact[n - i - 1]) % MOD;
                k--;
            } else {
                int less = a[i] - 1 - bit.query(a[i] - 1);
                res = (res + fact[n - i - 1] * less % MOD * inv[k]) % MOD;
                bit.add(a[i], 1);
            }
        }
        System.out.println((res + 1) % MOD);
    }

    static int lower(Integer[] free, BIT bit) {
        int cnt = 0;
        for (int x : free)
            if (bit.query(x - 1) == 0) cnt++;
        return cnt;
    }

    static void pre() {
        fact[0] = inv[0] = 1;
        for (int i = 1; i < MAX; i++) {
            fact[i] = fact[i - 1] * i % MOD;
            inv[i] = pow(fact[i], MOD - 2);
        }
    }

    static long pow(long a, long b) {
        long r = 1;
        while (b > 0) {
            if ((b & 1) != 0) r = r * a % MOD;
            a = a * a % MOD;
            b >>= 1;
        }
        return r;
    }

    static class BIT {
        int[] t;
        int n;

        BIT(int n) {
            this.n = n;
            t = new int[n + 2];
        }

        void add(int i, int v) {
            while (i < t.length) {
                t[i] += v;
                i += i & -i;
            }
        }

        int query(int i) {
            int r = 0;
            while (i > 0) {
                r += t[i];
                i -= i & -i;
            }
            return r;
        }
    }
}
