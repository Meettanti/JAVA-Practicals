package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TransactionProcessorTest {
    @Test
    void waitsForAllSubmittedTransactions() throws InterruptedException {
        int taskCount = 40;
        AtomicInteger completed = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(taskCount);
        TransactionProcessor processor = new TransactionProcessor();
        try {
            for (int i = 0; i < taskCount; i++) {
                processor.submit(() -> {
                    completed.incrementAndGet();
                    latch.countDown();
                });
            }
        } finally {
            processor.stop();
        }

        if (!latch.await(1, TimeUnit.SECONDS)) {
            throw new AssertionError("Transaction tasks did not all complete.");
        }
        assertEquals(taskCount, completed.get());
    }
}
