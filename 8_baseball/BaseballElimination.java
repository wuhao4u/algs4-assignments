import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BaseballElimination {
    // edge -> network -> ff
    final private int T;
    final private HashMap<String, Integer> teamToIndex;
    final private HashMap<Integer, String> indexToTeam;
    final private int[] w, l, r;
    final private int[][] g;
    final private HashMap<String, Set<String>> certOfElimination;
    private boolean[] isEliminated;

    public BaseballElimination(String filename) {
        // step 1: Write code to read in the input file and store the data.
        In in = new In(filename);
        this.T = in.readInt();

        this.teamToIndex = new HashMap<>(T);
        this.indexToTeam = new HashMap<>(T);
        this.w = new int[T];
        this.l = new int[T];
        this.r = new int[T];
        this.g = new int[T][T];
        this.certOfElimination = new HashMap<>(T);
        this.isEliminated = new boolean[T];

        for (int i = 0; i < T; ++i) {
            // team name
            String teamName = in.readString();
            this.teamToIndex.put(teamName, i);
            this.indexToTeam.put(i, teamName);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();

            for (int j = 0; j < T; ++j) {
                g[i][j] = in.readInt();
            }
        }

        for (int t = 0; t < this.T; ++t) {
            this.preprocessing(t, this.indexToTeam.get(t));
        }
    }

    private void preprocessing(int teamIndex, String team) {
        Set<String> eliminated = this.getTrivialEliminator(teamIndex, team);
        Set<String> nonTrivialEliminated = this.getNonTrivialEliminator(teamIndex, team);

        eliminated.addAll(nonTrivialEliminated);

        if (eliminated.isEmpty()) {
            this.isEliminated[teamIndex] = false;
        } else {
            this.isEliminated[teamIndex] = true;
            this.certOfElimination.put(team, eliminated);
        }
    }

    private Set<String> getTrivialEliminator(int teamIndex, String team) {
        if (team == null || team.isEmpty())  throw new IllegalArgumentException();

        Set<String> trivialEliminater = new HashSet<>();

        // If the maximum number of games team x can win is less than the number of wins of some other team i,
        // then team x is trivially eliminated. That is, if w[x] + r[x] < w[i], then team x is mathematically eliminated.
        int mostWinPossible = this.w[teamIndex] + this.r[teamIndex];

        for (int i = 0; i < this.T; ++i) {
            if (i == teamIndex) continue;

            if (mostWinPossible < this.w[i]) {
                trivialEliminater.add(this.indexToTeam.get(i));
            }
        }
        return trivialEliminater;
    }

    private Set<String> getNonTrivialEliminator(int teamIndex, String team) {
        if (team == null || team.isEmpty())  throw new IllegalArgumentException();

        Set<String> eliminator = new HashSet<>();

        // number of vertices in FF graph, for the current team, team X to test on
        // int totalV =  T * (T - 1) / 2 + (this.T - 1) + 2 + 1;
        int totalV = T * T; // total matrix cell
        totalV -= T; // team cannot against self
        totalV /= 2; // a vs. b is the same as b vs. a
        totalV = totalV - T + 1; // minus team against the current team
        totalV = totalV + (T - 1) + 2;

        FlowNetwork flowNetwork = new FlowNetwork(totalV);
        FordFulkerson fordFulkerson;

        // v0 is s, totalV-1 is t
        int vPtr = 1;

        // teamIndex in g, vertex index in flowNetwork
        HashMap<Integer, Integer> teamVertexMap = new HashMap<>(this.T - 1);
        for (int t = 0; t < this.T; ++t) {
            if (t == teamIndex) continue;
            teamVertexMap.put(t, vPtr);

            int toTCap = w[teamIndex] + r[teamIndex] - w[t];
            // connecting team vertices to t: w4 + r4 - w2
            // new FlowEdge(int v, int w, double capacity);
            if (toTCap < 0) {
                toTCap = 0;
            }
            FlowEdge teamVToT = new FlowEdge(vPtr, totalV - 1, toTCap);
            flowNetwork.addEdge(teamVToT);

            ++vPtr;
        }

        // connecting S to game vertices
        for (int row = 0; row < this.T; ++row) {
            if (row == teamIndex) continue;

            // only scan the left side of the game matrix,
            // avoiding duplicated game vertices being added
            for (int col = 0; col < row; ++col) {
                if (col == teamIndex) continue;

                FlowEdge sToMatchV = new FlowEdge(0, vPtr, this.g[row][col]);
                flowNetwork.addEdge(sToMatchV);

                FlowEdge matchVToTeamV1 = new FlowEdge(vPtr, teamVertexMap.get(row), Double.POSITIVE_INFINITY);
                FlowEdge matchVToTeamV2 = new FlowEdge(vPtr, teamVertexMap.get(col), Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(matchVToTeamV1);
                flowNetwork.addEdge(matchVToTeamV2);

                ++vPtr;
            }
        }

        // System.out.println(flowNetwork.toString());

        fordFulkerson = new FordFulkerson(flowNetwork, 0, totalV - 1);

        // int maxFlowTargetVal = (this.T - 1) * (w[teamIndex] + r[teamIndex]) - (Arrays.stream(this.w).sum() - w[teamIndex]);

        // if (maxFlowTargetVal == fordFulkerson.value()) {
        //     // maxflow, team is not out
        //     System.out.println("Team: " + team + " is not eliminated.");
        // }

        // teamVertexMap: t, vPtr
        for (int t = 0; t < this.T; ++t) {
            if (t == teamIndex) continue;

            if (fordFulkerson.inCut(teamVertexMap.get(t))) {
                eliminator.add(this.indexToTeam.get(t));
            }
        }

        return eliminator;
    }

    public int numberOfTeams() {
        // number of teams
        return this.T;
    }

    public Iterable<String> teams() {
        // all teams
        return this.teamToIndex.keySet();
    }

    private void teamInputCheck(String team) {
        if (team == null || team.isEmpty())
            throw new IllegalArgumentException();

        if (!this.teamToIndex.containsKey(team))
            throw new IllegalArgumentException();
    }

    public int wins(String team) {
        teamInputCheck(team);

        // number of wins for given team
        return this.w[this.teamToIndex.get(team)];
    }

    public int losses(String team) {
        teamInputCheck(team);

        // number of losses for given team
        return this.l[this.teamToIndex.get(team)];
    }

    public int remaining(String team) {
        teamInputCheck(team);

        // number of remaining games for given team
        return this.r[this.teamToIndex.get(team)];
    }

    public int against(String team1, String team2) {
        teamInputCheck(team1);
        teamInputCheck(team2);
        if (team1.equals(team2)) throw new IllegalArgumentException();

        // number of remaining games between team1 and team2
        int t1Index = this.teamToIndex.get(team1);
        int t2Index = this.teamToIndex.get(team2);

        return g[t1Index][t2Index];
    }

    public boolean isEliminated(String team) {
        teamInputCheck(team);

        // is given team eliminated?
        return this.isEliminated[this.teamToIndex.get(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        teamInputCheck(team);
        // subset R of teams that eliminates given team; null if not eliminated

        if (this.isEliminated(team)) {
            return this.certOfElimination.get(team);
        } else {
            return null;
        }
    }



    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
        }
    }
}
