import java.util.Scanner;
import java.util.logging.Logger;
import java.util.Random;

class App {

    private static final char[] BOX = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static final int BOARD_SIZE = 9;
    private static final char PLAYER_MARK = 'X';
    private static final char COMPUTER_MARK = 'O';
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static final Random random = new Random();

    public static void main(String[] args) {
        logger.info("Enter box number to select. Enjoy!\n");

        while (true) {
            displayBoard();

            int gameStatus = checkGameStatus();
            if (gameStatus != 0) {
                displayResult(gameStatus);
                break;
            }

            makePlayerMove();
            gameStatus = checkGameStatus();

            // Перевіряємо статус гри після ходу гравця
            if (gameStatus != 0) {
                displayBoard();
                displayResult(gameStatus);
                break;
            }

            makeComputerMove();
        }
        SCANNER.close(); // Закриття Scanner додає правильне управління ресурсами для запобігання потенційних витоків
    }

    private static void displayBoard() {
        System.out.println(String.format(
                "%n%n %c | %c | %c %n-----------%n %c | %c | %c %n-----------%n %c | %c | %c %n",
                BOX[0], BOX[1], BOX[2],
                BOX[3], BOX[4], BOX[5],
                BOX[6], BOX[7], BOX[8]
        ));
    }

    private static int checkGameStatus() {       // зберігає всі можливі виграшні комбінації і перевіряє,
                                                 // чи є переможець. Це усуває дублювання та підвищує читабельність
        int[][] winningCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Рядки
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Стовбці
                {0, 4, 8}, {2, 4, 6}             // Діагоналі
        };

        for (int[] combo : winningCombinations) {
            if (BOX[combo[0]] == PLAYER_MARK && BOX[combo[1]] == PLAYER_MARK && BOX[combo[2]] == PLAYER_MARK) {
                return 1; // Гравець одержує перемогу
            }
            if (BOX[combo[0]] == COMPUTER_MARK && BOX[combo[1]] == COMPUTER_MARK && BOX[combo[2]] == COMPUTER_MARK) {
                return 2; // Комп'ютер одержує перемогу
            }
        }

        for (char c : BOX) {
            if (c != PLAYER_MARK && c != COMPUTER_MARK) {
                return 0; // Гра ще триває
            }
        }

        return 3; // Нічия
    }

    private static void makePlayerMove() {             // обробляє хід гравця з перевіркою введення
        while (true) {
            logger.info("Enter your move (1-9): ");
            byte input = SCANNER.nextByte();
            if (input > 0 && input <= BOARD_SIZE && isBoxFree(input - 1)) {
                BOX[input - 1] = PLAYER_MARK;
                break;
            }
            logger.info("Invalid input or box already taken. Please try again.");
        }
    }

    private static void makeComputerMove() {         // обробляє хід комп'ютера з випадковим вибором вільного поля
        while (true) {
            int rand = random.nextInt(BOARD_SIZE);
            if (isBoxFree(rand)) {
                BOX[rand] = COMPUTER_MARK;
                break;
            }
        }
    }

    private static boolean isBoxFree(int index) {
        return BOX[index] != PLAYER_MARK && BOX[index] != COMPUTER_MARK;
    }

    private static void displayResult(int gameStatus) {
        displayBoard();
        switch (gameStatus) {
            case 1:
                logger.info("You won the game!\nThanks for playing!");
                break;
            case 2:
                logger.info("You lost the game!\nThanks for playing!");
                break;
            case 3:
                logger.info("It's a draw!\nThanks for playing!");
                break;
            default:
                logger.warning("Invalid game status.");
        }
    }
}