package app.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import app.model.Task;

public class TaskFileStore {
    private static final ObjectInputFilter TASK_FILTER = info -> {
        if (info.depth() > 12 || info.references() > 10_000 || info.streamBytes() > 5_000_000) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> type = info.serialClass();
        if (type == null) {
            return ObjectInputFilter.Status.UNDECIDED;
        }
        return type == ArrayList.class || type == Object[].class || type == String.class
                || type == Task.class || type == Task.Status.class || type == Enum.class
                ? ObjectInputFilter.Status.ALLOWED
                : ObjectInputFilter.Status.REJECTED;
    };

    public void save(Path file, List<Task> tasks) throws IOException {
        if (tasks == null || tasks.stream().anyMatch(task -> task == null)) {
            throw new IllegalArgumentException("Task list cannot be null or contain null values.");
        }
        Path absoluteFile = file.toAbsolutePath().normalize();
        Files.createDirectories(absoluteFile.getParent());
        Path temporaryFile = Files.createTempFile(absoluteFile.getParent(), "tasks-", ".tmp");
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(
                    Files.newOutputStream(temporaryFile))) {
                output.writeObject(new ArrayList<>(tasks));
            }
            try {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    public List<Task> load(Path file) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            input.setObjectInputFilter(TASK_FILTER);
            Object stored;
            try {
                stored = input.readObject();
            } catch (ClassNotFoundException exception) {
                throw new IOException("Saved tasks contain an unknown class.", exception);
            }
            if (!(stored instanceof ArrayList<?> values)) {
                throw new InvalidObjectException("Saved file does not contain a task list.");
            }
            ArrayList<Task> tasks = new ArrayList<>(values.size());
            for (Object value : values) {
                if (!(value instanceof Task task)) {
                    throw new InvalidObjectException("Saved task list contains a non-task value.");
                }
                tasks.add(task);
            }
            return List.copyOf(tasks);
        }
    }
}
