package application;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class ShadowData {
    private GridPane gridPane; // Holds the grid where the game buttons are displayed
    private Button[][] buttons; // 2D array to store buttons representing the letter inputs
    private int currentRow = 0; // Tracks the current row for guesses

    // Constructor initializes the ShadowData with a given gridPane
    public ShadowData(GridPane gridPane) {
        this.gridPane = gridPane;
        buttons = new Button[6][5]; //Initialize buttons for 6 attempts, each with 5 letters
        initializeButtons(); //Call to method that sets up the buttons on the grid
    }

    // Initializes buttons on the gridPane
    private void initializeButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                Button button = new Button();
                button.setMinWidth(50); 
                button.setMinHeight(50); 
                button.setFont(Font.font(24)); 
                gridPane.add(button, j, i); 
                buttons[i][j] = button; // Store button in the array for future reference
            }
        }
    }

    // Clears all text and styles from buttons in the specified attempt row
    public void clearCurrentAttempt(int attempt) {
        for (int i = 0; i < 5; i++) {
            Button button = getButtonAt(attempt, i);
            button.setText(""); // Clear the text on the button
            button.setStyle(""); // Reset style to default
        }
    }

    // Retrieves a button at a specific row and column
    public Button getButtonAt(int row, int column) {
        return buttons[row][column]; // Return the button located at specified row and column
    }

    // Moves to the next row for guessing
    public void nextRow() {
        currentRow = Math.min(currentRow + 1, buttons.length - 1); //Increment currentRow
    }

    // Returns the current active row for guesses
    public int getCurrentRow() {
        return currentRow; 
    }

    // Resets all buttons to initial state, clearing texts and styles
    public void reset() {
        currentRow = 0; // Reset current row to 0
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText(""); // Clear text on button
                button.setStyle(null); 
            }
        }
    }
}
