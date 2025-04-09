import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Checkers extends JPanel implements ActionListener {

    private Timer timer;
    private final int[][][] board = new int[8][8][2];
    private Point selectedPiece = null;
    private boolean playerTurn = true;
    private boolean isLocked;
    private boolean isChainJumping;

    public Checkers() {
        setPreferredSize(new Dimension(Program.WINDOW_WIDTH, Program.WINDOW_HEIGHT));
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);
        setLayout(null);
        MouseAdapter mouseAdapter = new MouseAdapter0();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        int[][] startingBlackPieces = {
                {0, 0}, {2, 0}, {4, 0}, {6, 0},
                {1, 1}, {3, 1}, {5, 1}, {7, 1},
                {0, 2}, {2, 2}, {4, 2}, {6, 2}//{0, 0}, {6, 6}, {6, 4}
        };
        int[][] startingRedPieces = {
                {1, 7}, {3, 7}, {5, 7}, {7, 7},
                {0, 6}, {2, 6}, {4, 6}, {6, 6},
                {1, 5}, {3, 5}, {5, 5}, {7, 5}//{1, 1}, {1, 3}, {7, 7}
        };
        for (int[] pos : startingBlackPieces) {
            board[pos[0]][pos[1]][0] = 1;
        }
        for (int[] pos : startingRedPieces) {
            board[pos[0]][pos[1]][0] = 2;
        }
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(100));
        g.setColor(Color.BLACK);
        g.fillRoundRect(300, 37, 800, 800, 10, 10);
        g.setColor(new Color(128, 64, 32));
        g.fillRect(312, 50, 775, 775);
        g.setColor(new Color(210, 189, 150));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                g.fillRect(j * 194 + (312 + (i % 2 * 97)), i * 97 + 50, 97, 97);
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j][0] > 0) {
                    int square = board[i][j][0];
                    g.setColor(switch (square) {
                        case 1 -> Color.BLACK;
                        case 2 -> new Color(255, 55, 55);
                        case 3 -> new Color(255, 255, 0, 128);
                        default -> Color.GRAY;
                    });
                    int drawX = i * 97 + 321;
                    int drawY = 778 - j * 97 - 41;
                    g.fillOval(drawX, drawY, 80, 80);
                    g.setColor(square == 1 ? Color.DARK_GRAY : square == 2 ? new Color(255, 110, 110) : g.getColor());
                    g.fillOval(i * 97 + 331, 788 - j * 97 - 41, 60, 60);
                    if (board[i][j][1] == 1) {
                        g.setColor(g.getColor().brighter());
                        g.fillOval(i * 97 + 341, 798 - j * 97 - 41, 40, 40);
                        g.setColor(g.getColor().brighter());
                        g.fillOval(i * 97 + 351, 808 - j * 97 - 41, 20, 20);
                    }
                }
            }
        }
        g.setFont(new Font("Arial",Font.BOLD,40));
        if (playerTurn) {
            g.setColor(Color.black);
            g.drawString("Black's turn", 50, 100);
        } else {
            g.setColor(Color.red);
            g.drawString("Red's turn", 50, 100);

        }
    }

    private void evaluateMove(int x, int y) {
        clearMoveMarkers();
        if (board[x][y][1] == 0) {
            if (board[x][y][0] == 1 && playerTurn) {
                tryJumpMove(-1, 1, x, y);
                tryJumpMove(1, 1, x, y);
                tryAddMove(-1, 1, x, y);
                tryAddMove(1, 1, x, y);
            } else if (board[x][y][0] == 2 && !playerTurn) {
                tryJumpMove(-1, -1, x, y);
                tryJumpMove(1, -1, x, y);
                tryAddMove(-1, -1, x, y);
                tryAddMove(1, -1, x, y);
            }
        } else {
            if ((board[x][y][0] == 1 && playerTurn) || (board[x][y][0] == 2 && !playerTurn)) {
                tryJumpMove(-1, -1, x, y);
                tryJumpMove(-1, 1, x, y);
                tryJumpMove(1, -1, x, y);
                tryJumpMove(1, 1, x, y);
                tryAddMove(-1, -1, x, y);
                tryAddMove(-1, 1, x, y);
                tryAddMove(1, -1, x, y);
                tryAddMove(1, 1, x, y);
            }
        }
    }

    private void tryAddMove(int dX, int dY, int x, int y) {
        try {
            if (x + dX < 0 || x + dX >= board.length ||
                    y + dY < 0 || y + dY >= board[0].length ||
                    board[x + dX][y + dY][0] != 0 || isLocked) {
                return;
            }
        } catch (ArrayIndexOutOfBoundsException _) {
        }
        board[x + dX][y + dY][0] = 3;
    }

    private boolean checkJumpMove(int dX, int dY, int x, int y) {
        try {
            if (x + dX * 2 < 0 || x + dX * 2 >= board.length ||
                    y + dY * 2 < 0 || y + dY * 2 >= board[0].length ||
                    board[x + dX * 2][y + dY * 2][0] != 0) {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException _) {
        }
        return board[x + dX][y + dY][0] == (playerTurn ? 2 : 1);
    }

    private void tryJumpMove(int dX, int dY, int x, int y) {
        if (checkJumpMove(dX, dY, x, y)) {
            board[x + dX * 2][y + dY * 2][0] = 3;
            isLocked = true;
        }
    }

    private boolean hasJumpMove(int x, int y) {
        int direction = playerTurn ? 1 : -1;
        boolean hasJump = checkJumpMove(-1, direction, x, y) ||
                checkJumpMove(1, direction, x, y);
        if (board[x][y][1] == 1) {
            if (checkJumpMove(-1, -direction, x, y) ||
                    checkJumpMove(1, -direction, x, y)) hasJump = true;
        }
        return hasJump;
    }

    private void clearMoveMarkers() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j][0] == 3) {
                    board[i][j][0] = 0;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class MouseAdapter0 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int boardX = (e.getX() - 312) / 97;
            int boardY = (775 - (e.getY() - 50)) / 97;
            boardX = Math.max(0, Math.min(boardX, 7));
            boardY = Math.max(0, Math.min(boardY, 7));
            if ((playerTurn && board[boardX][boardY][0] == 1) ||
                    (!playerTurn && board[boardX][boardY][0] == 2)) {
                selectedPiece = new Point(boardX, boardY);
                evaluateMove(boardX, boardY);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int boardX = (e.getX() - 312) / 97;
            int boardY = (775 - (e.getY() - 50)) / 97;
            boardX = Math.max(0, Math.min(boardX, 7));
            boardY = Math.max(0, Math.min(boardY, 7));
            if (board[boardX][boardY][0] == 3 && selectedPiece != null) {
                boolean redWin = true;
                boolean blackWin = true;
                board[boardX][boardY][0] = playerTurn ? 1 : 2;
                board[boardX][boardY][1] = board[selectedPiece.x][selectedPiece.y][1];
                board[selectedPiece.x][selectedPiece.y][0] = 0;
                board[selectedPiece.x][selectedPiece.y][1] = 0;
                if (Math.abs(selectedPiece.x - boardX) == 2) {
                    board[(selectedPiece.x + boardX) / 2][(selectedPiece.y + boardY) / 2][0] = 0;
                    board[(selectedPiece.x + boardX) / 2][(selectedPiece.y + boardY) / 2][1] = 0;
                    isChainJumping = hasJumpMove(boardX, boardY);
                }
                isLocked = false;
                playerTurn = !playerTurn;
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        evaluateMove(i, j);
                        if (board[i][j][0] == 1)
                            redWin = false;
                        if (board[i][j][0] == 0)
                            blackWin = false;
                        if (board[i][0][0] == 2) {
                            board[i][0][1] = 1;
                        }
                        if (board[i][board.length - 1][0] == 1) {
                            board[i][board.length - 1][1] = 1;
                        }
                    }
                }
                playerTurn = !playerTurn;
                if (blackWin || redWin) {
                    JOptionPane.showMessageDialog(null, blackWin ? "Black wins the game!" :
                                    "Red wins the game!", "Game ends",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                if (!isChainJumping)
                    playerTurn = !playerTurn;
            }
            clearMoveMarkers();
            selectedPiece = null;
        }
    }
}