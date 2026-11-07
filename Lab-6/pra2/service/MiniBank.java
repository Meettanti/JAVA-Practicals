package service;

import static util.Validator.isValidMobile;

import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.MenuOption;
import model.SavingsAccount;
import model.WithdrawRule;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        Account savingsAccount = new SavingsAccount("Meet", 10000, 2000);

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
}
