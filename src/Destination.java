public record Destination(int x, int y) {

    @Override
    public String toString() {
        return "Destination at (" + x + ", " + y + ")";
    }
}