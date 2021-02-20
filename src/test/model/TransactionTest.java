package model;

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
        testTransaction = new Transaction("Tuition", testTransactionCost,
                "1-1-2021");
    }

    @Test
    void testConstructor() {
        assertEquals("Tuition", testTransaction.getTransactionName());
        assertEquals(new BigDecimal("500.00"), testTransaction.getTransactionCost());
        assertEquals("1-1-2021", testTransaction.getTransactionDate());
    }

    @Test
    void testSetTransactionDate() {
        testTransaction.setTransactionDate("1-2-2021");
        assertEquals("1-2-2021", testTransaction.getTransactionDate());
    }
}
