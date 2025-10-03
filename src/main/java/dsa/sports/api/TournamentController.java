package dsa.sports.api;

import dsa.sports.core.Team;
import dsa.sports.core.Match;
import dsa.sports.core.SimulationService;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin // allows calls from a frontend (localhost:3000, etc.)
public class TournamentController {

    // ---- Request/Response DTOs ----
    public static class TeamIn {
        public String name;
        public double rating;
    }

    public static class SeedResponse {
        public List<Team> seeded;
        public List<Match> firstRound;
    }

    public static class SimRequest {
        public List<TeamIn> teams;
        public int simulations = 2000;
        public Long randomSeed; // optional
    }

    public static class SimResult {
        public Map<String, Integer> titleCounts;
        public Map<String, Double> titleProb;
    }

    // ---- Endpoints ----

    @PostMapping("/seed")
    public SeedResponse seed(@RequestBody List<TeamIn> teamsIn) {
        List<Team> teams = new ArrayList<>();
        for (TeamIn t : teamsIn) teams.add(new Team(t.name, t.rating));

        List<Team> seeded = SimulationService.greedySeed(teams);
        SeedResponse res = new SeedResponse();
        res.seeded = seeded;
        res.firstRound = SimulationService.buildFirstRound(seeded);
        return res;
    }

    @PostMapping("/simulate")
    public SimResult simulate(@RequestBody SimRequest req) {
        List<Team> teams = new ArrayList<>();
        for (TeamIn t : req.teams) teams.add(new Team(t.name, t.rating));

        int sims = Math.max(1, req.simulations);
        long seed = (req.randomSeed == null) ? System.currentTimeMillis() : req.randomSeed;

        Map<String, Integer> counts = SimulationService.simulateMany(teams, sims, seed);

        SimResult out = new SimResult();
        out.titleCounts = counts;

        Map<String, Double> prob = new LinkedHashMap<>();
        for (var e : counts.entrySet())
        { prob.put(e.getKey(), e.getValue() * 1.0 / sims);
        out.titleProb = prob;
        }

        return out;
    }
}