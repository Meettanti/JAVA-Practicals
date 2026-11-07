package util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.SavingsAccount;
import org.junit.jupiter.api.Test;

class AnnotationValidatorTest {
    @Test
    void validatesInheritedAccountFields() throws Exception {
        SavingsAccount valid = new SavingsAccount("Valid", 100, 0);
        SavingsAccount invalid = new SavingsAccount("X".repeat(41), -1, 0);

        assertArrayEquals(new String[0], AnnotationValidator.validate(valid));
        String[] errors = AnnotationValidator.validate(invalid);
        if (errors.length != 2) {
            throw new AssertionError("Expected name and balance validation errors.");
        }
    }

    @Test
    void rejectsNullObjects() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationValidator.validate(null));
    }
}
