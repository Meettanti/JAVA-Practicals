package util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Customer;
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

    @Test
    void validatesCustomerFieldLengthsAndContactFormats() throws Exception {
        Customer valid = new Customer("Valid", "valid@example.com", "9876543210");
        Customer overlongName = new Customer("X".repeat(41), "valid@example.com", "9876543210");

        assertArrayEquals(new String[0], AnnotationValidator.validate(valid));
        assertArrayEquals(new String[] {"must have at most 40 characters"},
                AnnotationValidator.validate(overlongName));
        assertTrue(Validator.isValidEmail(valid.getEmail()));
        assertTrue(Validator.isValidMobile(valid.getMobile()));
        assertFalse(Validator.isValidEmail("not-an-email"));
        assertFalse(Validator.isValidMobile("1234567890"));
    }
}
