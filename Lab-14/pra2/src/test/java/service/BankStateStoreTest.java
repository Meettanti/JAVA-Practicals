package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.nio.file.Path;
import java.util.List;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BankStateStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void roundTripsAllSupportedAccountTypes() throws Exception {
        BankStateStore store = new BankStateStore();
        Path file = temporaryDirectory.resolve("accounts.ser");
        List<Account> expected = List.of(
                new SavingsAccount("Saver", 1200, 100),
                new CurrentAccount("Current", 500, 200),
                new FixedDepositAccount("Fixed", 800));

        store.save(file, expected);

        List<Account> loaded = store.load(file);
        assertEquals(expected.stream().map(Account::getAccountNumber).toList(),
                loaded.stream().map(Account::getAccountNumber).toList());
        assertInstanceOf(SavingsAccount.class, loaded.get(0));
        assertInstanceOf(CurrentAccount.class, loaded.get(1));
        assertInstanceOf(FixedDepositAccount.class, loaded.get(2));
        assertEquals(List.of(1200L, 500L, 800L),
                loaded.stream().map(Account::getBalance).toList());
    }
}
