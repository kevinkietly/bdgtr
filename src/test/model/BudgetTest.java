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

    @BeforeEach
    void runBefore() throws NegativeCostException {
        try {
            testBudget = new Budget("Test Budget", new BigDecimal("1000.00"));
            testCategory = new Category("Test Category");
            anotherTestCategory = new Category("Another Test Category");
            Transaction testTransaction = new Transaction("Test Transaction", new BigDecimal("500.00"),
                    "January 1, 2021");
            Transaction anotherTestTransaction = new Transaction("Another Test Transaction",
                    new BigDecimal("500.00"), "January 1, 2021");
            testCategory.addTransaction(testTransaction);
            anotherTestCategory.addTransaction(anotherTestTransaction);
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        }
    }

    @Test
    void testConstructor() {
        Calendar testStartDate = Calendar.getInstance();
        assertEquals("Test Budget", testBudget.getName());
        assertEquals(new BigDecimal("1000.00"), testBudget.getAmount());
        assertEquals(new BigDecimal("0.00"), testBudget.getAmountSpent());
        assertEquals(new BigDecimal("1000.00"), testBudget.getAmountRemaining());
        assertEquals(testStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " "
                + testStartDate.get(Calendar.DAY_OF_MONTH) + ", " + testStartDate.get(Calendar.YEAR),
                testBudget.getStartDate());
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(0, testBudget.getCategoriesToCalculate().size());
        assertEquals(0, testBudget.getCategoriesToRemove().size());
    }

    @Test
    void testConstructorEmptyNameException() {
        try {
            Budget anotherTestBudget = new Budget("", new BigDecimal("1000.00"));
            fail("EmptyNameException should have been thrown.");
        } catch (EmptyNameException exception) {
            /* Expected. */
        } catch (NegativeAmountException exception) {
            fail("NegativeAmountException should not have been thrown.");
        }
    }

    @Test
    void testConstructorNegativeAmountException() {
        try {
            Budget anotherTestBudget = new Budget("Another Test Budget", new BigDecimal("-1000.00"));
            fail("NegativeAmountException should have been thrown.");
        } catch (EmptyNameException exception) {
            fail("EmptyNameException should not have been thrown.");
        } catch (NegativeAmountException exception) {
            /* Expected. */
        }
    }

    @Test
    void testAddCategory() {
        try {
            testBudget.addCategory(testCategory);
            assertEquals(1, testBudget.getCategories().size());
            assertTrue(testBudget.getCategories().contains(testCategory));
            testBudget.addCategory(anotherTestCategory);
            assertEquals(2, testBudget.getCategories().size());
            assertTrue(testBudget.getCategories().contains(anotherTestCategory));
            assertEquals(new BigDecimal("1000.00"), testBudget.getAmountSpent());
            assertEquals(new BigDecimal("0.00"), testBudget.getAmountRemaining());
        } catch (DuplicateCategoryException exception) {
            fail("DuplicateCategoryException should not have been thrown.");
        }
    }

    @Test
    void testAddDuplicateCategory() {
        testAddCategory();
        try {
            testBudget.addCategory(testCategory);
            fail("DuplicateCategoryException should have been thrown.");
        } catch (DuplicateCategoryException exception) {
            /* Expected. */
        }
    }

    @Test
    void testDeleteCategory() {
        testAddCategory();
        try {
            testBudget.deleteCategory(testCategory);
            assertEquals(1, testBudget.getCategories().size());
            assertFalse(testBudget.getCategories().contains(testCategory));
            testBudget.deleteCategory(anotherTestCategory);
            assertEquals(0, testBudget.getCategories().size());
            assertFalse(testBudget.getCategories().contains(anotherTestCategory));
            assertEquals(new BigDecimal("0.00"), testBudget.getAmountSpent());
            assertEquals(new BigDecimal("1000.00"), testBudget.getAmountRemaining());
        } catch (NonexistentCategoryException exception) {
            fail("NonexistentCategoryException should not have been thrown.");
        } catch (EmptyCategoriesException exception) {
            fail("EmptyCategoriesException should not have been thrown.");
        }
    }

    @Test
    void testDeleteNonexistentCategory() throws EmptyNameException {
        testAddCategory();
        try {
            testBudget.deleteCategory(new Category("Nonexistent Test Category"));
            fail("NonexistentCategoryException should have been thrown.");
        } catch (NonexistentCategoryException exception) {
            /* Expected. */
        } catch (EmptyCategoriesException exception) {
            fail("EmptyCategoriesException should not have been thrown.");
        }
    }

    @Test
    void testDeleteCategoryEmptyCategories() {
        try {
            testBudget.deleteCategory(testCategory);
            fail("EmptyCategoriesException should have been thrown.");
        } catch (NonexistentCategoryException exception) {
            fail("NonexistentCategoryException should not have been thrown.");
        } catch (EmptyCategoriesException exception) {
            /* Expected. */
        }
    }

    @Test
    void testCalculateAmountSpent() {
        testBudget.getCategories().add(testCategory);
        testBudget.getCategories().add(anotherTestCategory);
        assertEquals(2, testBudget.getCategories().size());
        assertEquals(0, testBudget.getCategoriesToCalculate().size());
        assertEquals(0, testBudget.getCategoriesToRemove().size());
        assertEquals(new BigDecimal("1000.00"), testBudget.calculateAmountSpent());
    }

    @Test
    void testCalculateAmountSpentEmptyCategories() {
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(0, testBudget.getCategoriesToCalculate().size());
        assertEquals(0, testBudget.getCategoriesToRemove().size());
        assertEquals(new BigDecimal("0.00"), testBudget.calculateAmountSpent());
    }

    @Test
    void testCalculateAmountRemaining() {
        testBudget.getCategories().add(testCategory);
        testBudget.getCategories().add(anotherTestCategory);
        assertEquals(2, testBudget.getCategories().size());
        assertEquals(0, testBudget.getCategoriesToCalculate().size());
        assertEquals(0, testBudget.getCategoriesToRemove().size());
        assertEquals(new BigDecimal("0.00"), testBudget.calculateAmountRemaining());
    }

    @Test
    void testCalculateAmountRemainingEmptyCategories() {
        testCalculateAmountSpentEmptyCategories();
        assertEquals(new BigDecimal("1000.00"), testBudget.calculateAmountRemaining());
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("name", testBudget.getName());
        testJson.put("amount", testBudget.getAmount().toString());
        testJson.put("amountSpent", testBudget.getAmountSpent().toString());
        testJson.put("amountRemaining", testBudget.getAmountRemaining().toString());
        testJson.put("startDate", testBudget.getStartDate());
        JSONArray testJsonArray = new JSONArray();
        for (Category nextCategory : testBudget.getCategories()) {
            testJsonArray.put(nextCategory.toJson());
        }
        testJson.put("categories", testJsonArray);
        assertEquals(testJson.toString(), testBudget.toJson().toString());
    }

    @Test
    void testCategoriesToJson() {
        testAddCategory();
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testCategory.toJson());
        testJsonArray.put(anotherTestCategory.toJson());
        assertEquals(testJsonArray.toString(), testBudget.categoriesToJson().toString());
    }
}
