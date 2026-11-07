package service;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class BankStateStore {
    private static final ObjectInputFilter ACCOUNT_FILTER = info -> {
        if (info.depth() > 16 || info.references() > 10_000 || info.streamBytes() > 10_000_000) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> type = info.serialClass();
        if (type == null) {
            return ObjectInputFilter.Status.UNDECIDED;
        }
        return type == ArrayList.class || type == Object[].class || type == String.class
                || type == Account.class || type == SavingsAccount.class
                || type == CurrentAccount.class || type == FixedDepositAccount.class
                ? ObjectInputFilter.Status.ALLOWED
                : ObjectInputFilter.Status.REJECTED;
    };

    public void save(Path file, List<Account> accounts) throws IOException {
        if (accounts == null) {
            throw new IllegalArgumentException("Account list cannot be null.");
        }
        ArrayList<Account> snapshot = new ArrayList<>(accounts);
        validateAccounts(snapshot);

        Path absoluteFile = file.toAbsolutePath().normalize();
        Path parent = absoluteFile.getParent();
        Files.createDirectories(parent);
        Path temporaryFile = Files.createTempFile(parent, "accounts-", ".tmp");
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(
                    Files.newOutputStream(temporaryFile))) {
                output.writeObject(snapshot);
            }
            try {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    public List<Account> load(Path file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            input.setObjectInputFilter(ACCOUNT_FILTER);
            Object stored;
            try {
                stored = input.readObject();
            } catch (ClassNotFoundException exception) {
                throw new IOException("Saved account data contains an unknown class.", exception);
            }
            if (!(stored instanceof ArrayList<?> values)) {
                throw new InvalidObjectException("Saved state is not an account list.");
            }

            ArrayList<Account> accounts = new ArrayList<>(values.size());
            for (Object value : values) {
                if (!(value instanceof Account account)) {
                    throw new InvalidObjectException("Saved state contains a non-account value.");
                }
                accounts.add(account);
            }
            validateAccounts(accounts);
            return accounts;
        }
    }

    private static void validateAccounts(List<Account> accounts) throws InvalidObjectException {
        Set<String> accountNumbers = new HashSet<>();
        for (Account account : accounts) {
            if (account == null) {
                throw new InvalidObjectException("Saved state contains a null account.");
            }
            Class<?> type = account.getClass();
            if (type != SavingsAccount.class && type != CurrentAccount.class
                    && type != FixedDepositAccount.class) {
                throw new InvalidObjectException("Unsupported account type: " + type.getName());
            }
            if (!accountNumbers.add(account.getAccountNumber())) {
                throw new InvalidObjectException(
                        "Duplicate account number: " + account.getAccountNumber());
            }
        }
    }
}
