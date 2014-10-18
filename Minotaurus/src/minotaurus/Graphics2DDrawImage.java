/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minotaurus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Kuba
 */
class MyCanvas extends JComponent {

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Image img1 = Toolkit.getDefaultToolkit().getImage("./theseus.png");
        g2.drawImage(img1, 10, 10, this);
    }
}

public class Graphics2DDrawImage {

    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setTitle("Theseus and Minotaurus");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 300, 300);
        window.getContentPane().add(new MyCanvas());
        window.setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int type = squares[j][i].getType();
                loadImage("./type" + type + ".png");
                this.prepareImage(img, this);
                backGround.drawImage(img, 0 + 60 * j, 30 + 60 * i, null);

            }
        }

        loadImage("./exit.png");
        this.prepareImage(img, this);
        backGround.drawImage(img, 5 + 60 * xExit, 40 + 60 * yExit, null);

        loadImage("./theseus.png");
        this.prepareImage(img, this);
        backGround.drawImage(img, 10 + xT, 35 + yT, null);

        loadImage("./minotaur.png");
        this.prepareImage(img, this);
        backGround.drawImage(img, 5 + xM, 37 + yM, null);

        if (checkDeath() == true) {
            loadImage("./dead.png");
            this.prepareImage(img, this);
            backGround.drawImage(img, 0, 0, null);
        } else {
            loadImage("./dead-cover.png");
            this.prepareImage(img, this);
            backGround.drawImage(img, 0, 0, null);
        }

        g.drawImage(backBuffer, 0, 0, this);
    }

    /**
     * Loads image
     *
     * @param name Name of image to load
     * @return Loaded image
     */
    public Image loadImage(String name) {

        img = getImage(getCodeBase(), name);
        return img;
    }
}
