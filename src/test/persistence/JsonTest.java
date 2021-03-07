package persistence;

import model.Budget;
import model.Category;
import model.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Code referenced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkBudget(String budgetName, BigDecimal budgetAmount, List<Category> budgetCategories,
                               Budget budget) {
        assertEquals(budgetName, budget.getBudgetName());
        assertEquals(budgetAmount, budget.getBudgetAmountSpent());
        assertEquals(budgetCategories, budget.getBudgetCategories());
    }
    protected void checkCategory(String categoryName, BigDecimal categoryAmountSpent,
                                 List<Transaction> categoryTransactions, Category category) {
        assertEquals(categoryName, category.getCategoryName());
        assertEquals(categoryAmountSpent, category.getCategoryAmountSpent());
        assertEquals(categoryTransactions, category.getCategoryTransactions());
    }

    protected void checkTransaction(String transactionName, BigDecimal transactionCost, String transactionDate,
                                    Transaction transaction) {
        assertEquals(transactionName, transaction.getTransactionName());
        assertEquals(transactionCost, transaction.getTransactionCost());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }
}
