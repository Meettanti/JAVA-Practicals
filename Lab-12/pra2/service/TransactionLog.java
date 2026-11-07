package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import model.Account;

public class TransactionLog {
    public TransactionRecord append(Path file, String type, Account account, long amount)
            throws IOException {
        requireAccount(account);
        TransactionRecord record = record(Instant.now(), type, account, amount);
        appendLines(file, record.toLogLine());
        return record;
    }

    public List<TransactionRecord> appendTransfer(
            Path file, Account source, Account destination, long amount)
            throws IOException {
        requireAccount(source);
        requireAccount(destination);
        if (source.equals(destination)) {
            throw new IllegalArgumentException("Transfer accounts must be different.");
        }
        Instant timestamp = Instant.now();
        TransactionRecord outgoing = record(timestamp, "TRANSFER_OUT", source, amount);
        TransactionRecord incoming = record(timestamp, "TRANSFER_IN", destination, amount);
        appendLines(file, outgoing.toLogLine() + incoming.toLogLine());
        return List.of(outgoing, incoming);
    }

    private static void requireAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Transaction account cannot be null.");
        }
    }

    private static TransactionRecord record(Instant timestamp, String type, Account account,
            long amount) {
        return new TransactionRecord(timestamp, type, account.getAccountNumber(), amount,
                account.getBalance());
    }

    private static void appendLines(Path file, String lines) throws IOException {
        Path absoluteFile = file.toAbsolutePath().normalize();
        Files.createDirectories(absoluteFile.getParent());
        Files.writeString(absoluteFile, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
