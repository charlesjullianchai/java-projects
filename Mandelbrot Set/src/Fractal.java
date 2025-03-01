import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Fractal extends JPanel implements ActionListener {

    private Timer timer;
    private int c;
    private final int[][] points = {{Program.WINDOW_WIDTH / 3, Program.WINDOW_HEIGHT / 3 * 2},
            {Program.WINDOW_WIDTH / 3 * 2, Program.WINDOW_HEIGHT / 3 * 2},
            {Program.WINDOW_WIDTH / 2, Program.WINDOW_HEIGHT / 3 - 100}};
    private final Random random = new Random();
    private Point currentPoint = new Point(650,400);
    private final ArrayList<Integer> pointListX = new ArrayList<>();
    private final ArrayList<Integer> pointListY = new ArrayList<>();

    Fractal() {
        setPreferredSize(new Dimension(Program.WINDOW_WIDTH, Program.WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);
        timer = new Timer(5, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.drawPolygon(new int[]{points[0][0],points[1][0],points[2][0]},
                new int[]{points[0][1],points[1][1],points[2][1]}, 3);
        for (int i = 0; i < pointListX.size(); i++) {
            g.drawRect(pointListX.get(i), pointListY.get(i), 1, 1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int vertex = random.nextInt(3);
        pointListX.add(currentPoint.x);
        pointListY.add(currentPoint.y);
        currentPoint.x = (currentPoint.x + points[vertex][0]) / 2;
        currentPoint.y = (currentPoint.y + points[vertex][1]) / 2;
        c++;
        if (c > 10000) {
            pointListX.removeFirst();
            pointListY.removeFirst();
            c--;
        }
        repaint();
    }
}