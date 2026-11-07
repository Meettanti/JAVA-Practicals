package service;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class BankApplication {
    private final Path stateFile = Path.of("data", "minibank", "accounts.ser");
    private final Path transactionFile = Path.of("data", "minibank", "transactions.log");
    private final Path reportFile = Path.of("data", "minibank", "reports", "end-of-day.txt");
    private final BankStateStore stateStore = new BankStateStore();
    private final TransactionLog transactionLog = new TransactionLog();
    private final EndOfDayReport endOfDayReport = new EndOfDayReport();
    private final List<Account> accounts = new ArrayList<>();

    public static void run() throws IOException {
        new BankApplication().runMenu();
    }

    private void runMenu() throws IOException {
        if (Files.exists(stateFile)) {
            accounts.addAll(stateStore.load(stateFile));
            System.out.println("Loaded " + accounts.size() + " saved account(s).");
        } else {
            Account openingAccount = new SavingsAccount("Opening Account", 1000, 0);
            accounts.add(openingAccount);
            stateStore.save(stateFile, accounts);
            transactionLog.append(transactionFile, "OPEN", openingAccount, 1000);
            System.out.println("Created a sample savings account: "
                    + openingAccount.getAccountNumber());
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("Accounts:");
                accounts.forEach(System.out::println);
                System.out.println("1. Open account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Save and exit");
                System.out.println("6. Save state");
                System.out.println("7. Load state");
                System.out.println("8. Generate end-of-day report");
                System.out.print("Choose an option: ");
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    saveState();
                    System.out.println("\nInput ended. State saved; exiting MiniBank.");
                    return;
                }

                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> openAccount(scanner);
                        case "2" -> deposit(scanner);
                        case "3" -> withdraw(scanner);
                        case "4" -> transfer(scanner);
                        case "5" -> {
                            saveState();
                            System.out.println("Goodbye!");
                            return;
                        }
                        case "6" -> saveState();
                        case "7" -> loadState();
                        case "8" -> generateReport();
                        default -> System.out.println("Invalid choice. Enter 1 through 8.");
                    }
                } catch (EOFException exception) {
                    saveState();
                    System.out.println("Input ended. State saved; exiting MiniBank.");
                    return;
                } catch (BankException | IllegalArgumentException | IOException exception) {
                    System.out.println("Operation failed: " + exception.getMessage());
                }
            }
        }
    }

    private void openAccount(Scanner scanner) throws IOException {
        long type = readLong(scanner, "Account type (1 savings, 2 current, 3 fixed deposit): ");
        if (type < 1 || type > 3) {
            throw new IllegalArgumentException("Account type must be 1, 2, or 3.");
        }
        String owner = readLine(scanner, "Owner name: ").trim();
        if (owner.isBlank()) {
            throw new IllegalArgumentException("Owner name cannot be blank.");
        }
        long openingBalance = readLong(scanner, "Opening balance: ");
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }

        Account account = switch ((int) type) {
            case 1 -> new SavingsAccount(owner, openingBalance,
                    readNonNegativeLong(scanner, "Minimum balance: "));
            case 2 -> new CurrentAccount(owner, openingBalance,
                    readNonNegativeLong(scanner, "Overdraft limit: "));
            case 3 -> new FixedDepositAccount(owner, openingBalance);
            default -> throw new IllegalArgumentException("Unknown account type.");
        };
        transactionLog.append(transactionFile, "OPEN", account, openingBalance);
        accounts.add(account);
        System.out.println("Opened " + account);
    }

    private void deposit(Scanner scanner) throws IOException, InvalidAmountException {
        Account account = findAccount(readLine(scanner, "Account number: ").trim());
        long amount = readPositiveLong(scanner, "Deposit amount: ");
        account.deposit(amount);
        try {
            transactionLog.append(transactionFile, "DEPOSIT", account, amount);
        } catch (IOException exception) {
            System.out.println("Deposit completed, but its transaction could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Deposit completed. Balance: " + account.getBalance());
    }

    private void withdraw(Scanner scanner) throws IOException, BankException {
        Account account = findAccount(readLine(scanner, "Account number: ").trim());
        long amount = readPositiveLong(scanner, "Withdrawal amount: ");
        account.withdraw(amount);
        try {
            transactionLog.append(transactionFile, "WITHDRAW", account, amount);
        } catch (IOException exception) {
            System.out.println("Withdrawal completed, but its transaction could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Withdrawal completed. Balance: " + account.getBalance());
    }

    private void transfer(Scanner scanner) throws IOException, BankException {
        Account source = findAccount(readLine(scanner, "Source account number: ").trim());
        Account destination = findAccount(
                readLine(scanner, "Destination account number: ").trim());
        if (source.equals(destination)) {
            throw new IllegalArgumentException("Transfer accounts must be different.");
        }
        long amount = readPositiveLong(scanner, "Transfer amount: ");
        TransferCoordinator.transferInAccountNumberOrder(source, destination, amount);
        try {
            transactionLog.appendTransfer(transactionFile, source, destination, amount);
        } catch (IOException exception) {
            System.out.println("Transfer completed, but its transaction records could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Transfer completed.");
    }

    private void saveState() throws IOException {
        stateStore.save(stateFile, accounts);
        System.out.println("Saved " + accounts.size() + " account(s) to " + stateFile + ".");
    }

    private void loadState() throws IOException {
        if (Files.notExists(stateFile)) {
            System.out.println("No saved state exists yet.");
            return;
        }
        List<Account> loadedAccounts = stateStore.load(stateFile);
        accounts.clear();
        accounts.addAll(loadedAccounts);
        System.out.println("Loaded " + accounts.size() + " account(s).");
    }

    private void generateReport() throws IOException {
        EndOfDayReport.Summary summary = endOfDayReport.generate(transactionFile, reportFile);
        System.out.print(summary.format());
        System.out.println("Report written to " + reportFile + ".");
    }

    private Account findAccount(String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + accountNumber));
    }

    private static long readNonNegativeLong(Scanner scanner, String prompt) throws IOException {
        long value = readLong(scanner, prompt);
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative.");
        }
        return value;
    }

    private static long readPositiveLong(Scanner scanner, String prompt) throws IOException {
        long value = readLong(scanner, prompt);
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive.");
        }
        return value;
    }

    private static long readLong(Scanner scanner, String prompt) throws IOException {
        return parseLong(readLine(scanner, prompt).trim(), "value");
    }

    private static long parseLong(String value, String description) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a whole-number " + description + ".",
                    exception);
        }
    }

    private static String readLine(Scanner scanner, String prompt) throws IOException {
        System.out.print(prompt);
        System.out.flush();
        if (!scanner.hasNextLine()) {
            throw new EOFException("Input ended while reading a value.");
        }
        return scanner.nextLine();
    }
}
