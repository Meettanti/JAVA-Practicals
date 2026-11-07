import java.util.Locale;
import java.util.Scanner;

public class ChatFilter {
    public static void printReport(Scanner scanner, String[] logLines) {
        System.out.print("Keyword to search: ");
        if (!scanner.hasNextLine()) {
            System.out.println("\nNo keyword was provided.");
            return;
        }

        String keyword = scanner.nextLine().toLowerCase(Locale.ROOT);
        StringBuilder report = new StringBuilder();
        int matchCount = 0;

        for (String line : logLines) {
            String[] parts = line.split(" ", 3);
            if (parts.length < 3) {
                continue;
            }

            String message = parts[2];
            if (message.toLowerCase(Locale.ROOT).contains(keyword)) {
                report.append(parts[0])
                        .append(' ')
                        .append(parts[1])
                        .append(": ")
                        .append(message)
                        .append(System.lineSeparator());
                matchCount++;
            }
        }

        System.out.println("Matches: " + matchCount);
        System.out.print(report);
    }
}
