package app.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import app.model.Task;

class TaskFileStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void savesAndRestoresTaskList() throws Exception {
        TaskFileStore store = new TaskFileStore();
        Path file = temporaryDirectory.resolve("tasks.ser");
        List<Task> tasks = List.of(
                new Task(1, "First task", Task.Status.OPEN),
                new Task(2, "Finished task", Task.Status.COMPLETED));

        store.save(file, tasks);

        assertEquals(tasks, store.load(file));
    }
}
