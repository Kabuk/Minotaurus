package minotaurus;


import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Minotaur applet
 * 
 * @author Jakub Kutil
 */
public class Minotaur extends Applet implements KeyListener, Runnable {

    private int width;
    private int height;
    private int xMinotaur;
    private int yMinotaur;
    private int xTheseus;
    private int yTheseus;
    private int xExit;
    private int yExit;
    private int movesCount = 0;
    private Square[][] squares;
    private Image img;
    private String instanceName;
    private String instancePlan;
    private String sessionId;
    private String sessionHash;
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

    /**
     * Initializes applet
     */
    @Override
    public void init() {

        instanceName = getParameter("instance_name");
        instancePlan = getParameter("instance_plan");
        sessionId = getParameter("session_id");
        sessionHash = getParameter("session_hash");

        if (instancePlan == null) {
            instancePlan ="4x3;2x1;2x3;4x2;10 5 7 16;4 14 4 14;9 5 8 16;"; // test level            
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

    /**
     * Checks whether it is possible to move from square in certain direction 
     * 
     * @param  x  X-coordinate of square
     * @param  y  Y-coordinate of square
     * @param  direction  Direction of move (0-up, 1-right, 2-down, 3-left)
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

    /**
     * Checks if Theseus is on same square as exit
     * 
     * @return TRUE if Theseus found exit, FALSE otherwise
     */
    public boolean checkVictory() {
        if (xExit == xTheseus && yExit == yTheseus) {
            return true;
        }
        return false;
    }

    /**
     * Checks if Theseus is on same square as Minotaur
     * 
     * @return TRUE if Minotaur is on same square as Theseus, FALSE otherwise
     */
    public boolean checkDeath() {
        if (xMinotaur == xTheseus && yMinotaur == yTheseus) {
            return true;
        }
        return false;
    }

    /**
     * Moves Theseus in certain direction depending on player's choice
     * 
     * @throws InterruptedException
     */
    public void moveT(int key) throws InterruptedException {

            if(checkDeath() == true){
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
                    this.sendMove(win, move);

                    try {
                        getAppletContext().showDocument(new URL("javascript:after_win()"));
                    } catch (MalformedURLException exe) {
                        System.out.println("bad URL" + exe);
                    }
                } else {
                    moveM();
                }            
        }       
        key = 0;      
    }

    /**
     * Moves Minotaur after player's move
     * 
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
            move =  "T:" + (xTheseus + 1) + "," + (yTheseus + 1) + ";M:" + (xMinotaur + 1) + "," + (yMinotaur + 1) + ";undo";
            undo = false;
            this.sendMove(win, move);
        }
        if (moveTheseus != -1) {
            this.sendMove(win, move);
        }
        if (moveMinotaur.length() == 0) {
            moveMinotaur += "g";
        }
    }

    /**
     * Loads level from string
     * 
     * @param input Level to load
     * 
     */
    public void load(String input) {

        try {
            if (input == null) {
                throw new IllegalArgumentException("Input is null.");
            }

            String[] firstSplit = input.split(";");
            String[] splited = firstSplit[0].split("x");
            width = Integer.parseInt(splited[0].trim());
            height = Integer.parseInt(splited[1].trim());
            squares = new Square[width][height];


            splited = firstSplit[1].split("x");
            xMinotaur = Integer.parseInt(splited[0].trim()) - 1;
            yMinotaur = Integer.parseInt(splited[1].trim()) - 1;

            splited = firstSplit[2].split("x");
            xTheseus = Integer.parseInt(splited[0].trim()) - 1;
            yTheseus = Integer.parseInt(splited[1].trim()) - 1;

            splited = firstSplit[3].split("x");
            xExit = Integer.parseInt(splited[0].trim()) - 1;
            yExit = Integer.parseInt(splited[1].trim()) - 1;

            for (int i = 0; i < height; i++) {
                int type = 0;

                String[] splitedTypes = firstSplit[i + 4].split(" ");

                for (int j = 0; j < width; j++) {

                    try {
                        type = Integer.parseInt(splitedTypes[j]);
                    } catch (NumberFormatException ex) {
                        System.out.println("Cislo");
                    }

                    Square sq = new Square(type);
                    squares[j][i] = sq;
                }
            }

        } catch (Exception ex) {
            System.out.println("Chyba pri nacitani urovne");
        }
    }

    /**
     * Sends data to interface
     * 
     * @param win  Representation of victory
     * @param move Representation of move
     */
    private void sendMove(int win, String move) {
        String url;

        if (win == 1) {
            url = this.getCodeBase().toString() + "../../interface/interface.php?win=1" + "&session_id=" + sessionId
                    + "&session_hash=" + sessionHash + "&move_number=" + movesCount;
        } else {

            url = this.getCodeBase().toString() + "../../interface/interface.php?move=" + move + "&session_id=" + sessionId
                    + "&session_hash=" + sessionHash + "&move_number=" + movesCount;
        }

        Thread thread = new Thread(new SenderThread(url));
        thread.start();
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

    /**
     * Thread used to send information about moves to Tutor
     * 
     */
    public class SenderThread implements Runnable {

        URL urlToSend = null;
        String move;

        public SenderThread(String move) {
            this.move = move;
        }

        @Override
        public void run() {
            try {
                urlToSend = new URL(move);
            } catch (MalformedURLException exe) {
                System.out.println("bad URL" + exe);
            }

            try {
                InputStream is = urlToSend.openStream();
                is.close();
            } catch (IOException exe) {
                System.out.println(exe);
            }
        }
    }

    /**
     * Square on board
     * 
     */
    public class Square {

        private boolean leftWall;
        private boolean rightWall;
        private boolean upperWall;
        private boolean downWall;
        private int type;

        /**
         * Constructor for objects of class Square
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

        public void changeUpperWall() {
            upperWall = !(upperWall);
        }

        public void changeRightWall() {
            rightWall = !(rightWall);
        }

        public void changeDownWall() {
            downWall = !(downWall);
        }

        public void changeLeftWall() {
            leftWall = !(leftWall);
        }
    }
}
