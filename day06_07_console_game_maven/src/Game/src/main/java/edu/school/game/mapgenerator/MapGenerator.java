package edu.school.game.mapgenerator;

import com.diogonunes.jcolor.Attribute;
import edu.school21.models.Coordinates2D;

import java.util.*;
import java.util.stream.IntStream;

import static com.diogonunes.jcolor.Ansi.colorize;

public class MapGenerator {
    public static void main(String[] args) {
        System.out.println("Test MapGenerator");

        final int mapHeight = 15, mapWidth = 60, objectsNumber = 300;

        MapGenerator mapGenerator = new MapGenerator(mapHeight, mapWidth, objectsNumber);

        IntStream.range(0, 10).forEach(i -> {
            boolean[][] map = mapGenerator.getNewMap();
            for (boolean[] line : map) {
                for (boolean obj : line) {
                    System.out.print(obj ?
                            colorize("#", Attribute.BRIGHT_BLACK_BACK()) :
                            colorize(" ", Attribute.WHITE_BACK()));
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        });
    }

    private final int MAP_HEIGHT, MAP_WIDTH;
    private final int OBJECTS_NUMBER;
    private final boolean[][] generatedMap;

    private final Random random = new Random();

    public MapGenerator(int mapHeight, int mapWidth, int objectsNumber) {
        this.MAP_HEIGHT = mapHeight;
        this.MAP_WIDTH = mapWidth;
        this.OBJECTS_NUMBER = objectsNumber;
        this.generatedMap = new boolean[mapHeight][mapWidth];
    }

    public boolean[][] getNewMap() {
        for (int i = 1; i < MAP_HEIGHT - 1; i++) {
            for (int j = 1; j < MAP_WIDTH; j++) {
                generatedMap[i][j] = false;
            }
        }
        Queue<Leaf> leafsQueue = new LinkedList<>();
        List<Leaf> leafs = new ArrayList<>();

        leafsQueue.add(new Leaf(1, 1, MAP_HEIGHT - 2, MAP_WIDTH - 2));
        while (!leafsQueue.isEmpty()) {
            Leaf l = leafsQueue.poll();
            if (l.split()) {
                leafsQueue.add(l.leftChild);
                leafsQueue.add(l.rightChild);
            } else {
                leafs.add(l);
            }
        }

        for (Leaf l : leafs) {
            l.updateCells();
        }

        int generatedObjectsNumber = 0;
        while (generatedObjectsNumber < OBJECTS_NUMBER) {
            if (leafs.isEmpty()) {
                break;
            }

            Leaf l = leafs.get(random.nextInt(leafs.size()));
            int leafCellsNumber = l.cells.size();
            if (leafCellsNumber == 0) {
                leafs.remove(l);
                continue;
            }

            int cellsFromLeaf = Math.min(1 + random.nextInt(leafCellsNumber), OBJECTS_NUMBER - generatedObjectsNumber);
            while (cellsFromLeaf-- > 0) {
                Coordinates2D coors = l.cells.get(random.nextInt(l.cells.size()));
                l.cells.remove(coors);
                int x = coors.x, y = coors.y;
                if (!overlaping(x, y)) {
                    generatedMap[x][y] = true;
                    generatedObjectsNumber++;
                }
            }
        }

        return generatedMap;
    }

    boolean overlaping(int newCellX, int newCellY) {
        Coordinates2D[] nearCells = new Coordinates2D[]{
                new Coordinates2D(newCellX + 1, newCellY),
                new Coordinates2D(newCellX - 1, newCellY),
                new Coordinates2D(newCellX, newCellY + 1),
                new Coordinates2D(newCellX, newCellY - 1)
        };

        for (Coordinates2D nearCell : nearCells) {
            int nearCellX = nearCell.x, nearCellY = nearCell.y;
            if (nearCellX < 0 || nearCellX > MAP_HEIGHT - 1 || nearCellY < 0 || nearCellY > MAP_WIDTH - 1 || generatedMap[nearCellX][nearCellY]) {
                continue;
            }

            Coordinates2D[] cells = new Coordinates2D[]{
                    new Coordinates2D(nearCellX + 1, nearCellY),
                    new Coordinates2D(nearCellX - 1, nearCellY),
                    new Coordinates2D(nearCellX, nearCellY + 1),
                    new Coordinates2D(nearCellX, nearCellY - 1)
            };
            boolean overlaped = true;
            for (Coordinates2D cell : cells) {
                int x = cell.x, y = cell.y;
                if (x < 0 || x > MAP_HEIGHT - 1 || y < 0 || y > MAP_WIDTH - 1) {
                    continue;
                }
                if (!generatedMap[x][y] && x != newCellX && y != newCellY) {
                    overlaped = false;
                    break;
                }
            }
            if (overlaped) {
                return true;
            }
        }
        return false;
    }

    class Leaf {
        private static final int MIN_LEAF_SIZE = 2;
        int x, y;
        int height, width;
        Leaf leftChild, rightChild;
        List<Coordinates2D> cells = new ArrayList<>();

        public Leaf(int x, int y, int height, int width) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.width = width;
        }

        boolean split() {
            if (leftChild != null && rightChild != null) {
                return false;
            }

            boolean splitH = random.nextBoolean();
            if (height > width && (double) height / width >= 1.25) {
                splitH = true;
            } else if (width > height && (double) width / height >= 1.25) {
                splitH = false;
            }


            int max = Math.max(height, width);
            if (max < MIN_LEAF_SIZE + 2) {
                return false;
            }

            int cuttingSide = width;
            if (splitH) {
                cuttingSide = height;
            }

            int splitLine = 1 + MIN_LEAF_SIZE + random.nextInt(cuttingSide - 1 - MIN_LEAF_SIZE);

            if (splitH) {
                leftChild = new Leaf(x, y, splitLine - 1, width);
                rightChild = new Leaf(x + splitLine, y, height - splitLine, width);
            } else {
                leftChild = new Leaf(x, y, height, splitLine - 1);
                rightChild = new Leaf(x, y + splitLine, height, width - splitLine);
            }

            return true;
        }

        void updateCells() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    cells.add(new Coordinates2D(x + i, y + j));
                }
            }
        }
    }
}
