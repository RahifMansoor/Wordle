package application;

import java.util.ArrayList;
import java.util.List;

public class GameStats {
    private List<String> gameResults = new ArrayList<>();
    private int wins = 0;
    private int gamesPlayed = 0; // Track the number of games played

    public void logResult(String result) {
        gameResults.add(result);
        gamesPlayed++; // Increment total games played with each result logged
        if ("Win".equals(result)) {
            wins++;
        }
    }

    // Method to display statistics in the console
    public void displayStats() {
        System.out.println("Game Results:");
        System.out.println("Total games played: " + gamesPlayed);
        System.out.println("Total wins: " + wins);
        System.out.println("Total losses: " + (gamesPlayed - wins)); // Calculating losses as games played minus wins
    }

    // Getter to retrieve the total number of wins
    public int getTotalWins() {
        return wins;
    }

    // Getter to retrieve the total number of games played
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    // Method to return all game results for possible review or analysis
    public List<String> getGameResults() {
        return new ArrayList<>(gameResults); // Return a copy of the results list
    }
}
