import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grid cityGrid = new Grid(10, 10);

        Store store = new Store(0, 0);
        Destination destination = new Destination(9, 5);
        cityGrid.addStore(store);
        cityGrid.addDestination(destination);
        cityGrid.addObstacle(new Obstacle(5, 6));
        cityGrid.addObstacle(new Obstacle(7, 7));
        cityGrid.addTunnel(new Tunnel(1, 2, 8, 8));

        Truck truck = new Truck(0, 0);
        cityGrid.addTruck(truck);

        testPathFinder(cityGrid, store, destination, truck);

        cityGrid.printGrid();
    }

    public static void testPathFinder(Grid cityGrid, Store store, Destination destination, Truck truck) {
        PathFinder pathFinder = new PathFinder(cityGrid);

        List<Movement> path = pathFinder.findPathBFS(store, destination);

        if (path.isEmpty()) {
            System.out.println("No path found from store to destination.");
        } else {
            System.out.println("Path from store to destination:");

            for (Movement move : path) {
                System.out.print(move + " -> ");
            }
            System.out.println("Destination reached.");

            cityGrid.printGrid();

            for (Movement move : path) {
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cityGrid.MoveTruck(truck, move);
                cityGrid.printGrid();
            }
        }
    }
}
