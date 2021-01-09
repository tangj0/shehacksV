import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Game game = new Game();

        frame.setBounds(10, 10, 900, 800); //set size
        frame.setBackground(Color.decode("0xFF0096"));  //TODO: Change this ugly PINK background

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);

    }
}
