package battleship.ships;

/**
 * Базовый класс корабля
 */
public class BaseShip {

    protected int cells;

    protected int attackedCells = 0;;

    /**
     * Название
     * @return название корабля, определенное как имя класса
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Число ячеек
     * @return количество ячеек, которое занимает корабль
     */
    public int getCellsCount() {
        return cells;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "X";
    }

    /**
     * Флаг потопленности
     * @return true, если корабль затонул и false если еще нет
     */
    public boolean sunk() {
        return attackedCells >= cells;
    }

    /**
     * Атакует корабль (увеличивает счетчик атакованных ячеек или топит торпедой)
     */
    public void attack(boolean torpedo) {
        attackedCells += 1;
        if (torpedo) {
            attackedCells = cells;
        }
    }

    /**
     * Восстанавливает корабль
     */
    public void recover() {
        attackedCells = 0;
    }
}

