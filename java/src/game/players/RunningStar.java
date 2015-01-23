package game.players;

import java.awt.*;
import java.util.*;
import java.util.List;

import static game.Constants.random;

public class RunningStar extends Player {

    @Override
    public Point takeTurn(String genome, Map<Point, Integer> vision) {
        Map<Integer, Integer> squareCosts = decode(genome);
        Path path = astar(vision, squareCosts);
        return path.get(1);
    }

    private Path astar(Map<Point, Integer> vision, Map<Integer, Integer> squareCosts) {
        Set<Path> closed = new HashSet<>();
        PriorityQueue<Path> open = new PriorityQueue<>();
        open.add(new Path(new Point(0, 0), 0));
        while (!open.isEmpty()){
            Path best = open.remove();
            int x = best.head().x;
            int y = best.head().y;
            if (x == 2 || (x > 0 && (y == 2 || y == -2))){
                return best;
            }
            for (Path path : pathsAround(best, vision, squareCosts)){
                if (!closed.contains(path) && !open.contains(path)){
                    open.add(path);
                }
            }
            closed.add(best);
        }
        Path p = new Path(new Point(0,0), 0);
        return p.add(new Point((int)(random.nextDouble() * 3 - 1), (int)(random.nextDouble() * 3 - 1)), 0);
    }

    private List<Path> pathsAround(Path path, Map<Point, Integer> vision, Map<Integer, Integer> costs) {
        Point head = path.head();
        List<Path> results = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i == 0 && j == 0){
                    continue;
                }
                Point p = new Point(head.x + i, head.y + j);
                if (!vision.containsKey(p) || vision.get(p) == -1){
                    continue;
                }
                results.add(path.add(p, costs.get(vision.get(p))));
            }
        }
        return results;
    }

    private Map<Integer, Integer> decode(String genome) {
        int chunkLength = genome.length()/16;
        Map<Integer, Integer> costs = new HashMap<>();
        for (int i = 0; i < 16; i++){
            int runSize = 0;
            int cost = 0;
            for (int j = i * chunkLength; j < (i + 1) * chunkLength; j++){
                switch (genome.charAt(j)){
                    case '0':
                        runSize = 0;
                        break;
                    case '1':
                        cost += ++runSize;
                }
            }
            costs.put(i, cost);
        }
        return costs;
    }

    private class Path implements Comparable<Path>{

        Point head;
        Path parent;
        int length;
        int totalCost;

        private Path(){}

        public Path(Point point, int cost) {
            length = 1;
            totalCost = cost;
            head = point;
            parent = null;
        }

        public Point get(int index) {
            if (index >= length || index < 0){
                throw new IllegalArgumentException(index + "");
            }
            if (index == length - 1){
                return head;
            }
            return parent.get(index);
        }

        public Point head() {
            return head;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Path path = (Path) o;

            return head.equals(path.head);

        }

        @Override
        public int hashCode() {
            return head.hashCode();
        }

        @Override
        public int compareTo(Path o) {
            return totalCost - o.totalCost;

        }

        public Path add(Point point, int cost) {
            Path p = new Path();
            p.head = point;
            p.totalCost = totalCost + cost;
            p.length = length + 1;
            p.parent = this;
            return p;
        }
    }
}
