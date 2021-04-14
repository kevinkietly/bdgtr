package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Budget class.
 */
class BudgetTest {
    private Budget testBudget;
    private Category testCategory;
    private Category anotherTestCategory;
    private Transaction testTransaction;

    @BeforeEach
    void runBefore() {
        try {
            testBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
            testCategory = new Category("Test Category");
            anotherTestCategory = new Category("Another Test Category");
            testTransaction = new Transaction("Test Transaction", new BigDecimal("100.00"),
                    "January 1, 2021");
            testCategory.addTransaction(testTransaction);
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
        Calendar testRightNow = Calendar.getInstance();
        assertEquals("Test Budget", testBudget.getName());
        assertEquals(new BigDecimal("1000.00"), testBudget.getAmount());
        assertEquals(BigDecimal.ZERO, testBudget.getAmountSpent());
        assertEquals(new BigDecimal("1000.00"), testBudget.getAmountRemaining());
        assertEquals(testRightNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " "
                + testRightNow.get(Calendar.DAY_OF_MONTH) + ", " + testRightNow.get(Calendar.YEAR),
                testBudget.getStartDate());
        assertEquals(0, testBudget.getCategories().size());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            testBudget = new Budget("", new BigDecimal("1000.00"));
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
            testBudget = new Budget("Test Budget", new BigDecimal("-1000.00"));
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
            testBudget = new Budget("Test Budget", new BigDecimal("0.00"));
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        } catch (ZeroAmountException exception) {
            /* Expected. */
        }
    }

    @Test
    void testSetStartDate() {
        testBudget.setStartDate("January 2, 2021");
        assertEquals("January 2, 2021", testBudget.getStartDate());
    }

    @Test
    void testAddCategory() {
        try {
            testBudget.addCategory(testCategory);
            testBudget.calculateAmountRemaining();
            assertEquals(1, testBudget.getCategories().size());
            assertTrue(testBudget.getCategories().contains(testCategory));
            assertEquals(new BigDecimal("100.00"), testBudget.getAmountSpent());
            assertEquals(new BigDecimal("900.00"), testBudget.getAmountRemaining());
        } catch (DuplicateCategoryException exception) {
            fail("DuplicateCategoryException should not have been thrown.");
        }
    }

    @Test
    void testAddDuplicateCategory() {
        try {
            testBudget.addCategory(testCategory);
            testBudget.calculateAmountRemaining();
            testBudget.addCategory(testCategory);
            fail("DuplicateCategoryException should have been thrown.");
        } catch (DuplicateCategoryException exception) {
            /* Expected. */
        }
    }

    @Test
    void testDeleteCategory() throws DuplicateCategoryException {
        testBudget.addCategory(testCategory);
        testBudget.calculateAmountRemaining();
        testBudget.addCategory(anotherTestCategory);
        anotherTestCategory.addTransaction(testTransaction);
        testBudget.calculateAmountRemaining();
        testBudget.deleteCategory(anotherTestCategory);
        assertEquals(1, testBudget.getCategories().size());
        assertFalse(testBudget.getCategories().contains(anotherTestCategory));
        assertEquals(new BigDecimal("100.00"), testBudget.getAmountSpent());
        assertEquals(new BigDecimal("900.00"), testBudget.getAmountRemaining());
    }

    @Test
    void testCalculateAmountSpent() {
        testBudget.getCategories().add(testCategory);
        assertEquals(1, testBudget.getCategories().size());
        assertEquals(new BigDecimal("100.00"), testBudget.calculateAmountSpent());
    }

    @Test
    void testCalculateAmountSpentEmptyCategories() {
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(BigDecimal.ZERO, testBudget.calculateAmountSpent());
    }

    @Test
    void testCalculateAmountRemaining() {
        testBudget.getCategories().add(testCategory);
        assertEquals(1, testBudget.getCategories().size());
        assertEquals(new BigDecimal("900.00"), testBudget.calculateAmountRemaining());
    }

    @Test
    void testCalculateAmountRemainingEmptyCategories() {
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(new BigDecimal("1000.00"), testBudget.calculateAmountRemaining());
    }

    @Test
    void testCategoriesToJson() throws DuplicateCategoryException {
        testBudget.addCategory(testCategory);
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testCategory.toJson());
        assertEquals(testJsonArray.toString(), testBudget.categoriesToJson().toString());
    }

    @Test
    void testToJson() {
        JSONObject testJsonObject = new JSONObject();
        testJsonObject.put("name", testBudget.getName());
        testJsonObject.put("amount", testBudget.getAmount().toString());
        testJsonObject.put("amountSpent", testBudget.getAmountSpent().toString());
        testJsonObject.put("amountRemaining", testBudget.getAmountRemaining().toString());
        testJsonObject.put("startDate", testBudget.getStartDate());
        JSONArray testJsonArray = new JSONArray();
        for (Category nextCategory : testBudget.getCategories()) {
            testJsonArray.put(nextCategory.toJson());
        }
        testJsonObject.put("categories", testJsonArray);
        assertEquals(testJsonObject.toString(), testBudget.toJson().toString());
    }

    @Test
    void testEquals() throws EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateCategoryException {
        Budget sameTestBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
        Budget anotherTestBudget = new Budget("Another Test Budget", new BigDecimal("2000.00"));
        testBudget.addCategory(testCategory);
        testBudget.calculateAmountRemaining();
        sameTestBudget.addCategory(testCategory);
        sameTestBudget.calculateAmountRemaining();
        assertTrue(testBudget.equals(testBudget));
        assertFalse(testBudget.equals(null));
        assertFalse(testBudget.equals(testCategory));
        assertTrue(testBudget.equals(sameTestBudget));
        assertFalse(testBudget.equals(anotherTestBudget));
        assertEquals(sameTestBudget.hashCode(), testBudget.hashCode());
        assertNotEquals(anotherTestBudget.hashCode(), testBudget.hashCode());

        sameTestBudget.deleteCategory(testCategory);
        sameTestBudget.addCategory(anotherTestCategory);
        anotherTestCategory.addTransaction(testTransaction);
        sameTestBudget.calculateAmountRemaining();
        assertFalse(testBudget.equals(sameTestBudget));
    }

    @Test
    void testToString() {
        assertEquals("Test Budget", testBudget.toString());
    }
}
