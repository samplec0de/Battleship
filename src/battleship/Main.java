package battleship;

public class Main {

    public static void main(String[] args) {
        int rows = -1, columns = -1, torpedoes = -1;
        int carriers = -1, battleShips = -1, cruisers = -1, destroyers = -1, submarines = -1;
        int recovery = -1;
        boolean wasWrong = false;
        boolean useCommandLine = args.length != 0;
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.matches("--rows=[0-9]+")) {
                rows = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.matches("--columns=[0-9]+")) {
                columns = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.matches("--torpedoes=[0-9]+")) {
                torpedoes = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.matches("--recovery=(yes|no)")) {
                String tmp = arg.split("=")[1];
                if (tmp.equals("yes")) {
                    recovery = 1;
                } else {
                    recovery = 0;
                }
            } else if (arg.matches("--ships=[0-9]+,[0-9]+,[0-9]+,[0-9]+,[0-9]+")) {
                String[] tmp = arg.split("=")[1].split(",");
                carriers = Integer.parseInt(tmp[0]);
                battleShips = Integer.parseInt(tmp[1]);
                cruisers = Integer.parseInt(tmp[2]);
                destroyers = Integer.parseInt(tmp[3]);
                submarines = Integer.parseInt(tmp[4]);
            } else {
                System.out.println("[BattleShip] Wrong parameter " + arg);
                wasWrong = true;
            }
        }

        BattleshipGame game = new BattleshipGame();

        if (useCommandLine && (wasWrong
                || rows == -1 || columns == -1 || torpedoes == -1 || carriers == -1 || recovery == -1)) {
            System.out.println("""
                    [BattleShip] Help page
                    If you are running in console args mode, you must provide ALL these arguments:
                    --rows=<Integer> - number of field rows
                    --columns=<Integer> - number of field columns
                    --ships=<Integer>,<Integer>,<Integer>,<Integer>,<Integer> - number of ships from longest to shortest
                    (5 to 1 cells)
                    --recovery=yes/no - enable ships recovery mode
                    --torpedoes=<Integer> - number of available torpedoes
                    If you do not know meaning of parameters, see description in runtime-input version
                    (run without parameters)
                    
                    Example: java -jar Game.jar --rows=5 --columns=10 --ships=3,0,0,0,0 --recovery=no --torpedoes=4
                    """);
        } else if (useCommandLine) {
            game.runGameCycle(rows, columns, recovery == 1, torpedoes,
                    carriers, battleShips, cruisers, destroyers, submarines);
        } else {
            game.runGameCycle();
        }
    }
}
