package model;

import java.io.Serial;

public class CurrentAccount extends Account {
    @Serial
    private static final long serialVersionUID = 1L;

    private long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        if (amount < 0) {
            return false;
        }

        long balance = getBalance();
        if (balance >= 0) {
            return amount <= balance || amount - balance <= overdraftLimit;
        }
        if (balance < -overdraftLimit) {
            return false;
        }
        return amount <= balance + overdraftLimit;
    }

    @Override
    protected synchronized long availableForWithdrawal() {
        long balance = getBalance();
        if (balance < -overdraftLimit) {
            return 0;
        }
        if (balance > 0 && overdraftLimit > Long.MAX_VALUE - balance) {
            return Long.MAX_VALUE;
        }
        return Math.max(0, balance + overdraftLimit);
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
