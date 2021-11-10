package battleship;

import battleship.field.BattleshipField;
import battleship.ships.BaseShip;

import java.util.Locale;
import java.util.Scanner;

public class BattleshipGame extends BattleshipField {
    Scanner scanner;
    protected int torpedoes;

    /**
     * Запуск консольной версии игры
     */
    public void runGameCycle() {
        System.out.println("[BattleShip] Welcome to the game!");
        System.out.println("""
                [BattleShip] In that game you are fighting against computer.
                You have to sink all the ships in the Ocean to win. Your score equals to count of shots.
                The ship sinks when all its cells are attacked.""");
        System.out.println("[BattleShip] The Ocean is the field of Battleship game. " +
                "It is a rectangle (matrix) of square cells. \n" +
                "Enter width and height of battle field, e.g: 10x5 (10 by width and 5 by height)");
        scanner = new Scanner(System.in);
        setFieldSizeFromUserInput();
        System.out.println("""
                [BattleShip] Each ship of the Fleet might be of one of the following ship types,
                which differ with the number of vertically or horizontally adjacent Ocean cells in a line they occupy:
                ●\tCarrier  (5 cells);
                ●\tBattleship (4 cells);
                ●\tCruiser (3 cells);
                ●\tDestroyer (2 cell);
                ●\tSubmarine (1 cell).
                """);
        System.out.println("""
                [BattleShip] Enter comma-separated list of values to specify the number for each of the
                ship types, where these numbers are listed in the order which corresponds to ship types sizes
                (the largest first), then: “0, 1, 2, 3, 4” would mean:
                “no carrier, one battleship, two cruisers, three destroyers and four submarines""");
        if (setShipsCountFromUserInput()) {
            System.out.println("""
                    [BattleShips] How many torpedoes do you want for game? When a ship is hit with a torpedo,
                    it becomes sunk entirely, with no respect to its state and size.
                    Each torpedo shot increments the total shots count as usual.
                    """);
            if (!getTorpedoesCountFromUserInput() || !getShipRecoveryMode()) {
                return;
            }
            while (hasAliveShips()) {
                System.out.println(this);
                if (!nextMoveFromUserInput()) {
                    return;
                }
            }
            System.out.println("[BattleShip] All ships sunk! You win with score: " + score + " (lower is better)");
            System.out.println(this);
        }
    }

    public void runGameCycle(int rows, int columns, boolean recovery, int torpedoes,
                             int carriers, int battleShips, int cruisers, int destroyers, int submarines) {
        System.out.println("[BattleShip] Welcome to the game!");
        System.out.println("""
                [BattleShip] In that game you are fighting against computer.
                You have to sink all the ships in the Ocean to win. Your score equals to count of shots.
                The ship sinks when all its cells are attacked.""");
        System.out.println("[BattleShip] The Ocean is the field of Battleship game. " +
                "It is a rectangle (matrix) of square cells.");
        scanner = new Scanner(System.in);
        setSize(columns, rows);
        System.out.println("""
                [BattleShip] Each ship of the Fleet might be of one of the following ship types,
                which differ with the number of vertically or horizontally adjacent Ocean cells in a line they occupy:
                ●\tCarrier  (5 cells);
                ●\tBattleship (4 cells);
                ●\tCruiser (3 cells);
                ●\tDestroyer (2 cell);
                ●\tSubmarine (1 cell).
                """);
        System.out.println("[BattleShip] Loading game...");
        if (resetGame(carriers, battleShips, cruisers, destroyers, submarines)) {
            System.out.println("""
                        [BattleShip] Randomly placed the defined combination of the ships
                        in the Ocean with the following restrictions (adjacency rules):
                        ships cannot overlap, and there are no ships, which occupy immediately adjacent cells,
                        either horizontally, vertically, or diagonally.""");
            this.torpedoes = torpedoes;
            this.recoveryMode = recovery;
            while (hasAliveShips()) {
                System.out.println(this);
                if (!nextMoveFromUserInput()) {
                    return;
                }
            }
            System.out.println("[BattleShip] All ships sunk! You win with score: " + score + " (lower is better)");
            System.out.println(this);
        } else {
            System.out.println("""
                        [BattleShip] Unfortunately, you combination cannot be placed with following restrictions:
                        ships cannot overlap, and there are no ships, which occupy immediately adjacent cells,
                        either horizontally, vertically, or diagonally.""");
        }
    }

    /**
     * Запрашивает у пользователя размер поля и устанавливает его
     */
    public void setFieldSizeFromUserInput() {
        int width, height;
        while (true) {
            boolean correctInput = scanner.hasNext("[0-9]+x[0-9]+");
            if (!correctInput) {
                System.out.println("[BattleShip] Wrong format! Enter width and height of battle field. Example: 10x5");
                scanner.nextLine();
                continue;
            }
            String[] parsed = scanner.nextLine().split("x");
            width = Integer.parseInt(parsed[0]);
            height = Integer.parseInt(parsed[1]);
            if (width == 0 || height == 0) {
                System.out.println("[BattleShip] Both width and height must be greater then zero!");
                continue;
            }
            break;
        }
        System.out.println("[BattleShip] Alright, your field will be "
                + width + " cells horizontally and " + height + " vertically.");
        setSize(width, height);
    }

    /**
     * Запрашивает у пользователя количество кораблей каждого типа и генерирует поле
     * @return true - продолжать игру, false - выйти
     */
    public boolean setShipsCountFromUserInput() {
        int carriers, battleShips, cruisers, destroyers, submarines;
        while (true) {
            String nextLine = scanner.nextLine().strip();
            boolean quit = nextLine.matches("\\s*quit\\s");
            if (quit) {
                System.out.println("[BattleShip] See you next time!");
                return false;
            }
            boolean correctInput = nextLine.matches("[0-9]+[,]\\s*[0-9]+,\\s*[0-9]+,\\s*[0-9]+,\\s*[0-9]+\\s*");
            if (!correctInput) {
                System.out.println("[BattleShip] Wrong format! " +
                        "Enter 5 digits separated with comma. \n" +
                        "Example: 0, 1, 2, 3, 4");
                continue;
            }
            String[] parsed = nextLine.split(",\s*");
            carriers = Integer.parseInt(parsed[0]);
            battleShips = Integer.parseInt(parsed[1]);
            cruisers = Integer.parseInt(parsed[2]);
            destroyers = Integer.parseInt(parsed[3]);
            submarines = Integer.parseInt(parsed[4]);
            System.out.println("[BattleShip] Loading game...");
            if (resetGame(carriers, battleShips, cruisers, destroyers, submarines)) {
                System.out.println("""
                        [BattleShip] Randomly placed the defined combination of the ships
                        in the Ocean with the following restrictions (adjacency rules):
                        ships cannot overlap, and there are no ships, which occupy immediately adjacent cells,
                        either horizontally, vertically, or diagonally.""");
                break;
            } else {
                System.out.println("""
                        [BattleShip] Unfortunately, you combination cannot be placed with following restrictions:
                        ships cannot overlap, and there are no ships, which occupy immediately adjacent cells,
                        either horizontally, vertically, or diagonally.""");
                System.out.println("[BattleShip] Please try again, or quit the game by entering \"quit\"");
            }
        }
        return true;
    }

    /**
     * Запрашивает у пользователя следующий ход и выполняет его
     * @return true - продолжать игру, false - выйти
     */
    public boolean nextMoveFromUserInput() {
        int row, column;
        boolean torpedo;
        while (true) {
            System.out.println("""
                [BattleShip] Enter the coordinates (row and column) of the point you want to hit.
                Add "T" before coordinates to throw torpedo (e.g. "T 1 1"). Note that left upper point is "1 1".
                """);
            String nextLine = scanner.nextLine();
            if (nextLine.matches("\s*quit\s*")) {
                return false;
            }
            if (!nextLine.matches("[T]*\s*[0-9]+\s*[0-9]+")) {
                System.out.println("""
                    [BattleShip] Wrong format!
                    Enter two positive integers that correspond to the number of the row and column to attack""");
                continue;
            }
            String[] parsed = nextLine.strip().split("\s+");
            if (parsed[0].equals("T")) {
                torpedo = true;
                row = Integer.parseInt(parsed[1]);
                column = Integer.parseInt(parsed[2]);
            } else {
                torpedo = false;
                row = Integer.parseInt(parsed[0]);
                column = Integer.parseInt(parsed[1]);
            }
            if (torpedo && torpedoes <= 0) {
                System.out.println("[BattleShip] There are no available torpedoes!");
                continue;
            }
            if (row < 1 || row > height) {
                System.out.println("[BattleShip] Try again! Row must be from 1 to " + height);
                continue;
            }
            if (column < 1 || column > width) {
                System.out.println("[BattleShip] Try again! Column must be from 1 to " + width);
                continue;
            }
            if (attacked(row - 1, column - 1)) {
                System.out.println("[BattleShip] Try again! That cell is already attacked");
                continue;
            }
            break;
        }
        if (hit(row - 1, column - 1, torpedo)) {
            BaseShip ship = shipAt(row - 1, column - 1);
            if (ship.sunk()) {
                System.out.println("[BattleShip] You just have sunk a " + ship.getClass().getSimpleName() + ".");
            } else {
                System.out.println("[BattleShip] Hit!");
            }
        } else {
            System.out.println("[BattleShip] Miss!");
        }
        return true;
    }

    /**
     * Запрашивает у пользователя количество торпед и записывает его
     * @return true - продолжать игру, false - выйти
     */
    public boolean getTorpedoesCountFromUserInput() {
        while (true) {
            String nextLine = scanner.nextLine().strip();
            if (nextLine.matches("\\s*quit\\s*")) {
                return false;
            }
            if (nextLine.matches("[0-9]+")) {
                torpedoes = Integer.parseInt(nextLine);
                System.out.println("[BattleShip] Ok, in that game you will have " + torpedoes + " torpedoes.");
                return true;
            } else {
                System.out.println("[BattleShip] Wrong format! Enter exactly one positive integer of zero.");
            }
        }
    }

    public boolean getShipRecoveryMode() {
        System.out.println("[BattleShip] Do you want to enable ship recovery mode: yes/no?\n" +
                "In recovery mode, ships will be recovered if you miss.");
        while (true) {
            String nextLine = scanner.nextLine().strip().toLowerCase(Locale.ROOT);
            if (nextLine.matches("\\s*quit\\s*")) {
                return false;
            }
            if (nextLine.matches("(yes|no)")) {
                if (nextLine.equals("yes")) {
                    recoveryMode = true;
                    System.out.println("[BattleShip] Recovery mode enabled!");
                } else {
                    recoveryMode = false;
                    System.out.println("[BattleShip] Recovery mode disabled!");
                }
                break;
            } else {
                System.out.println("[BattleShip] Wrong format! Enter \"yes\" or \"no\".");
            }
        }
        return true;
    }
}
