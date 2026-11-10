package service;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.AnnotationValidator;
import util.Validator;

public class BankApplication {
    private final Path stateFile = Path.of("data", "minibank", "accounts.ser");
    private final Path transactionFile = Path.of("data", "minibank", "transactions.log");
    private final Path reportFile = Path.of("data", "minibank", "reports", "end-of-day.txt");
    private final BankStateStore stateStore = new BankStateStore();
    private final TransactionLog transactionLog = new TransactionLog();
    private final TransactionHistory transactionHistory = new TransactionHistory();
    private final EndOfDayReport endOfDayReport = new EndOfDayReport();
    private final GenericRepository<Account> accounts = new GenericRepository<>(Account.class);

    public static void run() throws IOException, IllegalAccessException {
        new BankApplication().runMenu();
    }

    private void runMenu() throws IOException, IllegalAccessException {
        if (Files.exists(stateFile)) {
            accounts.replaceAll(stateStore.load(stateFile));
            System.out.println("Loaded " + accounts.size() + " saved account(s).");
        } else {
            Account openingAccount = new SavingsAccount("Opening Account", 1000, 0);
            accounts.create(openingAccount);
            stateStore.save(stateFile, sortedAccounts());
            transactionLog.append(transactionFile, "OPEN", openingAccount, 1000);
            System.out.println("Created a sample savings account: "
                    + openingAccount.getAccountNumber());
        }
        transactionHistory.load(transactionFile);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("Accounts:");
                sortedAccounts().forEach(System.out::println);
                System.out.println("1. Open account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Save and exit");
                System.out.println("6. Save state");
                System.out.println("7. Load state");
                System.out.println("8. Generate end-of-day report");
                System.out.println("9. Show sorted transaction history");
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
                        case "9" -> showTransactionHistory();
                        default -> System.out.println("Invalid choice. Enter 1 through 9.");
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

    private void openAccount(Scanner scanner) throws IOException, IllegalAccessException {
        long type = readLong(scanner, "Account type (1 savings, 2 current, 3 fixed deposit): ");
        if (type < 1 || type > 3) {
            throw new IllegalArgumentException("Account type must be 1, 2, or 3.");
        }
        String owner = readLine(scanner, "Customer name: ").trim();
        if (owner.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank.");
        }
        String email = readLine(scanner, "Customer email: ").trim();
        String mobile = readLine(scanner, "Customer mobile: ").trim();
        String addressLine = readLine(scanner, "Address line: ").trim();
        String city = readLine(scanner, "City: ").trim();
        String pincode = readLine(scanner, "Pincode: ").trim();
        Customer customer = new Customer(owner, email, mobile,
                new Customer.Address(addressLine, city, pincode));
        String[] annotationErrors = AnnotationValidator.validate(customer);
        if (annotationErrors.length > 0) {
            throw new IllegalArgumentException(
                    "Invalid customer details: " + String.join("; ", annotationErrors));
        }
        if (!Validator.isValidEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is invalid.");
        }
        if (!Validator.isValidMobile(customer.getMobile())) {
            throw new IllegalArgumentException("Customer mobile is invalid.");
        }
        long openingBalance = readLong(scanner, "Opening balance: ");
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }

        Account account = switch ((int) type) {
            case 1 -> new SavingsAccount(customer, openingBalance,
                    readNonNegativeLong(scanner, "Minimum balance: "));
            case 2 -> new CurrentAccount(customer, openingBalance,
                    readNonNegativeLong(scanner, "Overdraft limit: "));
            case 3 -> new FixedDepositAccount(customer, openingBalance);
            default -> throw new IllegalArgumentException("Unknown account type.");
        };
        TransactionRecord transaction =
                transactionLog.append(transactionFile, "OPEN", account, openingBalance);
        accounts.create(account);
        transactionHistory.add(transaction);
        System.out.println("Opened " + account + " for customer "
                + customer.getCustomerId());
    }

    private void deposit(Scanner scanner) throws IOException, InvalidAmountException {
        Command command = readTransactionCommand(scanner, TransactionType.DEPOSIT);
        Account account = findAccount(command.accountNumber());
        long amount = command.amount();
        account.deposit(amount);
        accounts.update(account);
        try {
            transactionHistory.add(
                    transactionLog.append(transactionFile, "DEPOSIT", account, amount));
        } catch (IOException exception) {
            System.out.println("Deposit completed, but its transaction could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Deposit completed.");
        System.out.println(StatementFormatter.buildStatement(account));
    }

    private void withdraw(Scanner scanner) throws IOException, BankException {
        Command command = readTransactionCommand(scanner, TransactionType.WITHDRAW);
        Account account = findAccount(command.accountNumber());
        long amount = command.amount();
        account.withdraw(amount);
        accounts.update(account);
        try {
            transactionHistory.add(
                    transactionLog.append(transactionFile, "WITHDRAW", account, amount));
        } catch (IOException exception) {
            System.out.println("Withdrawal completed, but its transaction could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Withdrawal completed.");
        System.out.println(StatementFormatter.buildStatement(account));
    }

    private void transfer(Scanner scanner) throws IOException, BankException {
        List<Account> endpoints = accounts.findByIds(
                readLine(scanner, "Source account number: ").trim(),
                readLine(scanner, "Destination account number: ").trim());
        if (endpoints.size() != 2) {
            throw new IllegalArgumentException("One or both transfer accounts were not found.");
        }
        Account source = endpoints.get(0);
        Account destination = endpoints.get(1);
        if (source.equals(destination)) {
            throw new IllegalArgumentException("Transfer accounts must be different.");
        }
        long amount = readPositiveLong(scanner, "Transfer amount: ");
        TransferCoordinator.transferInAccountNumberOrder(source, destination, amount);
        accounts.update(source);
        accounts.update(destination);
        try {
            transactionHistory.addAll(
                    transactionLog.appendTransfer(transactionFile, source, destination, amount));
        } catch (IOException exception) {
            System.out.println("Transfer completed, but its transaction records could not be logged: "
                    + exception.getMessage());
            return;
        }
        System.out.println("Transfer completed.");
        System.out.println(StatementFormatter.buildStatement(source));
        System.out.println(StatementFormatter.buildStatement(destination));
    }

    private void saveState() throws IOException {
        stateStore.save(stateFile, sortedAccounts());
        System.out.println("Saved " + accounts.size() + " account(s) to " + stateFile + ".");
    }

    private void loadState() throws IOException {
        if (Files.notExists(stateFile)) {
            System.out.println("No saved state exists yet.");
            return;
        }
        List<Account> loadedAccounts = stateStore.load(stateFile);
        accounts.replaceAll(loadedAccounts);
        System.out.println("Loaded " + accounts.size() + " account(s).");
    }

    private void generateReport() throws IOException {
        EndOfDayReport.Summary summary = endOfDayReport.generate(transactionFile, reportFile);
        System.out.print(summary.format());
        System.out.println("Report written to " + reportFile + ".");
    }

    private void showTransactionHistory() {
        List<TransactionRecord> sortedTransactions = transactionHistory.sortedByTime();
        if (sortedTransactions.isEmpty()) {
            System.out.println("No transaction history is available.");
            return;
        }
        System.out.println("Transaction history (chronological):");
        sortedTransactions.forEach(System.out::println);
    }

    private List<Account> sortedAccounts() {
        return accounts.findAll(Comparator.comparing(Account::getAccountNumber));
    }

    private Account findAccount(String accountNumber) {
        return accounts.readById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + accountNumber));
    }

    private static Command readTransactionCommand(Scanner scanner, TransactionType expectedType)
            throws IOException {
        Command command = CommandParser.parse(readLine(scanner,
                "Enter command (" + expectedType + " <account-number> <amount>): "));
        if (command.type() != expectedType) {
            throw new IllegalArgumentException("Expected a " + expectedType + " command.");
        }
        return command;
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
