import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Codes inspired by https://www.youtube.com/watch?v=_SqnzvJuKiA
 */
public class Game extends JPanel implements KeyListener, ActionListener {
    //SCORE
    private int score = 0;

    //Contains train head and cats (body)
    private int[] xTrainCart = new int[800];
    private int[] yTrainCart = new int[800];

    private boolean isUp = false;
    private boolean isDown = false;
    private boolean isLeft = false;
    private boolean isRight = false;

    //WALL
    private ImageIcon wallImg;
    private int RIGHT_WALL_POS = 850 + 25;
    private int LEFT_WALL_POS = 20;
    private int UP_WALL_POS = 70;
    private int DOWN_WALL_POS = 650 + 75;

    //TRAIN HEAD IMAGE ICON
    private ImageIcon trainHeadImg;
    private ImageIcon collectorImg;
    private int IMAGE_SIZE = 50;
    private int r = IMAGE_SIZE / 2;
    private double h = 2 * Math.sqrt(Math.pow(r, 2) + Math.pow(r, 2));

    //TRAIN BODY IMAGE ICON
    private ImageIcon trainBodyImg;

    //TRAIN length
    private int trainLength = 1; //default length

    //TIMER
    private Timer timer;
    private int delayTime = 200; //175


    int moves = 0;

    //PLASTIC BOTTLES
    private ImageIcon waterBottleImg;
    private int Xmax = 820;
    private int Ymax = 670;
    private int Xmin1 = 20; // collector = [20, 80]
    private int Xmin2 = 80;
    private int Ymin1 = 70; // collector = [70, 110]
    private int Ymin2 = 110;
    private int xpos = (int) (Math.random() * (Xmax - Xmin2)) + Xmin2;
    private int ypos = (int) (Math.random() * (Ymax - Ymin2)) + Ymin2;

    //Collector
    private int Xcollector = 5;
    private int Ycollector = 40;


    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delayTime, this);
        timer.start();
    }

    public void paint(Graphics graphics) {

        /**Executed only once at beginning**/
        //1) PLACE THE HEAD of the TRAIN
        if (moves == 0) {  //if just started
            //1) Set default position of train
            //X default position
            xTrainCart[0] = 150;

            // Y default position
            yTrainCart[0] = 150;

        }




        /**GAME**/
        graphics.setColor(Color.GREEN); //border
        graphics.drawRect(24, 74, 851, 577);

        graphics.setColor(Color.decode("#732101"));  //background
        //graphics.setColor(Color.BLACK);
        //graphics.fillRect(25, 75, 850, 650);
        graphics.fillRect(0, 0, 900, 800);

        graphics.setColor(Color.decode("#290C00"));
        graphics.fillRect(18, 75, 850, 650);
        /**TITLE*/

        graphics.setColor(Color.decode("#F9EDE6"));
        graphics.setFont(new Font("times new roman", Font.BOLD, 23));
        graphics.drawString("Recycling Train", 20, 40);

        /**score**/
        graphics.setColor(Color.decode("#F9EDE6"));
        graphics.setFont(new Font("times new roman", Font.BOLD, 17));
        graphics.drawString("Scores: " + score, 780, 30);

        /**LENGTH OF TRAIN CART**/
        graphics.setFont(new Font("times new roman", Font.BOLD, 17));
        int nbCarts = trainLength-1;
        graphics.drawString("# Carts: " + nbCarts, 778, 55);

        /**Recycling Station**/
        Image scaledCollectorImg = new ImageIcon("collector.png").getImage();
        scaledCollectorImg = scaledCollectorImg.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        collectorImg = new ImageIcon(scaledCollectorImg);
        collectorImg.paintIcon(this, graphics, Xcollector, Ycollector);

        /**Recycling Train**/
        Image scaledTrainHeadImg = new ImageIcon("train_head_right.png").getImage();
        scaledTrainHeadImg = scaledTrainHeadImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        trainHeadImg = new ImageIcon((scaledTrainHeadImg));

        /**ADD Head + body**/
        for (int i = 0; i < trainLength; i++) {

            //1) Detect direction of head. i=0 ==> HEAD
            //1A Left Head
            if (isLeft && i == 0) {
                scaledTrainHeadImg = new ImageIcon("train_head_left.png").getImage();
                scaledTrainHeadImg = scaledTrainHeadImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                trainHeadImg = new ImageIcon((scaledTrainHeadImg));
            }
            //1B RIGHT Head
            if (isRight && i == 0) {
                scaledTrainHeadImg = new ImageIcon("train_head_right.png").getImage();
                scaledTrainHeadImg = scaledTrainHeadImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                trainHeadImg = new ImageIcon((scaledTrainHeadImg));
            }
            //1C UP Head
            if (isUp && i == 0) {
                scaledTrainHeadImg = new ImageIcon("train_head_up.png").getImage();
                scaledTrainHeadImg = scaledTrainHeadImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                trainHeadImg = new ImageIcon((scaledTrainHeadImg));
            }
            //1D DOWN Head
            if (isDown && i == 0) {
                scaledTrainHeadImg = new ImageIcon("train_head_down.png").getImage();
                scaledTrainHeadImg = scaledTrainHeadImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                trainHeadImg = new ImageIcon((scaledTrainHeadImg));
            }

            trainHeadImg.paintIcon(this, graphics, xTrainCart[0], yTrainCart[0]);

            //1E BODY
            if (i != 0) {  //not head
                Image scaledTrainBodyImg = new ImageIcon("cart_with_wheels.png").getImage();
                scaledTrainBodyImg = scaledTrainBodyImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                trainBodyImg = new ImageIcon((scaledTrainBodyImg));
                trainBodyImg.paintIcon(this, graphics, xTrainCart[i], yTrainCart[i]);
            }
        }


        /**DETECTION COLLISION with OBJ**/
        waterBottleImg = new ImageIcon("water_bottle.png");
        if (Math.sqrt( Math.pow(xpos-xTrainCart[0],2) + Math.pow(ypos-yTrainCart[0],2) ) < h){
            //score++;
            trainLength++;
            xpos = (int) (Math.random() * (Xmax - Xmin1)) + Xmin1;
            ypos = (int) (Math.random() * (Ymax - Ymin1)) + Ymin1;
            if (xpos < Xmin2 && ypos < Ymin2){
                xpos = (int) (Math.random() * (Xmax - Xmin1)) + Xmin1; //just playing the probability...lol
                ypos = (int) (Math.random() * (Ymax - Ymin1)) + Ymin1;
            }
        }
        waterBottleImg.paintIcon(this, graphics, xpos, ypos);

        /*DETECTION COLLISION with SELF*/
        for (int i = 1; i < trainLength; i++) {
           //check if position of body = position HEAD
            if (xTrainCart[i] == xTrainCart[0] && yTrainCart[i] == yTrainCart[0]) {
                //COLLISION occurs
                displayGameOver(graphics);
            }
        }

        /**DETECTION COLLISION with wall**/
        if (xTrainCart[0]+ IMAGE_SIZE >= RIGHT_WALL_POS ||
                xTrainCart[0] <= LEFT_WALL_POS ||
                yTrainCart[0] <= UP_WALL_POS ||
                yTrainCart[0]+IMAGE_SIZE >= DOWN_WALL_POS) {

            displayGameOver(graphics);
        }

        if (trainLength >= 5 &&
                xTrainCart[0] < Xcollector + Xmin2 &&
                yTrainCart[0] < Ycollector + Ymin2){
            score = score + trainLength/5;
            trainLength = trainLength%5;
        }

        graphics.dispose();
    }

    private void displayGameOver(Graphics graphics) {
        isRight = false;
        isLeft = false;
        isUp = false;
        isDown = false;
        graphics.setColor(Color.decode("#290C00"));
        graphics.fillRect(18, 75, 850, 650);
        graphics.setColor(Color.decode("#F9EDE6"));
        graphics.setFont(new Font("times new roman", Font.BOLD, 60));
        graphics.drawString("GAME OVER", 255, 310);

        graphics.setFont(new Font("times new roman", Font.BOLD, 30));
        graphics.drawString("Press SPACE to restart", 290, 365);

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        //1) CHECK DIRECTION
        if (isRight) {

            /**POSITIONS OF HEAD & CARTS**/

            //2) Y position. Pos of next body cart = previous pos of prev cart
            for (int i = trainLength - 1; i >= 0; i--) {
                yTrainCart[i + 1] = yTrainCart[i];
            }

            //3) X position
            for (int i = trainLength; i >= 0; i--) {

                //4) Check Head or Body
                if (i == 0) {   //4A) head, shift head position to RIGHT by 50
                    xTrainCart[i] = xTrainCart[i] + 50;
                } else {//4B) BODY, set x position to be same as prev cart position
                    xTrainCart[i] = xTrainCart[i - 1];
                }
            }
            repaint(); //it will call paint() automatically
        }

        if (isLeft) {

            /**POSITIONS OF HEAD & CARTS**/

            //2) Y position. Pos of next body cart = previous pos of prev cart
            for (int i = trainLength - 1; i >= 0; i--) {
                yTrainCart[i + 1] = yTrainCart[i];
            }

            //3) X position
            for (int i = trainLength; i >= 0; i--) {

                //4) Check Head or Body
                if (i == 0) {   //4A) head, shift head position to LEFT by 50
                    xTrainCart[i] = xTrainCart[i] - 50;
                } else {//4B) BODY, set x position to be same as prev cart position
                    xTrainCart[i] = xTrainCart[i - 1];
                }

            }
            repaint(); //it will call paint() automatically

        }

        if (isDown) {

            /**POSITIONS OF HEAD & CARTS**/

            for (int i = trainLength - 1; i >= 0; i--) {
                xTrainCart[i + 1] = xTrainCart[i];
            }

            for (int i = trainLength; i >= 0; i--) {

                if (i == 0) {   //4A) head, shift head position to LEFT by 50
                    yTrainCart[i] = yTrainCart[i] + 50;
                } else {//4B) BODY, set x position to be same as prev cart position
                    yTrainCart[i] = yTrainCart[i - 1];
                }

            }
            repaint(); //it will call paint() automatically
        }

        if (isUp) {

            /**POSITIONS OF HEAD & CARTS**/

            for (int i = trainLength - 1; i >= 0; i--) {
                xTrainCart[i + 1] = xTrainCart[i];
            }

            for (int i = trainLength; i >= 0; i--) {

                if (i == 0) {
                    yTrainCart[i] = yTrainCart[i] - 50;
                } else {
                    yTrainCart[i] = yTrainCart[i - 1];
                }
            }
            repaint(); //it will call paint() automatically
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {  //let us know which key is pressed
        //SPACE = END OF GAME
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moves = 0;
            score = 0;
            trainLength = 1;
            repaint();
        }


        //RIGHT key pressed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moves++;
            //Check possible collisions

            if (!isLeft) {  //NOT moving to Left
                isRight = true;
            } else {
                isRight = false;
                isLeft = true;
            }
            isUp = false;
            isDown = false;

        }

        //LEFT key pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moves++;
            //Check possible collisions

            if (!isRight) {  //NOT moving to Right
                isLeft = true;
            } else {
                isLeft = false;
                isRight = true;
            }
            isUp = false;
            isDown = false;
        }

        //UP key pressed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            moves++;
            //Check possible collisions

            if (!isDown) {  //NOT moving to Right
                isUp = true;
            } else {
                isUp = false;
                isDown = true;
            }
            isLeft = false;
            isRight = false;
        }


        //DOWN key pressed
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moves++;
            //Check possible collisions

            if (!isUp) {  //NOT moving to Right
                isDown = true;
            } else {
                isDown = false;
                isUp = true;
            }
            isLeft = false;
            isRight = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
