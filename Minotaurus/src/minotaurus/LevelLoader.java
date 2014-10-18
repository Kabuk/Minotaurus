/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurus;

import minotaurus.characters.Theseus;
import minotaurus.characters.Minotaurus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author Kuba
 */
public class LevelLoader {

    public String loadLevel(String levelName) {
        try {
            FileInputStream fis = new FileInputStream(levelName + ".txt");
            
            return "test";
        } catch (FileNotFoundException e) {
            return "wrong";
        }
    }

    /**
     * Loads level from string
     *
     * @param input Level to load
     * @return loaded level
     *
     */
    public Level load(String input) {

        if (input == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        Level loadedLevel = new Level();

        try {

            String[] firstSplit = input.split(";");
            String[] splited = firstSplit[0].split("x");
            int width = Integer.parseInt(splited[0].trim());
            int height = Integer.parseInt(splited[1].trim());
            Square[][] squares = new Square[width][height];

            splited = firstSplit[1].split("x");

            Minotaurus minotaurus = new Minotaurus();
            Coordinates minotaurusCoordinates = new Coordinates(
                Integer.parseInt(splited[0].trim()) - 1,
                Integer.parseInt(splited[1].trim()) - 1);

            minotaurus.setCoordinates(minotaurusCoordinates);
            loadedLevel.setMinotaurus(minotaurus);

            splited = firstSplit[2].split("x");
            Theseus theseus = new Theseus();
            Coordinates theseusCoordinates = new Coordinates(
                Integer.parseInt(splited[0].trim()) - 1,
                Integer.parseInt(splited[1].trim()) - 1);

            theseus.setCoordinates(theseusCoordinates);
            loadedLevel.setTheseus(theseus);

            splited = firstSplit[3].split("x");
            Coordinates exitCoordinates = new Coordinates(
                Integer.parseInt(splited[0].trim()) - 1,
                Integer.parseInt(splited[1].trim()) - 1);

            loadedLevel.setExitCoordinates(exitCoordinates);

            for (int i = 0; i < height; i++) {
                int type = 0;

                String[] splitedTypes = firstSplit[i + 4].split(" ");

                for (int j = 0; j < width; j++) {

                    try {
                        type = Integer.parseInt(splitedTypes[j]);
                    } catch (NumberFormatException ex) {
                        System.out.println("Wrong number");
                    }

                    Square sq = new Square(type);
                    squares[j][i] = sq;
                }
            }

            loadedLevel.setSquares(squares);

        } catch (IllegalArgumentException ex) {
            System.out.println("Error loading level");
        }
        return loadedLevel;
    }
}
