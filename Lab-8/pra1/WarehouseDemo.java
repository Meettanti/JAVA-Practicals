import java.util.List;

public class WarehouseDemo {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        List<StockRequest> requests = List.of(
                new StockRequest("bolt", 3),
                new StockRequest("bolt", 20),
                new StockRequest("nut", 0),
                new StockRequest("missing", 2),
                new StockRequest("nut", -1));

        for (StockRequest request : requests) {
            try {
                warehouse.issue(request.item(), request.quantity());
                System.out.println("Issued " + request.quantity() + " " + request.item() + ".");
            } catch (OutOfStockException exception) {
                System.out.println(exception.getMessage()
                        + " Shortfall value: " + exception.getShortfall() + ".");
            } catch (InvalidQuantityException exception) {
                System.out.println("Issue rejected: " + exception.getMessage());
            }
        }

        System.out.println("Remaining bolts: " + warehouse.getStock("bolt"));
        System.out.println("Remaining nuts: " + warehouse.getStock("nut"));
    }
}
