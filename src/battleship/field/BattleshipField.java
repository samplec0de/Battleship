package battleship.field;

import battleship.ships.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BattleshipField {
    protected final int MAX_RETRIES = 100000;
    protected int width;
    protected int height;
    protected int score = 0;
    protected boolean recoveryMode;
    protected FieldCell[][] cells;

    /**
     * Устанавливает размер поля
     * @param width ширина поля (количество столбцов)
     * @param height высота поля (количество строк)
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new FieldCell[height][width];
    }

    /**
     * Случайным образом расставляет корабли
     * @param carriers количество кораблей типа Carrier
     * @param battleShips количество кораблей типа Battleship
     * @param cruisers количество кораблей типа Cruiser
     * @param destroyers количество кораблей типа Destroyer
     * @param submarines количество кораблей типа Submarine
     * @return true - удалось расставить корабли в заданном количестве на поле,
     * false - не удалось расставить корабли по правилам
     */
    public boolean resetGame(int carriers, int battleShips, int cruisers, int destroyers, int submarines) {
        int totalShips = battleShips + carriers + cruisers + destroyers + submarines;

        ArrayList<BaseShip> ships = new ArrayList<>();
        for (int i = 0; i < battleShips; ++i) {
            ships.add(new Battleship());
        }
        for (int i = 0; i < carriers; ++i) {
            ships.add(new Carrier());
        }
        for (int i = 0; i < cruisers; ++i) {
            ships.add(new Cruiser());
        }
        for (int i = 0; i < destroyers; ++i) {
            ships.add(new Destroyer());
        }
        for (int i = 0; i < submarines; ++i) {
            ships.add(new Submarine());
        }

        int retries = 0;

        while (retries < MAX_RETRIES) {

            retries += 1;

            clearField();

            boolean placedAll = true;
            for (int i = 0; i < totalShips; ++i) {
                int row = ThreadLocalRandom.current().nextInt(0, height);
                int column = ThreadLocalRandom.current().nextInt(0, width);
                boolean vertically = ThreadLocalRandom.current().nextBoolean();
                if (!placeShip(ships.get(i), row, column, vertically)) {
                    placedAll = false;
                    break;
                }
            }

            if (placedAll) {
                return true;
            }
            clearField();
        }
        return false;
    }

    /**
     * Очищает поле от кораблей, выставляет пустые ячейки
     */
    protected void clearField() {
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                this.cells[i][j] = new FieldCell();
            }
        }
    }

    /**
     * Проверяет можно ли уместить корабль не нарушая правил (два корабля не имеют общих ячеек и не касаются)
     * @param ship корабль
     * @param row строка
     * @param column столбец
     * @param vertically ориентация (true - вертикальная, false - горизонтальная)
     * @return возможность размещения корабля
     */
    private boolean shipFits(BaseShip ship, int row, int column, boolean vertically) {
        if (vertically) {
            if (row + ship.getCellsCount() > this.height) {
                return false;
            }
            for (int i = row; i < row + ship.getCellsCount(); ++i) {
                if (this.cells[i][column].ship != null || cellHasNeighbour(i, column)) {
                    return false;
                }
            }
        } else {
            if (column + ship.getCellsCount() > this.width) {
                return false;
            }
            for (int j = column; j < column + ship.getCellsCount(); ++j) {
                if (this.cells[row][j].ship != null || cellHasNeighbour(row, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Размещает корабль (передаются координаты кармы коробля)
     * @param ship корабль
     * @param row строка
     * @param column столбец
     * @param vertically ориентация (true - вертикальная, false - горизонтальная)
     * @return успех размещения
     */
    protected boolean placeShip(BaseShip ship, int row, int column, boolean vertically) {
        if (!shipFits(ship, row, column, vertically)) {
            return false;
        }
        if (vertically) {
            for (int i = row; i < row + ship.getCellsCount(); ++i) {
                this.cells[i][column].ship = ship;
            }
        } else {
            for (int j = column; j < column + ship.getCellsCount(); ++j) {
                this.cells[row][j].ship = ship;
            }
        }
        return true;
    }

    /**
     * Флаг наличия корабля по заданным координатам
     * @param row строка
     * @param column столбец
     * @return стоит ли корабль на заданной ячейке
     */
    protected boolean isShip(int row, int column) {
        if (row < 0 || column < 0 || row >= height || column >= width) {
            return false;
        }
        return cells[row][column].ship != null;
    }

    /**
     * Флаг возможности размещения корабля на ячейке
     * @param row строка
     * @param column столбец
     * @return есть ли у клетки занятые соседи
     */
    protected boolean cellHasNeighbour(int row, int column) {
        int[] deltaRow = {1, -1, 0, 0, 1, -1, 1, -1};
        int[] deltaColumn = {0, 0, 1, -1, 1, -1, -1, 1};
        for (int i = 0; i < 8; ++i) {
            if (isShip(row + deltaRow[i], column + deltaColumn[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Наносит удар по клетке
     * @param row строка
     * @param column столбец
     * @param torpedo удар торпедой
     * @return true - если в клетке есть корабль, false - если нет
     */
    public boolean hit(int row, int column, boolean torpedo) {
        if (row < 0 || column < 0 || row >= height || column >= width) {
            return false;
        }
        score += 1;
        if (isShip(row, column)) {
            if (!cells[row][column].attacked) {
                cells[row][column].attacked = true;
                cells[row][column].ship.attack(torpedo);
            }
            return true;
        }
        cells[row][column].attacked = true;
        if (recoveryMode) {
            boolean clearFlag = false;
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    if (cells[i][j].attacked && isShip(i, j) && !shipAt(i, j).sunk()) {
                        cells[i][j].attacked = false;
                        shipAt(i, j).recover();
                        clearFlag = true;
                    }
                }
            }
            if (clearFlag) {
                System.out.println("[BattleShip] Ships were recovered because of recovery mode!");
            }
        }
        return false;
    }

    /**
     * Атакована ли клетка
     * @param row строка
     * @param column столбец
     * @return true - если игрок атаковал клетку, false - если нет
     */
    public boolean attacked(int row, int column) {
        return row >= 0 && row < height && column >= 0 && column < width && cells[row][column].attacked;
    }

    /**
     * Возвращает объект корабля по координатам
     * @param row строка
     * @param column столбец
     * @return корабль или null
     */
    public BaseShip shipAt(int row, int column) {
        if (row >= 0 && row < height && column >= 0 && column < width) {
            return cells[row][column].ship;
        }
        return null;
    }

    /**
     * Признак наличия не потопленных кораблей на поле
     * @return true - если есть ещё не потопленные корабли, false - если все корабли потоплены
     */
    public boolean hasAliveShips() {
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                if (cells[i][j].ship != null && !cells[i][j].ship.sunk()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Строковое представление поля
     * @return строка, содержащая поле
     */
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                ans.append(" ");
                ans.append(cells[i][j].getIcon());
                ans.append(" ");
            }
            ans.append("\n");
        }
        return ans.toString();
    }

}
