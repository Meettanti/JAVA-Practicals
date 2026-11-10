package service;

import exception.BankException;
import model.Account;

public class TransactionTask implements Runnable {
    private final Account account;
    private final String transactionId;
    private final long amount;
    private final boolean deposit;

    public TransactionTask(Account account, String transactionId, long amount, boolean deposit) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        this.account = account;
        this.transactionId = transactionId;
        this.amount = amount;
        this.deposit = deposit;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            if (deposit) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
            System.out.println(threadName + " completed " + transactionId);
        } catch (BankException exception) {
            System.out.println(threadName + " failed " + transactionId
                    + ": " + exception.getMessage());
        }
    }
}
