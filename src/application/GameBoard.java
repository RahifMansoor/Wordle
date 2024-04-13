package application;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameBoard {
    private Scene scene;
    private GridPane gridPane;
    private GameLogic gameLogic;
    private GameStats gameStats;
    private ShadowData shadowData;
    private TextField inputField;

    public GameBoard() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        shadowData = new ShadowData(gridPane);
        gameStats = new GameStats();
        String wordFilePath = "src/application/words.txt";  // Ensure this file exists
        WordSelector wordSelector = new WordSelector(wordFilePath);
        gameLogic = new GameLogic(wordSelector, shadowData, gameStats, this);  // Pass GameBoard reference
        initializeBoard(root);
        scene = new Scene(root, 400, 500);
    }

    private void initializeBoard(VBox root) {
        inputField = new TextField();
        inputField.setPromptText("Enter a 5-letter word");
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> handleGuess());
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> gameLogic.resetGame());

        Text instructions = new Text("Guess the 5-letter word. You have 6 attempts.");
        instructions.setFont(instructions.getFont().font(15));

        root.getChildren().addAll(instructions, gridPane, inputField, submitButton, resetButton);
    }

    private void handleGuess() {
        String guess = inputField.getText().trim().toLowerCase();
        if (guess.length() != 5) {
            showAlert("Error", "Please enter a 5-letter word.", AlertType.ERROR);
            return;
        }
        gameLogic.processGuess(guess);
        inputField.clear();
    }

    public void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return this.scene;
    }
}
