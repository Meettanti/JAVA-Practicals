package model;

public class SavingsAccount extends Account {
    private long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        if (minBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return amount >= 0
                && getBalance() >= minBalance
                && amount <= getBalance() - minBalance;
    }

    @Override
    protected synchronized long availableForWithdrawal() {
        return getBalance() <= minBalance ? 0 : getBalance() - minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }
}
