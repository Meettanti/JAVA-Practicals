public class Intern extends Employee {
    private double stipend;

    public Intern(String name, String id, double stipend) {
        super(name, id);
        if (stipend < 0) {
            throw new IllegalArgumentException("Stipend cannot be negative.");
        }
        this.stipend = stipend;
    }

    @Override
    public double monthlySalary() {
        return stipend;
    }
}
