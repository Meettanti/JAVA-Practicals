package model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static long accountCounter = 0;

    @Id
    private final String accountNumber;
    @MaxLength(40)
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    public Account(String ownerName, long openingBalance) {
        accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        balance = openingBalance;
        active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    @Serial
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (accountNumber == null || !accountNumber.matches("AC\\d{4,}")) {
            throw new InvalidObjectException("Invalid serialized account number.");
        }
        try {
            long loadedId = Long.parseLong(accountNumber.substring(2));
            synchronized (Account.class) {
                accountCounter = Math.max(accountCounter, loadedId);
            }
        } catch (NumberFormatException exception) {
            InvalidObjectException invalid = new InvalidObjectException(
                    "Serialized account number is out of range.");
            invalid.initCause(exception);
            throw invalid;
        }
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        balance += amount;
    }

    public void depositUnsynchronizedForDemo(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        long previous = balance;
        Thread.yield();
        balance = previous + amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        long available = availableForWithdrawal();
        if (amount > available) {
            throw new InsufficientFundsException(amount - available);
        }
        balance -= amount;
        return true;
    }

    public abstract boolean canWithdraw(long amount);

    protected abstract long availableForWithdrawal();

    public void transfer(Account recipient, long amount) throws BankException {
        try {
            if (recipient == null) {
                throw new AccountNotFoundException("destination");
            }
            withdraw(amount);
            recipient.deposit(amount);
        } catch (BankException exception) {
            throw exception;
        } finally {
            System.out.println("Transfer attempt finished.");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account)) {
            return false;
        }
        Account account = (Account) other;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
