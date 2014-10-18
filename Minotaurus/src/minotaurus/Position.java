/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurus;

/**
 * Class used for representation of single position of Theseus and Minotaur
 *
 *
 */
public class Position {

    private int xMinotaurPosition;
    private int yMinotaurPosition;
    private int xTheseusPosition;
    private int yTheseusPosition;

    public Position(int xTheseusPosition, int yTheseusPosition, int xMinotaurPosition, int yMinotaurPosition) {
        this.xMinotaurPosition = xMinotaurPosition;
        this.yMinotaurPosition = yMinotaurPosition;
        this.xTheseusPosition = xTheseusPosition;
        this.yTheseusPosition = yTheseusPosition;
    }

    public int getxMinotaurPosition() {
        return xMinotaurPosition;
    }

    public int getxTheseusPosition() {
        return xTheseusPosition;
    }

    public int getyMinotaurPosition() {
        return yMinotaurPosition;
    }

    public int getyTheseusPosition() {
        return yTheseusPosition;
    }
}
