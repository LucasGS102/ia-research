import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    static class SAM {
        static final int ALPH = 26;
        int size, last;
        int[] len, link;
        int[][] next;
        long[] substrCount;
        boolean[] visited;

        SAM(int maxLen) {
            size = 1;
            last = 0;
            len = new int[2 * maxLen];
            link = new int[2 * maxLen];
            next = new int[2 * maxLen][ALPH];
            for (int i = 0; i < 2 * maxLen; i++)
                Arrays.fill(next[i], -1);
            substrCount = new long[2 * maxLen];
            visited = new boolean[2 * maxLen];
            link[0] = -1;
        }

        void addChar(char c) {
            int cur = size++;
            len[cur] = len[last] + 1;
            int p = last;
            int cIdx = -1;
            if (c >= 'a' && c <= 'z') cIdx = c - 'a';
            while (p != -1 && (cIdx == -1 || next[p][cIdx] == -1)) {
                if (cIdx != -1) next[p][cIdx] = cur;
                p = link[p];
            }
            if (p == -1) {
                link[cur] = 0;
            } else if (cIdx != -1) {
                int q = next[p][cIdx];
                if (len[p] + 1 == len[q]) {
                    link[cur] = q;
                } else {
                    int clone = size++;
                    len[clone] = len[p] + 1;
                    link[clone] = link[q];
                    System.arraycopy(next[q], 0, next[clone], 0, ALPH);
                    while (p != -1 && next[p][cIdx] == q) {
                        next[p][cIdx] = clone;
                        p = link[p];
                    }
                    link[q] = clone;
                    link[cur] = clone;
                }
            } else {
                link[cur] = 0;
            }
            last = cur;
        }

        long dfsCount(int u) {
            if (visited[u]) return substrCount[u];
            visited[u] = true;
            long res = 1;
            for (int c = 0; c < ALPH; c++) {
                int nxt = next[u][c];
                if (nxt != -1) {
                    res += dfsCount(nxt);
                }
            }
            substrCount[u] = res;
            return res;
        }

        String kthSubstring(int u, long k) {
            if (k == 0) return "";
            for (int c = 0; c < ALPH; c++) {
                int nxt = next[u][c];
                if (nxt == -1) continue;
                long cnt = substrCount[nxt];
                if (k <= cnt) {
                    return (char)(c + 'a') + kthSubstring(nxt, k - 1);
                } else {
                    k -= cnt;
                }
            }
            return null;
        }
    }

    public static List<String> findStrings(List<String> w, List<Integer> queries) {
        StringBuilder sb = new StringBuilder();
        int sep = 123;
        for (int i = 0; i < w.size(); i++) {
            sb.append(w.get(i));
            sb.append((char)(sep + i));
        }
        String concat = sb.toString();
        SAM sam = new SAM(concat.length());
        for (char c : concat.toCharArray()) {
            sam.addChar(c);
        }
        sam.dfsCount(0);

        List<String> res = new ArrayList<>();
        for (int q : queries) {
            long k = q;
            if (k >= sam.substrCount[0]) {
                res.add("INVALID");
            } else {
                String s = sam.kthSubstring(0, k);
                int pos = 0;
                while (pos < s.length() && s.charAt(pos) >= 'a' && s.charAt(pos) <= 'z') pos++;
                res.add(s.substring(0, pos));
            }
        }
        return res;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int wCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> w = IntStream.range(0, wCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(toList());

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(String::trim).map(Integer::parseInt).collect(toList());

        List<String> result = Result.findStrings(w, queries);

        bufferedWriter.write(
            result.stream().collect(joining("\n")) + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
