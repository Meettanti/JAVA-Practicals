import java.util.Objects;

public class GenericBox<T> {
    private T value;

    public GenericBox(T value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = Objects.requireNonNull(value, "value");
    }
}
