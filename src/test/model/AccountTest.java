package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Account class.
 */
class AccountTest {
    private Account testAccount;
    private Budget testBudget;
    private Budget anotherTestBudget;

    @BeforeEach
    void runBefore() throws EmptyNameException, NegativeAmountException, DuplicateCategoryException,
            NegativeCostException {
        try {
            testAccount = new Account("Test Username", "Test Password");
            testBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
            anotherTestBudget = new Budget("Another Test Budget", new BigDecimal("1000.00"));
            Category testCategory = new Category("Test Category");
            Category anotherTestCategory = new Category("Another Test Category");
            Transaction testTransaction = new Transaction("Test Transaction", new BigDecimal("500.00"),
                    "January 1, 2021");
            Transaction anotherTestTransaction = new Transaction("Another Test Transaction",
                    new BigDecimal("500.00"), "January 1, 2021");
            testCategory.addTransaction(testTransaction);
            anotherTestCategory.addTransaction(anotherTestTransaction);
            testBudget.addCategory(testCategory);
            anotherTestBudget.addCategory(anotherTestCategory);
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test Username", testAccount.getUsername());
        assertEquals("Test Password", testAccount.getPassword());
        assertEquals(0, testAccount.getBudgets().size());
    }

    @Test
    void testConstructorEmptyUsernameException() {
        try {
            Account anotherTestAccount = new Account("", "Test Password");
            fail("EmptyUsernameException should have been thrown.");
        } catch (EmptyUsernameException exception) {
            /* Expected. */
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructorEmptyPasswordException() {
        try {
            Account anotherTestAccount = new Account("Test Username", "");
            fail("EmptyPasswordException should have been thrown.");
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            /* Expected. */
        }
    }

    @Test
    void testAddBudget() {
        try {
            testAccount.addBudget(testBudget);
            assertEquals(1, testAccount.getBudgets().size());
            assertTrue(testAccount.getBudgets().contains(testBudget));
            testAccount.addBudget(anotherTestBudget);
            assertEquals(2, testAccount.getBudgets().size());
            assertTrue(testAccount.getBudgets().contains(anotherTestBudget));
        } catch (DuplicateBudgetException exception) {
            fail("DuplicateBudgetException should not have been thrown.");
        }
    }

    @Test
    void testAddDuplicateBudget() {
        testAddBudget();
        try {
            testAccount.addBudget(testBudget);
            fail("DuplicateBudgetException should have been thrown.");
        } catch (DuplicateBudgetException exception) {
            /* Expected. */
        }
    }

    @Test
    void testDeleteBudget() {
        testAddBudget();
        try {
            testAccount.deleteBudget(testBudget);
            assertEquals(1, testAccount.getBudgets().size());
            assertFalse(testAccount.getBudgets().contains(testBudget));
            testAccount.deleteBudget(anotherTestBudget);
            assertEquals(0, testAccount.getBudgets().size());
            assertFalse(testAccount.getBudgets().contains(anotherTestBudget));
        } catch (NonexistentBudgetException exception) {
            fail("NonexistentBudgetException should not have been thrown.");
        } catch (EmptyBudgetsException exception) {
            fail("EmptyBudgetsException should not have been thrown.");
        }
    }

    @Test
    void testDeleteNonexistentBudget() throws EmptyNameException, NegativeAmountException {
        testAddBudget();
        try {
            testAccount.deleteBudget(new Budget("Nonexistent Test Budget", new BigDecimal("1000.00")));
            fail("NonexistentBudgetException should have been thrown.");
        } catch (NonexistentBudgetException exception) {
            /* Expected. */
        } catch (EmptyBudgetsException exception) {
            fail("EmptyBudgetsException should not have been thrown.");
        }
    }

    @Test
    void testDeleteBudgetEmptyBudgets() {
        try {
            testAccount.deleteBudget(testBudget);
            fail("EmptyBudgetException should have been thrown.");
        } catch (NonexistentBudgetException exception) {
            fail("NonexistentBudgetException should not have been thrown.");
        } catch (EmptyBudgetsException exception) {
            /* Expected. */
        }
    }

    @Test
    void testToJson() {
        testAddBudget();
        JSONObject testJson = new JSONObject();
        testJson.put("username", testAccount.getUsername());
        testJson.put("password", testAccount.getPassword());
        JSONArray testJsonArray = new JSONArray();
        for (Budget nextBudget : testAccount.getBudgets()) {
            testJsonArray.put(nextBudget.toJson());
        }
        testJson.put("budgets", testJsonArray);
        assertEquals(testJson.toString(), testAccount.toJson().toString());
    }

    @Test
    void testBudgetsToJson() {
        testAddBudget();
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testBudget.toJson());
        testJsonArray.put(anotherTestBudget.toJson());
        assertEquals(testJsonArray.toString(), testAccount.budgetsToJson().toString());
    }
}
