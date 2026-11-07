public class OutOfStockException extends Exception {
    private static final long serialVersionUID = 1L;

    private final int shortfall;

    public OutOfStockException(String item, int shortfall) {
        super("Not enough stock for " + item + "; short by " + shortfall + ".");
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}
