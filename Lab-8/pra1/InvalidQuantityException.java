public class InvalidQuantityException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidQuantityException(int quantity) {
        super("Quantity must be greater than zero: " + quantity);
    }
}
