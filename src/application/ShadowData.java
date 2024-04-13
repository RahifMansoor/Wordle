package application;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class ShadowData {
    private GridPane gridPane;
    private Button[][] buttons;
    private int currentRow = 0;  // Track the current row for the guesses

    public ShadowData(GridPane gridPane) {
        this.gridPane = gridPane;
        buttons = new Button[6][5];  // 6 attempts of 5 letters each
        initializeButtons();
    }

    private void initializeButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                Button button = new Button();
                button.setMinWidth(50);
                button.setMinHeight(50);
                button.setFont(Font.font(24));
                gridPane.add(button, j, i);
                buttons[i][j] = button;
            }
        }
    }

    public Button getButtonAt(int row, int column) {
        return buttons[row][column];
    }

    public void nextRow() {
        currentRow = Math.min(currentRow + 1, buttons.length - 1);
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void reset() {
        currentRow = 0;
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
                button.setStyle(null);
            }
        }
    }
}
