import java.util.*;

public class PathFinder {
    private final Grid grid;

    public PathFinder(Grid grid) {
        this.grid = grid;
    }

    public Result findPathDFS(Store store, Destination destination){
        Stack<Node> stack = new Stack<>();
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null);
        stack.add(startNode);
        visited.add(startCell);

        while (!stack.isEmpty()){
            Node currentNode = stack.pop();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode,nodesExpanded);
            }

            for (Movement movement : Movement.values()) {
                Cell neighbor = grid.getNeighbor(currentCell, movement);

                if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                    Node neighborNode = new Node(neighbor, currentNode, movement);
                    stack.add(neighborNode);
                    visited.add(neighbor);
                }
            }
        }
        return new Result(nodesExpanded,null);
    }

    public Result findPathBFS(Store store, Destination destination) {
        Queue<Node> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null);
        queue.add(startNode);
        visited.add(startCell);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode,nodesExpanded);
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
        return new Result(nodesExpanded,null);
    }

    public Result findPathIDDFS(Store store, Destination destination) {
        int depth = 0;
        int nodesExpanded = 0;
        while (true) {
            Set<Cell> visited = new HashSet<>();
            Result result = depthLimitedSearch(store, destination, depth, visited, nodesExpanded);
            nodesExpanded = result.nodesExpanded();
            if (result.path() != null) {
                return result;
            }
            depth++;
        }
    }

    private Result depthLimitedSearch(Store store, Destination destination, int depthLimit, Set<Cell> visited, int nodesExpanded) {
        Stack<Node> stack = new Stack<>();
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null);
        stack.add(startNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode, nodesExpanded);
            }

            if (depthLimit > 0) {
                visited.add(currentCell);

                for (Movement movement : Movement.values()) {
                    Cell neighbor = grid.getNeighbor(currentCell, movement);

                    if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                        Node neighborNode = new Node(neighbor, currentNode, movement);
                        stack.add(neighborNode);
                    }
                }
            }
        }
        return new Result(nodesExpanded,null);
    }


    private Result reconstructPath(Node node, Integer nodesExpanded) {
        List<Movement> path = new ArrayList<>();
        while (node != null && node.movement() != null) {
            path.add(node.movement());
            node = node.parent();
        }
        Collections.reverse(path);
        return new Result(nodesExpanded,path);
    }
}

record Node(Cell cell, Node parent, Movement movement) { }

record Result(int nodesExpanded, List<Movement> path) {}
