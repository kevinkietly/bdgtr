package model.exceptions;

import model.Budget;

/**
 * Represents the exception that occurs when the specified budget already exists in the account.
 */
public class DuplicateBudgetException extends DuplicateException {

    public DuplicateBudgetException(Budget budget) {
        super(budget.getName());
    }
}
