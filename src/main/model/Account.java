package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JsonArrayIndent;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an account that has a username, password, and list of budgets
public class Account implements Writable {
    private String username;      // the username of the account
    private String password;      // the password of the account
    private List<Budget> budgets; // the list of budgets in the account

    // REQUIRES: username and password have non-zero length
    // EFFECTS: constructs an account with the given username, given password and empty list of budgets
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        budgets = new ArrayList<>();
    }

    // getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    // MODIFIES: this
    // EFFECTS: returns true and adds the given budget to the list of budgets if it is not already in the list of
    // budgets, otherwise return false and do nothing
    public boolean addBudget(Budget budget) {
        boolean isAdded = false;
        if (budgets.size() > 0) {
            for (Budget nextBudget : budgets) {
                if (!nextBudget.getName().equals(budget.getName())) {
                    budgets.add(budget);
                    isAdded = true;
                    break;
                }
            }
        } else {
            budgets.add(budget);
            isAdded = true;
        }
        return isAdded;
    }

    // REQUIRES: the given budget must already be in the list of budgets
    // MODIFIES: this
    // EFFECTS: removes the given budget from the list of budgets
    public void removeBudget(Budget budget) {
        budgets.remove(budget);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("account username", username);
        json.put("account password", password);
        json.put("budgets", budgetsToJson());
        return json;
    }

    // EFFECTS: returns budgets in this account as a JSON array
    public JSONArray budgetsToJson() {
        JSONArray jsonArray = new JsonArrayIndent();

        for (Budget nextBudget : budgets) {
            jsonArray.put(nextBudget.toJson());
        }

        return jsonArray;
    }
}
