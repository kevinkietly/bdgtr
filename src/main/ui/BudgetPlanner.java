package ui;

import model.Budget;
import model.Category;
import model.Transaction;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Budget Planner application
// Code referenced from https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
public class BudgetPlanner {
    private static final String MENU_COMMAND = "menu";
    private static final String QUIT_COMMAND = "quit";
    private static final String CREATE_BUDGET_COMMAND = "add budget";
    private static final String VIEW_BUDGETS_COMMAND = "view budgets";
    private static final String ADD_CATEGORY_COMMAND = "add category";
    private static final String ADD_TRANSACTION_COMMAND = "add transaction";

    private List<Budget> budgets;
    private Budget budget;
    private Category category;
    private Transaction transaction;
    private Scanner input;
    private boolean keepGoing;

    // EFFECTS: runs the Budget Planner application
    public BudgetPlanner() {
        keepGoing = true;
        budgets = new ArrayList<>();
        runProgram();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runProgram() {
        String command;

        input = new Scanner(System.in);

        displayLandingPage();

        while (keepGoing) {
            command = input.nextLine();
            command = command.toLowerCase();
            processCommand(command);
        }
        System.out.println("Goodbye.");
    }

    // EFFECTS: displays landing page to user
    private void displayLandingPage() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                  Budget Planner                                  |");
        System.out.println("------------------------------------------------------------------------------------");
        displayOptionsMenu();
    }

    // EFFECTS: displays options menu to user
    private void displayOptionsMenu() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                   Options Menu                                   |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To create a new budget, enter '" + CREATE_BUDGET_COMMAND
                + "'.                                      |");
        System.out.println("| To view your budgets, enter '" + VIEW_BUDGETS_COMMAND
                + "'.                                      |");
        System.out.println("| To quit Budget Planner, enter '" + QUIT_COMMAND
                + "'.                                            |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // EFFECTS: displays all budgets to user
    private void displayBudgets() {
        if (budgets.size() == 0) {
            System.out.println("You do not have any budgets. To create one, enter '" + CREATE_BUDGET_COMMAND + "'.");
        } else {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                   Your Budgets                                   |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.format("%-16s %-16s %-16s %-16s %-16s", "| Name", "| Amount", "| Spent",
                    "| Remaining", "| Date Created |");
            System.out.println("\n------------------------------------------------------------------------------------"
                    + "");
            for (Budget budget : budgets) {
                System.out.format("%-16s %-16s %-16s %-16s %-16s", "| ", "| ", "| ", "| ", "| " + "             |");
                System.out.format("\n%-16s %-16s %-16s %-16s %-16s", "| " + budget.getBudgetName(),
                        "| $" + budget.getBudgetAmount(), "| $" + budget.getBudgetAmountSpent(), "| $"
                                + budget.getBudgetAmountRemaining(), "| " + budget.formatStartingDate() + "    |");
                System.out.println("\n---------------------------------------------------------------------------------"
                        + "---");
            }
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| To access a budget, enter the budget's name.                                     |");
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.length() > 0) {
            if (command.equals(MENU_COMMAND)) {
                displayOptionsMenu();
            } else if (command.equals(CREATE_BUDGET_COMMAND)) {
                createAndAddBudget();
            } else if (command.equals(VIEW_BUDGETS_COMMAND)) {
                displayBudgets();
            } else if (command.equals(ADD_CATEGORY_COMMAND)) {
                createAndAddCategory();
            } else if (isDeleteCategory(command)) {
                updateBudgetDeleteCategory();
            } else if (command.equals(ADD_TRANSACTION_COMMAND)) {
                createAndAddTransaction();
            } else if (isDeleteTransaction(command)) {
                updateBudgetDeleteTransaction();
            } else if (isInBudgets(command)) {
                setBudget(budget);
            } else if (command.equals(QUIT_COMMAND)) {
                keepGoing = false;
            } else {
                processMoreCommands(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes more user commands
    public void processMoreCommands(String command) {
        if (command.length() > 0) {
            if (isInBudget(command)) {
                setCategory(category);
            } else {
                System.out.println("Sorry, you entered an invalid command. Please try again.");
            }
        }
    }

    // EFFECTS: returns true if the given input matches a budget in the list of budgets, false otherwise
    public boolean isInBudgets(String command) {
        boolean containsBudget = false;
        for (Budget budget : budgets) {
            if (command.equalsIgnoreCase(budget.getBudgetName())) {
                containsBudget = true;
                this.budget = budget;
                break;
            }
        }
        return containsBudget;
    }

    // MODIFIES: this
    // EFFECTS: sets the selected budget to the given budget
    public void setBudget(Budget budget) {
        this.budget = budget;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // EFFECTS: returns true if the given input matches a category in the budget, false otherwise
    public boolean isInBudget(String command) {
        boolean containsCategory = false;
        if (budget == null) {
            return false;
        } else {
            for (Category category : budget.getBudgetCategoriesToDisplay()) {
                if (command.equalsIgnoreCase(category.getCategoryName())) {
                    containsCategory = true;
                    this.category = category;
                    break;
                }
            }
        }
        return containsCategory;
    }

    // MODIFIES: this
    // EFFECTS: sets the selected category to the given budget
    public void setCategory(Category category) {
        this.category = category;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // EFFECTS: returns true if the given input to delete a category matches a category in the budget, false otherwise
    public boolean isDeleteCategory(String command) {
        boolean deleteCategory = false;
        if (budget == null) {
            return false;
        } else {
            for (Category category : budget.getBudgetCategoriesToDisplay()) {
                if (command.equalsIgnoreCase("delete category " + category.getCategoryName())) {
                    deleteCategory = true;
                    this.category = category;
                    break;
                }
            }
        }
        if (command.contains("delete category ") && !deleteCategory) {
            System.out.println("The category you want to delete is not in the budget.");
        }
        return deleteCategory;
    }

    // MODIFIES: this
    // EFFECTS: updates the budget after a category is deleted
    public void updateBudgetDeleteCategory() {
        budget.removeCategory(category);
        System.out.println("Category '" + category.getCategoryName() + "' successfully deleted!");
        this.category = null;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // MODIFIES: this
    // EFFECTS: returns true if the given input to delete a transaction matches a transaction in the category, false
    // otherwise
    public boolean isDeleteTransaction(String command) {
        boolean deleteTransaction = false;
        if (category == null) {
            return false;
        } else {
            for (Transaction transaction : category.getCategoryTransactions()) {
                if (command.equalsIgnoreCase("delete transaction " + transaction.getTransactionName())) {
                    deleteTransaction = true;
                    this.transaction = transaction;
                    break;
                }
            }
        }
        if (command.contains("delete transaction ") && !deleteTransaction) {
            System.out.println("The transaction you want to delete is not in the budget.");
        }
        return deleteTransaction;
    }

    // MODIFIES: this
    // EFFECTS: updates the budget after a transaction is deleted
    public void updateBudgetDeleteTransaction() {
        category.removeTransaction(transaction);
        System.out.println("Transaction '" + transaction.getTransactionName() + "' successfully deleted!");
        budget.calculateBudgetAmountRemaining();
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // REQUIRES: the amount of the budget must be a number
    // MODIFIES: this
    // EFFECTS: creates a new budget
    private void createAndAddBudget() {
        System.out.print("Enter the name of your budget: ");
        String budgetName = input.nextLine();
        System.out.print("Enter the amount of your budget (for example, if you want to budget $1000.00, enter "
                + "'1000.00'): $");
        BigDecimal budgetAmount = input.nextBigDecimal();
        budget = new Budget(budgetName, budgetAmount);
        budgets.add(budget);
        System.out.println("Budget successfully created! View it below.");
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds category to a budget
    private void createAndAddCategory() {
        System.out.print("Enter the name of the category: ");
        String categoryName = input.nextLine();
        category = new Category(categoryName);
        if (!budget.addCategory(category)) {
            System.out.println("The category you entered already exists. You cannot add duplicate categories.");
        } else {
            System.out.println("Category successfully added! View it and the rest of your budget below.");
            displayEverythingEmptyCategoriesAndTransactions();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and adds transaction to a category, and formats the given transaction date
    private void createAndAddTransaction() {
        System.out.print("Enter the name of the transaction: ");
        String transactionName = input.nextLine();
        System.out.print("Enter the cost of the transaction: $");
        BigDecimal transactionCost = input.nextBigDecimal();
        System.out.print("Enter the date of the transaction in the format DD-MM-YYYY: ");
        input.nextLine();
        String transactionDate = input.nextLine();
        transaction = new Transaction(transactionName, transactionCost, transactionDate);
        while (!validateTransactionDate(transactionDate)) {
            System.out.println("Sorry, you entered an invalid date. Please try again.");
            System.out.print("Enter the date of the transaction in the format DD-MM-YYYY: ");
            transactionDate = input.nextLine();
        }
        category.addTransaction(transaction);
        budget.calculateBudgetAmountRemaining();
        System.out.println("Transaction successfully added! View it and the rest of your budget below.");
        displayTransactionInTable();
        }

    // MODIFIES: this
    // EFFECTS: displays everything but categories and transactions are empty,
    public void displayEverythingEmptyCategoriesAndTransactions() {
        if (budget.getBudgetCategoriesToDisplay().size() == 0) {
            displayBudgetInTable();
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                    Categories                                    |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| You have no categories. To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                 |");
            System.out.println("------------------------------------------------------------------------------------"
                    + "");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                   Transactions                                   |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| You have no transactions. To add transactions, first add a category.             |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("------------------------------------------------------------------------------------");
            appendViewOptionsMenuInstruction();
        } else {
            displayEverythingEmptyTransactions();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays everything but transactions are empty
    public void displayEverythingEmptyTransactions() {
        if (transactionsIsEmpty()) {
            displayCategoryInTable();
            System.out.println("-----------------------------------------------------------------------------------"
                    + "-");
            System.out.println("|                                   Transactions                                  "
                    + " |");
            System.out.println("----------------------------------------------------------------------------------"
                    + "--");
            System.out.println("| You have no transactions. To add a transaction, enter '"
                    + ADD_TRANSACTION_COMMAND + "'.         |");
            System.out.println("------------------------------------------------------------------------------------");
            appendCategoryInstructions();
            appendViewOptionsMenuInstruction();
        } else {
            displayTransactionInTable();
        }
    }

    // EFFECTS: returns true if list of transactions is empty, false otherwise
    public boolean transactionsIsEmpty() {
        boolean noTransactions = false;
        int numCategoryTransactions = 0;
        for (Category category : budget.getBudgetCategoriesToDisplay()) {
            numCategoryTransactions += category.getCategoryTransactions().size();
        }
        if (numCategoryTransactions == 0) {
            noTransactions = true;
        }
        return noTransactions;
    }

    // MODIFIES: this
    // EFFECTS: displays the budget in a table with columns for name, amount, spent, remaining and date created
    public void displayBudgetInTable() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                      Budget                                      |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-16s %-16s %-16s %-16s %-16s", "| Name", "| Amount", "| Spent",
                "| Remaining", "| Date Created |");
        System.out.println("\n------------------------------------------------------------------------------------");
        System.out.format("%-16s %-16s %-16s %-16s %-16s", "| ", "| ", "| ", "| ", "| " + "             |");
        System.out.format("\n%-16s %-16s %-16s %-16s %-16s", "| " + budget.getBudgetName(),
                "| $" + budget.getBudgetAmount(), "| $" + budget.getBudgetAmountSpent(), "| $"
                        + budget.getBudgetAmountRemaining(), "| " + budget.formatStartingDate() + "    |");
        System.out.println("\n------------------------------------------------------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: displays the category in a table with the list of transactions
    public void displayCategoryInTable() {
        displayBudgetInTable();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                    Categories                                    |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-40s %-40s", "| Name", "| Spent" + "                                   |");
        System.out.println("\n------------------------------------------------------------------------------------");
        for (Category category : budget.getBudgetCategoriesToDisplay()) {
            System.out.format("%-40s %-40s", "| ", "| " + "                                        |");
            System.out.format("\n%-40s %-40s", "| " + category.getCategoryName(), "| $"
                    + formatCategoryAmountSpentInTable(category.getCategoryAmountSpent()));
            System.out.println("\n---------------------------------------------------------------------------------"
                    + "---");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the transaction in a table with the name, cost and date
    public void displayTransactionInTable() {
        displayCategoryInTable();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                   Transactions                                   |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-16s %-24s %-20s %-20s", "| Category", "| Transaction Name", "| Transaction Cost",
                "| Transaction Date  |");
        System.out.println("\n------------------------------------------------------------------------------------");
        for (Category category : budget.getBudgetCategoriesToDisplay()) {
            for (Transaction transaction : category.getCategoryTransactions()) {
                System.out.format("%-16s %-24s %-20s %-20s", "| ", "| ", "| ", "| "
                        + "                  |");
                System.out.format("\n%-16s %-24s %-20s %-20s", "| " + category.getCategoryName(), "| "
                        + transaction.getTransactionName(), "| $" + transaction.getTransactionCost(), "| "
                        + formatTransactionDateInTable(transaction.getTransactionDate()));
                System.out.println("\n---------------------------------------------------------------------------------"
                        + "---");
            }
        }
        appendCategoryInstructions();
        appendTransactionInstructions();
        appendViewOptionsMenuInstruction();
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to view the options menu to the end of the table
    public void appendViewOptionsMenuInstruction() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To view the options menu, enter '" + MENU_COMMAND
                + "'.                                          |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to add or delete a category to the end of the table
    public void appendCategoryInstructions() {
        if (category == null) {
            System.out.println(" SELECTED CATEGORY: No category selected. To select one, enter the category name.   ");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                                         |");
        } else {
            System.out.println(" SELECTED CATEGORY: '" + category.getCategoryName() + "'");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                                         |");
            System.out.println("| To delete a category, enter 'delete category' and the category name.             |");
            System.out.println("| To change the selected category, enter the category name.                        |");
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to add or delete a transaction to the end of the table
    public void appendTransactionInstructions() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To add a transaction, enter '" + ADD_TRANSACTION_COMMAND
                + "'.                                   |");
        System.out.println("| To delete a transaction, enter 'delete transaction' and the transaction name.    |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: formats the given amount spent in the category depending on the value and returns it in the table
    public String formatCategoryAmountSpentInTable(BigDecimal categoryAmountSpent) {
        String formattedCategoryAmountSpent = "";
        BigDecimal ten = new BigDecimal("10.00");
        BigDecimal oneHundred = new BigDecimal("100.00");
        BigDecimal oneThousand = new BigDecimal("1000.00");
        BigDecimal tenThousand = new BigDecimal("10000.00");
        BigDecimal oneHundredThousand = new BigDecimal("100000.00");
        BigDecimal oneMillion = new BigDecimal("1000000.00");
        if (categoryAmountSpent.compareTo(ten) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                   |";
        } else if (categoryAmountSpent.compareTo(oneHundred) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                  |";
        } else if (categoryAmountSpent.compareTo(oneThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                 |";
        } else if (categoryAmountSpent.compareTo(tenThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                |";
        } else if (categoryAmountSpent.compareTo(oneHundredThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                               |";
        } else if (categoryAmountSpent.compareTo(oneMillion) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                              |";
        }
        return formattedCategoryAmountSpent;
    }

    // MODIFIES: this
    // EFFECTS: formats the given transaction date depending on the value and returns it in the table
    public String formatTransactionDateInTable(String transactionDate) {
        String formattedTransactionDate;
        if (transactionDate.length() == 8) {
            formattedTransactionDate = transactionDate + "          |";
        } else if (transactionDate.length() == 9) {
            formattedTransactionDate = transactionDate + "         |";
        } else {
            formattedTransactionDate = transactionDate + "        |";
        }
        return formattedTransactionDate;
    }

    // EFFECTS: returns true if the given input is a valid date, false otherwise
    public boolean validateTransactionDate(String transactionDate) {
        boolean isValid = true;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            dateFormatter.parse(transactionDate);
            transaction.setTransactionDate(transactionDate);
        } catch (DateTimeParseException exception) {
            isValid = false;
        }
        return isValid;
    }
}
