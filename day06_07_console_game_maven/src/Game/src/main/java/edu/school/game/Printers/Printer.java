package edu.school.game.Printers;

import edu.school.game.models.GameMap;
import edu.school.game.models.UnitType;
import edu.school21.models.Coordinates2D;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Printer {
    private final int CLEAR_WIDTH = 60;
    private final String HELP_MSG = "restart: 4 | new map: 6 | exit: 9";
    private final boolean needCleanScreen;

    private GameMap gameMap;
    private String surroundMapSpace;

    public Printer(boolean needCleanScreen) {
        this.needCleanScreen = needCleanScreen;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
        updateSurroundText();
    }

    private void updateSurroundText() {
        int mapWidth = gameMap.getSize();
        int mapHeight = gameMap.getSize();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\033[1;%dH", (HELP_MSG.length() + 1)));
        for (int i = 0; i < CLEAR_WIDTH - HELP_MSG.length(); i++) {
            sb.append(' ');
        }

        if (mapWidth < CLEAR_WIDTH) {
            for (int line = 2, i = 0; i < mapHeight; i++) {
                sb.append(String.format("\033[%d;%dH", line++, (mapWidth + 1)));
                for (int j = 0; j < CLEAR_WIDTH - mapWidth; j++) {
                    sb.append(' ');
                }
            }
        }

        sb.append(String.format("\033[%d;1H", (mapHeight + 2)));
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < CLEAR_WIDTH; i++) {
                sb.append(' ');
            }
            sb.append('\n');
        }

        sb.append("\033[1;1H");
        surroundMapSpace = sb.toString();
    }

    public void display() {
        if (needCleanScreen) {
            System.out.print(surroundMapSpace);
        }

        System.out.println(HELP_MSG);

        int size = gameMap.getSize();
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                UnitType ut = gameMap.getUnit(new Coordinates2D(x, y)).getUnitType();
                sb.append(colorize("" + ut.getSymbol(), ut.getColorAttributes()));
                if (y == size - 1) {
                    sb.append('\n');
                }
            }
        }
        System.out.print(sb);
    }


}
