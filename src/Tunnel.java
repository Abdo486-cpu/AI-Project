public record Tunnel(int x1, int y1, int x2, int y2) {

    @Override
    public String toString() {
        return "Tunnel entrance at (" + x1 + ", " + y1 + ")  Tunnel exit at (" + x2 + ", " + y2 + ")  ";
    }
    public int tunnelCost(){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}