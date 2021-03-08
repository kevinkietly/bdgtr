package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents a budget that has a name, starting date, list of categories, amount, amount spent and amount remaining
public class Budget implements Writable {
    private String name;                        // the name of the budget
    private final Calendar startingDate;        // the starting date of the budget
    private List<Category> categories;          // the list of categories in the budget
    private List<Category> categoriesToRemove;  // the list of categories to remove in the budget
    private List<Category> categoriesToDisplay; // the list of categories to display in the budget (this becomes
                                                // the main list of categories in the budget)
    private BigDecimal amount;                  // the amount of the budget
    private BigDecimal amountSpent;             // the amount spent of the budget
    private BigDecimal amountRemaining;         // the amount remaining in the budget

    // REQUIRES: name has a non-zero length, amount >= 0
    // EFFECTS: constructs a budget with the given name, given amount, zero amount spent, zero amount remaining,
    // starting date and empty list of categories
    public Budget(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
        amountSpent = new BigDecimal("0.00");
        this.amountRemaining = this.amount;
        startingDate = Calendar.getInstance();
        categories = new ArrayList<>();
        categoriesToRemove = new ArrayList<>();
        categoriesToDisplay = new ArrayList<>();
    }

    // getters
    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Category> getCategoriesToDisplay() {
        return categoriesToDisplay;
    }

    // EFFECTS: returns the amount spent of the budget
    public BigDecimal calculateAmountSpent() {
        if (categories.size() == 0) {
            amountSpent = new BigDecimal("0.00");
            for (Category category : categoriesToDisplay) {
                amountSpent = amountSpent.add(category.getAmountSpent());
            }
        }
        for (Category category : categories) {
            amountSpent = amountSpent.add(category.getAmountSpent());
            categoriesToRemove.add(category);
        }
        for (Category category : categoriesToRemove) {
            categories.remove(category);
        }
        return amountSpent;
    }

    // EFFECTS: returns the amount remaining in the budget
    public BigDecimal calculateAmountRemaining() {
        calculateAmountSpent();
        return amountRemaining = amount.subtract(amountSpent);
    }

    // MODIFIES: this
    // EFFECTS: returns true and adds the given category to the budget if it is not already in the budget, otherwise
    // return false and do nothing
    public boolean addCategory(Category category) {
        boolean isAdded = false;
        if (categories.size() > 0) {
            for (Category nextCategory : categoriesToDisplay) {
                if (!nextCategory.getName().equals(category.getName())) {
                    categories.add(category);
                    categoriesToDisplay.add(category);
                    isAdded = true;
                    break;
                }
            }
        } else {
            categories.add(category);
            categoriesToDisplay.add(category);
            isAdded = true;
        }
        return isAdded;
    }

    // REQUIRES: the given category must already be in the budget
    // MODIFIES: this
    // EFFECTS: removes the given category from the budget
    public void removeCategory(Category category) {
        categories.remove(category);
        categoriesToDisplay.remove(category);
        amountSpent = amountSpent.subtract(category.getAmountSpent());
        amountRemaining = amount.subtract(amountSpent);
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
        return "Name: " + name + ", Amount: " + amount + ", Spent: " + amountSpent
                + ", Remaining: " + amountRemaining + ", Categories: " + categories;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("budget name", name);
        json.put("budget amount", amount.toString());
        json.put("budget categories", categoriesToJson());
        return json;
    }

    // EFFECTS: returns categories in this budget as a JSON array
    public JSONArray categoriesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Category category : categories) {
            jsonArray.put(category.toJson());
        }

        return jsonArray;
    }
}
