package dsa.sports.core;

import java.util.Random;

public class Match {
    public Team a;
    public Team b;
    public int round;   // 1 = first round, larger = later rounds
    public int slot;    // order within the round
    public Team winner; // set after play()

    public Match(Team a, Team b, int round, int slot) {
        this.a = a;
        this.b = b;
        this.round = round;
        this.slot = slot;
    }

    /** Bradley–Terry style probability that A beats B. */
    private double pWinA() {
        double ra = Math.max(1e-6, a.rating);
        double rb = Math.max(1e-6, b.rating);
        return ra / (ra + rb);
    }

    /** Simulate this match once and set winner. */
    public Team playOnce(Random rng) {
        double p = pWinA();
        boolean aWins = rng.nextDouble() < p;
        winner = aWins ? a : b;
        return winner;
    }

    @Override
    public String toString() {
        String an = (a == null ? "-" : a.name);
        String bn = (b == null ? "-" : b.name);
        String wn = (winner == null ? "?" : winner.name);
        return String.format("R%d S%d: %s vs %s -> %s", round, slot, an, bn, wn);
    }
}