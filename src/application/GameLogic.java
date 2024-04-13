package application;

import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameLogic {
    private String currentWord;
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
        resetGame();
    }

    public void processGuess(String guess) {
        if (guess.length() != 5) {
            gameBoard.showAlert("Input Error", "Please enter a 5-letter word.", Alert.AlertType.ERROR);
            return;
        }

        if (gameWon || attemptCount >= 6) {
            gameBoard.showAlert("Game Over", "Game over! Please reset to play again.", Alert.AlertType.INFORMATION);
            return;
        }

        boolean correctGuess = true;
        for (int i = 0; i < guess.length(); i++) {
            char guessedLetter = guess.charAt(i);
            Button letterButton = shadowData.getButtonAt(attemptCount, i);
            letterButton.setText(String.valueOf(guessedLetter));

            if (guessedLetter == currentWord.charAt(i)) {
                letterButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            } else {
                correctGuess = false;
                if (currentWord.contains(String.valueOf(guessedLetter))) {
                    letterButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                } else {
                    letterButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
                }
            }
        }

        attemptCount++;
        if (correctGuess) {
            gameWon = true;
            gameStats.logResult("Win");
            gameBoard.showAlert("Congratulations!", "You've guessed the word correctly!\nGame Results:\nTotal wins: " + gameStats.getTotalWins(), Alert.AlertType.INFORMATION);
        } else if (attemptCount == 6) {
            gameStats.logResult("Lose");
            gameBoard.showAlert("Game Over", "Out of attempts! The word was: " + currentWord, Alert.AlertType.INFORMATION);
        }
    }

    public void resetGame() {
        this.currentWord = wordSelector.getRandomWord();
        this.shadowData.reset();
        this.gameWon = false;
        this.attemptCount = 0;
        gameBoard.showAlert("New Game", "New game started. Good luck!", Alert.AlertType.INFORMATION);
    }
}
