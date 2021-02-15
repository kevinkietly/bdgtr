package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents a budget that has a name, starting date, list of categories, amount, amount spent and amount remaining
public class Budget {
    private String budgetName;                        // the name of the budget
    private final Calendar startingDate;              // the starting date of the budget
    private List<Category> budgetCategories;          // the list of categories in the budget
    private List<Category> budgetCategoriesToRemove;  // the list of categories to remove in the budget
    private List<Category> budgetCategoriesToDisplay; // the list of categories to display in the budget (this becomes
                                                      // the main list of categories in the budget)
    private BigDecimal budgetAmount;                  // the amount of the budget
    private BigDecimal budgetAmountSpent;             // the amount spent of the budget
    private BigDecimal budgetAmountRemaining;         // the amount remaining in the budget

    // REQUIRES: budgetName has a non-zero length, budgetAmount >= 0
    // EFFECTS: constructs a budget with a name, starting date, empty list of categories, given amount, zero amount
    // spent and amount remaining
    public Budget(String budgetName, BigDecimal budgetAmount) {
        this.budgetName = budgetName;
        startingDate = Calendar.getInstance();
        budgetCategories = new ArrayList<>();
        this.budgetAmount = budgetAmount;
        budgetAmountSpent = new BigDecimal("0.00");
        this.budgetAmountRemaining = this.budgetAmount;
        budgetCategoriesToRemove = new ArrayList<>();
        budgetCategoriesToDisplay = new ArrayList<>();
    }

    // getters
    public String getBudgetName() {
        return budgetName;
    }

    public Calendar getStartingDate() {
        return startingDate;
    }

    public List<Category> getBudgetCategories() {
        return budgetCategories;
    }

    public List<Category> getBudgetCategoriesToRemove() {
        return budgetCategoriesToRemove;
    }

    public List<Category> getBudgetCategoriesToDisplay() {
        return budgetCategoriesToDisplay;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public BigDecimal getBudgetAmountSpent() {
        return budgetAmountSpent;
    }

    public BigDecimal getBudgetAmountRemaining() {
        return budgetAmountRemaining;
    }

    // setters
    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    // EFFECTS: returns the amount spent of the budget
    public BigDecimal calculateBudgetAmountSpent() {
        if (budgetCategories.size() == 0) {
            budgetAmountSpent = new BigDecimal("0.00");
            for (Category category : budgetCategoriesToRemove) {
                budgetAmountSpent = budgetAmountSpent.add(category.getCategoryAmountSpent());
            }
        }
        for (Category category : budgetCategories) {
            budgetAmountSpent = budgetAmountSpent.add(category.getCategoryAmountSpent());
            budgetCategoriesToRemove.add(category);
        }
        for (Category category : budgetCategoriesToRemove) {
            budgetCategories.remove(category);
        }
        return budgetAmountSpent;
    }

    // EFFECTS: returns the amount remaining in the budget
    public BigDecimal calculateBudgetAmountRemaining() {
        calculateBudgetAmountSpent();
        return budgetAmountRemaining = budgetAmount.subtract(budgetAmountSpent);
    }

    // MODIFIES: this
    // EFFECTS: returns true and adds the given category to the budget if it is not already in the budget, otherwise
    // return false and do nothing
    public boolean addCategory(Category category) {
        boolean isSuccessfullyAdded = false;
        if (budgetCategories.size() > 0) {
            for (Category nextCategory : budgetCategories) {
                if (!nextCategory.getCategoryName().equals(category.getCategoryName())) {
                    budgetCategories.add(category);
                    budgetCategoriesToDisplay.add(category);
                    isSuccessfullyAdded = true;
                    break;
                }
            }
        } else {
            budgetCategories.add(category);
            budgetCategoriesToDisplay.add(category);
            isSuccessfullyAdded = true;
        }
        return isSuccessfullyAdded;
    }

    // REQUIRES: the given category must already be in the list of categories
    // MODIFIES: this
    // EFFECTS: removes the given category from the budget
    public void removeCategory(Category category) {
        budgetCategoriesToDisplay.remove(category);
        budgetAmountSpent = budgetAmountSpent.subtract(category.getCategoryAmountSpent());
    }

    // EFFECTS: returns the starting date in the format DD/MM/YYYY
    public String formatStartingDate() {
        int day = startingDate.get(Calendar.DAY_OF_MONTH);
        int month = startingDate.get(Calendar.MONTH);
        month++;
        int year = startingDate.get(Calendar.YEAR);
        return day + "-" + month + "-" + year;
    }

    // EFFECTS: returns a string representation of budget
    @Override
    public String toString() {
        return "Name: " + budgetName + ", Amount: " + budgetAmount + ", Spent: " + budgetAmountSpent
                + ", Remaining: " + budgetAmountRemaining + ", Categories: " + budgetCategories;
    }
}
