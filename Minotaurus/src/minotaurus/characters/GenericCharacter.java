/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurus.characters;

import minotaurus.Coordinates;

/**
 *
 * @author Kuba
 */
public abstract class GenericCharacter {

    private Coordinates coordinates;

    public abstract void move();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

}
