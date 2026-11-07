import java.util.Locale;

public class CommandParser {
    public static Command parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Command cannot be null.");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Expected three parts: transaction type, account number and amount.");
        }

        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown transaction type: " + parts[0], exception);
        }

        if (!Validator.isValidAmount(parts[2])) {
            throw new IllegalArgumentException("Amount must be a positive whole number.");
        }

        long amount;
        try {
            amount = Long.parseLong(parts[2]);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Amount is outside the supported range.", exception);
        }

        return new Command(type, parts[1], amount);
    }
}
