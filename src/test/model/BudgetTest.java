package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {
    private Budget testBudget;
    private BigDecimal testBudgetAmount;
    private Category testCategory;
    private Transaction testTransaction;
    private BigDecimal testTransactionCost;
    private BigDecimal zero;

    @BeforeEach
    void runBefore() {
        testBudgetAmount = new BigDecimal("1000.00");
        testTransactionCost = new BigDecimal("500.00");
        testBudget = new Budget("test budget", testBudgetAmount);
        testCategory = new Category("test category");
        testTransaction = new Transaction("test transaction", testTransactionCost, "1-1-2021");
        zero = new BigDecimal("0.00");
    }

    @Test
    void testConstructor() {
        assertEquals("test budget", testBudget.getName());
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(testBudgetAmount, testBudget.getAmount());
        assertEquals(zero, testBudget.getAmountSpent());
        assertEquals(testBudgetAmount, testBudget.getAmountRemaining());
    }

    @Test
    void testCalculateBudgetAmountSpentNoCategories() {
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(zero, testBudget.calculateAmountSpent());
    }

    @Test
    void testCalculateBudgetAmountSpentMultipleCategories() {
        testCategory.addTransaction(testTransaction);
        testBudget.addCategory(testCategory);
        BigDecimal anotherTestTransactionCost = new BigDecimal("4.99");
        Transaction anotherTestTransaction = new Transaction("Spotify Subscription",
                anotherTestTransactionCost, "1-1-2021");
        Category anotherTestCategory = new Category("Entertainment");
        anotherTestCategory.addTransaction(anotherTestTransaction);
        testBudget.addCategory(anotherTestCategory);
        BigDecimal expected = testTransactionCost.add(anotherTestTransactionCost);
        assertEquals(expected, testBudget.calculateAmountSpent());
        testBudget.calculateAmountRemaining();
        assertEquals(expected, testBudget.getAmountSpent());
    }

    @Test
    void testCalculateBudgetAmountRemainingNoCategories() {
        assertEquals(0, testBudget.getCategories().size());
        assertEquals(testBudgetAmount, testBudget.calculateAmountRemaining());
    }

    @Test
    void testCalculateBudgetAmountRemainingSameCategory() {
        BigDecimal anotherTestTransactionCost = new BigDecimal("100.00");
        Transaction anotherTestTransaction = new Transaction("Textbook", anotherTestTransactionCost,
                "1-1-2021");
        testCategory.addTransaction(testTransaction);
        testCategory.addTransaction(anotherTestTransaction);
        testBudget.addCategory(testCategory);
        BigDecimal expected = new BigDecimal("400.00");
        assertEquals(expected, testBudget.calculateAmountRemaining());
    }

    @Test
    void testCalculateBudgetAmountRemainingDifferentCategories() {
        testCategory.addTransaction(testTransaction);
        testBudget.addCategory(testCategory);
        BigDecimal anotherTestTransactionCost = new BigDecimal("4.99");
        Transaction anotherTestTransaction = new Transaction("Spotify Subscription",
                anotherTestTransactionCost, "1-1-2021");
        Category anotherTestCategory = new Category("Entertainment");
        anotherTestCategory.addTransaction(anotherTestTransaction);
        testBudget.addCategory(anotherTestCategory);
        BigDecimal expected = new BigDecimal("495.01");
        assertEquals(expected, testBudget.calculateAmountRemaining());
    }

    @Test
    void testAddCategory() {
        testCategory.addTransaction(testTransaction);
        assertTrue(testBudget.addCategory(testCategory));
        testBudget.calculateAmountRemaining();
        assertEquals(1, testBudget.getCategoriesToDisplay().size());
        assertTrue(testBudget.getCategoriesToDisplay().contains(testCategory));
        BigDecimal testTransactionCost = new BigDecimal("500.00");
        assertEquals(testTransactionCost, testBudget.getAmountSpent());
    }

    @Test
    void testAddCategoryAlreadyInBudget() {
        assertTrue(testBudget.addCategory(testCategory));
        assertEquals(1, testBudget.getCategories().size());
        assertTrue(testBudget.getCategories().contains(testCategory));

        assertFalse(testBudget.addCategory(testCategory));
        assertEquals(1, testBudget.getCategories().size());
        assertTrue(testBudget.getCategories().contains(testCategory));
    }

    @Test
    void testRemoveCategory() {
        testBudget.addCategory(testCategory);
        testBudget.removeCategory(testCategory);
        assertEquals(0, testBudget.getCategoriesToDisplay().size());
        assertFalse(testBudget.getCategoriesToDisplay().contains(testCategory));
        assertEquals(zero, testBudget.getAmountSpent());
    }

    @Test
    void testFormatStartingDate() {
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int month = rightNow.get(Calendar.MONTH);
        month++;
        int year = rightNow.get(Calendar.YEAR);
        assertEquals(day + "-" + month + "-" + year, testBudget.formatStartingDate());
    }

    @Test
    void testToString() {
        assertTrue(testBudget.toString().contains("Name: test budget, Amount: 1000.00, Spent: 0.00, " +
                "Remaining: 1000.00, Categories: []"));
    }

    @Test
    void testToJson() {
        JSONObject testJson = new JSONObject();
        testJson.put("budget name", "test budget");
        testJson.put("budget amount", testBudgetAmount.toString());
        JSONArray testJsonArray = new JSONArray();
        for (Category category : testBudget.getCategories()) {
            testJsonArray.put(category.toJson());
        }
        testJson.put("budget categories", testJsonArray);
        assertEquals(testJson.toString(), testBudget.toJson().toString());
    }

    @Test
    void testCategoriesToJson() {
        JSONArray testJsonArray = new JSONArray();
        testJsonArray.put(testCategory.toJson());
        testBudget.addCategory(testCategory);
        assertEquals(testJsonArray.toString(), testBudget.categoriesToJson().toString());
    }
}
