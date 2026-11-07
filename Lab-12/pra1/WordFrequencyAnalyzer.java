import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WordFrequencyAnalyzer {
    public static void main(String[] args) throws IOException {
        Path file = args.length == 0 ? Path.of("sample-text.txt") : Path.of(args[0]);
        String text = Files.readString(file).toLowerCase(Locale.ROOT);
        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : text.split("[^\\p{L}\\p{N}']+")) {
            if (!word.isBlank()) {
                frequencies.merge(word, 1, Math::addExact);
            }
        }

        List<Map.Entry<String, Integer>> rankedWords = new ArrayList<>(frequencies.entrySet());
        rankedWords.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey));
        System.out.println("Word frequencies for " + file.toAbsolutePath().normalize());
        rankedWords.forEach(entry ->
                System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
