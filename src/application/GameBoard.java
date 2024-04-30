package application;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameBoard {
    private Scene scene; //Main scene for the game
    private GridPane gridPane; //Layout for displaying the game grid
    private HBox keyboardRow1, keyboardRow2, keyboardRow3, actionRow; //Keyboard rows and action button row
    private Button[] keyboardButtons; //Buttons for each letter in the alphabet
    private Button submitButton, resetButton; //Buttons for submitting guesses and resetting the game
    private GameLogic gameLogic; //Handles the logic for the game
    private GameStats gameStats; //Tracks game statistics
    private ShadowData shadowData; //Manages shadow effects and visual updates for buttons

    public GameBoard() { 
        VBox root = new VBox(10); //Vertical box for arranging elements
        root.setAlignment(Pos.CENTER); //Center alignment for all elements in VBox
        
        //Attempt to load the game logo
        Image logoImage = null;
        try {
            logoImage = new Image(new FileInputStream("src\\application\\wordle_logo.png"));
        } catch (FileNotFoundException e) {
            System.err.println("Logo file not found: " + e.getMessage()); //Error message if file not found
        }

        if (logoImage != null) {
            ImageView logoView = new ImageView(logoImage);
            logoView.setPreserveRatio(true);
            logoView.setFitHeight(100); //Adjust logo size
            root.getChildren().add(logoView); //Add logo to the root VBox
        }

        gridPane = new GridPane(); //Initialize the grid pane
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5); //Vertical gap between grid elements
        gridPane.setHgap(5); //Horizontal gap between grid elements

        shadowData = new ShadowData(gridPane); //Initialize shadow data for button effects
        gameStats = new GameStats(); //Initialize game statistics
        initializeBoard(root); //Initialize the board elements
        initializeKeyboard(root); //Initialize the keyboard layout
        initializeActionButtons(root); //Initialize action buttons for game control

        String wordFilePath = "src/application/words.txt"; //Path to the word file
        WordSelector wordSelector = new WordSelector(wordFilePath); //Word selector for choosing and validating words
        gameLogic = new GameLogic(wordSelector, shadowData, gameStats, this); //Initialize game logic
        gameLogic.resetGame(); //Reset the game at the start
        gameStats.loadStatsFromFile("game_stats.txt"); //Load game statistics from file

        scene = new Scene(root, 600, 800); // Set the scene size
        setUpKeyHandlers(); //Setup key handlers for keyboard interactions
    }
    
    // Set up keyboard handlers to react to key presses
    private void setUpKeyHandlers() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                gameLogic.removeLastLetter(); //Remove last letter on backspace press
            }
        });
    }

    //Initialize the game board with instructions
    private void initializeBoard(VBox root) {
        Text instructions = new Text("Guess the 5-letter word. You have 6 attempts.");
        instructions.setFont(instructions.getFont().font(15));
        root.getChildren().add(instructions); //Add instructions to the root
        root.getChildren().add(gridPane); // add the grid pane to the root
    }

    //Initialize the keyboard layout with buttons for each letter
    private void initializeKeyboard(VBox root) {
        keyboardRow1 = new HBox(5);
        keyboardRow2 = new HBox(5);
        keyboardRow3 = new HBox(5);
        keyboardRow1.setAlignment(Pos.CENTER);
        keyboardRow2.setAlignment(Pos.CENTER);
        keyboardRow3.setAlignment(Pos.CENTER);

        String row1 = "QWERTYUIOP";
        String row2 = "ASDFGHJKL";
        String row3 = "ZXCVBNM";
        keyboardButtons = new Button[26]; //Allocate space for 26 alphabet buttons

        addKeyboardRow(keyboardRow1, row1, 0);
        addKeyboardRow(keyboardRow2, row2, row1.length());
        addKeyboardRow(keyboardRow3, row3, row1.length() + row2.length());

        root.getChildren().addAll(keyboardRow1, keyboardRow2, keyboardRow3); //Add keyboard rows to the VBox
    }

    //Add a row of keyboard buttons to the game
    private void addKeyboardRow(HBox row, String letters, int startIndex) {
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            Button btn = new Button(String.valueOf(letter));
            btn.setMinWidth(40);
            btn.setOnAction(event -> gameLogic.processGuessFromKeyboard(String.valueOf(letter)));
            keyboardButtons[Character.toUpperCase(letter) - 'A'] = btn; //Index button by letter
            row.getChildren().add(btn); //Add button to the row
        }
    }

    //Initialize action buttons for submitting guesses and resetting the game
    private void initializeActionButtons(VBox root) {
        actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER);

        submitButton = new Button("Submit");
        submitButton.setOnAction(event -> gameLogic.processCurrentGuess());
        resetButton = new Button("Reset");
        resetButton.setOnAction(event -> gameLogic.resetGame());

        actionRow.getChildren().addAll(submitButton, resetButton);
        root.getChildren().add(actionRow); //Add action row to the VBox
    }

    //Reset the keyboard by enabling all keys and resetting styles
    public void resetKeyboard() {
        for (Button btn : keyboardButtons) {
            btn.setDisable(false);
            btn.setStyle("");
        }
    }

    //Retrieve a specific keyboard button by letter
    public Button getKeyboardButton(char letter) {
        return keyboardButtons[Character.toUpperCase(letter) - 'A'];
    }

    //Display an alert with a specific title and message
    public void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Retrieve the main scene of the game
    public Scene getScene() {
        return this.scene;
    }
}
