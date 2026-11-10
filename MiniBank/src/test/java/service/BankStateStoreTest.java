package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.nio.file.Path;
import java.util.List;
import model.Account;
import model.CurrentAccount;
import model.Customer;
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
        Customer customer = new Customer("Saver", "saver@example.com", "9876543210",
                new Customer.Address("12 Lake Road", "Anand", "388001"));
        List<Account> expected = List.of(
                new SavingsAccount(customer, 1200, 100),
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
        assertEquals(customer.getCustomerId(), loaded.get(0).getCustomer().getCustomerId());
        assertEquals(customer.getEmail(), loaded.get(0).getCustomer().getEmail());
        assertEquals("12 Lake Road", loaded.get(0).getCustomer().getAddress().getLine());
    }
}
