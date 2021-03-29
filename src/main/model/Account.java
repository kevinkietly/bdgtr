package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Account.
 */
public class Account implements Writable {
    private String username;
    private String password;
    private List<Budget> budgets;

    /**
     * Creates an Account with the given username, the given password and no budgets.
     *
     * @param username the username for this Account
     * @param password the password for this Account
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     */
    public Account(String username, String password) throws EmptyUsernameException, EmptyPasswordException {
        if (username.isEmpty()) {
            throw new EmptyUsernameException();
        } else if (password.isEmpty()) {
            throw new EmptyPasswordException();
        }
        this.username = username;
        this.password = password;
        budgets = new ArrayList<>();
    }

    /**
     * Gets the username of this Account.
     *
     * @return the username of this Account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of this Account.
     *
     * @return the password of this Account
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the budgets in this Account.
     *
     * @return the budgets in this Account
     */
    public List<Budget> getBudgets() {
        return budgets;
    }

    /**
     * Adds the given Budget to this Account.
     *
     * @param budget the Budget to be added
     * @throws DuplicateBudgetException if the Budget already exists in this Account
     */
    public void addBudget(Budget budget) throws DuplicateBudgetException {
        if (budgets.size() != 0) {
            for (Budget nextBudget : budgets) {
                if (nextBudget.getName().equals(budget.getName())) {
                    throw new DuplicateBudgetException(budget);
                }
            }
        }
        budgets.add(budget);
    }

    /**
     * Deletes the given Budget from this Account.
     *
     * @param budget the Budget to be deleted
     * @throws NonexistentBudgetException if the Budget does not exist in this Account
     * @throws EmptyBudgetsException if this Account has no budgets
     */
    public void deleteBudget(Budget budget) throws NonexistentBudgetException, EmptyBudgetsException {
        boolean isDeleted = false;
        if (budgets.size() != 0) {
            for (Budget nextBudget : budgets) {
                if (nextBudget.getName().equals(budget.getName())) {
                    budgets.remove(budget);
                    isDeleted = true;
                    break;
                }
            }
            if (!isDeleted) {
                throw new NonexistentBudgetException(budget);
            }
        } else {
            throw new EmptyBudgetsException();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", getUsername());
        json.put("password", getPassword());
        json.put("budgets", budgetsToJson());
        return json;
    }

    /**
     * Converts budgets in this Account to JSON.
     *
     * @return the budgets in this Account as a JSON array
     */
    public JSONArray budgetsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Budget nextBudget : budgets) {
            jsonArray.put(nextBudget.toJson());
        }
        return jsonArray;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Account account = (Account) object;
        if (!username.equals(account.getUsername()) && !password.equals(account.getPassword())) {
            return false;
        }
        return budgets.equals(account.getBudgets());
    }

    @Override
    public int hashCode() {
        int result = username.hashCode() + password.hashCode();
        result = 31 * result + budgets.hashCode();
        return result;
    }
}
