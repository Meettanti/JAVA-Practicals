import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.stream.Stream;

public class DirectoryReport {
    public static void main(String[] args) throws IOException {
        Path directory = args.length == 0 ? Path.of(".") : Path.of(args[0]);
        Path root = directory.toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) {
            throw new IOException("Not a directory: " + root);
        }

        long fileCount = 0;
        long directoryCount = 0;
        long totalBytes = 0;
        try (Stream<Path> entries = Files.walk(root)) {
            Iterator<Path> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Path entry = iterator.next();
                if (entry.equals(root)) {
                    continue;
                }
                BasicFileAttributes attributes = Files.readAttributes(
                        entry, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                if (attributes.isDirectory()) {
                    directoryCount++;
                } else if (attributes.isRegularFile()) {
                    fileCount++;
                    totalBytes = Math.addExact(totalBytes, attributes.size());
                }
            }
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        } catch (ArithmeticException exception) {
            throw new IOException("Directory size exceeds the supported total.", exception);
        }

        System.out.println("Directory: " + root);
        System.out.println("Files: " + fileCount);
        System.out.println("Subdirectories: " + directoryCount);
        System.out.println("Total bytes: " + totalBytes);
    }
}
