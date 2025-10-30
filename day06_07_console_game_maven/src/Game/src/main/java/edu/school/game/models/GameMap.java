package edu.school.game.models;


import edu.school21.models.Coordinates2D;

public class GameMap {
    Integer size;
    private Unit[][] filed;

    public GameMap(Integer size) {
        this.filed = new Unit[size][size];
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public Unit[][] getFiled() {
        return filed;
    }

    public void setFiled(Unit[][] filed) {
        this.filed = filed;
    }

    public void setUnit(Unit unit, Coordinates2D coors) {
        filed[coors.x][coors.y] = unit;
    }

    public Unit getUnit(Coordinates2D coors) {
        return filed[coors.x][coors.y];
    }
}
