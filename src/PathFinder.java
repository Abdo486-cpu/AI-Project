import java.util.*;

public class PathFinder {
    private final Grid grid;

    public PathFinder(Grid grid) {
        this.grid = grid;
    }

    public List<Movement> findPathBFS(Store store, Destination destination) {
        Queue<Node> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();

        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null);
        queue.add(startNode);
        visited.add(startCell);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode);
            }

            for (Movement movement : Movement.values()) {
                Cell neighbor = grid.getNeighbor(currentCell, movement);

                if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                    Node neighborNode = new Node(neighbor, currentNode, movement);
                    queue.add(neighborNode);
                    visited.add(neighbor);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Movement> reconstructPath(Node node) {
        List<Movement> path = new ArrayList<>();
        while (node != null && node.movement() != null) {
            path.add(node.movement());
            node = node.parent();
        }
        Collections.reverse(path);
        return path;
    }
}

record Node(Cell cell, Node parent, Movement movement) {
}
