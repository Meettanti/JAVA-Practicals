package app.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import app.model.Task;

public interface TaskRepository {
    Task create(String title);

    Optional<Task> findById(long id);

    Task update(Task task);

    boolean deleteById(long id);

    List<Task> findAll();

    void replaceAll(Collection<Task> tasks);
}
