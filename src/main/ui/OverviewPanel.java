package ui;

import model.Account;
import model.Budget;
import model.Category;
import model.exceptions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents the overview panel.
 */
public class OverviewPanel extends JPanel implements FontRepository, ColourRepository {
    private Account account;
    private Budget budget;
    private Category category;
    private JPanel accountPanel;
    private RoundedPanel activeBudgetPanel;
    private RoundedPanel categoriesPanel;
    private IconTextField searchField;
    private JButton addBudgetButton;
    private JButton addCategoryButton;
    private JTextField budgetNameField;
    private JTextField budgetAmountField;
    private JTextField categoryNameField;
    private JOptionPane addBudgetOptionPane;
    private JOptionPane addCategoryOptionPane;
    private JDialog addBudgetDialog;
    private JDialog addCategoryDialog;
    private JLabel emptyBudgetsLabel;
    private JLabel emptyCategoriesLabel;
    private JLabel budgetNameLabel;
    private JLabel budgetStartDateLabel;
    private JLabel budgetAmountLabel;
    private JLabel categoryNameLabel;
    private JLabel categoryAmountSpentLabel;
    private JProgressBar budgetAmountBar;

    /**
     * Creates a new overview panel with the specified account.
     *
     * @param account the account that is signed in
     */
    public OverviewPanel(Account account) {
        this.account = account;
        setLayout(new GridBagLayout());
        initializeSearchField();
        initializeAccountPanel();
        initializeActiveBudgetPanel();
        initializeCategoriesPanel();
    }

    /**
     * Initializes the search field and adds it to this overview panel.
     */
    private void initializeSearchField() {
        searchField = new IconTextField();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        searchField.putClientProperty("JTextField.placeholderText", "Search");
        searchField.setColumns(40);
        searchField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        searchField.setIcon(new ImageIcon("./icons/search.png"));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, -345, 73, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(searchField, gridBagConstraints);
    }

    /**
     * Initializes the account panel and adds it to this overview panel.
     */
    private void initializeAccountPanel() {
        accountPanel = new JPanel(new GridBagLayout());
        JLabel accountLabel = new JLabel(account.getUsername(), new ImageIcon("./icons/account.png"),
                SwingConstants.CENTER);
        JButton accountDropdownButton = new JButton(new ImageIcon("./icons/dropdownArrow.png"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        accountLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        accountLabel.setForeground(Color.WHITE);
        accountLabel.setIconTextGap(15);
        accountDropdownButton.setBorderPainted(false);
        accountDropdownButton.setContentAreaFilled(false);
        accountDropdownButton.setOpaque(false);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        accountPanel.add(accountLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        accountPanel.add(accountDropdownButton, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, 73, -345);
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        add(accountPanel, gridBagConstraints);
    }

    /**
     * Initializes the active budget panel and adds it to this overview panel.
     */
    private void initializeActiveBudgetPanel() {
        activeBudgetPanel = new RoundedPanel();
        JLabel activeBudgetLabel = new JLabel("Active Budget");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        activeBudgetLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        activeBudgetLabel.setForeground(Color.WHITE);
        activeBudgetPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        activeBudgetPanel.setLayout(new GridBagLayout());
        activeBudgetPanel.setPreferredSize(new Dimension(646, 150));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-80, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        activeBudgetPanel.add(activeBudgetLabel, gridBagConstraints);
        initializeAddBudgetButton();
        initializeSeparatorForActiveBudgetPanel();
        initializeActiveBudgetPanelContent();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(-150, -343, 0, 0);
        add(activeBudgetPanel, gridBagConstraints);
    }

    /**
     * Initializes the category panel and adds it to this overview panel.
     */
    private void initializeCategoriesPanel() {
        categoriesPanel = new RoundedPanel();
        JLabel categoriesLabel = new JLabel("Categories");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoriesLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        categoriesLabel.setForeground(Color.WHITE);
        categoriesPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        categoriesPanel.setLayout(new GridBagLayout());
        categoriesPanel.setPreferredSize(new Dimension(270, 300));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-227, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        categoriesPanel.add(categoriesLabel, gridBagConstraints);
        initializeAddCategoryButton();
        initializeSeparatorForCategoriesPanel();
        initializeCategoriesPanelContent();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 0, -343);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(categoriesPanel, gridBagConstraints);
    }

    /**
     * Initializes the add budget button and adds it to the active budget panel.
     */
    private void initializeAddBudgetButton() {
        addBudgetButton = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeAddBudgetDialog();
        addBudgetButton.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
        addBudgetButton.setBackground(ACCENT_COLOUR);
        addBudgetButton.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        addBudgetButton.setForeground(Color.WHITE);
        addBudgetButton.setPreferredSize(new Dimension(30, 30));
        addBudgetButton.addActionListener(event -> addBudgetDialog.setVisible(true));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-80, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        activeBudgetPanel.add(addBudgetButton, gridBagConstraints);
    }

    /**
     * Initializes the add category button and adds it to the categories panel.
     */
    private void initializeAddCategoryButton() {
        addCategoryButton = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeAddCategoryDialog();
        addCategoryButton.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
        addCategoryButton.setBackground(ACCENT_COLOUR);
        addCategoryButton.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        addCategoryButton.setForeground(Color.WHITE);
        addCategoryButton.setPreferredSize(new Dimension(30, 30));
        addCategoryButton.addActionListener(event -> addCategoryDialog.setVisible(true));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-227, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        categoriesPanel.add(addCategoryButton, gridBagConstraints);
    }

    /**
     * Initializes and shows the dialog to add a budget.
     */
    private void initializeAddBudgetDialog() {
        budgetNameField = new JTextField(10);
        budgetAmountField = new JTextField(10);
        Object[] fields = {"Enter the name for this budget:", budgetNameField, "Set the amount for this budget",
                budgetAmountField};
        addBudgetOptionPane = new JOptionPane(fields, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        addBudgetDialog = new JDialog((Dialog) null, "bdgtr");
        initializePropertyChangeListenerForAddBudgetOptionPane();
        addBudgetDialog.setContentPane(addBudgetOptionPane);
        addBudgetDialog.pack();
        addBudgetDialog.setLocationRelativeTo(null);
    }

    /**
     * Initializes and shows the dialog to add a category.
     */
    private void initializeAddCategoryDialog() {
        categoryNameField = new JTextField(10);
        Object[] field = {"Enter the name for this category:", categoryNameField};
        addCategoryOptionPane = new JOptionPane(field, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        addCategoryDialog = new JDialog((Dialog) null, "bdgtr");
        initializePropertyChangeListenerForAddCategoryOptionPane();
        addCategoryDialog.setContentPane(addCategoryOptionPane);
        addCategoryDialog.pack();
        addCategoryDialog.setLocationRelativeTo(null);
    }

    /**
     * Initializes the property change listener for the option pane to add a budget.
     */
    private void initializePropertyChangeListenerForAddBudgetOptionPane() {
        addBudgetOptionPane.addPropertyChangeListener(event -> {
            if (addBudgetDialog.isVisible() && (event.getSource() == addBudgetOptionPane)) {
                Object option = addBudgetOptionPane.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                addBudgetOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    addBudget();
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    addBudgetDialog.dispose();
                }
            }
        });
    }

    /**
     * Initializes the property change listener for the option pane to add a category.
     */
    private void initializePropertyChangeListenerForAddCategoryOptionPane() {
        addCategoryOptionPane.addPropertyChangeListener(event -> {
            if (addCategoryDialog.isVisible() && (event.getSource() == addCategoryOptionPane)) {
                Object option = addCategoryOptionPane.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                addCategoryOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    addCategory();
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    addCategoryDialog.dispose();
                }
            }
        });
    }

    /**
     * Initializes the content of the active budget panel. Shows the "You have no budgets." label if the account has no
     * budgets, show most recently added budget otherwise.
     */
    private void initializeActiveBudgetPanelContent() {
        if (account.getBudgets().isEmpty()) {
            emptyBudgetsLabel = new JLabel("You have no budgets.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyBudgetsLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyBudgetsLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(0, 0, -70, 0);
            activeBudgetPanel.add(emptyBudgetsLabel, gridBagConstraints);
        } else {
            budget = account.getBudgets().get(account.getBudgets().size() - 1);
            budgetNameLabel = new JLabel(budget.getName());
            budgetStartDateLabel = new JLabel(budget.getStartDate());
            budgetAmountLabel = new JLabel("＄" + budget.getAmountSpent().toString() + " / ＄"
                    + budget.getAmount().toString());
            budgetAmountBar = new JProgressBar();
            initializeActiveBudget();
        }
    }

    /**
     * Initializes the content of the categories panel. Shows the "You have no categories." label if the active budget
     * has no categories, show categories otherwise.
     */
    private void initializeCategoriesPanelContent() {
        if (account.getBudgets().isEmpty() || budget.getCategories().isEmpty()) {
            emptyCategoriesLabel = new JLabel("You have no categories.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyCategoriesLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyCategoriesLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(0, 0, -67, 0);
            categoriesPanel.add(emptyCategoriesLabel, gridBagConstraints);
        } else {
            for (Category nextCategory : budget.getCategories()) {
                categoryNameLabel = new JLabel(nextCategory.getName());
                categoryAmountSpentLabel = new JLabel("＄" + nextCategory.getAmountSpent());
                initializeCategories();
            }
        }
    }

    /**
     * Initializes the active budget (most recently added budget).
     */
    private void initializeActiveBudget() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        budgetNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_BOLD);
        budgetNameLabel.setForeground(Color.WHITE);
        budgetStartDateLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetStartDateLabel.setForeground(Color.GRAY);
        budgetAmountLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_BOLD);
        budgetAmountLabel.setForeground(Color.WHITE);
        budgetAmountBar.setPreferredSize(new Dimension(300, 15));
        budgetAmountBar.setStringPainted(true);
        budgetAmountBar.setValue(budget.getAmountSpent().divide(budget.getAmount(), RoundingMode.HALF_EVEN).intValue());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -50, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        activeBudgetPanel.add(budgetNameLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -90, 0);
        activeBudgetPanel.add(budgetStartDateLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -70, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        activeBudgetPanel.add(budgetAmountLabel, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        activeBudgetPanel.add(budgetAmountBar, gridBagConstraints);
        refresh();
    }

    /**
     * Initializes the categories in the active budget.
     */
    private void initializeCategories() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoryNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_BOLD);
        categoryNameLabel.setForeground(Color.WHITE);
        categoryAmountSpentLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_BOLD);
        categoryAmountSpentLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -50, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        categoriesPanel.add(categoryNameLabel, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        categoriesPanel.add(categoryAmountSpentLabel, gridBagConstraints);
        refresh();
    }

    /**
     * Initializes the separator and adds it to the active budget panel.
     */
    private void initializeSeparatorForActiveBudgetPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-10, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        activeBudgetPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator and adds it to the categories panel.
     */
    private void initializeSeparatorForCategoriesPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-160, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        categoriesPanel.add(separator, gridBagConstraints);
    }

    /**
     * Adds the budget if it does not already exist in the account and all fields are valid, try again otherwise.
     */
    private void addBudget() {
        try {
            budget = new Budget(budgetNameField.getText(), new BigDecimal(budgetAmountField.getText()));
            refreshActiveBudgetPanel();
            account.addBudget(budget);
            initializeActiveBudgetPanelContent();
            addBudgetDialog.setVisible(false);
        } catch (EmptyNameException | NegativeAmountException | ZeroAmountException
                | DuplicateBudgetException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "bdgtr",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "You must enter an amount.", "bdgtr",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds the category if it does not already exist in the active budget and the name of the category is valid,
     * try again otherwise.
     */
    private void addCategory() {
        try {
            category = new Category(categoryNameField.getText());
            refreshCategoriesPanel();
            budget.addCategory(category);
            initializeCategoriesPanelContent();
            addCategoryDialog.setVisible(false);
        } catch (EmptyNameException | DuplicateCategoryException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "bdgtr",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the active budget panel.
     */
    private void refreshActiveBudgetPanel() {
        if (account.getBudgets().isEmpty()) {
            activeBudgetPanel.remove(emptyBudgetsLabel);
        } else {
            activeBudgetPanel.remove(budgetNameLabel);
            activeBudgetPanel.remove(budgetStartDateLabel);
            activeBudgetPanel.remove(budgetAmountLabel);
            activeBudgetPanel.remove(budgetAmountBar);
        }
        refresh(activeBudgetPanel);
    }

    /**
     * Refreshes the categories panel.
     */
    private void refreshCategoriesPanel() {
        if (budget.getCategories().isEmpty()) {
            categoriesPanel.remove(emptyCategoriesLabel);
        }
        refresh(categoriesPanel);
    }

    /**
     * Refreshes this overview panel.
     */
    private void refresh() {
        revalidate();
        repaint();
    }

    /**
     * Refreshes the specified panel.
     *
     * @param panel the panel to be refreshed
     */
    private void refresh(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}
