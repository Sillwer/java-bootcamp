package edu.school.game.gamelogic;

import edu.school.chaselogic.Pursuing;
import edu.school.game.exceptions.IllegalParametersException;
import edu.school.game.exceptions.UserLostException;
import edu.school.game.exceptions.UserWinException;
import edu.school.game.mapgenerator.MapGenerator;
import edu.school.game.models.*;
import edu.school21.models.Coordinates2D;
import edu.school21.models.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapProcessor {
    private final Integer mapSize;
    private final Integer enemiesCount;
    private final Integer wallsCount;
    private GameMap gameMap;

    private final Random random = new Random();

    private final Unit[] enemies;
    private final Coordinates2D[] enemiesStartPositions;

    private Unit goal;
    private Coordinates2D goalStartPositions;

    private Unit player;
    private Coordinates2D playerStartPositions;

    private final Unit empty = new Unit(UnitType.EMPTY, null);
    private final MapGenerator mapGenerator;

    private enum Direction {
        LEFT('A'), RIGHT('D'), UP('W'), DOWN('S');
        private char key;

        Direction(char key) {
            this.key = key;
        }

        public static Direction byKey(char key) {
            for (Direction d : values()) {
                if (d.key == key) {
                    return d;
                }
            }
            return null;
        }
    }

    public MapProcessor(Integer mapSize, Integer enemiesCount, Integer wallsCount) {
        this.mapSize = mapSize;
        this.enemiesCount = enemiesCount;
        this.wallsCount = wallsCount;
        this.mapGenerator = new MapGenerator(mapSize, mapSize, wallsCount);
        enemies = new Unit[enemiesCount];
        enemiesStartPositions = new Coordinates2D[enemiesCount];
        // TODO
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    void generateMap() {
        List<String> errors = new ArrayList<>();
        final int FIELD_NODES_NUMBER = mapSize * mapSize;

        if (enemiesCount < 1) {
            errors.add("Enemies' count can't be less than 1");
        }
        if (wallsCount < 0) {
            errors.add("Walls' count can't be less than 0");
        }
        if (FIELD_NODES_NUMBER < UnitType.values().length * 2) {
            errors.add(String.format("Field's size=%d (%d * %d) can't be less than %s",
                    FIELD_NODES_NUMBER, mapSize, mapSize, UnitType.values().length * 2));
        }
        if (FIELD_NODES_NUMBER < (enemiesCount + wallsCount + 1) * 2) {
            errors.add(String.format("Field's size=%d (%d * %d) can't be less than %s ((enemiesCount + wallsCount + 1) * 2)",
                    FIELD_NODES_NUMBER, mapSize, mapSize, (enemiesCount + wallsCount + 1) * 2));
        }

        if (!errors.isEmpty()) {
            throw new IllegalParametersException("Game map generation exception:\n\t" + String.join("\n\t", errors));
        }

        gameMap = new GameMap(mapSize);
        final List<Coordinates2D> freeCells = new ArrayList<>();

        boolean[][] generatedMapWithWall = mapGenerator.getNewMap();
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                gameMap.setUnit(
                        new Unit(generatedMapWithWall[x][y] ? UnitType.WALL : UnitType.EMPTY, null),
                        new Coordinates2D(x, y)
                );
                if (!generatedMapWithWall[x][y]) {
                    freeCells.add(new Coordinates2D(x, y));
                }
            }
        }

        for (int i = 0; i < enemiesCount; i++) {
            enemiesStartPositions[i] = freeCells.get(random.nextInt(freeCells.size()));
            freeCells.remove(enemiesStartPositions[i]);
        }

        playerStartPositions = freeCells.get(random.nextInt(freeCells.size()));
        freeCells.remove(playerStartPositions);

        goalStartPositions = freeCells.get(random.nextInt(freeCells.size()));
        while (!freeCells.isEmpty()) {
            Coordinates2D coors = freeCells.get(random.nextInt(freeCells.size()));
            freeCells.remove(coors);
            double distX = Math.abs(coors.x - playerStartPositions.x);
            double distY = Math.abs(coors.y - playerStartPositions.y);
            double dist = Math.sqrt(distX * distX + distY * distY);
            if ((dist >= (double) mapSize / 2)) {
                goalStartPositions = coors;
                break;
            }
        }

        installActiveUnits();
    }

    void installActiveUnits() {
        if (player != null) {
            for (Unit enemy : enemies) {
                gameMap.setUnit(empty, enemy.getCoordinates());
            }
            gameMap.setUnit(empty, player.getCoordinates());
            gameMap.setUnit(empty, goal.getCoordinates());
        }

        for (int i = 0; i < enemiesCount; i++) {
            enemies[i] = new Unit(UnitType.ENEMY, enemiesStartPositions[i]);
            gameMap.setUnit(enemies[i], enemies[i].getCoordinates());
        }

        player = new Unit(UnitType.PLAYER, playerStartPositions);
        gameMap.setUnit(player, player.getCoordinates());

        goal = new Unit(UnitType.GOAL, goalStartPositions);
        gameMap.setUnit(goal, goal.getCoordinates());
    }

    boolean movePlayer(char moveKey) {
        Direction direction = Direction.byKey(moveKey);
        if (!canMoveThatDirection(player, direction)) {
            return false;
        }

        Coordinates2D nextCoors = nextCoordinates(player, direction);
        if (nextCoors == null) {
            return false;
        }

        Unit unitAtNextStep = gameMap.getUnit(nextCoors);
        switch (unitAtNextStep.getUnitType()) {
            case GOAL:
                throw new UserWinException(">>> The player has reached the goal. Victory <<<");
            case ENEMY:
                throw new UserLostException(">>> The player came across an enemy. Game over <<<");
        }

        moveUnit(player, nextCoors);
        return true;
    }

    private void moveUnit(Unit unit, Coordinates2D nextCoors) {
        gameMap.setUnit(empty, unit.getCoordinates());
        gameMap.setUnit(unit, nextCoors);
        unit.setCoordinates(nextCoors);
    }

    private boolean canMoveThatDirection(Unit unit, Direction direction) {
        if (direction == null) {
            return false;
        }

        Coordinates2D nextCoors = nextCoordinates(unit, direction);
        if (nextCoors == null) {
            return false;
        }
        return canStepAtCoordinates(unit, nextCoors);
    }

    private boolean canStepAtCoordinates(Unit unit, Coordinates2D nextCoors) {
        if (nextCoors == null
                || nextCoors.x < 0 || nextCoors.x > mapSize - 1
                || nextCoors.y < 0 || nextCoors.y > mapSize - 1) {
            return false;
        }

        UnitType unitAtNextCoors = gameMap.getUnit(nextCoors).getUnitType();
        switch (unit.getUnitType()) {
            case PLAYER:
                return unitAtNextCoors != UnitType.WALL;
            case ENEMY:
                return unitAtNextCoors != UnitType.WALL
                        && unitAtNextCoors != UnitType.GOAL
                        && unitAtNextCoors != UnitType.ENEMY;
            default:
                return false;
        }
    }

    /**
     * @return null if next coordinates out of map
     */
    private Coordinates2D nextCoordinates(Unit unit, Direction direction) {
        int x = unit.getCoordinates().x;
        int y = unit.getCoordinates().y;
        switch (direction) {
            case UP:
                x -= 1;
                break;
            case DOWN:
                x += 1;
                break;
            case LEFT:
                y -= 1;
                break;
            case RIGHT:
                y += 1;
                break;
        }

        if (x >= 0 && x < mapSize && y >= 0 && y < mapSize) {
            return new Coordinates2D(x, y);
        }

        return null;
    }

    boolean moveEnemy(int i) {
        if (i >= enemiesCount) {
            return false;
        }
        Unit enemy = enemies[i];
        if (canCatchPlayer(enemy)) {
            throw new UserLostException(">>> The enemy caught the player. The end of the game <<<");
        }

        List<Pair<Coordinates2D, Double>> weights = Pursuing.evaluateMoveWeights(
                enemy.getCoordinates(),
                player.getCoordinates(),
                goal.getCoordinates()
        );

        weights.sort((w1, w2) -> (int) ((w2.val - w1.val) * 10));
        for (Pair<Coordinates2D, Double> entry : weights) {
            if (canStepAtCoordinates(enemy, entry.key)) {
                moveUnit(enemy, entry.key);
                break;
            }
        }

        if (userSurrounded()) {
            throw new UserLostException(">>> The enemy surround the player. Game over <<<");
        }

        return true;
    }

    private boolean canCatchPlayer(Unit enemy) {
        int playerX = player.getCoordinates().x;
        int playerY = player.getCoordinates().y;

        int enemyX = enemy.getCoordinates().x;
        int enemyY = enemy.getCoordinates().y;

        return (enemyX == playerX && enemyY >= playerY - 1 && enemyY <= playerY + 1)
                || (enemyY == playerY && enemyX >= playerX - 1 && enemyX <= playerX + 1);
    }

    private boolean userSurrounded() {
        Coordinates2D[] nearCoors = new Coordinates2D[]{
                new Coordinates2D(player.getCoordinates().x, player.getCoordinates().y - 1),
                new Coordinates2D(player.getCoordinates().x, player.getCoordinates().y + 1),
                new Coordinates2D(player.getCoordinates().x - 1, player.getCoordinates().y),
                new Coordinates2D(player.getCoordinates().x + 1, player.getCoordinates().y),
        };

        int surroundFlags = 0;
        for (Coordinates2D coors : nearCoors) {
            int x = coors.x, y = coors.y;
            if (x < 0 || y < 0 || x >= mapSize || y >= mapSize) {
                surroundFlags++;
                continue;
            }

            switch (gameMap.getUnit(coors).getUnitType()) {
                case WALL:
                case ENEMY:
                    surroundFlags++;
            }
        }
        return surroundFlags == 4;
    }
}
