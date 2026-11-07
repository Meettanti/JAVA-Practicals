import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

public class UniqueVisitors {
    public static void main(String[] args) throws IOException {
        Path file = args.length == 0 ? Path.of("sample-visitors.txt") : Path.of(args[0]);
        Set<String> visitors = new LinkedHashSet<>();
        long duplicateVisits = 0;
        try (var lines = Files.lines(file)) {
            for (String line : lines.toList()) {
                String visitor = line.trim();
                if (!visitor.isEmpty() && !visitors.add(visitor)) {
                    duplicateVisits++;
                }
            }
        }

        System.out.println("Unique visitors: " + visitors.size());
        System.out.println("Duplicate visits: " + duplicateVisits);
        visitors.forEach(System.out::println);
    }
}
