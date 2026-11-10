package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CommandParserTest {
    @Test
    void parsesCaseInsensitiveTransactionCommands() {
        assertEquals(new Command(TransactionType.DEPOSIT, "AC0001", 250),
                CommandParser.parse("deposit AC0001 250"));
        assertEquals(new Command(TransactionType.TRANSFER, "AC0002", 75),
                CommandParser.parse("TRANSFER AC0002 75"));
    }

    @Test
    void rejectsMalformedAndNonPositiveAmounts() {
        assertThrows(IllegalArgumentException.class, () -> CommandParser.parse(null));
        assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("DEPOSIT AC0001"));
        assertThrows(IllegalArgumentException.class,
                () -> CommandParser.parse("DEPOSIT AC0001 0"));
        assertThrows(IllegalArgumentException.class,
                () -> CommandParser.parse("DEPOSIT AC0001 999999999999999999999999"));
    }
}
