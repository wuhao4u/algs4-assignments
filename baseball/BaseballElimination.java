import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class BaseballElimination {
    /*
    A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place.
    Trivial elimination. If the maximum number of games team x can win is less than the number of wins of some other team i, then team x is trivially eliminated

    Nontrivial elimination. Otherwise, we create a flow network and solve a maxflow problem in it.
    In the network, feasible integral flows correspond to outcomes of the remaining schedule.
    There are vertices corresponding to teams (other than team x) and
    to remaining divisional games (not involving team x).
    Intuitively, each unit of flow in the network corresponds to a remaining game.
    As it flows through the network from s to t, it passes from a game vertex, say between teams i and j,
    then through one of the team vertices i or j, classifying this game as being won by that team.


                w[i] l[i] r[i]          g[i][j]
i  team         wins loss left   NY Bal Bos Tor Det
---------------------------------------------------
0  New York      75   59   28     -   3   8   7   3
1  Baltimore     71   63   28     3   -   2   7   7
2  Boston        69   66   27     8   2   -   0   3
3  Toronto       63   72   27     7   7   0   -   3
4  Detroit       49   86   27     3   7   3   3   -

5
New_York    75 59 28   0 3 8 7 3
Baltimore   71 63 28   3 0 2 7 7
Boston      69 66 27   8 2 0 0 3
Toronto     63 72 27   7 7 0 0 3
Detroit     49 86 27   3 7 3 3 0

     */

    // edge -> network -> ff
    int numOfTeams;
    Set<String> teams;
    FlowNetwork flowNetwork;
    FordFulkerson fordFulkerson;
    int[] w, l, r;
    int[][] g;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String inValue = br.readLine();
            this.numOfTeams = Integer.parseInt(inValue);

            // New_York    75 59 28   0 3 8 7 3
            int numOfLines = this.numOfTeams+1;
            for (int i = 1; i < numOfLines; ++i) {
                inValue = br.readLine();
                String[] line = inValue.trim().split("\\s\\s+");
                System.out.println(line[0]);
                System.out.println(line[1]);
                System.out.println(line[2]);
                System.out.println("-----------------------------------");
            }

            /*
            while ((inValue = br.readLine()) != null) {
                String[] line = inValue.trim().split("\\s\\s+");
                System.out.println(line[0]);
                System.out.println(line[1]);
                System.out.println(line[2]);
                System.out.println("-----------------------------------");
            }
             */
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int numberOfTeams() {
        // number of teams
        return this.numOfTeams;
    }

    public Iterable<String> teams() {
        // all teams

        return new ArrayList<>();
    }

    public int wins(String team) {
        // number of wins for given team

        return -1;
    }

    public int losses(String team) {
        // number of losses for given team

        return -1;
    }

    public int remaining(String team) {
        // number of remaining games for given team

        return -1;
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2

        return -1;
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?

        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated

        return null;
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
