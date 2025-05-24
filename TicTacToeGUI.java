import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private static final char PLAYER = 'O';  // İnsan oyuncu
    private static final char AI = 'X';      // Yapay zeka oyuncu
    private char currentPlayer = PLAYER;    // Başlangıçta oyuncu

    private JButton[] buttons = new JButton[9];
    private char[] board = new char[9];    // Oyun tahtası

    public TicTacToeGUI() {
        setTitle("Tic-Tac-Toe");
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Butonları oluştur ve paneli düzenle
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].addActionListener(new ButtonClickListener(i));
            add(buttons[i]);
            board[i] = ' ';  // Tahtadaki tüm hücreler boş
        }

        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        private int index;

        public ButtonClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[index] == ' ' && currentPlayer == PLAYER) {
                board[index] = PLAYER;
                buttons[index].setText(String.valueOf(PLAYER));
                if (isGameOver()) return;
                currentPlayer = AI;
                aiMove();
            }
        }
    }

    private void aiMove() {
        int move = getBestMove();
        if (move != -1) {
            board[move] = AI;
            buttons[move].setText(String.valueOf(AI));
            if (isGameOver()) return;
            currentPlayer = PLAYER;
        }
    }

    private int getBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;

        // Tüm boş alanları dene
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = AI;
                int moveVal = minimax(board, 0, false);
                board[i] = ' ';

                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[] board, int depth, boolean isMax) {
        if (checkWin(AI)) return 10 - depth;
        if (checkWin(PLAYER)) return depth - 10;
        if (isBoardFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = AI;
                    best = Math.max(best, minimax(board, depth + 1, false));
                    board[i] = ' ';
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = PLAYER;
                    best = Math.min(best, minimax(board, depth + 1, true));
                    board[i] = ' ';
                }
            }
            return best;
        }
    }

    private boolean checkWin(char symbol) {
        // 3 satır, 3 sütun ve 2 çapraz kontrolü
        for (int i = 0; i < 3; i++) {
            if ((board[i * 3] == symbol && board[i * 3 + 1] == symbol && board[i * 3 + 2] == symbol) ||
                (board[i] == symbol && board[i + 3] == symbol && board[i + 6] == symbol)) {
                return true;
            }
        }
        if ((board[0] == symbol && board[4] == symbol && board[8] == symbol) ||
            (board[2] == symbol && board[4] == symbol && board[6] == symbol)) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') return false;
        }
        return true;
    }

    private boolean isGameOver() {
        if (checkWin(AI)) {
            JOptionPane.showMessageDialog(this, "AI kazandı!");
            restartGame();
            return true;
        }
        if (checkWin(PLAYER)) {
            JOptionPane.showMessageDialog(this, "Oyuncu kazandı!");
            restartGame();
            return true;
        }
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "Beraberlik!");
            restartGame();
            return true;
        }
        return false;
    }

    private void restartGame() {
        int response = JOptionPane.showConfirmDialog(this, "Yeni bir oyun başlatmak ister misiniz?", "Oyun Bitti", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Tahtayı temizle
            for (int i = 0; i < 9; i++) {
                board[i] = ' ';
                buttons[i].setText("");
            }
            currentPlayer = PLAYER;  // Oyuncudan başla
        } else {
            System.exit(0);  // Uygulamayı kapat
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
