package model;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String ownerName, long openingBalance) {
        super(ownerName, openingBalance);
    }

    public FixedDepositAccount(String ownerName) {
        super(ownerName);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    protected synchronized long availableForWithdrawal() {
        return 0;
    }
}
