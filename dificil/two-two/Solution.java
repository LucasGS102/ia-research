import java.io.*;
import java.util.*;

public class Solution {
    static class TrieNode {
        TrieNode[] next = new TrieNode[10];
        boolean isEnd;
    }

    static void insert(TrieNode root, String num) {
        TrieNode cur = root;
        for (char c : num.toCharArray()) {
            int idx = c - '0';
            if (cur.next[idx] == null) cur.next[idx] = new TrieNode();
            cur = cur.next[idx];
        }
        cur.isEnd = true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        TrieNode root = new TrieNode();
        String two = "1";
        insert(root, two);
        for (int i = 1; i <= 800; i++) {
            two = multiplyByTwo(two);
            insert(root, two);
        }
        StringBuilder sb = new StringBuilder();
        while (T-- > 0) {
            String s = br.readLine();
            int n = s.length();
            long count = 0;
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == '0') continue;
                TrieNode cur = root;
                for (int j = i; j < n; j++) {
                    int idx = s.charAt(j) - '0';
                    if (cur.next[idx] == null) break;
                    cur = cur.next[idx];
                    if (cur.isEnd) count++;
                }
            }
            sb.append(count).append('\n');
        }
        System.out.print(sb);
    }

    static String multiplyByTwo(String num) {
        StringBuilder res = new StringBuilder();
        int carry = 0;
        for (int i = num.length() - 1; i >= 0; i--) {
            int cur = (num.charAt(i) - '0') * 2 + carry;
            res.append(cur % 10);
            carry = cur / 10;
        }
        if (carry > 0) res.append(carry);
        return res.reverse().toString();
    }
}
