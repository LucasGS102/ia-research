import java.io.*;
import java.util.*;

class Node {
    Node[] next = new Node[26];
    Node fail;
    List<Integer> out = new ArrayList<>();
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        String[] genes = br.readLine().split(" ");
        String[] hs = br.readLine().split(" ");
        int[] health = new int[n];
        for (int i = 0; i < n; i++) health[i] = Integer.parseInt(hs[i]);
        int s = Integer.parseInt(br.readLine());

        Node root = new Node();
        for (int i = 0; i < n; i++) {
            Node cur = root;
            for (char ch : genes[i].toCharArray()) {
                int idx = ch - 'a';
                if (cur.next[idx] == null) cur.next[idx] = new Node();
                cur = cur.next[idx];
            }
            cur.out.add(i);
        }

        Queue<Node> q = new ArrayDeque<>();
        for (int i = 0; i < 26; i++) {
            if (root.next[i] != null) {
                root.next[i].fail = root;
                q.add(root.next[i]);
            }
        }

        while (!q.isEmpty()) {
            Node u = q.poll();
            for (int i = 0; i < 26; i++) {
                Node v = u.next[i];
                if (v == null) continue;
                Node f = u.fail;
                while (f != null && f.next[i] == null) f = f.fail;
                v.fail = (f == null) ? root : f.next[i];
                v.out.addAll(v.fail.out);
                q.add(v);
            }
        }

        long min = Long.MAX_VALUE, max = 0;

        while (s-- > 0) {
            String[] p = br.readLine().split(" ");
            int a = Integer.parseInt(p[0]), b = Integer.parseInt(p[1]);
            String d = p[2];
            Node u = root;
            long sum = 0;
            for (char ch : d.toCharArray()) {
                int idx = ch - 'a';
                while (u != null && u.next[idx] == null) u = u.fail;
                u = (u == null) ? root : u.next[idx];
                for (int i : u.out) if (i >= a && i <= b) sum += health[i];
            }
            if (sum < min) min = sum;
            if (sum > max) max = sum;
        }

        System.out.println(min + " " + max);
    }
}
