import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {
    public static void main(String[] args) throws IOException {
        Path file = args.length == 0 ? Path.of("sample-scores.txt") : Path.of(args[0]);
        Map<String, Integer> totals = new HashMap<>();
        try (var lines = Files.lines(file)) {
            for (String line : lines.toList()) {
                if (!line.isBlank()) {
                    addScore(totals, line);
                }
            }
        }

        List<Map.Entry<String, Integer>> ranking = new ArrayList<>(totals.entrySet());
        ranking.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey));
        for (int i = 0; i < ranking.size(); i++) {
            Map.Entry<String, Integer> player = ranking.get(i);
            System.out.println((i + 1) + ". " + player.getKey() + " - " + player.getValue());
        }
    }

    private static void addScore(Map<String, Integer> totals, String line) throws IOException {
        String[] fields = line.split(",", -1);
        if (fields.length != 2 || fields[0].isBlank()) {
            throw new IOException("Malformed score entry: " + line);
        }
        try {
            int score = Integer.parseInt(fields[1].trim());
            if (score < 0) {
                throw new IOException("Score cannot be negative: " + line);
            }
            totals.merge(fields[0].trim(), score, Math::addExact);
        } catch (NumberFormatException exception) {
            throw new IOException("Invalid score entry: " + line, exception);
        }
    }
}
