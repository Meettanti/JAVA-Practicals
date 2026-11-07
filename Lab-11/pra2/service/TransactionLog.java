package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Set;
import model.Account;

public class TransactionLog {
    private static final Set<String> TYPES =
            Set.of("OPEN", "DEPOSIT", "WITHDRAW", "TRANSFER_IN", "TRANSFER_OUT");

    public void append(Path file, String type, Account account, long amount) throws IOException {
        validate(type, account, amount);
        appendLines(file, line(Instant.now(), type, account, amount));
    }

    public void appendTransfer(Path file, Account source, Account destination, long amount)
            throws IOException {
        validate("TRANSFER_OUT", source, amount);
        validate("TRANSFER_IN", destination, amount);
        if (source.equals(destination)) {
            throw new IllegalArgumentException("Transfer accounts must be different.");
        }
        Instant timestamp = Instant.now();
        appendLines(file, line(timestamp, "TRANSFER_OUT", source, amount)
                + line(timestamp, "TRANSFER_IN", destination, amount));
    }

    private static void validate(String type, Account account, long amount) {
        if (!TYPES.contains(type)) {
            throw new IllegalArgumentException("Unsupported transaction type: " + type);
        }
        if (account == null) {
            throw new IllegalArgumentException("Transaction account cannot be null.");
        }
        if (amount < 0 || (amount == 0 && !type.equals("OPEN"))) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }
    }

    private static String line(Instant timestamp, String type, Account account, long amount) {
        return timestamp + "|" + type + "|" + account.getAccountNumber() + "|"
                + amount + "|" + account.getBalance() + System.lineSeparator();
    }

    private static void appendLines(Path file, String lines) throws IOException {
        Path absoluteFile = file.toAbsolutePath().normalize();
        Files.createDirectories(absoluteFile.getParent());
        Files.writeString(absoluteFile, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
