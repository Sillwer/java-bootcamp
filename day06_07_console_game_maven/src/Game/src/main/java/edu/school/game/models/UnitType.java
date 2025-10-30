package edu.school.game.models;

import com.diogonunes.jcolor.Attribute;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.diogonunes.jcolor.Attribute.*;

public enum UnitType {
    PLAYER('o', BRIGHT_GREEN_BACK()),
    ENEMY('X', BRIGHT_RED_BACK()),
    GOAL('+', BRIGHT_BLUE_BACK()),
    WALL('#', BRIGHT_MAGENTA_BACK()),
    EMPTY(' ', YELLOW_BACK());

    private static Map<String, Attribute> availableColors = null;

    private char symbol;
    private Attribute[] colors = {BLACK_BACK(), BLACK_TEXT(), Attribute.BOLD()};

    UnitType(char symbol, Attribute color) {
        this.symbol = symbol;
        colors[0] = color;
    }

    public static Attribute attributeByName(String colorName) {
        if (availableColors == null) {
            setupAttributes();
        }
        Attribute attribute = availableColors.get(colorName.toUpperCase());
        if (attribute == null) throw new RuntimeException("Unexpected color: '" + colorName + "'");

        return attribute;
    }

    private static void setupAttributes() {
        try {
            availableColors = new HashMap<>();

            Method[] methods = Class.forName("com.diogonunes.jcolor.Attribute").getMethods();
            for (Method m : methods) {
                String name = m.getName();
                if (!(m.getReturnType().equals(Attribute.class)
                        && name.contains("_BACK")
                        && m.getParameters().length == 0)) {
                    continue;
                }
                name = name.substring(0, name.indexOf("_BACK"));
                availableColors.put(name, (Attribute) m.invoke(Attribute.class));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> availableColorNames() {
        if (availableColors == null) {
            setupAttributes();
        }

        return availableColors.keySet();
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public Attribute getColor() {
        return colors[0];
    }

    public void setColor(Attribute color) {
        colors[0] = color;
    }

    public Attribute[] getColorAttributes() {
        return colors;
    }
}


