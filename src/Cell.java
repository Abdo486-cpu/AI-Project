public class Cell {
    private CellType type;

    public Cell(int x, int y) {
        this.type = CellType.EMPTY;
    }

    public void setType(CellType type) {
        this.type = type;
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