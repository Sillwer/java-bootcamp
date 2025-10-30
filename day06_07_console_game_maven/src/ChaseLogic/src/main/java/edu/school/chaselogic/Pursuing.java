package edu.school.chaselogic;

import edu.school21.models.Coordinates2D;
import edu.school21.models.Pair;

import java.util.LinkedList;
import java.util.List;

public class Pursuing {

    public static List<Pair<Coordinates2D, Double>> evaluateMoveWeights(
            Coordinates2D unitCoors,
            Coordinates2D mainTargetCoors,
            Coordinates2D secondaryTargetCoors
    ) {
        List<Pair<Coordinates2D, Double>> weights = new LinkedList<>();
        double mainTargetDistance = calculateDistance(unitCoors, mainTargetCoors);
        double secondaryTargetDistance = calculateDistance(unitCoors, secondaryTargetCoors);

        Coordinates2D[] newCoordinates = new Coordinates2D[]{
                new Coordinates2D(unitCoors.x, unitCoors.y - 1),
                new Coordinates2D(unitCoors.x, unitCoors.y + 1),
                new Coordinates2D(unitCoors.x - 1, unitCoors.y),
                new Coordinates2D(unitCoors.x + 1, unitCoors.y)
        };

        for (Coordinates2D coordinates : newCoordinates) {
            double mainTargetDistanceNext = calculateDistance(coordinates, mainTargetCoors);
            double secondaryTargetDistanceNext = calculateDistance(coordinates, secondaryTargetCoors);

            double weight = (mainTargetDistance - mainTargetDistanceNext) * 10;
            weight += (secondaryTargetDistance - secondaryTargetDistanceNext);

            weights.add(new Pair<>(coordinates, weight));
        }

        return weights;
    }

    private static double calculateDistance(Coordinates2D coors1, Coordinates2D coors2) {
        double x1 = coors1.x, x2 = coors2.x;
        double y1 = coors1.y, y2 = coors2.y;

        double line1 = Math.abs(x1 - x2);
        double line2 = Math.abs(y1 - y2);

        return Math.sqrt(line1 * line1 + line2 * line2);
    }
}
