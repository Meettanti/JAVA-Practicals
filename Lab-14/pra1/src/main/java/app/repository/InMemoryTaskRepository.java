package app.repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import app.model.Task;

public class InMemoryTaskRepository implements TaskRepository {
    private volatile ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    @Override
    public synchronized Task create(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank.");
        }
        long id = nextId.incrementAndGet();
        Task task = new Task(id, title.trim(), Task.Status.OPEN);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public synchronized Task update(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        if (tasks.replace(task.id(), task) == null) {
            throw new IllegalArgumentException("Task not found: " + task.id());
        }
        return task;
    }

    @Override
    public synchronized boolean deleteById(long id) {
        return tasks.remove(id) != null;
    }

    @Override
    public List<Task> findAll() {
        return tasks.values().stream()
                .sorted(Comparator.comparingLong(Task::id))
                .toList();
    }

    @Override
    public synchronized void replaceAll(Collection<Task> updatedTasks) {
        if (updatedTasks == null) {
            throw new IllegalArgumentException("Task collection cannot be null.");
        }
        ConcurrentHashMap<Long, Task> replacement = new ConcurrentHashMap<>();
        long maximumId = 0;
        for (Task task : updatedTasks) {
            if (task == null || replacement.putIfAbsent(task.id(), task) != null) {
                throw new IllegalArgumentException("Task collection contains a null or duplicate.");
            }
            maximumId = Math.max(maximumId, task.id());
        }
        tasks = replacement;
        nextId.set(maximumId);
    }
}
