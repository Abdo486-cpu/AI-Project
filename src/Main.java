import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Plan("5;3;2;1;3,2,3,0;1,2;2,0,1,1;", "0,1,0,0,3;0,2,0,1,0;1,0,0,0,3;1,1,0,1,1;1,1,1,0,3;1,2,0,2,0;1,2,1,1,1;2,0,1,0,1;2,1,1,1,0;2,1,2,0,1;2,2,1,2,0;2,2,2,1,0;3,0,2,0,3;3,1,2,1,2;3,1,3,0,3;3,2,2,2,0;3,2,3,1,2;4,0,3,0,0;4,1,3,1,0;4,1,4,0,2;4,2,3,2,1;4,2,4,1,2;","AS2", true);
    }

    public static void Plan(String states,String traffic,String strategy, boolean visualize){
        Grid grid = GenGrid(states,traffic);
        SearchStrategy method;
        switch (strategy){
            case "BF"-> method = SearchStrategy.BFS;
            case "DF"-> method = SearchStrategy.DFS;
            case "UC"-> method = SearchStrategy.UCS;
            case "ID"-> method = SearchStrategy.ID;
            case "GR1"-> method = SearchStrategy.G1;
            case "GR2"-> method = SearchStrategy.G2;
            case "AS1"-> method = SearchStrategy.A1;
            default -> method = SearchStrategy.A2;
        }

        Result[] chosenPaths = new Result[grid.destinations.size()];
        int[] chosenStores = new int[grid.destinations.size()];
        for(int i = 0; i < grid.destinations.size(); i++){
            int best = Integer.MAX_VALUE;
            Result chosen = null;
            for(int j = 0; j < grid.stores.size(); j++) {
                Result res = Path(grid,grid.stores.get(j),grid.destinations.get(i),method);
                if(res.cost() < best){
                    best = res.cost();
                    chosenStores[i] = j;
                    chosen = res;
                }
            }
            chosenPaths[i] = chosen;
        }
        for (int i = 0; i < chosenPaths.length; i++) {
            System.out.println("Chosen path for destination "+(chosenPaths.length-i)+": Store "+ (chosenStores[i]+1)
                    +" Cost: "+chosenPaths[i].cost()+" Expanded nodes: "+chosenPaths[i].nodesExpanded());
            System.out.println("Path from store to destination:");
            List<Movement> path = chosenPaths[i].path();
            for (Movement move : path) {
                System.out.print(move + " -> ");
            }
            System.out.println("Destination reached.");
            if(visualize){
                Truck truck = new Truck(grid.stores.get(chosenStores[i]).x(), grid.stores.get(chosenStores[i]).y());
                grid.addTruck(truck);
                grid.printGrid();
                for (Movement move : path) {
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    grid.MoveTruck(truck, move);
                    grid.printGrid();
                }
            }
        }
    }

    public static Grid GenGrid(String states ,String traffic) {
        String[] sections = states.split(";");
        int width = Integer.parseInt(sections[0]);
        int height = Integer.parseInt(sections[1]);
        Grid grid = new Grid(width, height);
        int p = Integer.parseInt(sections[2]);
        int s = Integer.parseInt(sections[3]);


        String[] destinations = sections[4].split(",");
        for (int i = 0; i < p*2; i=i+2) {
            int x = Integer.parseInt(destinations[i]);
            int y = Integer.parseInt(destinations[i+1]);
            grid.addDestination(new Destination(x, y));
        }

        String[] stores = sections[5].split(",");
        for (int i = 0; i < s*2; i=i+2) {
            int x = Integer.parseInt(stores[i]);
            int y = Integer.parseInt(stores[i+1]);
            grid.addStore(new Store(x, y));
        }

        String[] tunnels = sections[6].split(",");
        for (int i = 0; i < tunnels.length; i=i+4) {
            int x1 = Integer.parseInt(tunnels[i]);
            int y1 = Integer.parseInt(tunnels[i+1]);
            int x2 = Integer.parseInt(tunnels[i+2]);
            int y2 = Integer.parseInt(tunnels[i+3]);
            grid.addTunnel(new Tunnel(x1, y1, x2, y2));
        }

        String[] costs = traffic.split(";");
        int[][] costHorizontal = new int[width - 1][height];
        int[][] costVertical = new int[width][height - 1];
        for (String string : costs) {
            String[] block = string.split(",");
            int x1 = Integer.parseInt(block[0]);
            int y1 = Integer.parseInt(block[1]);
            int x2 = Integer.parseInt(block[2]);
            int y2 = Integer.parseInt(block[3]);
            int cost = Integer.parseInt(block[4]);
            if (cost != 0) {
                if (x1 == x2) {
                    if (y1 < y2) {
                        costHorizontal[x1][y1] = cost;
                    } else {
                        costVertical[x2][y2] = cost;
                    }
                } else {
                    if (x1 < x2) {
                        costHorizontal[x1][y1] = cost;
                    } else {
                        costHorizontal[x2][y2] = cost;
                    }
                }
            } else {
                grid.addObstacle(new Obstacle(x1, y1, x2, y2));
            }
        }
        grid.addCosts(costHorizontal,costVertical);
        return grid;
    }

    public static Result Path(Grid grid,Store store, Destination destination, SearchStrategy strategy) {
        PathFinder pathFinder = new PathFinder(grid);
        switch (strategy) {
            case A1 -> {
                return pathFinder.findPathAStar(store,destination,Heuristic.euclideanDistance());
            }
            case A2 -> {
                return pathFinder.findPathAStar(store,destination,Heuristic.manhattanDistance());
            }
            case G1 -> {
                return pathFinder.findPathGreedy(store,destination,Heuristic.euclideanDistance());
            }
            case G2 -> {
                return pathFinder.findPathGreedy(store,destination,Heuristic.manhattanDistance());
            }
            case ID -> {
                return pathFinder.findPathIDDFS(store,destination);
            }
            case DFS -> {
                return pathFinder.findPathDFS(store,destination);
            }
            case UCS -> {
                return pathFinder.findPathUCS(store,destination);
            }
            default ->  {
                return pathFinder.findPathBFS(store,destination);
            }
        }
    }
}


