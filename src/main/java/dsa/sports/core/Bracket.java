package dsa.sports.core;

import java.util.ArrayList;
import java.util.List;

public class Bracket {
    // rounds.get(0) = first round, rounds.get(1) = quarterfinals, etc.
    public List<List<Match>> rounds = new ArrayList<>();

    public Bracket() {}

    public void addRound(List<Match> matches) {
        rounds.add(matches);
    }

    public List<Match> getRound(int roundNumber) {
        if (roundNumber <= 0 || roundNumber > rounds.size()) return null;
        return rounds.get(roundNumber - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rounds.size(); r++) {
            sb.append("Round ").append(r + 1).append(":\n");
            for (Match m : rounds.get(r)) {
                sb.append("   ").append(m).append("\n");
            }
        }
        return sb.toString();
    }
}