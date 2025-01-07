import javax.swing.*;
import java.awt.*;

public class Game {
    private JFrame frame;
    private JPanel player1GridPanel, player1AttackGridPanel;
    private JPanel player2GridPanel, player2AttackGridPanel;
    private JButton[][] player1GridButtons, player1AttackGridButtons;
    private JButton[][] player2GridButtons, player2AttackGridButtons;
    private boolean[][] player1Grid, player2Grid;
    private int gridSize = 10;
    private int currentPlayer = 1;
    private JLabel instructionsLabel;
    private boolean gameInProgress = false;
    private boolean gamePaused = false;
    private int shipsToPlace = 5;
    private JTextArea logger;

    public Game() {

        player1Grid = new boolean[gridSize][gridSize];
        player2Grid = new boolean[gridSize][gridSize];

        frame = new JFrame("BattleShip by zenta . (Hugo Ghesquier)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 800);
        frame.setLayout(new BorderLayout());

        instructionsLabel = new JLabel("Joueur 1 : Placez vos bateaux (5 restants)");
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(instructionsLabel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> togglePause());
        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> System.exit(0));
        controlPanel.add(pauseButton);
        controlPanel.add(quitButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        logger = new JTextArea();
        logger.setEditable(false);
        JScrollPane loggerScrollPane = new JScrollPane(logger);
        loggerScrollPane.setPreferredSize(new Dimension(200, 800));
        frame.add(loggerScrollPane, BorderLayout.EAST);

        player1GridPanel = createGridPanel();
        player1AttackGridPanel = createGridPanel();
        player2GridPanel = createGridPanel();
        player2AttackGridPanel = createGridPanel();

        frame.add(player1GridPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        JButton[][] gridButtons = new JButton[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JButton button = new JButton();
                button.setBackground(Color.CYAN);

                final int currentRow = row;
                final int currentCol = col;

                button.addActionListener(e -> handleButtonClick(button, currentRow, currentCol, gridPanel));

                gridButtons[row][col] = button;
                gridPanel.add(button);
            }
        }

        if (gridPanel == player1GridPanel) player1GridButtons = gridButtons;
        if (gridPanel == player1AttackGridPanel) player1AttackGridButtons = gridButtons;
        if (gridPanel == player2GridPanel) player2GridButtons = gridButtons;
        if (gridPanel == player2AttackGridPanel) player2AttackGridButtons = gridButtons;

        return gridPanel;
    }

    private void handleButtonClick(JButton button, int row, int col, JPanel gridPanel) {
        if (gamePaused) {
            logAction("Le jeu est en pause !");
            return;
        }

        if (!gameInProgress) {

            placeShip(button, row, col, gridPanel);
        } else {

            attack(button, row, col);
        }
    }

    private void placeShip(JButton button, int row, int col, JPanel gridPanel) {
        boolean[][] currentGrid = (currentPlayer == 1) ? player1Grid : player2Grid;

        currentGrid[row][col] = true;
        button.setBackground(Color.GRAY);
        button.setEnabled(false);

        shipsToPlace--;

        if (shipsToPlace == 0) {
            if (currentPlayer == 1) {
                currentPlayer = 2;
                shipsToPlace = 5;
                JOptionPane.showMessageDialog(frame, "Joueur 2, à vous de placer vos bateaux !");
                frame.remove(player1GridPanel);
                frame.add(player2GridPanel, BorderLayout.CENTER);
                instructionsLabel.setText("Joueur 2 : Placez vos bateaux (5 restants)");
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Tous les bateaux ont été placés. Début de la bataille !");
                gameInProgress = true;
                currentPlayer = 1;
                frame.remove(player2GridPanel);
                frame.add(player1AttackGridPanel, BorderLayout.CENTER);
                instructionsLabel.setText("Joueur 1 : À vous d'attaquer !");
                frame.revalidate();
                frame.repaint();
            }
        } else {
            instructionsLabel.setText("Joueur " + currentPlayer + " : Placez vos bateaux (" + shipsToPlace + " restants)");
        }
    }

    private void attack(JButton button, int row, int col) {
        boolean[][] targetGrid = (currentPlayer == 1) ? player2Grid : player1Grid;

        if (targetGrid[row][col]) {
            button.setBackground(Color.RED);
            targetGrid[row][col] = false;
            logAction("Joueur " + currentPlayer + " a touché un bateau en " + toGridCoordinate(row, col));
        } else {
            button.setBackground(Color.BLUE);
            logAction("Joueur " + currentPlayer + " a raté en " + toGridCoordinate(row, col));
        }

        button.setEnabled(false);


        if (isGameOver()) {
            JOptionPane.showMessageDialog(frame, "Joueur " + currentPlayer + " a gagné !");
            frame.dispose();
            return;
        }

        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        JOptionPane.showMessageDialog(frame, "C'est au tour du joueur " + currentPlayer + " de jouer.");

        frame.remove((currentPlayer == 1) ? player2AttackGridPanel : player1AttackGridPanel);
        frame.add((currentPlayer == 1) ? player1AttackGridPanel : player2AttackGridPanel, BorderLayout.CENTER);

        instructionsLabel.setText("Joueur " + currentPlayer + " : À vous d'attaquer !");
        frame.revalidate();
        frame.repaint();
    }

    private boolean isGameOver() {
        boolean[][] targetGrid = (currentPlayer == 1) ? player2Grid : player1Grid;

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (targetGrid[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    private void logAction(String action) {
        logger.append(action + "\n");
        logger.setCaretPosition(logger.getDocument().getLength());
    }

    private String toGridCoordinate(int row, int col) {
        return "" + (char) ('A' + row) + (col + 1);
    }

    private void togglePause() {
        gamePaused = !gamePaused;
        logAction("Le jeu est " + (gamePaused ? "en pause" : "repris") + ".");
    }
}