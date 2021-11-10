package battleship.ships;

/**
 * Частная реализация корабля вида "Carrier" на 5 ячеек
 */
public class Carrier extends BaseShip {
    /**
     * Конструктор корабля Carrier, задает размер 5
     */
    public Carrier() {
        cells = 5;
    }

    /**
     * Иконка
     * @return строка с иконкой потонувшего корабля
     */
    public String getIcon() {
        return "C";
    }
}
