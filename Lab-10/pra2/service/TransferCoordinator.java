package service;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import exception.AccountNotFoundException;
import exception.BankException;
import model.Account;

public final class TransferCoordinator {
    private TransferCoordinator() {
    }

    public static void transferWithOppositeLockOrder(Account source, Account destination,
            long amount, CyclicBarrier bothAccountsLocked)
            throws BankException, InterruptedException, BrokenBarrierException {
        if (source == null || destination == null) {
            throw new AccountNotFoundException("transfer account");
        }
        synchronized (source) {
            bothAccountsLocked.await();
            synchronized (destination) {
                source.withdraw(amount);
                destination.deposit(amount);
            }
        }
    }

    public static void transferInAccountNumberOrder(Account source, Account destination,
            long amount) throws BankException {
        if (source == null || destination == null) {
            throw new AccountNotFoundException("transfer account");
        }

        Account first = source;
        Account second = destination;
        if (source.getAccountNumber().compareTo(destination.getAccountNumber()) > 0) {
            first = destination;
            second = source;
        }

        synchronized (first) {
            synchronized (second) {
                source.withdraw(amount);
                destination.deposit(amount);
            }
        }
    }
}
