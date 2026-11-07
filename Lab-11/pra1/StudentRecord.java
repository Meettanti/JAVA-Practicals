import java.io.Serial;
import java.io.Serializable;

public record StudentRecord(int id, String name, double grade) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public StudentRecord {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name cannot be blank.");
        }
        if (!Double.isFinite(grade) || grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
    }
}
