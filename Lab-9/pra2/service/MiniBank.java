package service;

import static util.Validator.isValidMobile;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.BankInfo;
import model.MenuOption;
import model.SavingsAccount;
import model.WithdrawRule;
import util.AnnotationValidator;

public class MiniBank {
    public static void main(String[] args) throws IllegalAccessException {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        Account savingsAccount = new SavingsAccount("Meet", 10000, 2000);
        Account invalidAccount = new SavingsAccount("Meet", -100, 0);
        Account longNameAccount = new SavingsAccount("M".repeat(41), 100, 0);

        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account != null && account.canWithdraw(amount);
            }
        };
        WithdrawRule lambdaRule = (account, amount) ->
                account != null && account.canWithdraw(amount);

        System.out.println(bankInfo);
        System.out.println("Anonymous withdrawal rule allows 1000: "
                + anonymousRule.allow(savingsAccount, 1000));
        System.out.println("Lambda withdrawal rule allows 9000: "
                + lambdaRule.allow(savingsAccount, 9000));
        System.out.println("Yearly savings interest: " + savingsAccount.yearlyInterest());
        System.out.println("Mobile validation: " + isValidMobile("9876543210"));
        System.out.println("Negative balance errors: "
                + Arrays.toString(AnnotationValidator.validate(invalidAccount)));
        System.out.println("Valid account errors: "
                + Arrays.toString(AnnotationValidator.validate(savingsAccount)));
        System.out.println("Maximum-length errors: "
                + Arrays.toString(AnnotationValidator.validate(longNameAccount)));

        demonstrateCheckedExceptions();
        try {
            demonstrateConcurrency();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Concurrency demonstration interrupted.");
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("1. Open account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                if (!scanner.hasNextLine()) {
                    System.out.println("\nInput ended. Exiting MiniBank.");
                    break;
                }

                String input = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    continue;
                }

                MenuOption option = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.EXIT;
                    default -> null;
                };

                if (option == null) {
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    continue;
                }

                String message = switch (option) {
                    case OPEN_ACCOUNT -> "Open account - to be implemented in a later lab.";
                    case DEPOSIT -> "Deposit - to be implemented in a later lab.";
                    case WITHDRAW -> "Withdraw - to be implemented in a later lab.";
                    case TRANSFER -> "Transfer - to be implemented in a later lab.";
                    case EXIT -> "Goodbye!";
                };
                System.out.println(message);

                if (option == MenuOption.EXIT) {
                    break;
                }
            }
        }
    }

    private static void demonstrateCheckedExceptions() {
        Account account = new SavingsAccount("Meet", 1000, 0);
        Account recipient = new SavingsAccount("Recipient", 250, 0);

        try {
            account.withdraw(5000);
            System.out.println("Withdrawal completed.");
        } catch (InsufficientFundsException exception) {
            System.out.println("Withdrawal failed: short by " + exception.getShortfall());
        } catch (InvalidAmountException exception) {
            System.out.println("Withdrawal failed: " + exception.getMessage());
        } finally {
            System.out.println("Balance after withdrawal attempt: " + account.getBalance());
        }

        try {
            account.withdraw(0);
            System.out.println("Withdrawal completed.");
        } catch (InsufficientFundsException exception) {
            System.out.println("Withdrawal failed: short by " + exception.getShortfall());
        } catch (InvalidAmountException exception) {
            System.out.println("Withdrawal failed: " + exception.getMessage());
        } finally {
            System.out.println("Zero withdrawal attempt finished.");
        }

        try {
            account.deposit(0);
        } catch (InvalidAmountException exception) {
            System.out.println("Deposit failed: " + exception.getMessage());
        } finally {
            System.out.println("Deposit attempt finished.");
        }

        try {
            account.transfer(recipient, 1500);
            System.out.println("Transfer completed.");
        } catch (InsufficientFundsException exception) {
            System.out.println("Transfer failed: short by " + exception.getShortfall());
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        } finally {
            System.out.println("Insufficient transfer attempt finished.");
        }

        try {
            account.transfer(recipient, 250);
            System.out.println("Transfer completed. Sender: " + account.getBalance()
                    + ", recipient: " + recipient.getBalance());
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        } finally {
            System.out.println("Transfer demo finished.");
        }

        try {
            account.transfer(null, 100);
        } catch (AccountNotFoundException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        }

        try (BankSession session = new BankSession("MiniBank exception demo")) {
            System.out.println("Using session: " + session.getName());
            throw new BankException("Demonstrating checked exception handling.");
        } catch (BankException exception) {
            System.out.println("Handled resource-block failure: " + exception.getMessage());
        } finally {
            System.out.println("Resource example finished.");
        }
    }

    private static void demonstrateConcurrency() throws InterruptedException {
        int workerCount = 10;
        int depositsPerWorker = 1000;
        long amount = 1;
        long expectedBalance = workerCount * depositsPerWorker * amount;

        Account unsynchronizedAccount = new SavingsAccount("Race demo", 0, 0);
        runWorkers(unsynchronizedAccount, workerCount, depositsPerWorker, amount, false);
        System.out.println("Unsynchronized account balance: "
                + unsynchronizedAccount.getBalance() + " (expected " + expectedBalance + ")");

        Account synchronizedAccount = new SavingsAccount("Safe demo", 0, 0);
        runWorkers(synchronizedAccount, workerCount, depositsPerWorker, amount, true);
        System.out.println("Synchronized account balance: "
                + synchronizedAccount.getBalance() + " (expected " + expectedBalance + ")");
    }

    private static void runWorkers(Account account, int workerCount, int times, long amount,
            boolean synchronizedDeposits) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        Thread[] threads = new Thread[workerCount];

        for (int i = 0; i < workerCount; i++) {
            Runnable worker = synchronizedDeposits
                    ? new AccountWorker(account, times, amount)
                    : new UnsynchronizedAccountWorker(account, times, amount);
            threads[i] = new Thread(() -> {
                try {
                    startGate.await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Worker interrupted before start.", exception);
                }
                worker.run();
            }, (synchronizedDeposits ? "AccountWorker-" : "RaceWorker-") + (i + 1));
            threads[i].start();
        }

        startGate.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
