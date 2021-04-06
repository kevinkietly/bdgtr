package model.exceptions;

import model.Category;

/**
 * Represents the exception that occurs when the specified category already exists in the budget.
 */
public class DuplicateCategoryException extends DuplicateException {

    public DuplicateCategoryException(Category category) {
        super(category.getName());
    }
}
