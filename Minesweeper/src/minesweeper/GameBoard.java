/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author Kenneth
 */
public class GameBoard {
    private ArrayList<Tile> tiles;
    private int mineCount;
    private int coveredCount;
    
    public GameBoard(int width, int height, HashMap<Integer, Image> images) {
        this.tiles = new ArrayList<>();
        
        for(int i=0; i < width; i++) {
            for(int j=0; j < height; j++) {
                tiles.add(new Tile(i, j, images));
            }   
        }
        
        this.mineCount = 0;
        this.coveredCount = tiles.size();
    }
    
    /**
     * Initializes a new game by assigning tiles status as mines,
     * initializing the score, and setting actions/images for tiles.
     * 
     * @param label - Label displayed above game board
     * @param gPane - The GridPane that defines the game board
     * @param images - HashMap containing the images that make up the game board
     */
    public void initializeNewGame(Label label, GridPane gPane, HashMap<Integer, Image> images) {
        for(Tile t: this.tiles) {
            
            // Randomly choose if this tile will be a mine
            if(Math.random() < 0.15) {
                t.setIsAMine(true);
                this.mineCount += 1;
                t.setImage(images.get(-3));
                GridPane.setConstraints(t.getUncoveredImg(), t.getX(), t.getY());
            }
            
            // Initialize the displayed score
            label.setText(" Score: " + (tiles.size() - this.coveredCount) + "/" + (tiles.size() - this.mineCount));
            label.setFont(new Font("Century Gothic", 16));
            
            GridPane.setConstraints(t.getCoveredImg(), t.getX(), t.getY());
            GridPane.setConstraints(t.getFlaggedImg(), t.getX(), t.getY());
            gPane.getChildren().add(t.getCoveredImg());
            
            // Set on-click action for each tile
            t.getCoveredImg().setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.PRIMARY) {
                    gPane.getChildren().remove((Node) t.getCoveredImg());
                    gPane.getChildren().add((Node) t.getUncoveredImg());

                    // Mines end the game
                    if(t.getIsAMine() && !t.getIsFlagged()) {
                        gameOver(t, label, gPane);
                    }

                    // Non-mines increase score
                    else if(!t.getIsFlagged()) {
                        this.coveredCount -= 1;

                        // Uncovering the last non-mine tile wins the game
                        if(this.coveredCount == this.mineCount) {
                            winGame(label, gPane);
                        }

                        // If the game isn't over, increase the score
                        else {
                            label.setText("Score: " + (tiles.size() - this.coveredCount) + "/" + (tiles.size() - this.mineCount));
                        }

                        // Tiles with zero mine neighbors uncover all connected non-mine tiles
                        if(t.getNumMineNeighbors(this.tiles) == 0) {
                            for(Tile n: t.getNeighbors(this.tiles)) {
                                if(gPane.getChildren().contains(n.getCoveredImg())) {
                                    Bounds localBounds = n.getCoveredImg().getBoundsInLocal();
                                    Bounds screenBounds = n.getCoveredImg().localToScreen(localBounds);
                                    n.getCoveredImg().getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, n.getX(), n.getY(), screenBounds.getMinX(), screenBounds.getMinY(),
                                        MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                                }
                            }
                        }
                    }
                }
                
                else if(e.getButton() == MouseButton.SECONDARY) {
                    t.setIsFlagged(true);
                    gPane.getChildren().remove((Node) t.getCoveredImg());
                    gPane.getChildren().add((Node) t.getFlaggedImg());
                }
  
            });
            
            // Set on-click action for flagged tiles
            t.getFlaggedImg().setOnMouseClicked(e2 -> {
                if(e2.getButton() == MouseButton.SECONDARY) {
                    t.setIsFlagged(false);
                    gPane.getChildren().remove((Node) t.getFlaggedImg());
                    gPane.getChildren().add((Node) t.getCoveredImg());
                }
            });
            
        }
        
        // Determine the number of mines surrounding each tile and
        // assign them the corresponding digit image and on-click action
        for(Tile t: this.tiles) {
            if(!t.getIsAMine()) {
                
                t.setImage(images.get(t.getNumMineNeighbors(this.tiles)));
                GridPane.setConstraints(t.getUncoveredImg(), t.getX(), t.getY());
                
                /* Numbered tiles will clear neighboring tiles if the appropriate
                 * number of flags has been placed around it.
                 * 
                 * (e.g. clicking a "2" will clear its neighbors iff 2 of its neighbors
                 * have been flagged.
                 */
                if(t.getNumMineNeighbors(this.tiles) > 0) {
                    t.getUncoveredImg().setOnMouseClicked(e -> {
                        if(t.getNumFlaggedNeighbors(this.tiles) == t.getNumMineNeighbors(this.tiles)) {
                            for(Tile n: t.getNeighbors(tiles)) {
                                if(gPane.getChildren().contains(n.getCoveredImg())) {
                                    Bounds localBounds = n.getCoveredImg().getBoundsInLocal();
                                    Bounds screenBounds = n.getCoveredImg().localToScreen(localBounds);
                                    n.getCoveredImg().getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, n.getX(), n.getY(), screenBounds.getMinX(), screenBounds.getMinY(),
                                        MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                                }
                            }
                        }
                    });
                }
            }
        }
    }
    
    /**
     * Ends the game by showing all the mines and displaying "Game over!"
     * 
     * @param mine - The mine that was clicked
     * @param label - Label displayed above game board
     * @param gPane - The GridPane that defines the game board
     */
    public void gameOver(Tile mine, Label label, GridPane gPane) {
        for(Tile t: this.tiles) {
            if(t.getIsAMine() && !(t.equals(mine)) && (gPane.getChildren().contains(t.getCoveredImg()))){
                gPane.getChildren().remove((Node) t.getCoveredImg());
                gPane.getChildren().add((Node) t.getUncoveredImg());
            }
            else {
                // Disable on-click action for all tiles
                t.getCoveredImg().setOnMouseClicked(e -> {
                    assert true;
                });
            }
        }
        
        label.setText("Game over!");
    }
    
    /**
     * Ends the game by disabling on-click action for all images
     * and displaying "You win!"
     * 
     * @param label - Label displayed above game board
     * @param gPane - The GridPane that defines the game board
     */
    public void winGame(Label label, GridPane gPane) {
        for(Tile t: this.tiles) {
            t.getCoveredImg().setOnMouseClicked(e -> {
                assert true;
            });
        }
        
        label.setText("You win!");
    }
    
}
