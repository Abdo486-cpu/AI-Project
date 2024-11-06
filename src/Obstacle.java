public record Obstacle(int x1, int y1, int x2, int y2) {

    @Override
    public String toString() {
        return "Obstacle between (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")";
    }
}