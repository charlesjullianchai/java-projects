import javax.swing.*;

public class Program {

    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 875;

    JFrame window = new JFrame("Game");

    public Program() {
    }

    public void run() {
        Checkers codebox = new Checkers();
        window.add(codebox);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
    }
}