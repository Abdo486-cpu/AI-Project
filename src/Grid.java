import java.util.*;

public class Grid {
    private final int width, height;
    private final Cell[][] grid;
    private final List<Store> stores;
    private final List<Destination> destinations;
    private final List<Obstacle> obstacles;
    private final List<Tunnel> tunnels;
    private final int[][] costHorizontal;
    private final int[][] costVertical;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
        this.costHorizontal = new int[width - 1][height];
        this.costVertical = new int[width][height - 1];
        this.stores = new ArrayList<>();
        this.destinations = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.tunnels = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height; j++) {
                costHorizontal[i][j] = (int) (Math.random() * 4) + 1;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                costVertical[i][j] = (int) (Math.random() * 4) + 1;
            }
        }
    }

    public void addStore(Store store) {
        stores.add(store);
        grid[store.x()][store.y()].setType(CellType.STORE);
    }

    public void addDestination(Destination destination) {
        destinations.add(destination);
        grid[destination.x()][destination.y()].setType(CellType.DESTINATION);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void addTunnel(Tunnel tunnel) {
        tunnels.add(tunnel);
        grid[tunnel.x1()][tunnel.y1()].setType(CellType.TUNNEL_ENTRY);
        grid[tunnel.x2()][tunnel.y2()].setType(CellType.TUNNEL_EXIT);
    }

    public void addTruck(Truck truck) {
        grid[truck.getX()][truck.getY()].setTruck();
    }

    public void MoveTruck(Truck truck, Movement movement) {
        Cell currentCell = grid[truck.getX()][truck.getY()];
        currentCell.removeTruck();

        switch (movement) {
            case UP -> truck.setY(truck.getY() + 1);
            case DOWN -> truck.setY(truck.getY() - 1);
            case LEFT -> truck.setX(truck.getX() - 1);
            case RIGHT -> truck.setX(truck.getX() + 1);
        }

        Cell newCell = grid[truck.getX()][truck.getY()];

        for (Tunnel tunnel : tunnels) {
            if (newCell.getX() == tunnel.x1() && newCell.getY() == tunnel.y1()) {
                truck.setX(tunnel.x2());
                truck.setY(tunnel.y2());
                newCell = grid[truck.getX()][truck.getY()];
                break;
            } else if (newCell.getX() == tunnel.x2() && newCell.getY() == tunnel.y2()) {
                truck.setX(tunnel.x1());
                truck.setY(tunnel.y1());
                newCell = grid[truck.getX()][truck.getY()];
                break;
            }
        }
        newCell.setTruck();
    }

    public void printGrid() {
        for (int j = height - 1; j >= 0; j--) {
            for (int i = 0; i < width; i++) {
                System.out.print(grid[i][j]);
                if (i < width - 1) {
                    boolean hasObstacleHorizontal = false;
                    for (Obstacle obs : obstacles) {
                        if ((i == obs.x1() && i + 1 == obs.x2() && j == obs.y1() && j == obs.y2()) ||
                                (i == obs.x2() && i + 1 == obs.x1() && j == obs.y2() && j == obs.y1())) {
                            hasObstacleHorizontal = true;
                            break;
                        }
                    }
                    System.out.print(hasObstacleHorizontal ? "--O--" : "--" + costHorizontal[i][j] + "--");
                }
            }
            System.out.println();
            if (j > 0) {
//                System.out.println("|     ".repeat(width));
                for (int i = 0; i < width; i++) {
                    boolean hasObstacleVerticle = false;
                    for (Obstacle obs : obstacles) {
                        if ((i == obs.x1() && i == obs.x2() && j == obs.y1() && j - 1 == obs.y2()) ||
                                (i == obs.x2() && i == obs.x1() && j == obs.y2() && j - 1 == obs.y1())) {
                            hasObstacleVerticle = true;
                            break;
                        }
                    }
                    System.out.print(hasObstacleVerticle ? "O     " : costVertical[i][j-1] + "     ");
                }
                System.out.println();
//                System.out.println("|     ".repeat(width));
            }
        }
        for (int i = 0; i < width; i++) {
            System.out.print("------");
        }
        System.out.println();
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public Cell getNeighbor(Cell cell, Movement movement) {
        int x = cell.getX();
        int y = cell.getY();
        Cell neighbor = null;

        switch (movement) {
            case UP:
                for (Obstacle obs : obstacles) {
                    if ((x == obs.x1() && x == obs.x2() && y == obs.y1() && y + 1 == obs.y2()) ||
                            (x == obs.x2() && x == obs.x1() && y == obs.y2() && y + 1 == obs.y1())) {
                        return null;
                    }
                }
                if (y + 1 < height) neighbor = grid[x][y + 1];
                break;
            case DOWN:
                for (Obstacle obs : obstacles) {
                    if ((x == obs.x1() && x == obs.x2() && y == obs.y1() && y - 1 == obs.y2()) ||
                            (x == obs.x2() && x == obs.x1() && y == obs.y2() && y - 1 == obs.y1())) {
                        return null;
                    }
                }
                if (y - 1 >= 0) neighbor = grid[x][y - 1];
                break;
            case LEFT:
                for (Obstacle obs : obstacles) {
                    if ((x == obs.x1() && x - 1 == obs.x2() && y == obs.y1() && y == obs.y2()) ||
                            (x == obs.x2() && x - 1 == obs.x1() && y == obs.y2() && y == obs.y1())) {
                        return null;
                    }
                }
                if (x - 1 >= 0) neighbor = grid[x - 1][y];
                break;
            case RIGHT:
                for (Obstacle obs : obstacles) {
                    if ((x == obs.x1() && x + 1 == obs.x2() && y == obs.y1() && y == obs.y2()) ||
                            (x == obs.x2() && x + 1 == obs.x1() && y == obs.y2() && y == obs.y1())) {
                        return null;
                    }
                }
                if (x + 1 < width) neighbor = grid[x + 1][y];
                break;
        }

        if (neighbor != null) {
            for (Tunnel tunnel : tunnels) {
                if (neighbor.getX() == tunnel.x1() && neighbor.getY() == tunnel.y1()) {
                    neighbor = grid[tunnel.x2()][tunnel.y2()];
                    break;
                } else if (neighbor.getX() == tunnel.x2() && neighbor.getY() == tunnel.y2()) {
                    neighbor = grid[tunnel.x1()][tunnel.y1()];
                    break;
                }
            }
        }

        return neighbor;
    }


}
