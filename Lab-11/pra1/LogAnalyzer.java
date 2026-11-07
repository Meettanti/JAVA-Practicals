import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogAnalyzer {
    public static void main(String[] args) throws IOException {
        Path file = args.length == 0 ? Path.of("sample-application.txt") : Path.of(args[0]);
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("INFO", 0L);
        counts.put("WARN", 0L);
        counts.put("ERROR", 0L);
        counts.put("OTHER", 0L);

        try (var lines = Files.lines(file)) {
            lines.map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> {
                        int separator = line.indexOf(' ');
                        String level = separator < 0 ? line : line.substring(0, separator);
                        counts.compute(counts.containsKey(level) ? level : "OTHER",
                                (ignored, count) -> count + 1);
                    });
        }

        System.out.println("Log analysis for " + file.toAbsolutePath().normalize());
        counts.forEach((level, count) -> System.out.println(level + ": " + count));
    }
}
