@FunctionalInterface
public interface Heuristic {
    int calculate(Cell cell, Destination destination);

    static Heuristic manhattanDistance() {
        return (cell, destination) ->
                Math.abs(cell.getX() - destination.x()) + Math.abs(cell.getY() - destination.y());
    }

    static Heuristic euclideanDistance() {
        return (cell, destination) ->
                (int) Math.sqrt(Math.pow(cell.getX() - destination.x(), 2) + Math.pow(cell.getY() - destination.y(), 2));
    }
}
