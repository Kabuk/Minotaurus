package minotaurus;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Minotaur applet
 *
 * @author Jakub Kutil
 */
public class Minotaur extends Applet implements KeyListener, Runnable {

    
    private int xMinotaur;
    private int yMinotaur;

    
    private int movesCount = 0;
    
    private Image img;
    private String instancePlan;
    private String move;
    private int win;
    private Image backBuffer;
    private Graphics backGround;
    private Thread minotaur;
    private int xM;
    private int yM;
    private int xT;
    private int yT;
    private int moveTheseus = -1;
    private String moveMinotaur = "";
    private boolean wait = false;
    private List<Position> moves = new ArrayList();
    private boolean undo = false;
    private int moveNumber = 0;

  

    /**
     * Initializes applet
     */
    public void init() {

        if (instancePlan == null) {
            instancePlan = "4x3;2x1;2x3;4x2;10 5 7 16;4 14 4 14;9 5 8 16;"; // test level            
        }

        this.load(instancePlan);
        this.setSize(width * 60, (height * 60) + 30);
        addKeyListener(this);

        xT = xTheseus * 60;
        yT = yTheseus * 60;

        xM = xMinotaur * 60;
        yM = yMinotaur * 60;

        Position pos = new Position(xTheseus, yTheseus, xMinotaur, yMinotaur);
        moves.add(pos);

        this.setFocusable(true);
        this.requestFocus();

        backBuffer = createImage(width * 60, (height * 60) + 30);
        backGround = backBuffer.getGraphics();

        minotaur = new Thread(this);
        minotaur.start();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    

    

    /**
     * Checks if Theseus is on same square as exit
     *
     * @return TRUE if Theseus has found exit, FALSE otherwise
     */
    public boolean checkVictory() {
        return xExit == xTheseus && yExit == yTheseus;
    }

    /**
     * Checks if Theseus is on same square as Minotaur
     *
     * @return TRUE if Minotaur is on same square as Theseus, FALSE otherwise
     */
    public boolean checkDeath() {
        return xMinotaur == xTheseus && yMinotaur == yTheseus;
    }

    /**
     * Moves Theseus in certain direction depending on player's choice
     *
     * @param key
     * @throws InterruptedException
     */
    public void moveT(int key) throws InterruptedException {

        if (checkDeath() == true) {
            moveTheseus = -1;
            moveM();
        }

        if (key == 85 && moveNumber > 0) {
            xTheseus = moves.get(moveNumber - 1).getxTheseusPosition();
            yTheseus = moves.get(moveNumber - 1).getyTheseusPosition();
            xMinotaur = moves.get(moveNumber - 1).getxMinotaurPosition();
            yMinotaur = moves.get(moveNumber - 1).getyMinotaurPosition();

            xT = xTheseus * 60;
            yT = yTheseus * 60;
            xM = xMinotaur * 60;
            yM = yMinotaur * 60;
            undo = true;

            moves.remove(moveNumber);
            moveNumber--;
            movesCount++;
        }

        if ((checkVictory() == false) && (checkDeath() == false)) {
            switch (key) {
                case 38:
                case 87:
                    if (findPassage(xTheseus, yTheseus, 0) == true) {
                        moveTheseus = 0;
                        yTheseus -= 1;
                        movesCount++;
                        moveNumber++;
                    }
                    break;

                case 39:
                case 68:
                    if (findPassage(xTheseus, yTheseus, 1) == true) {
                        moveTheseus = 1;
                        xTheseus += 1;
                        movesCount++;
                        moveNumber++;
                    }
                    break;

                case 40:
                case 83:
                    if (findPassage(xTheseus, yTheseus, 2) == true) {
                        moveTheseus = 2;
                        yTheseus += 1;
                        movesCount++;
                        moveNumber++;
                    }
                    break;

                case 37:
                case 65:
                    if (findPassage(xTheseus, yTheseus, 3) == true) {
                        moveTheseus = 3;
                        xTheseus -= 1;
                        movesCount++;
                        moveNumber++;
                    }
                    break;

                case 71:
                    moveTheseus = 4;
                    movesCount++;
                    moveNumber++;
                    break;

                default:
                    break;
            }

            if ((checkVictory() == true) && (checkDeath() == false)) {
                win = 1;
                move = "T:" + (xTheseus + 1) + "," + (yTheseus + 1) + ";M:" + (xMinotaur + 1) + "," + (yMinotaur + 1);
//                    this.sendMove(win, move);

//                    try {
//                        getAppletContext().showDocument(new URL("javascript:after_win()"));
//                    } catch (MalformedURLException exe) {
//                        System.out.println("bad URL" + exe);
//                    }
            } else {
                moveM();
            }
        }
        key = 0;
    }

    /**
     * Moves Minotaur after player's move
     *
     * @throws java.lang.InterruptedException
     */
    public void moveM() throws InterruptedException {
        move = "";

        if (moveTheseus != -1) {
            for (int i = 0; i < 2; i++) {

                int hor = xMinotaur - xTheseus;
                int ver = yMinotaur - yTheseus;
                int change = 0;
                if (hor != 0) {
                    if (hor > 0 && findPassage(xMinotaur, yMinotaur, 3)) {
                        moveMinotaur += "l";
                        xMinotaur -= 1;
                        change = 1;
                    }
                    if (hor < 0 && findPassage(xMinotaur, yMinotaur, 1)) {
                        moveMinotaur += "r";
                        xMinotaur += 1;
                        change = 1;
                    }
                }

                if (change == 0 && ver != 0) {
                    if (ver > 0 && findPassage(xMinotaur, yMinotaur, 0)) {
                        moveMinotaur += "u";
                        yMinotaur -= 1;
                    }
                    if (ver < 0 && findPassage(xMinotaur, yMinotaur, 2)) {
                        moveMinotaur += "d";
                        yMinotaur += 1;
                    }
                }
            }

            Position pos = new Position(xTheseus, yTheseus, xMinotaur, yMinotaur);
            moves.add(pos);

            move = "T:" + (xTheseus + 1) + "," + (yTheseus + 1) + ";M:" + (xMinotaur + 1) + "," + (yMinotaur + 1);
        }
        if (undo == true) {
            move = "T:" + (xTheseus + 1) + "," + (yTheseus + 1) + ";M:" + (xMinotaur + 1) + "," + (yMinotaur + 1) + ";undo";
            undo = false;
        }
        if (moveTheseus != -1) {
        }
        if (moveMinotaur.length() == 0) {
            moveMinotaur += "g";
        }
    }

    

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (wait == false) {
            this.removeKeyListener(this);
            try {
                this.moveT(e.getKeyCode());

            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (moveTheseus == 0) {
                    yT = yT - 60;
                    repaint();
                    minotaur.sleep(120);
                }
                if (moveTheseus == 1) {
                    xT = xT + 60;
                    repaint();
                    minotaur.sleep(120);
                }
                if (moveTheseus == 2) {
                    yT = yT + 60;
                    repaint();
                    minotaur.sleep(120);
                }
                if (moveTheseus == 3) {
                    xT = xT - 60;
                    repaint();
                    minotaur.sleep(120);
                }
                moveTheseus = -1;

                if (moveMinotaur.equals("u")) {
                    yM = yM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("r")) {
                    xM = xM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("d")) {
                    yM = yM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("l")) {
                    xM = xM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("uu")) {
                    for (int y = 0; y < 2; y++) {
                        yM = yM - 60;
                        repaint();
                        minotaur.sleep(120);
                    }
                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("rr")) {
                    for (int y = 0; y < 2; y++) {
                        xM = xM + 60;
                        repaint();
                        minotaur.sleep(120);
                    }
                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("dd")) {
                    for (int y = 0; y < 2; y++) {
                        yM = yM + 60;
                        repaint();
                        minotaur.sleep(120);
                    }
                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("ll")) {
                    for (int y = 0; y < 2; y++) {
                        xM = xM - 60;
                        repaint();
                        minotaur.sleep(120);
                    }
                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("ur")) {
                    yM = yM - 60;
                    repaint();
                    minotaur.sleep(120);

                    xM = xM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("ul")) {

                    yM = yM - 60;
                    repaint();
                    minotaur.sleep(120);

                    xM = xM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("dr")) {
                    yM = yM + 60;
                    repaint();
                    minotaur.sleep(120);

                    xM = xM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("dl")) {
                    yM = yM + 60;
                    repaint();
                    minotaur.sleep(120);

                    xM = xM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("lu")) {
                    xM = xM - 60;
                    repaint();
                    minotaur.sleep(120);

                    yM = yM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("ld")) {
                    xM = xM - 60;
                    repaint();
                    minotaur.sleep(120);

                    yM = yM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("ru")) {
                    xM = xM + 60;
                    repaint();
                    minotaur.sleep(120);

                    yM = yM - 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                if (moveMinotaur.equals("rd")) {
                    xM = xM + 60;
                    repaint();
                    minotaur.sleep(120);

                    yM = yM + 60;
                    repaint();
                    minotaur.sleep(120);

                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }

                if (moveMinotaur.equals("g")) {
                    moveMinotaur = "";
                    this.addKeyListener(this);
                    wait = false;
                }
                repaint();
                minotaur.sleep(50);

            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
