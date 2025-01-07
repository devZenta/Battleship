# Bataille Navale

Bataille Navale is a classic battleship game implemented in Java using Swing for the graphical user interface. This project allows two players to place their ships on a grid and take turns attacking each other's ships until one player wins.

## Features

- Two-player mode
- Graphical user interface with Swing
- Ship placement phase
- Attack phase with alternating turns
- Game pause and resume functionality
- Game log to track actions

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- IntelliJ IDEA or any other Java IDE

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/bataille-navale.git
    ```
2. Open the project in your IDE.

### Running the Game

1. Navigate to the `src` directory.
2. Run the `Main.java` file to start the game.

### How to Play

1. **Ship Placement Phase**:
    - Player 1 places 5 ships on their grid.
    - Player 2 places 5 ships on their grid.
2. **Attack Phase**:
    - Player 1 starts the attack phase.
    - Players take turns to attack each other's grid.
    - A pop-up message will indicate the next player's turn.
3. **Winning the Game**:
    - The game ends when all ships of one player are destroyed.
    - A pop-up message will announce the winner.

## Code Structure

- `Main.java`: Entry point of the application.
- `Game.java`: Contains the main game logic, including ship placement and attack phases.
- `createGridPanel()`: Creates the grid panels for both players.
- `handleButtonClick()`: Handles button clicks for both ship placement and attacks.
- `placeShip()`: Manages the ship placement logic.
- `attack()`: Manages the attack logic and alternates turns between players.
- `isGameOver()`: Checks if the game is over.
- `logAction()`: Logs actions to the game log.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Acknowledgements

- [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/) for the graphical user interface.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) for the development environment.