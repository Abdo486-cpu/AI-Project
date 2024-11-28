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
        Node startNode = new Node(startCell, null, null,0);
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
                    int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                    Node neighborNode = new Node(neighbor, currentNode, movement,cost);
                    stack.add(neighborNode);
                    visited.add(neighbor);
                }
            }
        }
        return new Result(nodesExpanded,null,0);
    }

    public Result findPathBFS(Store store, Destination destination) {
        Queue<Node> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null,0);
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
                    int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                    Node neighborNode = new Node(neighbor, currentNode, movement,cost);
                    queue.add(neighborNode);
                    visited.add(neighbor);
                }
            }
        }
        return new Result(nodesExpanded,null,0);
    }

    public Result findPathIDDFS(Store store, Destination destination) {
        int depth = 0;
        int nodesExpanded = 0;
        while (true) {
            Result result = depthLimitedSearch(store, destination, depth, nodesExpanded);
            nodesExpanded = result.nodesExpanded();
            if (result.path() != null) {
                return result;
            }
            depth++;
        }
    }

    private Result depthLimitedSearch(Store store, Destination destination, int depthLimit, int nodesExpanded) {
        Stack<Node> stack = new Stack<>();
        Set<Cell> visited = new HashSet<>();
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null,0);
        stack.add(startNode);
        int stop = 0;
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode, nodesExpanded);
            }

            if (depthLimit > 0) {
                visited.add(currentCell);

                if (stop < depthLimit) {
                    for (Movement movement : Movement.values()) {
                        Cell neighbor = grid.getNeighbor(currentCell, movement);

                        if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                            int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                            Node neighborNode = new Node(neighbor, currentNode, movement,cost);
                            stack.add(neighborNode);
                            stop++;
                        }
                    }
                }
            }
        }
        return new Result(nodesExpanded,null,0);
    }

    public Result findPathUCS(Store store, Destination destination) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::cost));
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;

        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null, 0);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode, nodesExpanded);
            }

            if (!visited.add(currentCell)) {
                continue;
            }

            for (Movement movement : Movement.values()) {
                Cell neighbor = grid.getNeighbor(currentCell, movement);
                if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                    int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                    Node neighborNode = new Node(neighbor, currentNode, movement, cost);
                    priorityQueue.add(neighborNode);
                }
            }
        }

        return new Result(nodesExpanded, null,0);
    }

    private int getMovementCost(Cell current, Cell neighbor, Movement movement) {
        switch (movement) {
            case UP, DOWN :
                if(current.getType(current) == CellType.TUNNEL_ENTRY || current.getType(current) == CellType.TUNNEL_EXIT){
                int tunnelCost = 0;
                for (Tunnel tunnel : grid.tunnels) {
                    if ((current.getX() == tunnel.x1()  && current.getY() == tunnel.y1()) ||
                            (current.getX() == tunnel.x2() && current.getY() == tunnel.y2() )) {
                        tunnelCost = tunnel.tunnelCost();
                        break;
                    }
                }
                return grid.costVertical[current.getX()][Math.min(current.getY(), neighbor.getY())] + tunnelCost;
            }else{
                return grid.costVertical[current.getX()][Math.min(current.getY(), neighbor.getY())];
            }
            case LEFT, RIGHT :
                if(current.getType(current) == CellType.TUNNEL_ENTRY || current.getType(current) == CellType.TUNNEL_EXIT){
                    int tunnelCost = 0;
                    for (Tunnel tunnel : grid.tunnels) {
                        if ((current.getX() == tunnel.x1()  && current.getY() == tunnel.y1()) ||
                                (current.getX() == tunnel.x2() && current.getY() == tunnel.y2() )) {
                            tunnelCost = tunnel.tunnelCost();
                            break;
                        }
                    }
                    return grid.costHorizontal[current.getX()][Math.min(current.getY(), neighbor.getY())] + tunnelCost;
                }else{
                    return grid.costHorizontal[current.getX()][Math.min(current.getY(), neighbor.getY())];
                }
        }
        return 0;
    }


    public Result findPathGreedy(Store store, Destination destination, Heuristic heuristic) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node ->
                heuristic.calculate(node.cell(), destination)));
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;
        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null,0);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode, nodesExpanded);
            }

            if (!visited.add(currentCell)) {
                continue;
            }

            for (Movement movement : Movement.values()) {
                Cell neighbor = grid.getNeighbor(currentCell, movement);
                if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                    int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                    Node neighborNode = new Node(neighbor, currentNode, movement, cost);
                    priorityQueue.add(neighborNode);
                }
            }
        }
        return new Result(nodesExpanded, null, 0);
    }

    public Result findPathAStar(Store store, Destination destination, Heuristic heuristic) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node ->
                node.cost() + heuristic.calculate(node.cell(), destination)));
        Set<Cell> visited = new HashSet<>();
        int nodesExpanded = 0;

        Cell startCell = grid.getCell(store.x(), store.y());
        Node startNode = new Node(startCell, null, null, 0);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            nodesExpanded++;
            Cell currentCell = currentNode.cell();

            if (currentCell.getX() == destination.x() && currentCell.getY() == destination.y()) {
                return reconstructPath(currentNode, nodesExpanded);
            }

            if (!visited.add(currentCell)) {
                continue;
            }

            for (Movement movement : Movement.values()) {
                Cell neighbor = grid.getNeighbor(currentCell, movement);
                if (neighbor != null && !visited.contains(neighbor) && !neighbor.isObstacle()) {
                    int cost = currentNode.cost() + getMovementCost(currentCell, neighbor, movement);
                    Node neighborNode = new Node(neighbor, currentNode, movement, cost);
                    priorityQueue.add(neighborNode);
                }
            }
        }

        return new Result(nodesExpanded, null, 0);
    }


    private Result reconstructPath(Node node, int nodesExpanded) {
        List<Movement> path = new ArrayList<>();
        int totalCost = node.cost();
        while (node != null && node.movement() != null) {
            path.add(node.movement());
            node = node.parent();
        }
        Collections.reverse(path);
        return new Result(nodesExpanded,path,totalCost);
    }
}

record Node(Cell cell, Node parent, Movement movement, int cost) { }

record Result(int nodesExpanded, List<Movement> path, int cost) {}