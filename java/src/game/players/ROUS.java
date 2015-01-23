package game.players;

import java.awt.*;
import java.util.Map;

public class ROUS extends Player {

    private static final int NUMBER_OF_GENES = 33;
    private static final int GENE_SIZE = 3;
    private static final Point[] coords = new Point[]{
            new Point(-1, -1),
            new Point(-1, 0),
            new Point(-1, 1),
            new Point(0, -1),
            new Point(0, 1),
            new Point(1, -1),
            new Point(1, 0),
            new Point(1, 1)
    };

    @Override
    public Point takeTurn(String dna, Map<Point, Integer> vision){
        Point[] table = decode(dna);
        int hash = hash(vision);
        return table[hash];
    }

    private int hash(Map<Point, Integer> surroundings) {
        return Math.abs(surroundings.hashCode()) % NUMBER_OF_GENES;
    }

    private Point[] decode(String dna) {
        Point[] result = new Point[NUMBER_OF_GENES];

        for (int i = 0; i < NUMBER_OF_GENES; i++){
            int p = Integer.parseInt(dna.substring(i * GENE_SIZE, (i + 1) * GENE_SIZE), 2);
            int x;
            int y;

            result[i] = coords[p];
        }
        return result;
    }
}