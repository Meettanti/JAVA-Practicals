public class MiniBankValidationDemo {
    public static void main(String[] args) {
        System.out.println("Mobile 9876543210: " + Validator.isValidMobile("9876543210"));
        System.out.println("Mobile 12345: " + Validator.isValidMobile("12345"));
        System.out.println("Email abc@xyz.com: " + Validator.isValidEmail("abc@xyz.com"));
        System.out.println("Email invalid: " + Validator.isValidEmail("not-an-email"));
        System.out.println("PAN ABCDE1234F: " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN invalid: " + Validator.isValidPan("AB123"));
        System.out.println("IFSC HDFC0001234: " + Validator.isValidIfsc("HDFC0001234"));
        System.out.println("IFSC invalid: " + Validator.isValidIfsc("INVALID"));
        System.out.println("Positive amount 500: " + Validator.isValidAmount("500"));
        System.out.println("Positive amount 0: " + Validator.isValidAmount("0"));

        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Command type: " + command.type());
        System.out.println("Command account: " + command.accountNumber());
        System.out.println("Command amount: " + command.amount());

        Account account = new Account("Riya", 4000);
        System.out.println(StatementFormatter.buildStatement(account));

        try {
            CommandParser.parse("DEPOSIT AC0001");
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid command rejected: " + exception.getMessage());
        }
    }
}
