package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.SavingsAccount;
import org.junit.jupiter.api.Test;

class StatementFormatterTest {
    @Test
    void formatsAnAccountStatement() {
        SavingsAccount account = new SavingsAccount("Riya", 4000, 0);

        assertEquals("Account Statement" + System.lineSeparator()
                        + "Account number: " + account.getAccountNumber() + System.lineSeparator()
                        + "Account holder: Riya" + System.lineSeparator()
                        + "Balance: 4000",
                StatementFormatter.buildStatement(account));
    }

    @Test
    void rejectsNullAccounts() {
        assertThrows(IllegalArgumentException.class,
                () -> StatementFormatter.buildStatement(null));
    }
}
