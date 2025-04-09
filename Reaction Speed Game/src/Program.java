import javax.swing.*;

public class Program {

    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 875;

    JFrame window = new JFrame("Game");

    public Program() {
    }

    public void run() {
        ReactionSpeed panel = new ReactionSpeed();
        window.add(panel);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
    }
}