package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameBoard gameBoard = new GameBoard();
        primaryStage.setScene(gameBoard.getScene());
        primaryStage.setTitle("JavaFX Wordle");
        primaryStage.show();

        showInstructionsPopup(); // Show instructions after the main window is visible
    }

    public void showInstructionsPopup() {
        // Create a new stage (window)
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL); // Set modality to block interaction with other windows
        dialog.setTitle("How to Play");

        //Create components for the dialog
        Label instructionsLabel = new Label("Welcome to Wordle!\n\n"
            + "Instructions:\n"
            + "- Guess the 5-letter word and type it into the input field.\n"
            + "- The color of the tiles changes to show how close your guess was.\n"
            + "  Green: Correct letter in the correct position.\n"
            + "  Yellow: Correct letter in the wrong position.\n"
            + "  Grey: Incorrect letter.\n"
            + "- You have six attempts to guess the correct word.");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close()); // Close the dialog when the button is clicked

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructionsLabel, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Set the scene and show the stage
        Scene dialogScene = new Scene(layout, 400, 250);
        dialog.setScene(dialogScene);
        dialog.showAndWait(); // Show the dialog and wait for it to be closed before returning focus
    }

    public static void main(String[] args) {
        launch(args);
    }
}
