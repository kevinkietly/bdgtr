package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    List<Budget> testBudgets;
    Budget testBudget;
    Budget anotherTestBudget;
    Category testCategory;
    Category anotherTestCategory;

    @BeforeEach
    void runBefore() {
        testBudgets = new ArrayList<>();
        testBudget = new Budget("test budget", new BigDecimal("0.00"));
        anotherTestBudget = new Budget("another test budget", new BigDecimal("0.00"));
        testCategory = new Category("test category");
        anotherTestCategory = new Category("another test category");
        testCategory.addTransaction(new Transaction("test transaction", new BigDecimal("0.00"),
                "1-1-2021"));
        anotherTestCategory.addTransaction(new Transaction("another test transaction",
                new BigDecimal("0.00"), "1-2-2021"));
        testBudget.addCategory(testCategory);
        anotherTestBudget.addCategory(anotherTestCategory);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException exception) {
            // pass
        }
    }

    @Test
    void testWriterEmptyBudgets() {
        try {
            executeWriting();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBudgets.json");
            testBudgets = reader.read();
            assertEquals(0, testBudgets.size());
        } catch (IOException exception) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBudgets() {
        try {
            testBudgets.add(testBudget);
            testBudgets.add(anotherTestBudget);
            executeWriting();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBudgets.json");
            testBudgets = reader.read();
            for (Budget testBudget : testBudgets) {
                checkBudget(testBudget.getBudgetName(), new BigDecimal("0.00"), testBudget.getBudgetCategories(),
                        testBudget);
                for (Category testCategory : testBudget.getBudgetCategories()) {
                    checkCategory(testCategory.getCategoryName(), new BigDecimal("0.00"),
                            testCategory.getCategoryTransactions(), testCategory);
                    for (Transaction testTransaction : testCategory.getCategoryTransactions()) {
                        checkTransaction(testTransaction.getTransactionName(), new BigDecimal("0.00"),
                                testTransaction.getTransactionDate(), testTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("Exception should not have been thrown");
        }
    }

    void executeWriting() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralBudgets.json");
        writer.open();
        writer.write(testBudgets);
        writer.close();
    }
}
