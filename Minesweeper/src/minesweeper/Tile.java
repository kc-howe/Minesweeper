/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Kenneth
 */
public class Tile {
    
    private int x;
    private int y;
    private boolean isAMine;
    private boolean isFlagged;
    private ImageView coveredImg;
    private ImageView uncoveredImg;
    private ImageView flaggedImg;
    
    public Tile(int x, int y, HashMap<Integer, Image> images) {
        this.coveredImg = new ImageView(images.get(-1));
        this.x = x;
        this.y = y;
        this.isFlagged = false;
        this.flaggedImg = new ImageView(images.get(-2));
    }
    
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public boolean getIsAMine() {
        return this.isAMine;
    }
    public boolean getIsFlagged() {
        return this.isFlagged;
    }
    public ImageView getCoveredImg() {
        return this.coveredImg;
    }
    public ImageView getUncoveredImg() {
        return this.uncoveredImg;
    }
    public ImageView getFlaggedImg() {
        return this.flaggedImg;
    }
    
    /**
     * Returns an ArrayList containing this tile's neighbors
     * 
     * @param tiles - The tiles in the game board
     * 
     * @return An ArrayList containing this tile's neighbors
    */
    public ArrayList<Tile> getNeighbors(ArrayList<Tile> tiles) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                for(Tile t: tiles)  {
                    if(t.x == this.x + i%3 - 1 && t.y == this.y + j%3 - 1) {
                        neighbors.add(t);
                    }
                }
            }
        }
        return neighbors;
    }
    
    /**
     * Returns a count of the mines neighboring this tile
     * 
     * @param tiles - The tiles in the game board
     * 
     * @return A count of the mines neighboring this tile
    */
    public int getNumMineNeighbors(ArrayList<Tile> tiles) {
        int numMineNeighbors = 0;
        for(Tile n: this.getNeighbors(tiles)) {
            if(n.getIsAMine()) {
                numMineNeighbors += 1;
            }
        }
        return numMineNeighbors;
    }
    
    /**
     * Returns a count of the flags neighboring this tile
     * 
     * @param tiles - The tiles in the game board
     * 
     * @return A count of the flags neighboring this tile
    */
    public int getNumFlaggedNeighbors(ArrayList<Tile> tiles) {
        int numFlaggedNeighbors = 0;
        for(Tile n: this.getNeighbors(tiles)) {
            if(n.getIsFlagged()) {
                numFlaggedNeighbors += 1;
            }
        }
        return numFlaggedNeighbors;
    }
    
    public void setIsAMine(boolean isAMine) {
        this.isAMine = isAMine;
    }
    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }
    public void setImage(Image image) {
        this.uncoveredImg = new ImageView(image);
    }
    
}
