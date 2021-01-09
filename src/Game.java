import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * Codes inspired by https://www.youtube.com/watch?v=_SqnzvJuKiA
 */
public class Game extends JPanel implements KeyListener, ActionListener {
    //TITLE OF THE GAME?
    private ImageIcon titleImg;  //TODO:Add Images

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
    private int RIGHT_WALL_POS = 850;  //TODO: Change these values when adding WALL Img
    private int LEFT_WALL_POS = 25; //TODO: Change these values when adding WALL Img
    private int UP_WALL_POS = 75;  //TODO: Change these values when adding WALL Img
    private int DOWN_WALL_POS = 850; //TODO: Change these values when adding WALL Img


    //TRAIN HEAD IMAGE ICON
    private ImageIcon trainHeadImg;
    private int TRAIN_HEAD_SIZE = 25;

    //TRAIN BODY IMAGE ICON
    private ImageIcon trainBodyImg;

    //TRAIN length
    private int trainLength = 1; //default length

    //TIMER
    private Timer timer;
    private int delayTime = 200;


    int moves = 0;

    /**PLASTIC BOTTLES**/

    private int BOTTLE_SIZE = 25;

    //PLASTIC BOTTLES POSITION
    private int[] xWaterbottle = {25, 50, 75, 100, 125, 150, 175, 225, 250, 300, 325,
            350, 375, 400, 425, 450, 500, 525, 550, 600, 625, 650, 675, 700, 750, 775, 800, 825, 850};
    private int[] yWaterbottle = {75, 100, 125, 150, 175, 200, 225, 250, 300, 325,
            350, 375, 400, 425, 450, 500, 525, 550, 600, 625};
    private ImageIcon waterBottleImg;
    private Random random = new Random();
    private int xpos = random.nextInt(25); //number of x position
    private int ypos = random.nextInt(20); //number of y position


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


        /**TITLE**/
        graphics.setColor(Color.WHITE);  //border
        graphics.drawRect(24, 10, 851, 55);
        //TODO: Uncomment. Write file name
        //titleImage = new ImageIcon("imagenametitle");  //image
        //titleImage.paintIcon(this,graphics,25,11);

        /**GAME**/
        graphics.setColor(Color.GREEN); //border
        graphics.drawRect(24, 74, 851, 577);

        graphics.setColor(Color.BLACK);  //background
        graphics.fillRect(25, 75, 850, 650);

        /**score**/
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("arial", Font.PLAIN, 14));
        graphics.drawString("Scores: " + score, 780, 30);

        /**LENGTH OF TRAIN CART**/
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("arial", Font.PLAIN, 14));
        int nbCarts = trainLength-1;
        graphics.drawString("# Carts: " + nbCarts, 780, 50);


        /**Recycling Train**/

        trainHeadImg = new ImageIcon("train_head_right.png");  //TODO: change file name

        /**ADD Head + body**/
        for (int i = 0; i < trainLength; i++) {

            //1) Detect direction of head. i=0 ==> HEAD
            //1A Left Head
            if (isLeft && i == 0) {
                trainHeadImg = new ImageIcon("train_head_left.png"); //TODO: change file name
            }
            //1B RIGHT Head
            if (isRight && i == 0) {
                trainHeadImg = new ImageIcon("train_head_right.png"); //TODO: change file name
            }
            //1C UP Head
            if (isUp && i == 0) {
                trainHeadImg = new ImageIcon("train_head_up.png"); //TODO: change file name
            }
            //1D DOWN Head
            if (isDown && i == 0) {
                trainHeadImg = new ImageIcon("train_head_down.png"); //TODO: change file name
            }
            trainHeadImg.paintIcon(this, graphics, xTrainCart[0], yTrainCart[0]);

            //1E BODY
            if (i != 0) {  //not head
                trainBodyImg = new ImageIcon("cart_with_wheels.png"); //TODO: change file name
                trainBodyImg.paintIcon(this, graphics, xTrainCart[i], yTrainCart[i]);
            }


        }

        /**DETECTION COLLISION with OBJ**/
        waterBottleImg = new ImageIcon("bottle.png"); //TODO: Change file name
//compare with HEAD traincart
        if ((xWaterbottle[xpos] == xTrainCart[0]) && yWaterbottle[ypos] == yTrainCart[0]) {  //same position
            score++; //increment the score
            trainLength++;
            xpos = random.nextInt(25);  //generate new enemies
            ypos = random.nextInt(20);

        }
        waterBottleImg.paintIcon(this, graphics, xWaterbottle[xpos], yWaterbottle[ypos]);

        /**DETECTION COLLISION with SELF**/
        for (int i = 1; i < trainLength; i++) {
            //check if position of body = position HEAD
            if (xTrainCart[i] == xTrainCart[0] && yTrainCart[i] == yTrainCart[0]) {
                //COLLISION occurs

                isRight = false;
                isLeft = false;
                isUp = false;
                isDown = false;

                displayGameOver(graphics);

            }
        }

        //TODO: WALL COLLISION
        /**DETECTION COLLISION with wall**/
        //CHECK HEAD

        if (isRight && xTrainCart[0] > RIGHT_WALL_POS) {
            displayGameOver(graphics);
        }
        if (isLeft && xTrainCart[0] < LEFT_WALL_POS) {
            displayGameOver(graphics);
        }
        if (isUp && yTrainCart[0] < UP_WALL_POS) {
            displayGameOver(graphics);
        }

        if (isDown && yTrainCart[0] > DOWN_WALL_POS) {
            displayGameOver(graphics);
        }

        graphics.dispose();
    }

    private void displayGameOver(Graphics graphics) {

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("arial", Font.BOLD, 50));
        graphics.drawString("GAME OVER", 300, 300);

        graphics.setFont(new Font("arial", Font.BOLD, 20));
        graphics.drawString("Press SPACE to restart", 350, 340);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
//1) CHECK DIRECTION
        //1A: Right
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
//4) Check whether the current position will be out of frame
                if (xTrainCart[i] > 850) {  //TODO: Change this value, when adding walls
                    //TODO: PUT GAME OVER
                   // System.out.println("GAME OVER: HIT RIGHT WALL DEAD");

                    //xTrainCart[i]=25; in tutorial: when go right, it appears back in the left wall
                }

            }
            repaint(); //it will call paint() automatically


        }
//1B: LEFT
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
//4) Check whether the current position will be out of frame
                if (xTrainCart[i] < 50) {  //TODO: Change this value, when adding walls
                    //TODO: PUT GAME OVER
                  //  System.out.println("GAME OVER: HIT RIGHT WALL DEAD");

                    //xTrainCart[i]=850; in tutorial: when go right, it appears back in the left wall
                }

            }
            repaint(); //it will call paint() automatically

        }

//DOWN
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
//4) Check whether the current position will be out of frame
                if (yTrainCart[i] > 625) {  //TODO: Change this value, when adding walls
                    //TODO: PUT GAME OVER
                   // System.out.println("GAME OVER: HIT BOTTOM WALL DEAD");

                    //xTrainCart[i]=850; in tutorial: when go right, it appears back in the left wall
                }

            }
            repaint(); //it will call paint() automatically
        }

        //UP
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
//4) Check whether the current position will be out of frame
                if (yTrainCart[i] < 75) {  //TODO: Change this value, when adding walls
                    //TODO: PUT GAME OVER
                  //  System.out.println("GAME OVER: HIT BOTTOM WALL DEAD");

                    //xTrainCart[i]=625; in tutorial: when go right, it appears back in the left wall
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
