package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import app.model.Task;
import app.repository.InMemoryTaskRepository;
import app.service.TaskService;
import app.util.TaskFileStore;

public class TaskApplication {
    public static void main(String[] args) throws IOException {
        Path stateFile = Path.of("data", "tasks.ser");
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskFileStore fileStore = new TaskFileStore();
        if (Files.exists(stateFile)) {
            repository.replaceAll(fileStore.load(stateFile));
        }

        TaskService service = new TaskService(repository);
        if (service.list().isEmpty()) {
            service.create("Review the practical requirements");
            service.create("Run the JUnit tests");
        }
        for (Task task : service.list()) {
            System.out.println(task);
        }
        fileStore.save(stateFile, service.list());
    }
}
