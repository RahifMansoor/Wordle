package application;

import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GameLogic {
    private String currentWord;
    private StringBuilder currentGuess = new StringBuilder();
    private WordSelector wordSelector;
    private ShadowData shadowData;
    private GameStats gameStats;
    private GameBoard gameBoard;  // Reference to GameBoard for alerts
    private boolean gameWon = false;
    private int attemptCount = 0;

    public GameLogic(WordSelector wordSelector, ShadowData shadowData, GameStats gameStats, GameBoard gameBoard) {
        this.wordSelector = wordSelector;
        this.shadowData = shadowData;
        this.gameStats = gameStats;
        this.gameBoard = gameBoard;
    }

    public void processGuessFromKeyboard(String letter) {
        if (currentGuess.length() < 5 && !gameWon && attemptCount < 6) {
            currentGuess.append(letter);
            Button letterButton = shadowData.getButtonAt(attemptCount, currentGuess.length() - 1);
            letterButton.setText(letter);
            letterButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: black;");
        }
    }
    

    public void processCurrentGuess() {
        if (currentGuess.length() == 5) {
            String guess = currentGuess.toString();
            if (wordSelector.isValidWord(guess)) {
                processGuess(guess);
                currentGuess.setLength(0); // Reset current guess for the next input
            } else {
                gameBoard.showAlert("Invalid Word", "This is not a valid word. Try again!", AlertType.WARNING);
                currentGuess.setLength(0); // Reset current guess even if it's not valid
                shadowData.clearCurrentAttempt(attemptCount); // Clear the current attempt from the board
            }
        }
    }

    private void processGuess(String guess) {
        // Ensure guess is in lowercase for consistency in comparison
        guess = guess.toLowerCase();
        currentWord = currentWord.toLowerCase(); // Ensure the currentWord is in lowercase
        boolean correctGuess = true;
        int[] letterCount = new int[26]; // Array to track occurrences of each letter in the current word

        // First, calculate the occurrences of each letter in the current word
        for (int i = 0; i < currentWord.length(); i++) {
            char c = currentWord.charAt(i);
            if (c >= 'a' && c <= 'z') { // Ensure that the character is a lowercase letter
                letterCount[c - 'a']++;
            }
        }

        // Check each guessed letter against the current word
        for (int i = 0; i < guess.length(); i++) {
            char guessedLetter = guess.charAt(i);
            Button gridButton = shadowData.getButtonAt(attemptCount, i);
            Button keyboardButton = gameBoard.getKeyboardButton(guessedLetter);

            if (guessedLetter == currentWord.charAt(i)) {
                gridButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                keyboardButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                letterCount[guessedLetter - 'a']--; // Decrement count as it's correctly guessed
            } else {
                correctGuess = false;
            }
        }

        // Second pass to check for presence of letters that are not in the correct position
        for (int i = 0; i < guess.length(); i++) {
            char guessedLetter = guess.charAt(i);
            Button gridButton = shadowData.getButtonAt(attemptCount, i);
            Button keyboardButton = gameBoard.getKeyboardButton(guessedLetter);

            if (guessedLetter != currentWord.charAt(i)) {
                if (letterCount[guessedLetter - 'a'] > 0) {
                    gridButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                    if (!"green".equals(keyboardButton.getStyle())) { // Only update if not already correct
                        keyboardButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                    }
                    letterCount[guessedLetter - 'a']--; // Decrement count as it's present
                } else {
                    gridButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
                    if (!"green".equals(keyboardButton.getStyle()) && !"yellow".equals(keyboardButton.getStyle())) {
                        keyboardButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
                    }
                }
            }
        }

        attemptCount++;
        if (correctGuess || attemptCount >= 6) {
            gameStats.logResult(correctGuess ? "Win" : "Lose", attemptCount);

            // Create and display the statistics chart in an alert
            BarChart<String, Number> statsChart = gameStats.getStatisticsChart();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(correctGuess ? "Congratulations!" : "Game Over");
            alert.setHeaderText(correctGuess ? "You've guessed the word correctly!" : "Out of attempts! The word was: " + currentWord);
            alert.getDialogPane().setContent(statsChart);
            alert.showAndWait();
        }
    }
    
    public void removeLastLetter() {
        if (currentGuess.length() > 0) {
            currentGuess.deleteCharAt(currentGuess.length() - 1);
            Button lastLetterButton = shadowData.getButtonAt(attemptCount, currentGuess.length());
            lastLetterButton.setText("");
            lastLetterButton.setStyle("");  // Reset to default style
           // System.out.println("Backspace applied: " + currentGuess);  // Debugging current guess state
        }
    }

    public void resetGame() {
        this.currentWord = wordSelector.getRandomWord();
        System.out.println("New game started with word " + this.currentWord);  // Debug statement
        this.shadowData.reset();
        this.gameBoard.resetKeyboard();
        this.gameWon = false;
        this.attemptCount = 0;
        gameBoard.showAlert("New Game", "New game started. Good luck!", Alert.AlertType.INFORMATION);
    }
}