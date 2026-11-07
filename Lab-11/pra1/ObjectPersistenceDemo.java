import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectPersistenceDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path file = args.length == 0 ? Path.of("data", "student.ser") : Path.of(args[0]);
        Path absoluteFile = file.toAbsolutePath().normalize();
        Files.createDirectories(absoluteFile.getParent());

        StudentRecord original = new StudentRecord(104, "Meet Tanti", 91.5);
        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(absoluteFile))) {
            output.writeObject(original);
        }

        Object loaded;
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(absoluteFile))) {
            input.setObjectInputFilter(ObjectPersistenceDemo::filterInput);
            loaded = input.readObject();
        }
        if (!(loaded instanceof StudentRecord student)) {
            throw new InvalidObjectException("The saved file does not contain a StudentRecord.");
        }
        System.out.println("Saved and loaded: " + student);
        System.out.println("File: " + absoluteFile);
    }

    private static ObjectInputFilter.Status filterInput(ObjectInputFilter.FilterInfo info) {
        if (info.depth() > 8 || info.references() > 100 || info.streamBytes() > 1_000_000) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> type = info.serialClass();
        if (type == null) {
            return ObjectInputFilter.Status.UNDECIDED;
        }
        return type == StudentRecord.class || type == String.class
                ? ObjectInputFilter.Status.ALLOWED
                : ObjectInputFilter.Status.REJECTED;
    }
}
