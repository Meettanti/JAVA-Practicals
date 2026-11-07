package service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import model.Account;

public class AccountStore {
    private volatile ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public synchronized void add(Account account) {
        Objects.requireNonNull(account, "account");
        Account previous = accounts.putIfAbsent(account.getAccountNumber(), account);
        if (previous != null) {
            throw new IllegalArgumentException(
                    "Account already exists: " + account.getAccountNumber());
        }
    }

    public synchronized void replaceAll(Collection<Account> updatedAccounts) {
        Objects.requireNonNull(updatedAccounts, "updatedAccounts");
        ConcurrentHashMap<String, Account> replacement = new ConcurrentHashMap<>();
        for (Account account : updatedAccounts) {
            Objects.requireNonNull(account, "account");
            Account previous = replacement.putIfAbsent(account.getAccountNumber(), account);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate account number: " + account.getAccountNumber());
            }
        }
        accounts = replacement;
    }

    public Account get(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    public List<Account> sortedByAccountNumber() {
        return accounts.values().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .toList();
    }

    public int size() {
        return accounts.size();
    }
}
