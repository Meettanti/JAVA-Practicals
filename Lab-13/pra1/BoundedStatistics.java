import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BoundedStatistics<T extends Number & Comparable<T>> {
    public Summary<T> summarize(List<T> values) {
        Objects.requireNonNull(values, "values");
        if (values.isEmpty()) {
            throw new IllegalArgumentException("At least one value is required.");
        }

        List<T> sorted = new ArrayList<>(values);
        double sum = 0;
        for (T value : sorted) {
            Objects.requireNonNull(value, "value");
            double number = value.doubleValue();
            if (!Double.isFinite(number)) {
                throw new IllegalArgumentException("Statistics require finite numbers.");
            }
            sum += number;
            if (!Double.isFinite(sum)) {
                throw new IllegalArgumentException("The sum exceeds the supported range.");
            }
        }
        sorted.sort(Comparator.naturalOrder());

        int size = sorted.size();
        double median = size % 2 == 1
                ? sorted.get(size / 2).doubleValue()
                : sorted.get(size / 2 - 1).doubleValue() / 2.0
                        + sorted.get(size / 2).doubleValue() / 2.0;
        return new Summary<>(size, sorted.get(0), sorted.get(size - 1),
                sum / size, median);
    }

    public record Summary<T>(long count, T minimum, T maximum, double mean, double median) {
    }
}
