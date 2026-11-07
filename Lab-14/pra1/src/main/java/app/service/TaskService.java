package app.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import app.model.Task;
import app.repository.TaskRepository;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public Task create(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank.");
        }
        return repository.create(title.trim());
    }

    public Task complete(long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        return repository.update(task.complete());
    }

    public Task get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }

    public List<Task> list() {
        return repository.findAll();
    }
}
