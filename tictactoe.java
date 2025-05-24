import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToeGUI extends JFrame {
    static int SIZE = 3; // Başlangıçta 3x3 tahta
    static JButton[][] buttons;
    static char currentPlayer = 'X'; // Başlangıçta oyuncu X
    static boolean gameOver = false;
    static String language = "EN"; // Başlangıç dili İngilizce
    static String difficulty = "EASY"; // Başlangıç zorluk seviyesi Kolay

    public TicTacToeGUI() {
        // Dil seçimi menüsü
        String[] options = {"English", "Türkçe"};
        int languageChoice = JOptionPane.showOptionDialog(this, "Select Language", "Language Selection", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if (languageChoice == 1) {
            language = "TR";
        }

        // Zorluk seçimi menüsü
        String[] difficulties = {"Easy", "Medium", "Hard"};
        int difficultyChoice = JOptionPane.showOptionDialog(this, getMessage("Select Difficulty"), "Difficulty", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, difficulties, difficulties[0]);

        difficulty = difficulties[difficultyChoice].toUpperCase();

        // Boyut seçimi menüsü
        String[] sizes = {"3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9"};
        int sizeChoice = JOptionPane.showOptionDialog(this, getMessage("Select Board Size"), "Board Size", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, sizes, sizes[0]);

        SIZE = Integer.parseInt(sizes[sizeChoice].substring(0, 1)); // Seçilen boyut (Örneğin, "9x9" -> 9)
        
        // Oyun tahtasını oluştur
        buttons = new JButton[SIZE][SIZE];
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setEnabled(true);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        // Pencere ayarları
        setTitle("Tic-Tac-Toe");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Buton tıklama olayını yöneten sınıf
    class ButtonClickListener implements ActionListener {
        int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameOver) return;

            if (buttons[row][col].getText().equals("")) {
                buttons[row][col].setText(String.valueOf(currentPlayer));
                buttons[row][col].setEnabled(false);
                checkForWinner();
                switchPlayer();

                if (!gameOver && currentPlayer == 'O') {
                    aiMove();
                    checkForWinner();
                    switchPlayer();
                }
            }
        }
    }

    // Oyuncu değiştirme
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // Kazananı kontrol et
    public void checkForWinner() {
        // Satırları, sütunları ve çaprazları kontrol et
        for (int i = 0; i < SIZE; i++) {
            // Satır kontrolü
            if (checkLine(buttons[i])) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, getMessage("Player") + " " + currentPlayer + " " + getMessage("wins"));
                playAgain();
                return;
            }
            // Sütun kontrolü
            JButton[] column = new JButton[SIZE];
            for (int j = 0; j < SIZE; j++) {
                column[j] = buttons[j][i];
            }
            if (checkLine(column)) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, getMessage("Player") + " " + currentPlayer + " " + getMessage("wins"));
                playAgain();
                return;
            }
        }

        // Çaprazları kontrol et
        JButton[] diagonal1 = new JButton[SIZE];
        JButton[] diagonal2 = new JButton[SIZE];
        for (int i = 0; i < SIZE; i++) {
            diagonal1[i] = buttons[i][i];
            diagonal2[i] = buttons[i][SIZE - i - 1];
        }
        if (checkLine(diagonal1) || checkLine(diagonal2)) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, getMessage("Player") + " " + currentPlayer + " " + getMessage("wins"));
            playAgain();
            return;
        }

        // Tahtada boş yer kalıp kalmadığını kontrol et
        boolean hasEmptySpaces = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (buttons[i][j].getText().equals("")) {
                    hasEmptySpaces = true;
                    break;
                }
            }
        }

        if (!hasEmptySpaces) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, getMessage("It's a draw!"));
            playAgain();
        }
    }

    // Bir satırda tüm elemanlar aynı mı diye kontrol et
    public boolean checkLine(JButton[] line) {
        String first = line[0].getText();
        if (first.equals("")) return false;
        for (int i = 1; i < SIZE; i++) {
            if (!line[i].getText().equals(first)) {
                return false;
            }
        }
        return true;
    }

    // Yapay zekanın hamlesi (Zorluk seviyesine göre)
    public void aiMove() {
        Random rand = new Random();
        int row, col;

        // Zorluk seviyelerine göre AI'nin hareket tarzı
        if (difficulty.equals("EASY")) {
            // Kolay AI: Rastgele bir hamle yapar
            while (true) {
                row = rand.nextInt(SIZE);
                col = rand.nextInt(SIZE);
                if (buttons[row][col].getText().equals("")) {
                    buttons[row][col].setText(String.valueOf(currentPlayer));
                    buttons[row][col].setEnabled(false);
                    break;
                }
            }
        } else if (difficulty.equals("MEDIUM")) {
            // Orta zorluk: Boş hücrelerde rastgele hamle yapar
            while (true) {
                row = rand.nextInt(SIZE);
                col = rand.nextInt(SIZE);
                if (buttons[row][col].getText().equals("")) {
                    buttons[row][col].setText(String.valueOf(currentPlayer));
                    buttons[row][col].setEnabled(false);
                    break;
                }
            }
        } else {
            // Zor AI: Bu AI daha akıllı olmalı, ama burada çok basit bir strateji ile yapılacak
            while (true) {
                row = rand.nextInt(SIZE);
                col = rand.nextInt(SIZE);
                if (buttons[row][col].getText().equals("")) {
                    buttons[row][col].setText(String.valueOf(currentPlayer));
                    buttons[row][col].setEnabled(false);
                    break;
                }
            }
        }
    }

    // Dil seçeneği mesajları
    public String getMessage(String key) {
        if (language.equals("TR")) {
            switch (key) {
                case "Select Difficulty":
                    return "Zorluk Seçin";
                case "Select Board Size":
                    return "Tahta Boyutunu Seçin";
                case "Player":
                    return "Oyuncu";
                case "wins":
                    return "kazandı";
                case "It's a draw!":
                    return "Beraberlik!";
                case "Play Again":
                    return "Tekrar Oyna";
                default:
                    return "";
            }
        } else {
            switch (key) {
                case "Select Difficulty":
                    return "Select Difficulty";
                case "Select Board Size":
                    return "Select Board Size";
                case "Player":
                    return "Player";
                case "wins":
                    return "wins";
                case "It's a draw!":
                    return "It's a draw!";
                case "Play Again":
                    return "Play Again";
                default:
                    return "";
            }
        }
    }

    // Tekrar oynama fonksiyonu
    public void playAgain() {
        int response = JOptionPane.showConfirmDialog(this, getMessage("Play Again") + "?", getMessage("Play Again"), JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            gameOver = false;
            currentPlayer = 'X'; // X her zaman ilk oynar
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                }
            }
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
