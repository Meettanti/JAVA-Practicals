import java.util.List;

public class GenericsDemo {
    public static void main(String[] args) {
        GenericBox<String> message = new GenericBox<>("Generic containers hold typed values.");
        GenericBox<Integer> quantity = new GenericBox<>(3);
        System.out.println("Message box: " + message.get());
        System.out.println("Quantity box: " + quantity.get());

        BoundedStatistics<Integer> statistics = new BoundedStatistics<>();
        System.out.println("Statistics: " + statistics.summarize(List.of(4, 8, 2, 6)));

        Pair<String, Integer> entry = new Pair<>("accounts", 4);
        System.out.println("Optional generic pair: " + entry);
    }
}
