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
            Budget testBudget = reader.read();
            fail("IOException expected");
        } catch (IOException exception) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBudget() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBudget.json");
        try {
            Budget testBudget = reader.read();
            assertEquals("test budget", testBudget.getBudgetName());
            assertEquals(0, testBudget.getBudgetCategories().size());
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBudget.json");
        try {
            Budget testBudget = reader.read();
            assertEquals("test budget", testBudget.getBudgetName());
            BigDecimal testBudgetAmount = new BigDecimal("0.00");
            assertEquals(testBudgetAmount, testBudget.getBudgetAmount());
            List<Category> budgetCategories = testBudget.getBudgetCategories();
            assertEquals(1, budgetCategories.size());
            BigDecimal zero = new BigDecimal("0.00");
            for (Category testCategory : testBudget.getBudgetCategories()) {
                checkCategory(testCategory.getCategoryName(), zero, testCategory.getCategoryTransactions(),
                        testCategory);
                for (Transaction testTransaction : testCategory.getCategoryTransactions()) {
                    checkTransaction(testTransaction.getTransactionName(), zero, testTransaction.getTransactionDate(),
                            testTransaction);
                }
            }
        } catch (IOException exception) {
            fail("Could not read from file");
        }
    }
}
