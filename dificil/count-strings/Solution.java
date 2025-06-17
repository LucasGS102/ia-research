import java.util.*;

class State {
    Map<Character, List<State>> edges = new HashMap<>();
    List<State> epsilon = new ArrayList<>();
}

class Fragment {
    State start;
    List<State> accepts;

    Fragment(State start, List<State> accepts) {
        this.start = start;
        this.accepts = accepts;
    }
}

public class Solution {
    static final int MOD = 1_000_000_007;

    static Fragment parse(String s) {
        if (s.equals("a") || s.equals("b")) {
            State s0 = new State();
            State s1 = new State();
            s0.edges.computeIfAbsent(s.charAt(0), k -> new ArrayList<>()).add(s1);
            return new Fragment(s0, List.of(s1));
        }
        if (s.charAt(0) != '(' || s.charAt(s.length() - 1) != ')') throw new RuntimeException();
        String inner = s.substring(1, s.length() - 1);
        if (inner.endsWith("*")) {
            Fragment frag = parse(inner.substring(0, inner.length() - 1));
            State s0 = new State();
            State s1 = new State();
            s0.epsilon.add(frag.start);
            s0.epsilon.add(s1);
            for (State a : frag.accepts) {
                a.epsilon.add(frag.start);
                a.epsilon.add(s1);
            }
            return new Fragment(s0, List.of(s1));
        }
        int depth = 0;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == '|' && depth == 0) {
                Fragment left = parse(inner.substring(0, i));
                Fragment right = parse(inner.substring(i + 1));
                State s0 = new State();
                State s1 = new State();
                s0.epsilon.add(left.start);
                s0.epsilon.add(right.start);
                for (State a : left.accepts) a.epsilon.add(s1);
                for (State a : right.accepts) a.epsilon.add(s1);
                return new Fragment(s0, List.of(s1));
            }
        }
        int mid = splitConcat(inner);
        Fragment left = parse(inner.substring(0, mid));
        Fragment right = parse(inner.substring(mid));
        for (State a : left.accepts) a.epsilon.add(right.start);
        return new Fragment(left.start, right.accepts);
    }

    static int splitConcat(String s) {
        int depth = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i - 1) == '(') depth++;
            else if (s.charAt(i - 1) == ')') depth--;
            if (depth == 0) return i;
        }
        throw new RuntimeException();
    }

    static Map<State, Integer> buildNFA(State start, List<State> allStates) {
        Map<State, Integer> idMap = new HashMap<>();
        Deque<State> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            State curr = stack.pop();
            if (idMap.containsKey(curr)) continue;
            int id = idMap.size();
            idMap.put(curr, id);
            allStates.add(curr);
            for (List<State> list : curr.edges.values()) stack.addAll(list);
            stack.addAll(curr.epsilon);
        }
        return idMap;
    }

    static Set<Integer> epsilonClosure(List<State> states, Map<State, Integer> idxMap, int start) {
        Set<Integer> closure = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        closure.add(start);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (State v : states.get(u).epsilon) {
                int vid = idxMap.get(v);
                if (!closure.contains(vid)) {
                    closure.add(vid);
                    stack.push(vid);
                }
            }
        }
        return closure;
    }

    static Object[] buildDFA(List<State> states, Map<State, Integer> idxMap, Fragment frag) {
        Map<Set<Integer>, Integer> dfaStates = new HashMap<>();
        List<Set<Integer>> dfaList = new ArrayList<>();
        List<Map<Character, Integer>> trans = new ArrayList<>();
        Queue<Set<Integer>> queue = new LinkedList<>();
        Set<Integer> startSet = epsilonClosure(states, idxMap, idxMap.get(frag.start));
        dfaStates.put(startSet, 0);
        dfaList.add(startSet);
        trans.add(new HashMap<>());
        queue.add(startSet);
        Set<Integer> accept = new HashSet<>();
        for (State a : frag.accepts) accept.add(idxMap.get(a));
        Set<Integer> accepting = new HashSet<>();
        while (!queue.isEmpty()) {
            Set<Integer> curr = queue.poll();
            int currId = dfaStates.get(curr);
            for (char c : List.of('a', 'b')) {
                Set<Integer> nextSet = new HashSet<>();
                for (int u : curr) {
                    for (State v : states.get(u).edges.getOrDefault(c, List.of())) {
                        nextSet.addAll(epsilonClosure(states, idxMap, idxMap.get(v)));
                    }
                }
                if (nextSet.isEmpty()) continue;
                if (!dfaStates.containsKey(nextSet)) {
                    dfaStates.put(nextSet, dfaList.size());
                    dfaList.add(nextSet);
                    trans.add(new HashMap<>());
                    queue.add(nextSet);
                }
                trans.get(currId).put(c, dfaStates.get(nextSet));
            }
        }
        for (Map.Entry<Set<Integer>, Integer> e : dfaStates.entrySet()) {
            for (int x : e.getKey()) {
                if (accept.contains(x)) {
                    accepting.add(e.getValue());
                    break;
                }
            }
        }
        return new Object[]{trans, 0, accepting};
    }

    static int[][] matMult(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int k = 0; k < n; k++)
                if (a[i][k] != 0)
                    for (int j = 0; j < n; j++)
                        res[i][j] = (int)((res[i][j] + 1L * a[i][k] * b[k][j]) % MOD);
        return res;
    }

    static int[][] matPow(int[][] mat, int power) {
        int n = mat.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) res[i][i] = 1;
        while (power > 0) {
            if ((power & 1) != 0) res = matMult(res, mat);
            mat = matMult(mat, mat);
            power >>= 1;
        }
        return res;
    }

    static int countStrings(String regex, int L) {
        Fragment frag = parse(regex);
        List<State> allStates = new ArrayList<>();
        Map<State, Integer> idxMap = buildNFA(frag.start, allStates);
        Object[] dfa = buildDFA(allStates, idxMap, frag);
        List<Map<Character, Integer>> trans = (List<Map<Character, Integer>>) dfa[0];
        int start = (int) dfa[1];
        Set<Integer> accepting = (Set<Integer>) dfa[2];
        int n = trans.size();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++)
            for (char c : List.of('a', 'b'))
                if (trans.get(i).containsKey(c))
                    mat[i][trans.get(i).get(c)]++;
        int[][] matL = matPow(mat, L);
        int total = 0;
        for (int i : accepting) total = (total + matL[start][i]) % MOD;
        return total;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = Integer.parseInt(sc.nextLine());
        for (int t = 0; t < T; t++) {
            String[] parts = sc.nextLine().split(" ");
            String R = parts[0];
            int L = Integer.parseInt(parts[1]);
            System.out.println(countStrings(R, L));
        }
    }
}
