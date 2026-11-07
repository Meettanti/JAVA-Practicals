package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import app.model.Task;
import app.repository.InMemoryTaskRepository;

class TaskServiceTest {
    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService(new InMemoryTaskRepository());
    }

    @Test
    void createsAndCompletesTask() {
        Task created = service.create("  Write tests  ");
        Task completed = service.complete(created.id());

        assertEquals("Write tests", created.title());
        assertEquals(Task.Status.COMPLETED, completed.status());
        assertEquals(completed, service.get(created.id()));
    }

    @Test
    void rejectsBlankTitlesAndUnknownTasks() {
        assertThrows(IllegalArgumentException.class, () -> service.create("  "));
        assertThrows(NoSuchElementException.class, () -> service.complete(99));
    }
}
