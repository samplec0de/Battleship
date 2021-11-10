package battleship.ships;

/**
 * Частная реализация корабля вида "Battleship" на 4 ячейки
 */
public class Battleship extends BaseShip {
    /**
     * Конструктор корабля Battleship, задает размер 4
     */
    public Battleship() {
        cells = 4;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "B";
    }
}
