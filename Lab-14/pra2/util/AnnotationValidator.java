package util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {
    public static String[] validate(Object object) throws IllegalAccessException {
        if (object == null) {
            throw new IllegalArgumentException("Object to validate cannot be null.");
        }

        List<String> errors = new ArrayList<>();
        for (Class<?> type = object.getClass(); type != null && type != Object.class;
                type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                Positive positive = field.getAnnotation(Positive.class);
                MaxLength maxLength = field.getAnnotation(MaxLength.class);
                if (positive == null && maxLength == null) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(object);
                if (positive != null && value != null) {
                    if (!(value instanceof Number number)) {
                        throw new IllegalArgumentException(
                                "@Positive requires a numeric field: " + field.getName());
                    }
                    if (!isPositive(number)) {
                        errors.add(positive.message());
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
                        errors.add("must have at most " + maxLength.value() + " characters");
                    }
                }
            }
        }
        return errors.toArray(String[]::new);
    }

    private static boolean isPositive(Number number) {
        if (number instanceof BigDecimal decimal) {
            return decimal.compareTo(BigDecimal.ZERO) > 0;
        }
        if (number instanceof BigInteger integer) {
            return integer.signum() > 0;
        }
        return number.doubleValue() > 0.0;
    }
}
