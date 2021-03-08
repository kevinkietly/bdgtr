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
        assertEquals(budgetName, budget.getName());
        assertEquals(budgetAmount, budget.getAmountSpent());
        assertEquals(budgetCategories, budget.getCategories());
    }
    protected void checkCategory(String categoryName, BigDecimal categoryAmountSpent,
                                 List<Transaction> categoryTransactions, Category category) {
        assertEquals(categoryName, category.getName());
        assertEquals(categoryAmountSpent, category.getAmountSpent());
        assertEquals(categoryTransactions, category.getTransactions());
    }

    protected void checkTransaction(String transactionName, BigDecimal transactionCost, String transactionDate,
                                    Transaction transaction) {
        assertEquals(transactionName, transaction.getName());
        assertEquals(transactionCost, transaction.getCost());
        assertEquals(transactionDate, transaction.getDate());
    }
}
