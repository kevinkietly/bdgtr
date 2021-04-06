package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Category class.
 */
class CategoryTest {
    private Category testCategory;
    private Transaction testTransaction;
    private Transaction anotherTestTransaction;

    @BeforeEach
    void runBefore() throws NegativeAmountException, ZeroAmountException {
        try {
            testCategory = new Category("Test Category");
            testTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                    "January 1, 2021");
            anotherTestTransaction = new Transaction("Another Test Transaction", new BigDecimal("200.00"),
                    "January 2, 2021");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test Category", testCategory.getName());
        assertEquals(BigDecimal.ZERO, testCategory.getAmountSpent());
        assertEquals(0, testCategory.getTransactions().size());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            testCategory = new Category("");
            fail("EmptyNameException should have been thrown.");
        } catch (EmptyNameException exception) {
            /* Expected. */
        }
    }

    @Test
    void testAddTransaction() {
        testCategory.addTransaction(testTransaction);
        assertEquals(1, testCategory.getTransactions().size());
        assertTrue(testCategory.getTransactions().contains(testTransaction));
        assertEquals(new BigDecimal("100.00"), testCategory.getAmountSpent());
    }

    @Test
    void testDeleteTransaction() {
        testCategory.addTransaction(testTransaction);
        testCategory.addTransaction(anotherTestTransaction);
        testCategory.deleteTransaction(anotherTestTransaction);
        assertEquals(1, testCategory.getTransactions().size());
        assertFalse(testCategory.getTransactions().contains(anotherTestTransaction));
        assertEquals(new BigDecimal("100.00"), testCategory.getAmountSpent());
    }

    @Test
    void testTransactionsToJson() {
        testCategory.addTransaction(testTransaction);
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testTransaction.toJson());
        assertEquals(testJsonArray.toString(), testCategory.transactionsToJson().toString());
    }

    @Test
    void testToJson() {
        JSONObject testJsonObject = new JSONObject();
        testJsonObject.put("name", testCategory.getName());
        testJsonObject.put("amountSpent", testCategory.getAmountSpent().toString());
        JSONArray testJsonArray = new JSONArray();
        for (Transaction nextTransaction : testCategory.getTransactions()) {
            testJsonArray.put(nextTransaction.toJson());
        }
        testJsonObject.put("transactions", testJsonArray);
        assertEquals(testJsonObject.toString(), testCategory.toJson().toString());
    }

    @Test
    void testEquals() throws EmptyNameException {
        Category sameTestCategory = new Category("Test Category");
        Category anotherTestCategory = new Category("Another Test Category");
        testCategory.addTransaction(testTransaction);
        sameTestCategory.addTransaction(testTransaction);
        assertTrue(testCategory.equals(testCategory));
        assertFalse(testCategory.equals(null));
        assertFalse(testCategory.equals(testTransaction));
        assertTrue(testCategory.equals(sameTestCategory));
        assertFalse(testCategory.equals(anotherTestCategory));
        assertEquals(sameTestCategory.hashCode(), testCategory.hashCode());
        assertNotEquals(anotherTestCategory.hashCode(), testCategory.hashCode());
    }
}
