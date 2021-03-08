package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    private Transaction testTransaction;
    private BigDecimal testTransactionCost;

    @BeforeEach
    void runBefore() {
        testTransactionCost = new BigDecimal("500.00");
        testTransaction = new Transaction("test transaction", testTransactionCost,
                "1-1-2021");
    }

    @Test
    void testConstructor() {
        assertEquals("test transaction", testTransaction.getName());
        assertEquals(new BigDecimal("500.00"), testTransaction.getCost());
        assertEquals("1-1-2021", testTransaction.getDate());
    }

    @Test
    void testSetTransactionDate() {
        testTransaction.setDate("1-2-2021");
        assertEquals("1-2-2021", testTransaction.getDate());
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("transaction name", "test transaction");
        testJson.put("transaction cost", testTransactionCost.toString());
        testJson.put("transaction date", "1-1-2021");
        assertEquals(testJson.toString(), testTransaction.toJson().toString());
    }
}
