package battleship.ships;

/**
 * Частная реализация корабля вида "Destroyer" на 2 ячейки
 */
public class Destroyer extends BaseShip {
    /**
     * Конструктор корабля Destroyer, задает размер 2
     */
    public Destroyer() {
        cells = 2;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "D";
    }
}
