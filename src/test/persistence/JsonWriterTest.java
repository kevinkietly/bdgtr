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
    Account testAccount;
    Budget testBudget;
    Budget anotherTestBudget;
    Category testCategory;
    Category anotherTestCategory;

    @BeforeEach
    void runBefore() {
        testAccount = new Account("test username", "test password");
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

            JsonReader reader = new JsonReader("./data/testWriterEmptyAccount.json");
            testAccount = reader.read();
            assertEquals(0, testAccount.getBudgets().size());
        } catch (IOException exception) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBudgets() {
        try {
            testAccount.addBudget(testBudget);
            testAccount.addBudget(anotherTestBudget);
            executeWriting();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAccount.json");
            testAccount = reader.read();
            for (Budget testBudget : testAccount.getBudgets()) {
                checkBudget(testBudget.getName(), new BigDecimal("0.00"), testBudget.getCategories(),
                        testBudget);
                for (Category testCategory : testBudget.getCategories()) {
                    checkCategory(testCategory.getName(), new BigDecimal("0.00"),
                            testCategory.getTransactions(), testCategory);
                    for (Transaction testTransaction : testCategory.getTransactions()) {
                        checkTransaction(testTransaction.getName(), new BigDecimal("0.00"),
                                testTransaction.getDate(), testTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("Exception should not have been thrown");
        }
    }

    void executeWriting() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralAccount.json");
        writer.open();
        writer.write(testAccount);
        writer.close();
    }
}
