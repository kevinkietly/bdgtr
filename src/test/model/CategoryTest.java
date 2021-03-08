package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private Category testCategory;
    private Transaction testTransaction;

    @BeforeEach
    void runBefore() {
        BigDecimal testTransactionCost = new BigDecimal("1000.00");
        testCategory = new Category("test category");
        testTransaction = new Transaction("test transaction", testTransactionCost,
                "1-1-2021");
    }

    @Test
    void testConstructor() {
        assertEquals("test category", testCategory.getName());
        assertEquals(0, testCategory.getTransactions().size());
    }

    @Test
    void testAddTransaction() {
        testCategory.addTransaction(testTransaction);
        assertEquals(1, testCategory.getTransactions().size());
        assertTrue(testCategory.getTransactions().contains(testTransaction));
        BigDecimal anotherTestTransactionCost = new BigDecimal("1000.00");
        assertEquals(anotherTestTransactionCost, testCategory.getAmountSpent());
    }

    @Test
    void testRemoveTransaction() {
        testCategory.addTransaction(testTransaction);
        testCategory.removeTransaction(testTransaction);
        assertEquals(0, testCategory.getTransactions().size());
        assertFalse(testCategory.getTransactions().contains(testTransaction));
        BigDecimal zero = new BigDecimal("0.00");
        assertEquals(zero, testCategory.getAmountSpent());
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("category name", "test category");
        JSONArray testJsonArray = new JSONArray();
        for (Transaction transaction : testCategory.getTransactions()) {
            testJsonArray.put(transaction.toJson());
        }
        testJson.put("category transactions", testJsonArray);
        assertEquals(testJson.toString(), testCategory.toJson().toString());
    }

    @Test
    void testCategoryTransactionsToJson() {
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testTransaction.toJson());
        testCategory.addTransaction(testTransaction);
        assertEquals(testJsonArray.toString(), testCategory.transactionsToJson().toString());
    }
}
