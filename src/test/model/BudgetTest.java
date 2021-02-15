package model;

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
        testBudget = new Budget("February 2021", testBudgetAmount);
        testCategory = new Category("Education");
        testTransaction = new Transaction("Tuition", testTransactionCost, "1-1-2021");
        zero = new BigDecimal("0.00");
    }

    @Test
    void testConstructor() {
        assertEquals("February 2021", testBudget.getBudgetName());
        assertEquals(0, testBudget.getBudgetCategories().size());
        assertEquals(testBudgetAmount, testBudget.getBudgetAmount());
        assertEquals(zero, testBudget.getBudgetAmountSpent());
        assertEquals(testBudgetAmount, testBudget.getBudgetAmountRemaining());
    }

    @Test
    void testCalculateBudgetAmountSpentNoCategories() {
        assertEquals(0, testBudget.getBudgetCategories().size());
        assertEquals(zero, testBudget.calculateBudgetAmountSpent());
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
        assertEquals(expected, testBudget.calculateBudgetAmountSpent());
        testBudget.calculateBudgetAmountRemaining();
        assertEquals(expected, testBudget.getBudgetAmountSpent());
    }

    @Test
    void testCalculateBudgetAmountRemainingNoCategories() {
        assertEquals(0, testBudget.getBudgetCategories().size());
        assertEquals(testBudgetAmount, testBudget.calculateBudgetAmountRemaining());
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
        assertEquals(expected, testBudget.calculateBudgetAmountRemaining());
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
        assertEquals(expected, testBudget.calculateBudgetAmountRemaining());
    }

    @Test
    void testAddCategoryNotAlreadyInBudget() {
        testCategory.addTransaction(testTransaction);
        assertTrue(testBudget.addCategory(testCategory));
        testBudget.calculateBudgetAmountRemaining();
        assertEquals(1, testBudget.getBudgetCategoriesToDisplay().size());
        assertTrue(testBudget.getBudgetCategoriesToDisplay().contains(testCategory));
        BigDecimal testTransactionCost = new BigDecimal("500.00");
        assertEquals(testTransactionCost, testBudget.getBudgetAmountSpent());
    }

    @Test
    void testAddCategoryAlreadyInBudget() {
        assertTrue(testBudget.addCategory(testCategory));
        assertEquals(1, testBudget.getBudgetCategories().size());
        assertTrue(testBudget.getBudgetCategories().contains(testCategory));

        assertFalse(testBudget.addCategory(testCategory));
        assertEquals(1, testBudget.getBudgetCategories().size());
        assertTrue(testBudget.getBudgetCategories().contains(testCategory));
    }

    @Test
    void testRemoveCategory() {
        testBudget.addCategory(testCategory);
        testBudget.removeCategory(testCategory);
        assertEquals(0, testBudget.getBudgetCategoriesToDisplay().size());
        assertFalse(testBudget.getBudgetCategoriesToDisplay().contains(testCategory));
        assertEquals(zero, testBudget.getBudgetAmountSpent());
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
        assertTrue(testBudget.toString().contains("Name: February 2021, Amount: 1000.00, Spent: 0.00, " +
                "Remaining: 1000.00, Categories: []"));
    }
}
