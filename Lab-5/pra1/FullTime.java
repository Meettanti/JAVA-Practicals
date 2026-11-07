public class FullTime extends Employee {
    private double fixedSalary;

    public FullTime(String name, String id, double fixedSalary) {
        super(name, id);
        if (fixedSalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.fixedSalary = fixedSalary;
    }

    @Override
    public double monthlySalary() {
        return fixedSalary;
    }
}
