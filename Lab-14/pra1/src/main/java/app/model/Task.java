package app.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public record Task(long id, String title, Status status) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Task {
        if (id <= 0) {
            throw new IllegalArgumentException("Task ID must be positive.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank.");
        }
        title = title.trim();
        Objects.requireNonNull(status, "status");
    }

    public Task complete() {
        return new Task(id, title, Status.COMPLETED);
    }

    public enum Status {
        OPEN, COMPLETED
    }
}
