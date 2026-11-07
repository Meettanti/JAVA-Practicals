package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionHistory {
    private static final Comparator<TransactionRecord> CHRONOLOGICAL_ORDER =
            Comparator.comparing(TransactionRecord::timestamp)
                    .thenComparing(TransactionRecord::accountNumber)
                    .thenComparing(TransactionRecord::type);

    private final CopyOnWriteArrayList<TransactionRecord> records = new CopyOnWriteArrayList<>();

    public void add(TransactionRecord record) {
        records.add(record);
    }

    public void addAll(List<TransactionRecord> newRecords) {
        records.addAll(newRecords);
    }

    public void load(Path logFile) throws IOException {
        if (Files.notExists(logFile)) {
            records.clear();
            return;
        }

        ArrayList<TransactionRecord> loadedRecords = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    loadedRecords.add(TransactionRecord.parse(line, lineNumber));
                }
            }
        }
        records.clear();
        records.addAll(loadedRecords);
    }

    public List<TransactionRecord> sortedByTime() {
        return records.stream().sorted(CHRONOLOGICAL_ORDER).toList();
    }

    public List<TransactionRecord> forAccount(String accountNumber) {
        return records.stream()
                .filter(record -> record.accountNumber().equals(accountNumber))
                .sorted(CHRONOLOGICAL_ORDER)
                .toList();
    }

    public int size() {
        return records.size();
    }
}
