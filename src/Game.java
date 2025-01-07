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
    private int[] shipsToPlace = {5, 4, 4, 3, 3, 2};
    private int currentShipIndex = 0;
    private JTextArea logger;

    public Game() {

        player1Grid = new boolean[gridSize][gridSize];
        player2Grid = new boolean[gridSize][gridSize];

        frame = new JFrame("BattleShip by zenta . (Hugo Ghesquier)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        instructionsLabel = new JLabel("Player 1: Place your boats (5 remaining)");
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(instructionsLabel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton pauseButton = new JButton("Stop");
        pauseButton.addActionListener(e -> togglePause());
        JButton quitButton = new JButton("Leave");
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
                button.setBackground(new Color(44, 62, 80));

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
            logAction("The game's on pause !");
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
        int shipSize = shipsToPlace[currentShipIndex];

        if (canPlaceShip(currentGrid, row, col, shipSize)) {
            for (int i = 0; i < shipSize; i++) {
                currentGrid[row][col + i] = true;
                gridPanel.getComponent(row * gridSize + col + i).setBackground(new Color(244, 246, 247));
                gridPanel.getComponent(row * gridSize + col + i).setEnabled(false);
            }

            currentShipIndex++;
            if (currentShipIndex >= shipsToPlace.length) {
                if (currentPlayer == 1) {
                    currentPlayer = 2;
                    currentShipIndex = 0;
                    JOptionPane.showMessageDialog(frame, "Player 2, it's your turn to place your boats !");
                    frame.remove(player1GridPanel);
                    frame.add(player2GridPanel, BorderLayout.CENTER);
                    instructionsLabel.setText("Player 2: Place your boats (5 remaining)");
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "All the boats have been placed. The battle begins !");
                    gameInProgress = true;
                    currentPlayer = 1;
                    frame.remove(player2GridPanel);
                    frame.add(player1AttackGridPanel, BorderLayout.CENTER);
                    instructionsLabel.setText("Player 1: It's your turn to attack !");
                    frame.revalidate();
                    frame.repaint();
                }
            } else {
                instructionsLabel.setText("Player " + currentPlayer + " : Position your boats (" + shipsToPlace[currentShipIndex] + " remaining)");
            }
        } else {
            logAction("Cannot place ship here!");
        }
    }

    private boolean canPlaceShip(boolean[][] grid, int row, int col, int size) {
        if (col + size > gridSize) return false;
        for (int i = 0; i < size; i++) {
            if (grid[row][col + i]) return false;
        }
        return true;
    }

    private void attack(JButton button, int row, int col) {
        boolean[][] targetGrid = (currentPlayer == 1) ? player2Grid : player1Grid;

        if (targetGrid[row][col]) {
            button.setBackground(new Color(17, 212, 37));
            targetGrid[row][col] = false;
            logAction("Player " + currentPlayer + " hit a boat in " + toGridCoordinate(row, col));
        } else {
            button.setBackground(new Color(247, 2, 2));
            logAction("Player " + currentPlayer + " missed in " + toGridCoordinate(row, col));
        }

        button.setEnabled(false);


        if (isGameOver()) {
            JOptionPane.showMessageDialog(frame, "Player " + currentPlayer + " has won !");
            frame.dispose();
            return;
        }

        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        JOptionPane.showMessageDialog(frame, "It's the player's turn " + currentPlayer + " to attack !");

        frame.remove((currentPlayer == 1) ? player2AttackGridPanel : player1AttackGridPanel);
        frame.add((currentPlayer == 1) ? player1AttackGridPanel : player2AttackGridPanel, BorderLayout.CENTER);

        instructionsLabel.setText("Player " + currentPlayer + " : It's your turn to attack !");
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
        logAction("The game is " + (gamePaused ? "is paused" : "is back on") + ".");
    }
}