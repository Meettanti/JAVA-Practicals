public class FormValidatorDemo {
    public static void main(String[] args) throws IllegalAccessException {
        SignupForm invalidForm = new SignupForm(" ", "a".repeat(81));
        SignupForm validForm = new SignupForm("Meet Tanti", "meet@example.com");

        System.out.println("Invalid form: " + FormValidator.validate(invalidForm));
        System.out.println("Valid form: " + FormValidator.validate(validForm));
    }
}
