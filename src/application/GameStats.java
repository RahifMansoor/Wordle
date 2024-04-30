package application;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GameStats {
    private Map<Integer, Integer> attemptsDistribution; // Stores number of games ended with each attempt count
    private List<String> gameResults = new ArrayList<>(); //Stores the result of each game 
    private List<Integer> attemptsPerGame = new ArrayList<>(); //Stores the number of attempts for each game

    //Constructor initializes maps for storing game statistics
    public GameStats() {
        attemptsDistribution = new HashMap<>();
        //Initialize the distribution map for attempts from 1 to 6 and a category for 6+ attempts
        for (int i = 1; i <= 6; i++) {
            attemptsDistribution.put(i, 0);
        }
        attemptsDistribution.put(7, 0); //This is for 6+ fails
    }

    // Logs results of a game, updating statistics maps
    public void logResult(String result, int attempts) {
        gameResults.add(result);
        attemptsPerGame.add(attempts);

        // Increment count in the appropriate attempts distribution bucket
        if ("Win".equals(result)) {
            attemptsDistribution.put(attempts, attemptsDistribution.getOrDefault(attempts, 0) + 1);
        } else {
            attemptsDistribution.put(7, attemptsDistribution.getOrDefault(7, 0) + 1); //Log fails in the 6+ category
        }
    }

    //Generates a bar chart showing the distribution of attempts used in all games
    public BarChart<String, Number> getStatisticsChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Attempts to Guess");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Games");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Attempts Distribution");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Attempts");

        //Add data to the series
        for (Map.Entry<Integer, Integer> entry : attemptsDistribution.entrySet()) {
            String attemptLabel = entry.getKey() <= 6 ? String.valueOf(entry.getKey()) : "6+";
            series.getData().add(new XYChart.Data<>(attemptLabel, entry.getValue()));
        }

        barChart.getData().add(series);
        return barChart;
    }

    //Saves the current state of game statistics to a file
    public void saveStatsToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < gameResults.size(); i++) {
            lines.add(gameResults.get(i) + "," + attemptsPerGame.get(i));
        }
        try {
            Files.write(Paths.get(filename), lines);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    //Loads game statistics from a file
    public void loadStatsFromFile(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    gameResults.add(parts[0]);
                    attemptsPerGame.add(Integer.parseInt(parts[1]));
                    //Update attempts distribution based on loaded data
                    if (parts[0].contains("Win")) {
                        attemptsDistribution.put(Integer.parseInt(parts[1]), attemptsDistribution.getOrDefault(Integer.parseInt(parts[1]), 0) + 1);
                    } else {
                        attemptsDistribution.put(7, attemptsDistribution.getOrDefault(7, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }
}
