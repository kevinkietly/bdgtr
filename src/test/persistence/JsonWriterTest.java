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
    private Account anotherTestAccount;
    private Budget testBudget;
    private Budget anotherTestBudget;

    @BeforeEach
    void runBefore() throws EmptyUsernameException,
            EmptyPasswordException, EmptyNameException, NegativeAmountException, DuplicateCategoryException,
            NegativeCostException {
        testAccount = new Account("Test Username", "Test Password");
        anotherTestAccount = new Account("Another Test Username", "Another Test Password");
        testBudget = new Budget("Test Budget", new BigDecimal("0.00"));
        anotherTestBudget = new Budget("Another Test Budget", new BigDecimal("0.00"));
        Category testCategory = new Category("Test Category");
        Category anotherTestCategory = new Category("Another Test Category");
        Transaction testTransaction = new Transaction("Test Transaction", new BigDecimal("0.00"),
                "January 1, 2021");
        Transaction anotherTestTransaction = new Transaction("Another Test Transaction",
                new BigDecimal("0.00"), "January 1, 2021");
        testCategory.addTransaction(testTransaction);
        anotherTestCategory.addTransaction(anotherTestTransaction);
        testBudget.addCategory(testCategory);
        anotherTestBudget.addCategory(anotherTestCategory);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter testJsonWriter = new JsonWriter("./data/\0invalidFile.json");
            testJsonWriter.open();
            fail("IOException should have been thrown.");
        } catch (IOException exception) {
            /* Expected. */
        }
    }

    @Test
    void testWriterEmptyAccounts() throws EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        try {
            executeWriting(testAccount, "./data/testWriterEmptyAccounts.json");
            executeWriting(anotherTestAccount, "./data/testWriterEmptyAccounts.json");
            JsonReader testJsonReader = new JsonReader("./data/testWriterEmptyAccounts.json");
            testAccount = testJsonReader.read("Test Username");
            checkAccount("Test Username", "Test Password", testAccount);
            assertEquals(0, testAccount.getBudgets().size());
            anotherTestAccount = testJsonReader.read("Another Test Username");
            checkAccount("Another Test Username", "Another Test Password", anotherTestAccount);
            assertEquals(0, anotherTestAccount.getBudgets().size());
        } catch (IOException exception) {
            fail("IOException should not have been thrown.");
        }
    }

    @Test
    void testWriterGeneralAccounts() throws EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        try {
            testAccount.addBudget(testBudget);
            anotherTestAccount.addBudget(anotherTestBudget);
            executeWriting(testAccount, "./data/testWriterGeneralAccounts.json");
            executeWriting(anotherTestAccount, "./data/testWriterGeneralAccounts.json");
            JsonReader testJsonReader = new JsonReader("./data/testWriterGeneralAccounts.json");
            testAccount = testJsonReader.read("Test Username");
            checkAccount("Test Username", "Test Password", testAccount);
            assertEquals(1, testAccount.getBudgets().size());
            anotherTestAccount = testJsonReader.read("Another Test Username");
            checkAccount("Another Test Username", "Another Test Password", anotherTestAccount);
            assertEquals(1, anotherTestAccount.getBudgets().size());
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
