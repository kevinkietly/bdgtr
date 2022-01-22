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
    void runBefore() throws EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateCategoryException {
        try {
            testAccount = new Account("Test First Name", "Test Last Name", "Test Username",
                    "Test Password");
            testBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
            anotherTestBudget = new Budget("Another Test Budget", new BigDecimal("1000.00"));
            Category testCategory = new Category("Test Category");
            Transaction testTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                    "January 1, 2021");
            testCategory.addTransaction(testTransaction);
            testBudget.addCategory(testCategory);
            anotherTestBudget.addCategory(testCategory);
        } catch (EmptyFirstNameException exception) {
            fail("EmptyFirstNameException should not have been thrown.");
        } catch (EmptyLastNameException exception) {
            fail("EmptyLastNameException should not have been thrown.");
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test First Name", testAccount.getFirstName());
        assertEquals("Test Last Name", testAccount.getLastName());
        assertEquals("Test Username", testAccount.getUsername());
        assertEquals("Test Password", testAccount.getPassword());
        assertEquals(0, testAccount.getBudgets().size());
    }

    @Test
    void testConstructorEmptyFirstNameException() {
        try {
            testAccount = new Account("", "Test Last Name", "Test Username",
                    "Test Password");
            fail("EmptyFirstNameException should have been thrown.");
        } catch (EmptyFirstNameException exception) {
            /* Expected. */
        } catch (EmptyLastNameException exception) {
            fail("EmptyLastNameException should not have been thrown.");
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructorEmptyLastNameException() {
        try {
            testAccount = new Account("Test First Name", "", "Test Username",
                    "Test Password");
            fail("EmptyLastNameException should have been thrown.");
        } catch (EmptyFirstNameException exception) {
            fail("EmptyFirstNameException should not have been thrown.");
        } catch (EmptyLastNameException exception) {
            /* Expected. */
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructorEmptyUsernameException() {
        try {
            testAccount = new Account("Test First Name", "Test Last Name", "",
                    "Test Password");
            fail("EmptyUsernameException should have been thrown.");
        } catch (EmptyFirstNameException exception) {
            fail("EmptyFirstNameException should not have been thrown.");
        } catch (EmptyLastNameException exception) {
            fail("EmptyLastNameException should not have been thrown.");
        } catch (EmptyUsernameException exception) {
            /* Expected. */
        } catch (EmptyPasswordException exception) {
            fail("EmptyPasswordException should not have been thrown.");
        }
    }

    @Test
    void testConstructorEmptyPasswordException() {
        try {
            testAccount = new Account("Test First Name", "Test Last Name", "Test Username",
                    "");
            fail("EmptyPasswordException should have been thrown.");
        } catch (EmptyFirstNameException exception) {
            fail("EmptyFirstNameException should not have been thrown.");
        } catch (EmptyLastNameException exception) {
            fail("EmptyLastNameException should not have been thrown.");
        } catch (EmptyUsernameException exception) {
            fail("EmptyUsernameException should not have been thrown.");
        } catch (EmptyPasswordException exception) {
            /* Expected. */
        }
    }

    @Test
    void testSetFirstName() {
        testAccount.setFirstName("Another Test First Name");
        assertEquals("Another Test First Name", testAccount.getFirstName());
    }

    @Test
    void testSetLastName() {
        testAccount.setLastName("Another Test Last Name");
        assertEquals("Another Test Last Name", testAccount.getLastName());
    }

    @Test
    void testSetUsername() {
        testAccount.setUsername("Another Test Username");
        assertEquals("Another Test Username", testAccount.getUsername());
    }

    @Test
    void testSetPassword() {
        testAccount.setPassword("Another Test Password");
        assertEquals("Another Test Password", testAccount.getPassword());
    }

    @Test
    void testAddBudget() {
        try {
            testAccount.addBudget(testBudget);
            assertEquals(1, testAccount.getBudgets().size());
            assertTrue(testAccount.getBudgets().contains(testBudget));
        } catch (DuplicateBudgetException exception) {
            fail("DuplicateBudgetException should not have been thrown.");
        }
    }

    @Test
    void testAddDuplicateBudget() {
        try {
            testAccount.addBudget(testBudget);
            testAccount.addBudget(testBudget);
            fail("DuplicateBudgetException should have been thrown.");
        } catch (DuplicateBudgetException exception) {
            /* Expected. */
        }
    }

    @Test
    void testDeleteBudget() throws DuplicateBudgetException {
        testAccount.addBudget(testBudget);
        testAccount.addBudget(anotherTestBudget);
        testAccount.deleteBudget(anotherTestBudget);
        assertEquals(1, testAccount.getBudgets().size());
        assertFalse(testAccount.getBudgets().contains(anotherTestBudget));
    }

    @Test
    void testBudgetsToJson() throws DuplicateBudgetException {
        testAccount.addBudget(testBudget);
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testBudget.toJson());
        assertEquals(testJsonArray.toString(), testAccount.budgetsToJson().toString());
    }

    @Test
    void testToJson() {
        JSONObject testJsonObject = new JSONObject();
        testJsonObject.put("firstName", testAccount.getFirstName());
        testJsonObject.put("lastName", testAccount.getLastName());
        testJsonObject.put("username", testAccount.getUsername());
        testJsonObject.put("password", testAccount.getPassword());
        JSONArray testJsonArray = new JSONArray();
        for (Budget nextBudget : testAccount.getBudgets()) {
            testJsonArray.put(nextBudget.toJson());
        }
        testJsonObject.put("budgets", testJsonArray);
        assertEquals(testJsonObject.toString(), testAccount.toJson().toString());
    }

    @Test
    void testEquals() throws EmptyFirstNameException, EmptyLastNameException, EmptyUsernameException,
            EmptyPasswordException, DuplicateBudgetException {
        Account sameTestAccount = new Account("Test First Name", "Test Last Name",
                "Test Username", "Test Password");
        Account anotherTestAccount = new Account("Another First Name", "Another Last Name",
                "Another Test Username", "Another Test Password");
        testAccount.addBudget(testBudget);
        sameTestAccount.addBudget(testBudget);
        assertTrue(testAccount.equals(testAccount));
        assertFalse(testAccount.equals(null));
        assertFalse(testAccount.equals(testBudget));
        assertTrue(testAccount.equals(sameTestAccount));
        assertFalse(testAccount.equals(anotherTestAccount));
        assertEquals(sameTestAccount.hashCode(), testAccount.hashCode());
        assertNotEquals(anotherTestAccount.hashCode(), testAccount.hashCode());

        sameTestAccount.deleteBudget(testBudget);
        sameTestAccount.addBudget(anotherTestBudget);
        assertFalse(testAccount.equals(sameTestAccount));
    }
}
