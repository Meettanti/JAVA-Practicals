package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenericRepositoryTest {
    private GenericRepository<Account> repository;

    @BeforeEach
    void setUp() {
        repository = new GenericRepository<>(Account.class);
    }

    @Test
    void discoversInheritedIdAndSupportsCrud() throws Exception {
        Account account = new SavingsAccount("Test", 100, 0);
        repository.create(account);
        assertSame(account, repository.readById(account.getAccountNumber()).orElseThrow());

        account.deposit(25);
        repository.update(account);
        assertEquals(125, repository.readById(account.getAccountNumber()).orElseThrow()
                .getBalance());
        assertSame(account, repository.delete(account.getAccountNumber()).orElseThrow());
        assertFalse(repository.readById(account.getAccountNumber()).isPresent());
    }

    @Test
    void bulkCreateAndVarargsLookupPreserveRequestedOrder() {
        Account first = new SavingsAccount("First", 100, 0);
        Account second = new SavingsAccount("Second", 200, 0);
        repository.createAll(List.of(first, second));

        assertEquals(List.of(second, first),
                repository.findByIds(second.getAccountNumber(), first.getAccountNumber()));
        assertThrows(IllegalArgumentException.class, () -> repository.create(first));
        assertTrue(repository.delete("missing").isEmpty());
    }

    @Test
    void findAllSupportsComparatorOrdering() {
        Account zed = new SavingsAccount("Zed", 100, 0);
        Account amy = new SavingsAccount("Amy", 200, 0);
        repository.createAll(List.of(zed, amy));

        assertEquals(List.of(amy, zed),
                repository.findAll(Comparator.comparing(Account::getOwnerName)));
    }

    @Test
    void concurrentCreatesKeepEveryUniqueAccount() throws InterruptedException {
        int count = 80;
        var executor = Executors.newFixedThreadPool(8);
        CountDownLatch completed = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int index = i;
            executor.execute(() -> {
                repository.create(new SavingsAccount("Concurrent-" + index, 0, 0));
                completed.countDown();
            });
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue(completed.await(1, TimeUnit.SECONDS));
        assertEquals(count, repository.size());
    }
}
