package persistence;

import model.*;
import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JsonWriter class.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
class JsonWriterTest extends JsonTest {
    private Account testAccount;
    private Budget testBudget;

    @BeforeEach
    void runBefore() throws EmptyFirstNameException, EmptyLastNameException, EmptyUsernameException,
            EmptyPasswordException, EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateCategoryException {
        testAccount = new Account("Test First Name", "Test Last Name", "Test Username",
                "Test Password");
        testBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
        Category testCategory = new Category("Test Category");
        Transaction testTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                "January 1, 2021");
        testBudget.setStartDate("January 1, 2021");
        testBudget.addCategory(testCategory);
        testCategory.addTransaction(testTransaction);
        testBudget.calculateAmountRemaining();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            executeWriting(testAccount, "./data/\0invalidFile.json");
            fail("IOException should have been thrown.");
        } catch (IOException exception) {
            /* Expected. */
        }
    }

    @Test
    void testWriterEmptyAccounts() throws EmptyFirstNameException, EmptyLastNameException, EmptyUsernameException,
            EmptyPasswordException, EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateBudgetException, DuplicateCategoryException {
        try {
            executeWriting(testAccount, "./data/testWriterEmptyAccounts.json");
            JsonReader testJsonReader = new JsonReader("./data/testWriterEmptyAccounts.json");
            testAccount = testJsonReader.read("Test Username");
            checkAccount("Test First Name", "Test Last Name", "Test Username",
                    "Test Password", testAccount);
            assertEquals(0, testAccount.getBudgets().size());
        } catch (IOException exception) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterGeneralAccounts() throws EmptyFirstNameException, EmptyLastNameException, EmptyUsernameException,
            EmptyPasswordException, EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateBudgetException, DuplicateCategoryException {
        try {
            testAccount.addBudget(testBudget);
            executeWriting(testAccount, "./data/testWriterGeneralAccounts.json");
            JsonReader testJsonReader = new JsonReader("./data/testWriterGeneralAccounts.json");
            testAccount = testJsonReader.read("Test Username");
            checkAccount("Test First Name", "Test Last Name", "Test Username",
                    "Test Password", testAccount);
            assertEquals(1, testAccount.getBudgets().size());
            for (Budget nextBudget : testAccount.getBudgets()) {
                checkBudget("Test Budget", new BigDecimal("1000.00"), new BigDecimal("100.00"),
                        new BigDecimal("900.00"), "January 1, 2021", nextBudget);
                assertEquals(1, nextBudget.getCategories().size());
                for (Category nextCategory : nextBudget.getCategories()) {
                    checkCategory("Test Category", new BigDecimal("100.00"), nextCategory);
                    assertEquals(1, nextCategory.getTransactions().size());
                    for (Transaction nextTransaction : nextCategory.getTransactions()) {
                        checkTransaction("Test Transaction", new BigDecimal("100.00"),
                                "January 1, 2021", nextTransaction);
                    }
                }
            }
        } catch (IOException exception) {
            fail("IOException should not have been thrown.");
        }
    }

    void executeWriting(Account account, String destination) throws IOException {
        JsonWriter testJsonWriter = new JsonWriter(destination);
        testJsonWriter.open();
        testJsonWriter.write(account);
        testJsonWriter.close();
    }
}
