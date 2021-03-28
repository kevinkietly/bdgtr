package persistence;

import model.*;
import model.exceptions.*;
import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a Reader that reads an Account from JSON data stored in the source file.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonReader {
    private String source;

    /**
     * Creates a Reader to read from the source file.
     *
     * @param source the source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * Reads the Account with the given username.
     *
     * @param accountUsername the username of the Account to be read
     * @return the Account with the given username
     * @throws IOException if an error occurs reading data from the file
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws DuplicateBudgetException if the Budget already exists in the Account
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    public Account read(String accountUsername) throws IOException, EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject, accountUsername);
    }

    /**
     * Reads the source file as string.
     *
     * @param source the source file
     * @return the source file as string
     * @throws IOException if an error occurs reading data from the file
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    /**
     * Parses the Account with the given username from the JSON object.
     *
     * @param jsonObject the JSON object
     * @param accountUsername the username of the Account to be parsed
     * @return the parsed Account with the given username
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws DuplicateBudgetException if the Budget already exists in the Account
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    private Account parseAccount(JSONObject jsonObject, String accountUsername) throws EmptyUsernameException,
            EmptyPasswordException, EmptyNameException, NegativeAmountException, DuplicateBudgetException,
            DuplicateCategoryException, NegativeCostException {
        JSONObject accountJsonObject = jsonObject.getJSONObject(accountUsername);
        String username = accountJsonObject.getString("username");
        String password = accountJsonObject.getString("password");
        Account account = new Account(username, password);
        addBudgets(account, accountJsonObject);
        return account;
    }

    /**
     * Parses the budgets from the Account JSON object and adds them to the Account.
     *
     * @param account the Account to which the budgets will be added
     * @param accountJsonObject the Account JSON object
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws DuplicateBudgetException if the Budget already exists in the Account
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    private void addBudgets(Account account, JSONObject accountJsonObject) throws EmptyNameException,
            NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException, NegativeCostException {
        JSONArray jsonArray = accountJsonObject.getJSONArray("budgets");
        for (Object json : jsonArray) {
            JSONObject nextBudget = (JSONObject) json;
            addBudget(account, nextBudget);
        }
    }

    /**
     * Parses a Budget from the Account JSON object and adds it to the list of budgets in the Account.
     *
     * @param account the Account with the list of budgets to which the Budget will be added
     * @param nextBudget the Budget to be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws DuplicateBudgetException if the Budget already exists in the Account
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    private void addBudget(Account account, JSONObject nextBudget) throws EmptyNameException, NegativeAmountException,
            DuplicateBudgetException, DuplicateCategoryException, NegativeCostException {
        String name = nextBudget.getString("name");
        BigDecimal amount = nextBudget.getBigDecimal("amount");
        Budget budget = new Budget(name, amount);
        addCategories(budget, nextBudget);
        account.addBudget(budget);
    }

    /**
     * Parses the categories from the Budget JSON object and adds them to the Budget.
     *
     * @param budget the Budget to which the categories will be added
     * @param nextBudget the Budget JSON object
     * @throws EmptyNameException if the name has length zero
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    private void addCategories(Budget budget, JSONObject nextBudget) throws EmptyNameException,
            DuplicateCategoryException, NegativeCostException {
        JSONArray jsonArray = nextBudget.getJSONArray("categories");
        for (Object json : jsonArray) {
            JSONObject nextCategory = (JSONObject) json;
            addCategory(budget, nextCategory);
        }
    }

    /**
     * Parses a Category from the Budget JSON object and adds it to the list of categories in the Budget.
     *
     * @param budget the Budget with the list of categories to which the Category will be added
     * @param nextCategory the Category to be added
     * @throws EmptyNameException if the name has length zero
     * @throws DuplicateCategoryException if the Category already exists in the Budget
     * @throws NegativeCostException if the cost is negative
     */
    private void addCategory(Budget budget, JSONObject nextCategory) throws EmptyNameException,
            DuplicateCategoryException, NegativeCostException {
        String name = nextCategory.getString("name");
        Category category = new Category(name);
        addTransactions(category, nextCategory);
        budget.addCategory(category);
    }

    /**
     * Parses the transactions from the Category JSON object and adds them to the Category.
     *
     * @param category the Category to which the transactions will be added
     * @param nextCategory the Category JSON object
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeCostException if the cost is negative
     */
    private void addTransactions(Category category, JSONObject nextCategory) throws EmptyNameException,
            NegativeCostException {
        JSONArray jsonArray = nextCategory.getJSONArray("transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransaction = (JSONObject) json;
            addTransaction(category, nextTransaction);
        }
    }

    /**
     * Parses a Transaction from the Category JSON object and adds it to the list of transactions in the Category.
     *
     * @param category the Category with the list of transactions to which the Transaction will be added
     * @param nextTransaction the Transaction to be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeCostException if the cost is negative
     */
    private void addTransaction(Category category, JSONObject nextTransaction) throws EmptyNameException,
            NegativeCostException {
        String name = nextTransaction.getString("name");
        BigDecimal cost = nextTransaction.getBigDecimal("cost");
        String date = nextTransaction.getString("date");
        Transaction transaction = new Transaction(name, cost, date);
        category.addTransaction(transaction);
    }
}
