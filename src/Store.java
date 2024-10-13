public record Store(int x, int y) {

    @Override
    public String toString() {
        return "Store at (" + x + ", " + y + ")";
    }
}