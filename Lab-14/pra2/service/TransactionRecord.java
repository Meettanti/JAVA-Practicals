package service;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record TransactionRecord(Instant timestamp, String type, String accountNumber,
        long amount, long balance) {
    private static final Set<String> TYPES =
            Set.of("OPEN", "DEPOSIT", "WITHDRAW", "TRANSFER_IN", "TRANSFER_OUT");

    public TransactionRecord {
        Objects.requireNonNull(timestamp, "timestamp");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(accountNumber, "accountNumber");
        if (!TYPES.contains(type) || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid transaction record.");
        }
        if (amount < 0 || (amount == 0 && !type.equals("OPEN"))) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }
    }

    public String toLogLine() {
        return timestamp + "|" + type + "|" + accountNumber + "|"
                + amount + "|" + balance + System.lineSeparator();
    }

    public static TransactionRecord parse(String line, int lineNumber) throws IOException {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 5) {
            throw new IOException("Malformed transaction log entry at line " + lineNumber + ".");
        }
        try {
            return new TransactionRecord(Instant.parse(fields[0]), fields[1], fields[2],
                    Long.parseLong(fields[3]), Long.parseLong(fields[4]));
        } catch (RuntimeException exception) {
            throw new IOException("Malformed transaction log entry at line " + lineNumber + ".",
                    exception);
        }
    }
}
