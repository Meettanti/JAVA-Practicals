import java.util.Locale;
import java.util.Scanner;

public class VendingMachine {
    private enum Coin {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args) {
        final int snackPrice = 15;
        int total = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (total < snackPrice) {
                System.out.print("Insert coin (ONE, TWO, FIVE, TEN): ");

                if (!scanner.hasNextLine()) {
                    System.out.println("\nPayment ended before the snack was paid for.");
                    return;
                }

                String coinName = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                Coin coin;

                try {
                    coin = Coin.valueOf(coinName);
                } catch (IllegalArgumentException exception) {
                    System.out.println("Invalid coin. Please enter ONE, TWO, FIVE or TEN.");
                    continue;
                }

                int coinValue = switch (coin) {
                    case ONE -> 1;
                    case TWO -> 2;
                    case FIVE -> 5;
                    case TEN -> 10;
                };

                total += coinValue;
                System.out.println("Total paid: " + total);
            }

            System.out.println("Paid. Change: " + (total - snackPrice));
        }
    }
}
