import java.util.Locale;

public class PayrollDemo {
    public static void main(String[] args) {
        Employee[] employees = {
            new FullTime("Asha", "E101", 50000),
            new PartTime("Dev", "E102", 80, 300),
            new Intern("Nina", "E103", 12000)
        };

        double totalSalary = 0;
        for (Employee employee : employees) {
            double salary = employee.monthlySalary();
            totalSalary += salary;

            System.out.printf(Locale.ROOT, "%s (%s): %.2f",
                    employee.getName(), employee.getId(), salary);
            if (employee instanceof Intern) {
                System.out.print(" - Intern stipend");
            }
            System.out.println();
        }

        System.out.printf(Locale.ROOT, "Total monthly salary: %.2f%n", totalSalary);
    }
}
