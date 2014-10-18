/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurus;

/**
 * Square on board
 *
 * @author Kuba
 */
public class Square {

    private boolean leftWall;
    private boolean rightWall;
    private boolean upperWall;
    private boolean downWall;
    private int type;

    /**
     * Constructor for objects of class Square
     *
     * @param type
     */
    public Square(int type) {

        if (type >= 0 && type <= 16) {
            this.type = type;
        } else {
            this.type = 17;
        }

        switch (type) {

            // no wall
            case 0:
                break;

            // one wall
            case 1:
                upperWall = true;
                break;

            case 2:
                rightWall = true;
                break;

            case 3:
                downWall = true;
                break;

            case 4:
                leftWall = true;
                break;

            // two walls - not touching
            case 5:
                upperWall = true;
                downWall = true;
                break;

            case 6:
                rightWall = true;
                leftWall = true;
                break;

            // two walls - corners
            case 7:
                upperWall = true;
                rightWall = true;
                break;

            case 8:
                rightWall = true;
                downWall = true;
                break;

            case 9:
                downWall = true;
                leftWall = true;
                break;

            case 10:
                upperWall = true;
                leftWall = true;
                break;

            // three walls
            case 11:
                rightWall = true;
                downWall = true;
                leftWall = true;
                break;

            case 12:
                upperWall = true;
                downWall = true;
                leftWall = true;
                break;

            case 13:
                upperWall = true;
                rightWall = true;
                leftWall = true;
                break;

            case 14:
                upperWall = true;
                rightWall = true;
                downWall = true;
                break;

            // four walls, also used for transparent and error square
            case 15:
            default:
                upperWall = true;
                rightWall = true;
                downWall = true;
                leftWall = true;
                break;
        }
    }

    public boolean isUpperPassable() {
        return !(upperWall);
    }

    public boolean isRightPassable() {
        return !(rightWall);
    }

    public boolean isDownPassable() {
        return !(downWall);
    }

    public boolean isLeftPassable() {
        return !(leftWall);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
