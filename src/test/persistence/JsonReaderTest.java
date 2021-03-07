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
            List<Budget> testBudgets = reader.read();
            fail("IOException expected");
        } catch (IOException exception) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBudgets() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBudgets.json");
        try {
            List<Budget> testBudgets = reader.read();
            assertEquals(0, testBudgets.size());
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBudgets.json");
        try {
            List<Budget> testBudgets = reader.read();
            BigDecimal zero = new BigDecimal("0.00");
            for (Budget testBudget : testBudgets) {
                checkBudget(testBudget.getBudgetName(), zero, testBudget.getBudgetCategories(), testBudget);
                for (Category testCategory : testBudget.getBudgetCategories()) {
                    checkCategory(testCategory.getCategoryName(), zero, testCategory.getCategoryTransactions(),
                            testCategory);
                    for (Transaction testTransaction : testCategory.getCategoryTransactions()) {
                        checkTransaction(testTransaction.getTransactionName(), zero,
                                testTransaction.getTransactionDate(), testTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }
}
