package persistence;

import model.*;
import model.exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JsonReader class.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonexistentFile() throws EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        JsonReader testJsonReader = new JsonReader("./data/nonexistentFile.json");
        try {
            Account testAccount = testJsonReader.read("Test Username");
            fail("IOException should have been thrown.");
        } catch (IOException exception) {
            /* Expected. */
        }
    }

    @Test
    void testReaderEmptyAccount() throws EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        JsonReader testJsonReader = new JsonReader("./data/testReaderEmptyAccount.json");
        try {
            Account testAccount = testJsonReader.read("Test Username");
            checkAccount("Test Username", "Test Password", testAccount);
        } catch (IOException exception) {
            fail("Unable to read from the source file.");
        }
    }

    @Test
    void testReaderGeneralAccount() throws EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        JsonReader testJsonReader = new JsonReader("./data/testReaderGeneralAccount.json");
        try {
            Account testAccount = testJsonReader.read("Test Username");
            checkAccount("Test Username", "Test Password", testAccount);
            assertEquals(1, testAccount.getBudgets().size());
            for (Budget nextBudget : testAccount.getBudgets()) {
                checkBudget("Test Budget", new BigDecimal("0.00"), nextBudget);
                assertEquals(1, nextBudget.getCategories().size());
                for (Category nextCategory : nextBudget.getCategories()) {
                    checkCategory("Test Category", new BigDecimal("0.00"), nextCategory);
                    assertEquals(1, nextCategory.getTransactions().size());
                    for (Transaction nextTransaction : nextCategory.getTransactions()) {
                        checkTransaction("Test Transaction", new BigDecimal("0.00"), "January 1, 2021",
                                nextTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("Unable to read from the source file.");
        }
    }
}
