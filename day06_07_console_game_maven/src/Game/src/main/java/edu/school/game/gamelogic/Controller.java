package edu.school.game.gamelogic;

import edu.school.game.Printers.Printer;

public class Controller {
    private final boolean devMode;
    private final MapProcessor mapProcessor;
    private final Printer printer;

    public Controller(Integer fieldSize, Integer enemiesCount, Integer wallsCount, boolean devMode) {
        this.mapProcessor = new MapProcessor(fieldSize, enemiesCount, wallsCount);
        this.devMode = devMode;
        printer = new Printer(!devMode);
    }

    public boolean isDevMode() {
        return devMode;
    }

    void generateMap() {
        mapProcessor.generateMap();
        printer.setGameMap(mapProcessor.getGameMap());
    }

    void restartMap() {
        mapProcessor.installActiveUnits();
    }

    void updateScreen() {
        printer.display();
    }

    boolean movePlayer(char moveKey) {
        return mapProcessor.movePlayer(moveKey);
    }

    /**
     * @return true - enemy was acted; false - index more than enemies number
     */
    boolean moveEnemy(int i) {
        return mapProcessor.moveEnemy(i);
    }
}
