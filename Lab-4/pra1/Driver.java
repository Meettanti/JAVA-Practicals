import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        String[] passwords = {
            "abc",
            "Abcd1234!",
            "abcd1234",
            "PASSWORD!"
        };

        for (String password : passwords) {
            PasswordChecker.printStrength(password);
            System.out.println();
        }

        String[] logLines = {
            "10:05 alice Hello there",
            "10:10 bob Meeting starts soon",
            "malformed line"
        };

        try (Scanner scanner = new Scanner(System.in)) {
            ChatFilter.printReport(scanner, logLines);
        }
    }
}
