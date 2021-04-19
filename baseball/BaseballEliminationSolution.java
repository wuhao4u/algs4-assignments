
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballEliminationSolution {

    private final int N;
    private final int[] wins, losses, remaining;
    private final int[][] against;
    private final ArrayList<String> teams;
    private ArrayList<String>[] coe;

    // create a baseball division from given filename in format specified below
    public BaseballEliminationSolution(String filename) {
        In in = new In(filename);
        N = in.readInt();
        teams = new ArrayList<String>(N);
        wins = new int[N];
        losses = new int[N];
        remaining = new int[N];
        // ?, not remaining, but against?
        against = new int[N][N];

        // ?
        coe = (ArrayList<String>[]) new ArrayList[N];

        // ?
        byte[] opponents = new byte[N];

        // best, is the number of wins for the team with the most wins so far
        // winnder, is the index for the team with the most wins so far
        int best = 0, winner = 0;

        for (int i = 0; i < N; i++) {
            // i is the current team index

            teams.add(in.readString());
            wins[i] = in.readInt();
            if (wins[i] > best) {
                // best, number of most wins
                best = wins[i];

                // winnder, team index
                winner = i;
            }
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < N; j++) {
                against[i][j] = in.readInt();
                if (j > i && against[i][j] > 0) {
                    opponents[i]++;
                    opponents[j]++;
                }
            }
        }

        calcElimination(opponents, best, winner);
    }

    private void calcElimination(byte[] opponents, int best, int winner) {
        int matches = 0;
        for (int i = 0; i < N; i++) matches += opponents[i];
        matches /= 2;
        // main loop
        double inf = Double.POSITIVE_INFINITY;
        for (int i = 0; i < N; i++) {
            int ideal = wins[i] + remaining[i];
            if (ideal < best) {
                coe[i] = new ArrayList<String>(N-1);
                coe[i].add(teams.get(winner));
                continue;
            }
            int cMatches = matches - opponents[i];
            FlowNetwork fn = new FlowNetwork(cMatches+N+2);
            int round = 1, fullFlow = 0;
            for (int m = 0; m < N; m++) {
                for (int n = m+1; n < N; n++) {
                    if (m == i || n == i || against[m][n] == 0) continue;
                    fullFlow += against[m][n];
                    fn.addEdge(new FlowEdge(0, round, against[m][n]));
                    fn.addEdge(new FlowEdge(round, cMatches+m+1, inf));
                    fn.addEdge(new FlowEdge(round++, cMatches+n+1, inf));
                }
            }
            for (int j = 0; j < N; j++) {
                if (j == i) continue;
                fn.addEdge(new FlowEdge(cMatches+j+1, cMatches+N+1, ideal-wins[j]));
            }
            FordFulkerson ff = new FordFulkerson(fn, 0, cMatches+N+1);
            if ((int) ff.value() == fullFlow) continue;
            coe[i] = new ArrayList<String>(N-1);
            for (int j = 0; j < N; j++)
                if (ff.inCut(cMatches+j+1)) coe[i].add(teams.get(j));
        }
    }

    // number of teams
    public int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return losses[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return remaining[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int id1 = teams.indexOf(team1), id2 = teams.indexOf(team2);
        if (id1 < 0 || id2 < 0) throw new IllegalArgumentException();
        return against[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return coe[index] != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int index = teams.indexOf(team);
        if (index < 0) throw new IllegalArgumentException();
        return coe[index];
    }

    public static void main(String[] args) {
        BaseballEliminationSolution division = new BaseballEliminationSolution(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}