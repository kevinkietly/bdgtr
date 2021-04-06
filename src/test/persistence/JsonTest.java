package persistence;

import model.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides tests to check an account, a budget, a category, and a transaction for equality.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonTest {

    protected void checkAccount(String firstName, String lastName, String username, String password, Account account) {
        assertEquals(firstName, account.getFirstName());
        assertEquals(lastName, account.getLastName());
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
    }

    protected void checkBudget(String name, BigDecimal amount, BigDecimal amountSpent, BigDecimal amountRemaining,
                               String startDate, Budget budget) {
        assertEquals(name, budget.getName());
        assertEquals(amount, budget.getAmount());
        assertEquals(amountSpent, budget.getAmountSpent());
        assertEquals(amountRemaining, budget.getAmountRemaining());
        assertEquals(startDate, budget.getStartDate());
    }

    protected void checkCategory(String name, BigDecimal amountSpent, Category category) {
        assertEquals(name, category.getName());
        assertEquals(amountSpent, category.getAmountSpent());
    }

    protected void checkTransaction(String name, BigDecimal amount, String date, Transaction transaction) {
        assertEquals(name, transaction.getName());
        assertEquals(amount, transaction.getAmount());
        assertEquals(date, transaction.getDate());
    }
}
