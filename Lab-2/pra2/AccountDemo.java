public class AccountDemo {
    public static void main(String[] args) {
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210");
        System.out.println("Customer: " + customer.getName() + ", ID: " + customer.getCustomerId());

        Account[] accounts = {
            new Account("Riya", 5000),
            new Account("Arjun"),
            new Account("Meera", 2500)
        };

        Account firstAccount = accounts[0];
        firstAccount.deposit(2000);
        System.out.println(firstAccount.getAccountNumber() + " balance after deposit: "
                + firstAccount.getBalance());

        boolean firstWithdrawal = firstAccount.withdraw(3000);
        System.out.println("Withdraw 3000 successful: " + firstWithdrawal);
        System.out.println(firstAccount.getAccountNumber() + " balance after withdrawal: "
                + firstAccount.getBalance());

        boolean secondWithdrawal = firstAccount.withdraw(10000);
        System.out.println("Withdraw 10000 successful: " + secondWithdrawal);
        System.out.println(firstAccount.getAccountNumber() + " balance after failed withdrawal: "
                + firstAccount.getBalance());

        firstAccount.deposit(-50);
        boolean negativeWithdrawal = firstAccount.withdraw(-10);
        System.out.println("Negative withdrawal successful: " + negativeWithdrawal);
        System.out.println("Balance after rejected negative amounts: " + firstAccount.getBalance());

        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " | " + account.getOwnerName()
                    + " | balance: " + account.getBalance() + " | active: " + account.isActive());
        }
    }
}
