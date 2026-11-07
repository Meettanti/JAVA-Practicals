public class CurrentAccount extends Account {
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
    public boolean canWithdraw(long amount) {
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

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
