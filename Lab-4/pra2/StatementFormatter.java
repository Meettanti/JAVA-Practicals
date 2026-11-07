public class StatementFormatter {
    public static String buildStatement(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        String lineSeparator = System.lineSeparator();
        StringBuilder statement = new StringBuilder();
        statement.append("Account Statement").append(lineSeparator);
        statement.append("Account number: ").append(account.getAccountNumber()).append(lineSeparator);
        statement.append("Account holder: ").append(account.getOwnerName()).append(lineSeparator);
        statement.append("Balance: ").append(account.getBalance());
        return statement.toString();
    }
}
