import java.util.*;

public class Grid {
    private final int width, height;
    private final Cell[][] grid;
    private final List<Store> stores;
    private final List<Destination> destinations;
    private final List<Obstacle> obstacles;
    private final List<Tunnel> tunnels;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
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
        grid[obstacle.x()][obstacle.y()].setType(CellType.OBSTACLE);
    }

    public void addTunnel(Tunnel tunnel) {
        tunnels.add(tunnel);
        grid[tunnel.x1()][tunnel.y1()].setType(CellType.TUNNEL_ENTRY);
        grid[tunnel.x2()][tunnel.y2()].setType(CellType.TUNNEL_EXIT);
    }

    public void addTruck(Truck truck){
        grid[truck.getX()][truck.getY()].setType(CellType.TRUCK);
    }

    public void MoveTruck(Truck truck , Movement movement) {
        grid[truck.getX()][truck.getY()].setType(CellType.EMPTY);
        switch (movement){
            case UP: truck.setY(truck.getY()+1); break;
            case DOWN: truck.setY(truck.getY()-1); break;
            case LEFT: truck.setX(truck.getX()-1); break;
            case RIGHT: truck.setX(truck.getX()+1); break;
        }
        grid[truck.getX()][truck.getY()].setType(CellType.TRUCK);
    }

    public void printGrid() {
        for (int j = height - 1; j >= 0; j--) {
            for (int i = 0; i < width; i++) {
                System.out.print(grid[i][j]);
                if (i < width - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        for (int i = 0; i < width; i++) {
            System.out.print("--");
        }
        System.out.println();
    }
}
