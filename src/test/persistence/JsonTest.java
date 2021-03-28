package persistence;

import model.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides tests to check an Account, a Budget, a Category and a Transaction.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonTest {

    protected void checkAccount(String username, String password, Account account) {
        assertEquals(username, account.getUsername());
        assertEquals(password, account.getPassword());
    }

    protected void checkBudget(String name, BigDecimal amount, Budget budget) {
        assertEquals(name, budget.getName());
        assertEquals(amount, budget.getAmount());
    }

    protected void checkCategory(String name, BigDecimal amountSpent, Category category) {
        assertEquals(name, category.getName());
        assertEquals(amountSpent, category.getAmountSpent());
    }

    protected void checkTransaction(String name, BigDecimal cost, String date, Transaction transaction) {
        assertEquals(name, transaction.getName());
        assertEquals(cost, transaction.getCost());
        assertEquals(date, transaction.getDate());
    }
}
