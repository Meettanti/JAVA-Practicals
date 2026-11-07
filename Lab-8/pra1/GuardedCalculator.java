import java.io.EOFException;
import java.util.Locale;
import java.util.Scanner;

public class GuardedCalculator {
    public static void main(String[] args) {
        boolean calculationSucceeded = false;
        boolean inputEnded = false;
        int attempt = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (!calculationSucceeded && !inputEnded) {
                attempt++;
                try {
                    String leftText = readLine(scanner, "First number: ");
                    String operatorText = readLine(scanner, "Operator (+, -, *, /): ");
                    String rightText = readLine(scanner, "Second number: ");

                    double left = Double.parseDouble(leftText);
                    double right = Double.parseDouble(rightText);
                    if (!Double.isFinite(left) || !Double.isFinite(right)) {
                        throw new NumberFormatException("Numbers must be finite.");
                    }
                    if (operatorText.length() != 1) {
                        throw new IllegalArgumentException("Enter one operator character.");
                    }

                    double result = calculate(left, operatorText.charAt(0), right);
                    System.out.printf(Locale.ROOT, "Result: %.2f%n", result);
                    calculationSucceeded = true;
                } catch (NumberFormatException exception) {
                    System.out.println("Invalid number: enter finite numeric values.");
                } catch (DivideByZeroException exception) {
                    System.out.println("Calculation failed: " + exception.getMessage());
                } catch (IllegalArgumentException exception) {
                    System.out.println("Invalid operator: " + exception.getMessage());
                } catch (EOFException exception) {
                    System.out.println("Input ended before a calculation succeeded.");
                    inputEnded = true;
                } finally {
                    System.out.println("Attempt " + attempt + " logged.");
                }
            }
        }
    }

    public static double calculate(double left, char operator, double right)
            throws DivideByZeroException {
        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> {
                if (right == 0.0) {
                    throw new DivideByZeroException("Cannot divide by zero.");
                }
                yield left / right;
            }
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private static String readLine(Scanner scanner, String prompt) throws EOFException {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new EOFException();
        }
        return scanner.nextLine().trim();
    }
}
