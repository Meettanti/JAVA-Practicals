public class PartTime extends Employee {
    private int hours;
    private double hourlyRate;

    public PartTime(String name, String id, int hours, double hourlyRate) {
        super(name, id);
        if (hours < 0 || hourlyRate < 0) {
            throw new IllegalArgumentException("Hours and hourly rate cannot be negative.");
        }
        this.hours = hours;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double monthlySalary() {
        return hours * hourlyRate;
    }
}
