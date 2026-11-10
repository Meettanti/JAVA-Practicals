package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransactionHistoryTest {
    @Test
    void sortsTransactionsChronologically() {
        TransactionHistory history = new TransactionHistory();
        TransactionRecord later = new TransactionRecord(
                Instant.parse("2026-01-02T00:00:00Z"), "DEPOSIT", "AC0001", 50, 150);
        TransactionRecord earlier = new TransactionRecord(
                Instant.parse("2026-01-01T00:00:00Z"), "OPEN", "AC0001", 100, 100);

        history.add(later);
        history.add(earlier);

        assertEquals(List.of(earlier, later), history.sortedByTime());
        assertEquals(List.of(earlier, later), history.forAccount("AC0001"));
    }
}
