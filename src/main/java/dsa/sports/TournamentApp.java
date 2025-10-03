package dsa.sports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TournamentApp {

    public static void main(String[] args) {
        SpringApplication.run(TournamentApp.class, args);
        System.out.println("✅ Sports Tournament Predictor is running...");
    }
}
