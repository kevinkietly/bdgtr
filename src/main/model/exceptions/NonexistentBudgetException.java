package model.exceptions;

import model.Budget;

/**
 * Represents the exception that occurs when the given Budget does not exist in the Account.
 */
public class NonexistentBudgetException extends NonexistentException {

    public NonexistentBudgetException(Budget budget) {
        super(budget.getName() + " does not exist in the account.");
    }
}
