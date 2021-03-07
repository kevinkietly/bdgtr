package model;

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
        testCategory = new Category("Education");
        testTransaction = new Transaction("Tuition", testTransactionCost,
                "1-1-2021");
    }

    @Test
    void testConstructor() {
        assertEquals("Education", testCategory.getCategoryName());
        assertEquals(0, testCategory.getCategoryTransactions().size());
    }

    @Test
    void testAddTransaction() {
        testCategory.addTransaction(testTransaction);
        assertEquals(1, testCategory.getCategoryTransactions().size());
        assertTrue(testCategory.getCategoryTransactions().contains(testTransaction));
        BigDecimal anotherTestTransactionCost = new BigDecimal("1000.00");
        assertEquals(anotherTestTransactionCost, testCategory.getCategoryAmountSpent());
    }

    @Test
    void testRemoveTransaction() {
        testCategory.addTransaction(testTransaction);
        testCategory.removeTransaction(testTransaction);
        assertEquals(0, testCategory.getCategoryTransactions().size());
        assertFalse(testCategory.getCategoryTransactions().contains(testTransaction));
        BigDecimal zero = new BigDecimal("0.00");
        assertEquals(zero, testCategory.getCategoryAmountSpent());
    }
}
