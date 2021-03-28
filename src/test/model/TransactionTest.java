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
    private Transaction testTransaction;

    @BeforeEach
    void runBefore() {
        try {
            testTransaction = new Transaction("Test Transaction", new BigDecimal("500.00"),
                    "January 1, 2021");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeCostException exception) {
            fail("NegativeCostException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Test Transaction", testTransaction.getName());
        assertEquals(new BigDecimal("500.00"), testTransaction.getCost());
        assertEquals("January 1, 2021", testTransaction.getDate());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            Transaction anotherTestTransaction = new Transaction("", new BigDecimal("500.00"),
                    "January 1, 2021");
        } catch (EmptyNameException exception) {
            /* Expected. */
        } catch (NegativeCostException exception) {
            fail("NegativeCostException should not have been thrown.");
        }
    }

    @Test
    void testConstructorNegativeCostException() {
        try {
            Transaction anotherTestTransaction = new Transaction("Another Test Transaction",
                    new BigDecimal("-500.00"), "January 1, 2021");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeCostException exception) {
            /* Expected. */
        }
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("name", testTransaction.getName());
        testJson.put("cost", testTransaction.getCost().toString());
        testJson.put("date", testTransaction.getDate());
        assertEquals(testJson.toString(), testTransaction.toJson().toString());
    }
}
