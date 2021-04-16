package model;

import model.exceptions.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Transaction class.
 */
class TransactionTest {
    private Category testCategory;
    private Transaction testTransaction;

    @BeforeEach
    void runBefore() {
        try {
            testCategory = new Category("Test Category");
            testTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                    "January 1, 2021");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        } catch (ZeroAmountException exception) {
            fail("ZeroAmountException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test Transaction", testTransaction.getName());
        assertEquals(new BigDecimal("100.00"), testTransaction.getAmount());
        assertEquals("January 1, 2021", testTransaction.getDate());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            testTransaction = new Transaction("", new BigDecimal("100.00"), "January 1, 2021");
            fail("EmptyNameException should have been thrown.");
        } catch (EmptyNameException exception) {
            /* Expected. */
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        } catch (ZeroAmountException exception) {
            fail("ZeroAmountException should not have been thrown.");
        }
    }

    @Test
    void testConstructorNegativeAmountException() {
        try {
            testTransaction = new Transaction("Test Transaction", new BigDecimal("-100.00"),
                    "January 1, 2021");
            fail("NegativeAmountException should have been thrown.");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            /* Expected. */
        } catch (ZeroAmountException exception) {
            fail("ZeroAmountException should not have been thrown.");
        }
    }

    @Test
    void testConstructorZeroAmountException() {
        try {
            testTransaction = new Transaction("Test Transaction", new BigDecimal("0.00"),
                    "January 1, 2021");
            fail("ZeroAmountException should have been thrown.");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        } catch (ZeroAmountException exception) {
            /* Expected. */
        }
    }

    @Test
    void testToJson() {
        JSONObject testJsonObject = new JSONObject();
        testJsonObject.put("name", testTransaction.getName());
        testJsonObject.put("amount", testTransaction.getAmount().toString());
        testJsonObject.put("date", testTransaction.getDate());
        assertEquals(testJsonObject.toString(), testTransaction.toJson().toString());
    }

    @Test
    void testEquals() throws EmptyNameException, NegativeAmountException, ZeroAmountException {
        Transaction sameTestTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                "January 1, 2021");
        Transaction anotherTestTransaction = new Transaction("Another Test Transaction",
                new BigDecimal("200.00"), "January 2, 2021");
        assertTrue(testTransaction.equals(testTransaction));
        assertFalse(testTransaction.equals(null));
        assertFalse(testTransaction.equals(testCategory));
        assertTrue(testTransaction.equals(sameTestTransaction));
        assertFalse(testTransaction.equals(anotherTestTransaction));
        assertEquals(sameTestTransaction.hashCode(), testTransaction.hashCode());
        assertNotEquals(anotherTestTransaction.hashCode(), testTransaction.hashCode());
    }
}
