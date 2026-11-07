import java.util.LinkedHashMap;
import java.util.Map;

public class Warehouse {
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public Warehouse() {
        inventory.put("bolt", 10);
        inventory.put("nut", 2);
    }

    public void issue(String item, int quantity)
            throws OutOfStockException, InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be blank.");
        }

        int available = inventory.getOrDefault(item, 0);
        if (quantity > available) {
            throw new OutOfStockException(item, quantity - available);
        }
        inventory.put(item, available - quantity);
    }

    public int getStock(String item) {
        return inventory.getOrDefault(item, 0);
    }
}
