import java.util.Locale;
import java.util.Scanner;

record Vehicle(String number, String type) {
}

public class TollBooth {
    public static void main(String[] args) {
        long totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Vehicle number (or done): ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                String number = scanner.nextLine().trim();
                if (number.equalsIgnoreCase("done")) {
                    break;
                }

                System.out.print("Vehicle type (bike, car, truck): ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String type = scanner.nextLine().trim();
                Vehicle vehicle = new Vehicle(number, type);
                String normalizedType = vehicle.type().toLowerCase(Locale.ROOT);

                int toll = switch (normalizedType) {
                    case "bike" -> 20;
                    case "car" -> 50;
                    case "truck" -> 150;
                    default -> -1;
                };

                if (toll < 0) {
                    System.out.println("Invalid vehicle type. Please enter bike, car or truck.");
                    continue;
                }

                totalToll += toll;
                switch (normalizedType) {
                    case "bike" -> bikeCount++;
                    case "car" -> carCount++;
                    case "truck" -> truckCount++;
                    default -> throw new IllegalStateException("Unexpected vehicle type.");
                }
            }
        }

        String mostFrequent = "none";
        int highestCount = 0;

        if (bikeCount > highestCount) {
            mostFrequent = "bike";
            highestCount = bikeCount;
        }
        if (carCount > highestCount) {
            mostFrequent = "car";
            highestCount = carCount;
        }
        if (truckCount > highestCount) {
            mostFrequent = "truck";
        }

        System.out.println("Total toll: " + totalToll);
        System.out.println("Most frequent: " + mostFrequent);
    }
}
