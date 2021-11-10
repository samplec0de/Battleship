package battleship.ships;

/**
 * Частная реализация корабля вида "Cruiser" на 3 ячейки
 */
public class Cruiser extends BaseShip {
    /**
     * Конструктор корабля Cruiser, задает размер 3
     */
    public Cruiser() {
        cells = 3;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "C";
    }
}
