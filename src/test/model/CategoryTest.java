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
    void runBefore() throws NegativeCostException {
        try {
            testCategory = new Category("Test Category");
            testTransaction = new Transaction("Test Transaction", new BigDecimal("500.00"),
                    "January 1, 2021");
            anotherTestTransaction = new Transaction("Another Test Transaction", new BigDecimal("500.00"),
                    "January 1, 2021");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test Category", testCategory.getName());
        assertEquals(new BigDecimal("0.00"), testCategory.getAmountSpent());
        assertEquals(0, testCategory.getTransactions().size());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            Category anotherTestCategory = new Category("");
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
        testCategory.addTransaction(anotherTestTransaction);
        assertEquals(2, testCategory.getTransactions().size());
        assertTrue(testCategory.getTransactions().contains(anotherTestTransaction));
        assertEquals(new BigDecimal("1000.00"), testCategory.getAmountSpent());
    }

    @Test
    void testDeleteTransaction() {
        testAddTransaction();
        try {
            testCategory.deleteTransaction(testTransaction);
            assertEquals(1, testCategory.getTransactions().size());
            assertFalse(testCategory.getTransactions().contains(testTransaction));
            testCategory.deleteTransaction(anotherTestTransaction);
            assertEquals(0, testCategory.getTransactions().size());
            assertFalse(testCategory.getTransactions().contains(anotherTestTransaction));
            assertEquals(new BigDecimal("0.00"), testCategory.getAmountSpent());
        } catch (NonexistentTransactionException exception) {
            fail("NonexistentTransactionException should not have been thrown.");
        } catch (EmptyTransactionsException exception) {
            fail("EmptyTransactionsException should not have been thrown.");
        }
    }

    @Test
    void testDeleteNonexistentTransaction() throws EmptyNameException, NegativeCostException {
        testAddTransaction();
        try {
            testCategory.deleteTransaction(new Transaction("Nonexistent Test Transaction",
                    new BigDecimal("500.00"), "2021-01-01"));
            fail("NonexistentTransactionException should have been thrown.");
        } catch (NonexistentTransactionException exception) {
            /* Expected. */
        } catch (EmptyTransactionsException exception) {
            fail("EmptyTransactionsException should not have been thrown.");
        }
    }

    @Test
    void testDeleteTransactionEmptyTransactions() {
        try {
            testCategory.deleteTransaction(testTransaction);
            fail("EmptyTransactionsException should have been thrown.");
        } catch (NonexistentTransactionException exception) {
            fail("NonexistentTransactionException should not have been thrown.");
        } catch (EmptyTransactionsException exception) {
            /* Expected. */
        }
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("name", testCategory.getName());
        testJson.put("amountSpent", testCategory.getAmountSpent());
        JSONArray testJsonArray = new JSONArray();
        for (Transaction nextTransaction : testCategory.getTransactions()) {
            testJsonArray.put(nextTransaction.toJson());
        }
        testJson.put("transactions", testJsonArray);
        assertEquals(testJson.toString(), testCategory.toJson().toString());
    }

    @Test
    void testTransactionsToJson() {
        testAddTransaction();
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testTransaction.toJson());
        testJsonArray.put(anotherTestTransaction.toJson());
        assertEquals(testJsonArray.toString(), testCategory.transactionsToJson().toString());
    }
}
