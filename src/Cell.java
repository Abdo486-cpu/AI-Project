public class Cell {
    private final int x;
    private final int y;
    private CellType type;
    private CellType originalType;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = CellType.EMPTY;
        this.originalType = CellType.EMPTY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setType(CellType type) {
        this.type = type;
        this.originalType = type;
    }

    public CellType getType(Cell cell) {
        return cell.type;
    }

    public void setTruck() {
        if (type != CellType.TRUCK) {
            originalType = type;
            type = CellType.TRUCK;
        }
    }

    public void removeTruck() {
        type = originalType;
    }

    public boolean isObstacle() {
        return this.type == CellType.OBSTACLE;
    }

    @Override
    public String toString() {
        return switch (type) {
            case STORE -> "S";
            case DESTINATION -> "D";
            case OBSTACLE -> "O";
            case TUNNEL_ENTRY -> "E";
            case TUNNEL_EXIT -> "X";
            case TRUCK -> "T";
            default -> "*";
        };
    }
}
