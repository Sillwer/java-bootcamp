package edu.school.game.models;

import edu.school21.models.Coordinates2D;

public class Unit {
    private UnitType unitType;
    private Coordinates2D coordinates2D;

    public Unit(UnitType unitType, Coordinates2D coordinates2D) {
        this.unitType = unitType;
        this.coordinates2D = coordinates2D;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Coordinates2D getCoordinates() {
        return coordinates2D;
    }

    public void setCoordinates(Coordinates2D coordinates2D) {
        this.coordinates2D = coordinates2D;
    }
}
