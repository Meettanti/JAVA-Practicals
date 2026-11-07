package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Set;

public class EndOfDayReport {
    private static final Set<String> TYPES =
            Set.of("OPEN", "DEPOSIT", "WITHDRAW", "TRANSFER_IN", "TRANSFER_OUT");

    public Summary generate(Path logFile, Path reportFile) throws IOException {
        Totals totals = new Totals();
        try (BufferedReader reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    readTransaction(line, lineNumber, totals);
                }
            }
        }

        Summary summary = totals.toSummary();
        String report = summary.format();
        Path absoluteReport = reportFile.toAbsolutePath().normalize();
        Files.createDirectories(absoluteReport.getParent());
        Files.writeString(absoluteReport, report, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return summary;
    }

    private static void readTransaction(String line, int lineNumber, Totals totals)
            throws IOException {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 5 || fields[2].isBlank() || !TYPES.contains(fields[1])) {
            throw malformed(lineNumber);
        }
        try {
            Instant.parse(fields[0]);
            long amount = Long.parseLong(fields[3]);
            Long.parseLong(fields[4]);
            if (amount < 0 || (amount == 0 && !fields[1].equals("OPEN"))) {
                throw malformed(lineNumber);
            }
            totals.add(fields[1], amount, lineNumber);
        } catch (RuntimeException exception) {
            throw malformed(lineNumber, exception);
        }
    }

    private static IOException malformed(int lineNumber) {
        return new IOException("Malformed transaction log entry at line " + lineNumber + ".");
    }

    private static IOException malformed(int lineNumber, Exception cause) {
        return new IOException("Malformed transaction log entry at line " + lineNumber + ".",
                cause);
    }

    public record Summary(long transactions, long accountsOpened,
            long deposits, long depositedAmount, long withdrawals, long withdrawnAmount,
            long transfersIn, long transferInAmount, long transfersOut, long transferOutAmount) {
        public String format() {
            return "MiniBank End-of-Day Report\n"
                    + "Transaction entries: " + transactions + "\n"
                    + "Accounts opened: " + accountsOpened + "\n"
                    + "Deposits: " + deposits + " (amount " + depositedAmount + ")\n"
                    + "Withdrawals: " + withdrawals + " (amount " + withdrawnAmount + ")\n"
                    + "Transfers in: " + transfersIn + " (amount " + transferInAmount + ")\n"
                    + "Transfers out: " + transfersOut + " (amount " + transferOutAmount + ")\n";
        }
    }

    private static class Totals {
        private long transactions;
        private long accountsOpened;
        private long deposits;
        private long depositedAmount;
        private long withdrawals;
        private long withdrawnAmount;
        private long transfersIn;
        private long transferInAmount;
        private long transfersOut;
        private long transferOutAmount;

        private void add(String type, long amount, int lineNumber) throws IOException {
            transactions = increment(transactions, lineNumber);
            switch (type) {
                case "OPEN" -> accountsOpened = increment(accountsOpened, lineNumber);
                case "DEPOSIT" -> {
                    deposits = increment(deposits, lineNumber);
                    depositedAmount = plus(depositedAmount, amount, lineNumber);
                }
                case "WITHDRAW" -> {
                    withdrawals = increment(withdrawals, lineNumber);
                    withdrawnAmount = plus(withdrawnAmount, amount, lineNumber);
                }
                case "TRANSFER_IN" -> {
                    transfersIn = increment(transfersIn, lineNumber);
                    transferInAmount = plus(transferInAmount, amount, lineNumber);
                }
                case "TRANSFER_OUT" -> {
                    transfersOut = increment(transfersOut, lineNumber);
                    transferOutAmount = plus(transferOutAmount, amount, lineNumber);
                }
                default -> throw malformed(lineNumber);
            }
        }

        private Summary toSummary() {
            return new Summary(transactions, accountsOpened, deposits, depositedAmount,
                    withdrawals, withdrawnAmount, transfersIn, transferInAmount,
                    transfersOut, transferOutAmount);
        }

        private static long increment(long value, int lineNumber) throws IOException {
            return plus(value, 1, lineNumber);
        }

        private static long plus(long left, long right, int lineNumber) throws IOException {
            try {
                return Math.addExact(left, right);
            } catch (ArithmeticException exception) {
                throw malformed(lineNumber, exception);
            }
        }
    }
}
