package battleship.field;

import battleship.ships.BaseShip;

/**
 * Яччейка игрового коля
 */
public class FieldCell {
    boolean attacked = false;
    BaseShip ship = null;

    /**
     * Иконка
     * @return строка, содержащая иконку ячейки в зависимости от статуса:
     * не атакована, атакована без корабля, есть корабль и атакована, есть корабль и он потоплен
     */
    public String getIcon() {
        if (ship == null) {
            if (attacked) {
                return "x";
            } else {
                return ".";
            }
        } else {
            if (ship.sunk()) {
                return ship.getIcon();
            } else if (attacked) {
                return "o";
            } else {
                return ".";
            }
        }
    }
}
