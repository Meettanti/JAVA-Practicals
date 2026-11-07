import java.util.regex.Pattern;

public class PasswordChecker {
    private static final Pattern LENGTH_RULE = Pattern.compile(".{8,}");
    private static final Pattern UPPERCASE_RULE = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT_RULE = Pattern.compile(".*[0-9].*");
    private static final Pattern SPECIAL_CHARACTER_RULE = Pattern.compile(".*[^A-Za-z0-9].*");

    public static String strength(String password) {
        boolean[] rules = checkRules(password);
        int passedCount = 0;
        for (boolean passed : rules) {
            if (passed) {
                passedCount++;
            }
        }

        if (passedCount <= 1) {
            return "Weak";
        }
        if (passedCount <= 3) {
            return "Medium";
        }
        return "Strong";
    }

    public static void printStrength(String password) {
        boolean[] rules = checkRules(password);
        String displayPassword = password == null ? "" : password;

        System.out.println("Password: " + displayPassword);
        System.out.println("Length >= 8: " + rules[0]);
        System.out.println("Contains uppercase: " + rules[1]);
        System.out.println("Contains digit: " + rules[2]);
        System.out.println("Contains special character: " + rules[3]);
        System.out.println("Strength: " + strength(password));
    }

    private static boolean[] checkRules(String password) {
        String value = password == null ? "" : password;
        return new boolean[] {
            LENGTH_RULE.matcher(value).matches(),
            UPPERCASE_RULE.matcher(value).matches(),
            DIGIT_RULE.matcher(value).matches(),
            SPECIAL_CHARACTER_RULE.matcher(value).matches()
        };
    }
}
