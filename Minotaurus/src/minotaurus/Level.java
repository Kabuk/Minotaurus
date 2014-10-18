/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minotaurus;

import minotaurus.characters.Theseus;
import minotaurus.characters.Minotaurus;

/**
 *
 * @author Kuba
 */
public class Level {
 
    private int width;
    private int height;
    private Coordinates exitCoordinates;
    private Square[][] squares;
    private Theseus theseus;
    private Minotaurus minotaurus;
    
    
    
    /**
     * Checks whether it is possible to move from square in certain direction
     *
     * @param x X-coordinate of square
     * @param y Y-coordinate of square
     * @param direction Direction of move (0-up, 1-right, 2-down, 3-left)
     * @return TRUE if move is possible, FALSE otherwise
     */
    public boolean findPassage(int x, int y, int direction) {

        switch (direction) {
            case 0:
                return squares[x][y].isUpperPassable();

            case 1:
                return squares[x][y].isRightPassable();

            case 2:
                return squares[x][y].isDownPassable();

            case 3:
                return squares[x][y].isLeftPassable();
            default:
                return false;
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Coordinates getExitCoordinates() {
        return exitCoordinates;
    }

    public void setExitCoordinates(Coordinates exitCoordinates) {
        this.exitCoordinates = exitCoordinates;
    }

    public Square[][] getSquares() {
        return squares;
    }

    public void setSquares(Square[][] squares) {
        this.squares = squares;
    }

    public Theseus getTheseus() {
        return theseus;
    }

    public void setTheseus(Theseus theseus) {
        this.theseus = theseus;
    }

    public Minotaurus getMinotaurus() {
        return minotaurus;
    }

    public void setMinotaurus(Minotaurus minotaurus) {
        this.minotaurus = minotaurus;
    }

    
    
    
    
}
