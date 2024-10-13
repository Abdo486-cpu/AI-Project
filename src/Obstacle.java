public record Obstacle(int x, int y) {

    @Override
    public String toString() {
        return "Obstacle at (" + x + ", " + y + ")";
    }
}