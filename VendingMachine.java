import java.util.Scanner;

public class VendingMachine {

    enum Coin {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args) {
        final int PRICE = 15;

        int total = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Snack price: " + PRICE);

        while (total < PRICE) {

            System.out.print("Insert coin (ONE, TWO, FIVE, TEN): ");
            String input = sc.next().toUpperCase();

            try {
                Coin coin = Coin.valueOf(input);

                int value = switch (coin) {
                    case ONE -> 1;
                    case TWO -> 2;
                    case FIVE -> 5;
                    case TEN -> 10;
                };

                total += value;

                System.out.println("Total inserted: " + total);

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid coin.");
            }
        }

        int change = total - PRICE;

        System.out.println("Paid. Change: " + change);

        sc.close();
    }
}