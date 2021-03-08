package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code referenced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonexistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account testAccount = reader.read();
            fail("IOException expected");
        } catch (IOException exception) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBudgets() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAccount.json");
        try {
            Account testAccount = reader.read();
            assertEquals(0, testAccount.getBudgets().size());
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAccount.json");
        try {
            Account testAccount = reader.read();
            BigDecimal zero = new BigDecimal("0.00");
            for (Budget testBudget : testAccount.getBudgets()) {
                checkBudget(testBudget.getName(), zero, testBudget.getCategories(), testBudget);
                for (Category testCategory : testBudget.getCategories()) {
                    checkCategory(testCategory.getName(), zero, testCategory.getTransactions(),
                            testCategory);
                    for (Transaction testTransaction : testCategory.getTransactions()) {
                        checkTransaction(testTransaction.getName(), zero,
                                testTransaction.getDate(), testTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }
}
