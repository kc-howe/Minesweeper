/*
 * Citations:
 * - Minesweeper icons: https://commons.wikimedia.org/wiki/Category:Minesweeper
 * - Help with triggering non-button onMouseClick events: https://stackoverflow.com/questions/40041625/how-to-fire-mouse-event-programmatically-in-javafx
 * - Help with creating information alerts: https://code.makery.ch/blog/javafx-dialogs-official/
 * - Help with speeding up "New Game" initialization with HashMap: Michelle Cheatham, e-mail correspondance, Feb-11-2020
*/
package minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A loose replica of the game Minesweeper as it appeared
 * on Windows XP.
 * 
 * @author Kenneth
 */
public class Minesweeper extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox root = new VBox();
        BorderPane bPane = new BorderPane();
        Label label = new Label();
        Button button = new Button("New Game");
        button.setFont(new Font("Century Gothic", 14));
        GridPane gPane = new GridPane();
        
        //Display the rules of the game
        Alert alert = new Alert(AlertType.INFORMATION);
        
        alert.setTitle("Welcome to Minesweeper!");
        alert.setGraphic((Node) new ImageView(new Image("file:src/minesweeper/icon.png", 50, 50, false, false, false))); //Change graphic
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/minesweeper/icon.png")); //Change icon
        
        alert.setHeaderText("How to Play");
        alert.setContentText("1. Click on a tile to uncover it.\n\n" +
                "2. If you uncover a mine, you lose.\n\n" +
                "3. Numbered tiles tell you how many mines are\n    hidden under neighboring tiles.\n\n" +
                "4. Uncover all non-mine tiles to win!\n");

        alert.showAndWait();
        
        // Allow the user to set the difficulty (i.e. board size)
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Beginner");
        choices.add("Intermediate");
        choices.add("Expert");
        
        String difficulty = "Choose an option";
        ChoiceDialog chooseDifficulty = new ChoiceDialog(difficulty, choices);
        chooseDifficulty.setTitle("Select Difficulty");
        chooseDifficulty.setHeaderText("Select a difficulty");
        chooseDifficulty.setGraphic((Node) new ImageView(new Image("file:src/minesweeper/icon.png", 50, 50, false, false, false))); //Change graphic
        ((Stage)chooseDifficulty.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:src/minesweeper/icon.png")); //Change icon
        
        // Initialize board size
        int width = 0;
        int height = 0;
        
        // Try to process the input until the user makes a valid decision
        while(difficulty.equals("Choose an option")) {
            Optional<String> userChoice = chooseDifficulty.showAndWait();
            if(userChoice.isPresent()) {
                difficulty = userChoice.get();
            }

            switch(difficulty) {
                case "Beginner":
                    width = 8;
                    height = 8;
                    break;
                case "Intermediate":
                    width = 16;
                    height = 16;
                    break;
                case "Expert":
                    width = 30;
                    height = 16;
                    break;
                default :
                    assert true;
                    break;
            }
        }
        
        final int widthFinal = width;
        final int heightFinal = height;
        
        // Create a HashMap to store the images we want to use for building the game board
        HashMap<Integer, Image> images = new HashMap<>();
        for(int i=-3; i<9; i++) {
            images.put(i, new Image("file:src/minesweeper/"+i+".png", 30, 30, false, false, false));
        }
        
        GameBoard gameBoard = new GameBoard(width, height, images);
        gameBoard.initializeNewGame(label, gPane, images);
        
        ArrayList<GameBoard> game = new ArrayList<>();
        game.add(gameBoard);
        
        // Make the "New Game" button initialize a new game
        button.setOnAction((ActionEvent e) -> {
            game.set(0, new GameBoard(widthFinal, heightFinal, images));
            game.get(0).initializeNewGame(label, gPane, images);
        });
        
        // Initialize game board header
        bPane.setLeft(label);
        bPane.setRight(button);
        BorderPane.setAlignment((Node) label, Pos.CENTER);
        root.getChildren().addAll(bPane, gPane);
        
        Scene scene = new Scene(root, 30*width-12, 16+30*height);
        
        primaryStage.setTitle("Minesweeper");
        primaryStage.getIcons().add(new Image("file:src/minesweeper/icon.png")); //Change app icon
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
