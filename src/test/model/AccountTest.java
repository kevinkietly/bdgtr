package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account testAccount;
    private Budget testBudget;
    private Category testCategory;
    private Transaction testTransaction;

    @BeforeEach
    void runBefore() {
        testAccount = new Account("test username", "test password");
        testBudget = new Budget("test budget", new BigDecimal("1000.00"));
        testCategory = new Category("test category");
        testTransaction = new Transaction("test tuition", new BigDecimal("500.00"), "1-1-2021");
        testCategory.addTransaction(testTransaction);
        testBudget.addCategory(testCategory);
    }

    @Test
    void testConstructor() {
        assertEquals("test username", testAccount.getUsername());
        assertEquals("test password", testAccount.getPassword());
        assertEquals(0, testAccount.getBudgets().size());
    }

    @Test
    void testAddBudget() {
        assertTrue(testAccount.addBudget(testBudget));
        assertEquals(1, testAccount.getBudgets().size());
        assertTrue(testAccount.getBudgets().contains(testBudget));

        Budget anotherTestBudget = new Budget("another test budget", new BigDecimal("1000.00"));
        assertTrue(testAccount.addBudget(anotherTestBudget));
        assertEquals(2, testAccount.getBudgets().size());
        assertTrue(testAccount.getBudgets().contains(anotherTestBudget));

    }

    @Test
    void testAddBudgetAlreadyInAccount() {
        assertTrue(testAccount.addBudget(testBudget));
        assertEquals(1, testAccount.getBudgets().size());
        assertTrue(testAccount.getBudgets().contains(testBudget));

        assertFalse(testAccount.addBudget(testBudget));
        assertEquals(1, testAccount.getBudgets().size());
        assertTrue(testAccount.getBudgets().contains(testBudget));
    }

    @Test
    void testRemoveBudget() {
        testAccount.addBudget(testBudget);
        testAccount.removeBudget(testBudget);
        assertEquals(0, testAccount.getBudgets().size());
        assertFalse(testAccount.getBudgets().contains(testBudget));
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("account username", "test username");
        testJson.put("account password", "test password");
        JSONArray testJsonArray = new JSONArray();
        for (Budget nextBudget : testAccount.getBudgets()) {
            testJsonArray.put(nextBudget.toJson());
        }
        testJson.put("budgets", testJsonArray);
        assertEquals(testJson.toString(), testAccount.toJson().toString());
    }

    @Test
    void testBudgetsToJson() {
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testBudget.toJson());
        testAccount.addBudget(testBudget);
        assertEquals(testJsonArray.toString(), testAccount.budgetsToJson().toString());
    }

}
