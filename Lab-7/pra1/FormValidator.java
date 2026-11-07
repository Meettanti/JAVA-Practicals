import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FormValidator {
    public static List<String> validate(Object form) throws IllegalAccessException {
        if (form == null) {
            throw new IllegalArgumentException("Form cannot be null.");
        }

        List<String> errors = new ArrayList<>();
        for (Field field : form.getClass().getDeclaredFields()) {
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (notBlank == null && maxLength == null) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(form);
            if (notBlank != null) {
                if (!(value instanceof CharSequence text)) {
                    if (value == null) {
                        errors.add(field.getName() + " must not be blank.");
                    } else {
                        throw new IllegalArgumentException(
                                "@NotBlank requires a text field: " + field.getName());
                    }
                } else if (text.toString().isBlank()) {
                    errors.add(field.getName() + " must not be blank.");
                }
            }

            if (maxLength != null) {
                if (maxLength.value() < 0) {
                    throw new IllegalArgumentException(
                            "@MaxLength cannot be negative: " + field.getName());
                }
                if (value != null && !(value instanceof CharSequence)) {
                    throw new IllegalArgumentException(
                            "@MaxLength requires a text field: " + field.getName());
                }
                if (value instanceof CharSequence text && text.length() > maxLength.value()) {
                    errors.add(field.getName() + " must have at most "
                            + maxLength.value() + " characters.");
                }
            }
        }
        return errors;
    }
}
