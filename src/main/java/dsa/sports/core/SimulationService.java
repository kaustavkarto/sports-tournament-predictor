package dsa.sports.core;

import java.util.*;

public class SimulationService {

    /** Greedy seeding: sort teams by rating and pair strongest vs weakest. */
    public static List<Team> greedySeed(List<Team> teams) {
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> Double.compare(b.rating, a.rating));
        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).seed = i + 1;
        }
        return sorted;
    }

    /** Build the first round matches from seeded list. */
    public static List<Match> buildFirstRound(List<Team> seeded) {
        int n = seeded.size();
        List<Match> matches = new ArrayList<>();
        int slot = 1;
        for (int i = 0; i < n / 2; i++) {
            Team a = seeded.get(i);
            Team b = seeded.get(n - 1 - i);
            matches.add(new Match(a, b, 1, slot++));
        }
        return matches;
    }

    /** Create a bracket with the right number of rounds. */
    public static Bracket makeBracket(List<Team> seeded) {
        if ((seeded.size() & (seeded.size() - 1)) != 0) {
            throw new IllegalArgumentException("Team count must be a power of 2 (like 8, 16).");
        }

        Bracket bracket = new Bracket();
        List<Match> first = buildFirstRound(seeded);
        bracket.addRound(first);

        int size = seeded.size();
        while (size > 1) {
            size /= 2;
            List<Match> next = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                next.add(new Match(null, null, bracket.rounds.size() + 1, i + 1));
            }
            bracket.addRound(next);
        }
        return bracket;
    }

    /** Play one round, linking winners to the next round. */
    public static void playRound(Bracket bracket, int round, Random rng) {
        List<Match> matches = bracket.getRound(round);
        for (Match m : matches) {
            m.playOnce(rng);
        }
        // link winners to next round
        if (round < bracket.rounds.size()) {
            List<Match> next = bracket.getRound(round + 1);
            for (int i = 0; i < next.size(); i++) {
                next.get(i).a = matches.get(2 * i).winner;
                next.get(i).b = matches.get(2 * i + 1).winner;
            }
        }
    }

    /** Monte Carlo simulation: run many tournaments and count titles. */
    public static Map<String, Integer> simulateMany(List<Team> teams, int sims, long seed) {
        List<Team> seeded = greedySeed(teams);
        Map<String, Integer> titles = new HashMap<>();
        for (Team t : seeded) titles.put(t.name, 0);

        Random rng = new Random(seed);
        for (int s = 0; s < sims; s++) {
            Bracket br = makeBracket(seeded);

            for (int r = 1; r <= br.rounds.size(); r++) {
                playRound(br, r, rng);
            }

            Match finalMatch = br.rounds.get(br.rounds.size() - 1).get(0);
            titles.put(finalMatch.winner.name, titles.get(finalMatch.winner.name) + 1);
        }
        return titles;
    }
}