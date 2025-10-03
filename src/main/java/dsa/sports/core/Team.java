package dsa.sports.core;

public class Team {
    public String name;
    public double rating; // 0..100
    public int seed;      // set after seeding (1 = strongest)

    public Team() {}
    public Team(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }
}