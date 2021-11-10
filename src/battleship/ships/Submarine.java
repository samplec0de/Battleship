package battleship.ships;

/**
 * Частная реализация корабля вида "Submarine" на 1 ячейку
 */
public class Submarine extends BaseShip {
    /**
     * Конструктор корабля Submarine, задает размер 1
     */
    public Submarine() {
        cells = 1;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "S";
    }
}
