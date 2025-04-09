import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class ReactionSpeed extends JPanel implements ActionListener {

    private Timer timer;
    private int c;
    private final static int TIME = 3;
    private final long startTime = System.currentTimeMillis();
    private double speed = 60;
    ArrayList<int[]> keys = new ArrayList<>();

    ReactionSpeed() {
        setPreferredSize(new Dimension(Program.WINDOW_WIDTH, Program.WINDOW_HEIGHT));
        setBackground(new Color(245, 231, 216));
        setFocusable(true);
        setLayout(null);
        addKeyListener(new KeyAdapter2());
        timer = new Timer(16, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Random random = new Random();
        if (c > speed) {
            keys.add(new int[]{random.nextInt(1250), random.nextInt(725),
                    (int) System.currentTimeMillis(), random.nextInt(65, 91)});
            c = 0;
            if (speed > 10)
                speed /= 1.02;
        }
        FontMetrics metrics = getFontMetrics(g.getFont());
        for (int[] key : keys) {
            if (((int) System.currentTimeMillis() - key[2]) / (TIME * 1000) >= 1)
                loser("You didn't hit the keys in time.");
            g.setColor(new Color(255 * (int) (System.currentTimeMillis() - key[2]) / (TIME * 1000),
                    255 - (255 * (int) (System.currentTimeMillis() - key[2]) / (TIME * 1000)), 64));
            g.fillOval(key[0], key[1],
                    150, 150);
            g.setColor(new Color(255 - g.getColor().getRed(),
                    255 - g.getColor().getRed(), 191));
            g.setFont(new Font("Arial", Font.BOLD, 100));
            g.drawString(String.valueOf((char) key[3]), key[0] + 56 -
                    metrics.stringWidth(String.valueOf(key[0])) / 2, key[1] + 115);
        }
    }

    private void loser(String message) {
        timer.stop();
        JOptionPane.showMessageDialog(null,
                message + "\nYour score was: " + (System.currentTimeMillis() - startTime),
                "Score", JOptionPane.INFORMATION_MESSAGE, null);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        c++;
        repaint();
    }
    private class KeyAdapter2 extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (e.getKeyCode() < 65 || e.getKeyCode() > 90)
                return;
            for (int i = 0; i < keys.size(); i++) {
                if (e.getKeyCode() == keys.get(i)[3]) {
                    keys.remove(i);
                    return;
                }
            }
            loser("You hit the wrong key.");
        }
    }
}