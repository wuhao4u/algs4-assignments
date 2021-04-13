import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class BaseballElimination {
    // edge -> network -> ff
    Set<String> teams;
    FlowNetwork flowNetwork;
    FordFulkerson fordFulkerson;

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
     */
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        try (FileReader reader = new FileReader(filename)) {
            // while (reader)
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
        return -1;
    }

    public Iterable<String> teams() {
        // all teams

        return null;
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
