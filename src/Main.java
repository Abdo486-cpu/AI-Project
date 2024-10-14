public class Main {
    public static void main(String[] args) {

        Grid cityGrid = new Grid(10, 10);
        Truck truck = new Truck(1, 1);
        cityGrid.addStore(new Store(5, 7));
        cityGrid.addDestination(new Destination(9, 5));
        cityGrid.addObstacle(new Obstacle(5, 5));
        cityGrid.addTunnel(new Tunnel(2, 2, 8, 8));
        cityGrid.addTruck(truck);

        cityGrid.printGrid();

        System.out.println(truck);

        cityGrid.MoveTruck(truck,Movement.LEFT);
        System.out.println(truck);
        cityGrid.printGrid();
    }
}
