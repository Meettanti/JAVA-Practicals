public class MiniBankPolymorphismDemo {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("Riya", 10000, 2000);
        CurrentAccount current = new CurrentAccount("Arjun", 5000, 2000);
        FixedDepositAccount fixedDeposit = new FixedDepositAccount("Meera", 25000);

        Account[] accounts = {savings, current, fixedDeposit};
        for (Account account : accounts) {
            System.out.println(account.getClass().getSimpleName()
                    + " interest rate: " + account.interestRate());

            if (account instanceof SavingsAccount savingsAccount) {
                System.out.println("Savings minimum balance: " + savingsAccount.getMinBalance());
            }
        }

        System.out.println("Savings withdraw 9000: " + savings.withdraw(9000));
        System.out.println("Savings withdraw 7000: " + savings.withdraw(7000));
        System.out.println("Savings balance: " + savings.getBalance());
        System.out.println("Current withdraw 6500: " + current.withdraw(6500));
        System.out.println("Current balance: " + current.getBalance());
        System.out.println("Current withdraw 600: " + current.withdraw(600));
        System.out.println("Fixed deposit withdraw 100: " + fixedDeposit.withdraw(100));
    }
}
