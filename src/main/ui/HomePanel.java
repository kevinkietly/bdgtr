package ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarBorderProperties;
import model.Account;
import model.Budget;
import model.Category;
import model.Transaction;
import model.exceptions.*;
import org.jfree.chart.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CenterTextMode;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.DefaultPieDataset;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Represents the home panel.
 */
public class HomePanel extends JPanel implements ColourRepository, FontRepository, SoundRepository {
    private final DecimalFormat decimalFormat = new DecimalFormat("＄#,##0.00");
    private Account account;
    private Budget budget;
    private boolean isBudgetAdded;
    private boolean isCategoryAdded;
    private boolean isTransactionAdded;
    private ResettableDialog dialogToAddBudget;
    private ResettableDialog dialogToAddCategory;
    private ResettableDialog dialogToAddTransaction;
    private RoundedPanel activeBudgetPanel;
    private RoundedPanel categoriesPanel;
    private RoundedPanel transactionsPanel;
    private JScrollPane categoriesScrollPane;
    private JScrollPane transactionsScrollPane;
    private JOptionPane optionPaneToAddBudget;
    private JOptionPane optionPaneToAddCategory;
    private JOptionPane optionPaneToAddTransaction;
    private JComboBox<Budget> budgetComboBox;
    private JComboBox<Category> categoryComboBox;
    private DatePickerSettings transactionDatePickerSettings;
    private DatePicker transactionDatePicker;
    private JButton buttonToAddBudget;
    private JButton buttonToDeleteBudget;
    private JButton buttonToAddCategory;
    private JButton buttonToAddTransaction;
    private JTextField budgetNameField;
    private JTextField budgetAmountField;
    private JTextField categoryNameField;
    private JTextField transactionNameField;
    private JTextField transactionAmountField;
    private JTextField transactionDateField;
    private JProgressBar budgetProgressBar;
    private JLabel emptyBudgetsLabel;
    private JLabel emptyCategoriesLabel;
    private JLabel emptyTransactionsLabel;
    private JLabel budgetAmountRemainingLabel;
    private JLabel budgetStartDateLabel;
    private JLabel transactionNameLabel;
    private JLabel transactionAmountLabel;
    private JLabel transactionDateLabel;
    private JLabel transactionCategoryLabel;
    private DefaultTableCellRenderer leftHeaderRenderer;
    private javax.swing.Timer timerForButtonToAddBudget;
    private javax.swing.Timer timerForButtonToAddCategory;
    private javax.swing.Timer timerForButtonToAddTransaction;
    private RingPlot ringPlot;
    private ChartPanel chartPanel;

    /**
     * Creates a new home panel with the specified account.
     * If the account is new, start step one of user onboarding.
     *
     * @param account the account the user is signed in to
     * @param isNewAccount determines if the account is new
     */
    public HomePanel(Account account, boolean isNewAccount) {
        this.account = account;
        setLayout(new GridBagLayout());
        initializeTextFields();
        initializeActiveBudgetPanel();
        initializeCategoriesPanel();
        initializeTransactionsPanel();
        if (isNewAccount) {
            userOnboardingStepOne();
        }
    }

    /**
     * Initializes all text fields.
     */
    private void initializeTextFields() {
        budgetNameField = new JTextField(12);
        budgetAmountField = new JTextField(12);
        categoryNameField = new JTextField(12);
        transactionNameField = new JTextField(12);
        transactionAmountField = new JTextField(12);
        transactionDateField = new JTextField(12);
        setPropertiesForTextFields();
    }

    /**
     * Sets the properties for all text fields.
     */
    private void setPropertiesForTextFields() {
        budgetNameField.putClientProperty("JTextField.placeholderText", "Name");
        budgetNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountField.putClientProperty("JTextField.placeholderText", "0.00");
        budgetAmountField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        categoryNameField.putClientProperty("JTextField.placeholderText", "Name");
        categoryNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionNameField.putClientProperty("JTextField.placeholderText", "Name");
        transactionNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionAmountField.putClientProperty("JTextField.placeholderText", "0.00");
        transactionAmountField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDateField.setEditable(false);
        transactionDateField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
    }

    /**
     * Initializes the active budget panel and adds it to this home panel.
     */
    private void initializeActiveBudgetPanel() {
        JLabel activeBudgetLabel = new JLabel("Active Budget");
        activeBudgetPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        activeBudgetLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        activeBudgetLabel.setForeground(Color.WHITE);
        activeBudgetPanel.setPreferredSize(new Dimension(1060, 150));
        activeBudgetPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        activeBudgetPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        activeBudgetPanel.add(activeBudgetLabel, gridBagConstraints);
        initializeButtonToAddBudget();
        initializeButtonToDeleteBudget();
        initializeSeparatorForActiveBudgetPanel();
        initializeBudgetComboBox();
        initializeContentForActiveBudgetPanel();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new Insets(-188, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        add(activeBudgetPanel, gridBagConstraints);
    }

    /**
     * Initializes the categories panel and adds it to this home panel.
     */
    private void initializeCategoriesPanel() {
        JLabel categoriesLabel = new JLabel("Categories");
        categoriesPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoriesLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        categoriesLabel.setForeground(Color.WHITE);
        categoriesPanel.setPreferredSize(new Dimension(350, 507));
        categoriesPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        categoriesPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        categoriesPanel.add(categoriesLabel, gridBagConstraints);
        initializeButtonToAddCategory();
        initializeFirstSeparatorForCategoriesPanel();
        initializeContentForCategoriesPanel();
        initializeSecondSeparatorForCategoriesPanel();
        initializeDoughnutChart();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -188, 0);
        add(categoriesPanel, gridBagConstraints);
        refresh();
    }

    /**
     * Initializes the transactions panel and adds it to this home panel.
     */
    private void initializeTransactionsPanel() {
        JLabel transactionsLabel = new JLabel("Transactions");
        transactionsPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        transactionsLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        transactionsLabel.setForeground(Color.WHITE);
        transactionsPanel.setPreferredSize(new Dimension(672, 507));
        transactionsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        transactionsPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        transactionsPanel.add(transactionsLabel, gridBagConstraints);
        initializeButtonToAddTransaction();
        initializeSeparatorForTransactionsPanel();
        initializeContentForTransactionsPanel();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -188, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        add(transactionsPanel, gridBagConstraints);
        refresh();
    }

    /**
     * Step one of user onboarding (add a budget). This method makes the button to add a budget flash.
     */
    private void userOnboardingStepOne() {
        timerForButtonToAddBudget = new Timer(500, new ActionListener() {
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent event) {
                counter++;
                if (counter % 2 == 0) {
                    buttonToAddBudget.setBackground(ACCENT_COLOUR);
                } else {
                    buttonToAddBudget.setBackground(ACCENT_BORDER_COLOUR);
                }
            }
        });
        timerForButtonToAddBudget.start();
    }

    /**
     * Step two of user onboarding (add a category). This method makes the button to add a category flash.
     */
    private void userOnboardingStepTwo() {
        timerForButtonToAddBudget.stop();
        buttonToAddBudget.setBackground(ACCENT_COLOUR);
        buttonToAddBudget.setEnabled(false);
        JOptionPane.showMessageDialog(null,
                "Then, add a category by clicking the flashing button in the 'Categories' panel.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        timerForButtonToAddCategory = new Timer(500, new ActionListener() {
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent event) {
                counter++;
                if (counter % 2 == 0) {
                    buttonToAddCategory.setBackground(ACCENT_COLOUR);
                } else {
                    buttonToAddCategory.setBackground(ACCENT_BORDER_COLOUR);
                }
            }
        });
        timerForButtonToAddCategory.start();
    }

    /**
     * Step three of user onboarding (add a transaction). This method makes the button to add a transaction flash.
     */
    private void userOnboardingStepThree() {
        timerForButtonToAddCategory.stop();
        buttonToAddCategory.setBackground(ACCENT_COLOUR);
        JOptionPane.showMessageDialog(null,
                "Now, add a transaction by clicking the flashing button in the 'Transactions' panel.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        timerForButtonToAddTransaction = new Timer(500, new ActionListener() {
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent event) {
                counter++;
                if (counter % 2 == 0) {
                    buttonToAddTransaction.setBackground(ACCENT_COLOUR);
                } else {
                    buttonToAddTransaction.setBackground(ACCENT_BORDER_COLOUR);
                }
            }
        });
        timerForButtonToAddTransaction.start();
    }

    /**
     * Step four of user onboarding (save changes and sign out).
     */
    private void userOnboardingStepFour() {
        JFrame mainWindow = (JFrame) getTopLevelAncestor();
        mainWindow.getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
        mainWindow.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
        timerForButtonToAddTransaction.stop();
        buttonToAddTransaction.setBackground(ACCENT_COLOUR);
        JOptionPane.showMessageDialog(null,
                "Congratulations on adding your first budget, category, and transaction!"
                        + " To demonstrate how your changes are saved, click the top-left menu that shows your name,"
                        + " then click the 'Sign Out' button. You will be notified that you have unsaved changes."
                        + " Click 'Yes' to save your changes, then sign in. You will see that your changes were saved."
                        + " Happy budgeting!", "bdgtr", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Initializes the button to add a budget and adds the button to the active budget panel.
     */
    private void initializeButtonToAddBudget() {
        buttonToAddBudget = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeOptionPaneToAddBudget();
        initializeDialogToAddBudget();
        buttonToAddBudget.setPreferredSize(new Dimension(30, 30));
        buttonToAddBudget.setBorderPainted(false);
        buttonToAddBudget.setBackground(ACCENT_COLOUR);
        buttonToAddBudget.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        buttonToAddBudget.setForeground(Color.WHITE);
        addActionListenerToButtonToAddBudget();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 43);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        activeBudgetPanel.add(buttonToAddBudget, gridBagConstraints);
    }

    /**
     * Initializes the button to delete a budget and adds the button to the active budget panel.
     */
    private void initializeButtonToDeleteBudget() {
        buttonToDeleteBudget = new JButton("－");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToDeleteBudget.setPreferredSize(new Dimension(30, 30));
        buttonToDeleteBudget.setBorderPainted(false);
        if (account.getBudgets().isEmpty()) {
            buttonToDeleteBudget.setEnabled(false);
        }
        buttonToDeleteBudget.setBackground(ERROR_COLOUR);
        buttonToDeleteBudget.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToDeleteBudget.setForeground(Color.WHITE);
        addActionListenerToButtonToDeleteBudget();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        activeBudgetPanel.add(buttonToDeleteBudget, gridBagConstraints);
    }

    /**
     * Initializes the button to add a category and adds the button to the categories panel.
     */
    private void initializeButtonToAddCategory() {
        buttonToAddCategory = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeOptionPaneToAddCategory();
        initializeDialogToAddCategory();
        buttonToAddCategory.setPreferredSize(new Dimension(30, 30));
        buttonToAddCategory.setBorderPainted(false);
        buttonToAddCategory.setBackground(ACCENT_COLOUR);
        buttonToAddCategory.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        buttonToAddCategory.setForeground(Color.WHITE);
        addActionListenerToButtonToAddCategory();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        categoriesPanel.add(buttonToAddCategory, gridBagConstraints);
    }

    /**
     * Initializes the button to add a transaction and adds the button to the transactions panel.
     */
    private void initializeButtonToAddTransaction() {
        buttonToAddTransaction = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeOptionPaneToAddTransaction();
        initializeDialogToAddTransaction();
        buttonToAddTransaction.setPreferredSize(new Dimension(30, 30));
        buttonToAddTransaction.setBorderPainted(false);
        buttonToAddTransaction.setBackground(ACCENT_COLOUR);
        buttonToAddTransaction.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        buttonToAddTransaction.setForeground(Color.WHITE);
        addActionListenerToButtonToAddTransaction();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        transactionsPanel.add(buttonToAddTransaction, gridBagConstraints);
    }

    /**
     * Adds an action listener to the button to add a budget.
     */
    private void addActionListenerToButtonToAddBudget() {
        buttonToAddBudget.addActionListener(event -> {
            buttonToAddBudget.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddBudget.setBorderPainted(true);
            dialogToAddBudget.setVisible(true);
        });
    }

    /**
     * Adds an action listener to the button to delete a budget.
     */
    private void addActionListenerToButtonToDeleteBudget() {
        buttonToDeleteBudget.addActionListener(event -> {
            buttonToDeleteBudget.putClientProperty("JComponent.outline", ERROR_BORDER_COLOUR);
            buttonToDeleteBudget.setBorderPainted(true);
            if (account.getBudgets().size() == 1) {
                JOptionPane.showMessageDialog(null, "You must always have an active budget."
                                + " If you want to delete this budget, add a new one first.", "bdgtr",
                        JOptionPane.INFORMATION_MESSAGE);
                buttonToDeleteBudget.setBorderPainted(false);
                refresh(buttonToDeleteBudget);
            } else {
                deleteBudget();
            }
        });
    }

    /**
     * Adds an action listener to the button to add a category.
     */
    private void addActionListenerToButtonToAddCategory() {
        buttonToAddCategory.addActionListener(event -> {
            buttonToAddCategory.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddCategory.setBorderPainted(true);
            if (account.getBudgets().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a budget first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budget.getCategories().size() == 10) {
                JOptionPane.showMessageDialog(null,
                        "You can only have up to 10 categories in a budget.", "bdgtr",
                        JOptionPane.ERROR_MESSAGE);
            } else if (budgetProgressBar.getValue() == 100) {
                JOptionPane.showMessageDialog(null, "This budget has been exhausted!",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else {
                dialogToAddCategory.setVisible(true);
            }
        });
    }

    /**
     * Adds an action listener to the button to add a transaction.
     */
    private void addActionListenerToButtonToAddTransaction() {
        buttonToAddTransaction.addActionListener(event -> {
            buttonToAddTransaction.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddTransaction.setBorderPainted(true);
            if (account.getBudgets().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a budget first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budget.getCategories().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a category first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budgetProgressBar.getValue() == 100) {
                JOptionPane.showMessageDialog(null, "This budget has been exhausted!",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else {
                dialogToAddTransaction.setVisible(true);
            }
        });
    }

    /**
     * Initializes the option pane to add a budget.
     */
    private void initializeOptionPaneToAddBudget() {
        JLabel budgetNameLabel = new JLabel("Enter the name for this budget:");
        JLabel budgetAmountLabel = new JLabel("Set the amount for this budget:");
        JPanel budgetLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel budgetFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToAddBudget = new JPanel(new BorderLayout(10, 10));
        optionPaneToAddBudget = new JOptionPane(panelToAddBudget, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        budgetNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetNameLabel.setForeground(Color.WHITE);
        budgetAmountLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountLabel.setForeground(Color.WHITE);
        budgetLabelsPanel.add(budgetNameLabel);
        budgetLabelsPanel.add(budgetAmountLabel);
        budgetFieldsPanel.add(budgetNameField);
        budgetFieldsPanel.add(budgetAmountField);
        panelToAddBudget.add(budgetLabelsPanel, BorderLayout.LINE_START);
        panelToAddBudget.add(budgetFieldsPanel, BorderLayout.CENTER);
        addPropertyChangeListenerToOptionPaneToAddBudget();
    }

    /**
     * Initializes the option pane to add a category.
     */
    private void initializeOptionPaneToAddCategory() {
        JLabel categoryNameLabel = new JLabel("Enter the name for this category:");
        JPanel categoryLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel categoryFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToAddCategory = new JPanel(new BorderLayout(10, 10));
        optionPaneToAddCategory = new JOptionPane(panelToAddCategory, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        categoryNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        categoryNameLabel.setForeground(Color.WHITE);
        categoryLabelsPanel.add(categoryNameLabel);
        categoryFieldsPanel.add(categoryNameField);
        panelToAddCategory.add(categoryLabelsPanel, BorderLayout.LINE_START);
        panelToAddCategory.add(categoryFieldsPanel, BorderLayout.CENTER);
        addPropertyChangeListenerToOptionPaneToAddCategory();
    }

    /**
     * Initializes the option pane to add a transaction.
     */
    private void initializeOptionPaneToAddTransaction() {
        JPanel transactionDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel transactionLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel transactionFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToAddTransaction = new JPanel(new BorderLayout(10, 10));
        optionPaneToAddTransaction = new JOptionPane(panelToAddTransaction, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        initializeComponentsForOptionPaneToAddTransaction();
        transactionDatePanel.add(transactionDateField);
        transactionDatePanel.add(transactionDatePicker);
        transactionLabelsPanel.add(transactionNameLabel);
        transactionLabelsPanel.add(transactionAmountLabel);
        transactionLabelsPanel.add(transactionDateLabel);
        transactionLabelsPanel.add(transactionCategoryLabel);
        transactionFieldsPanel.add(transactionNameField);
        transactionFieldsPanel.add(transactionAmountField);
        transactionFieldsPanel.add(transactionDatePanel);
        if (!account.getBudgets().isEmpty()) {
            transactionFieldsPanel.add(categoryComboBox);
        }
        panelToAddTransaction.add(transactionLabelsPanel, BorderLayout.LINE_START);
        panelToAddTransaction.add(transactionFieldsPanel, BorderLayout.CENTER);
        addPropertyChangeListenerToOptionPaneToAddTransaction();
    }

    /**
     * Initializes the option pane to delete a category or transaction.
     *
     * @param message the message of the option pane
     * @return either 0 or 1 in which 0 represents the "Yes" option and 1 represents the "No" option
     */
    private int initializeOptionPaneToDelete(String message) {
        String[] buttonLabels = {"Yes", "No"};
        String defaultOption = buttonLabels[0];
        return JOptionPane.showOptionDialog(null,
                message, "bdgtr", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, buttonLabels,
                defaultOption);
    }

    /**
     * Initializes the components for the option pane to add a transaction.
     */
    private void initializeComponentsForOptionPaneToAddTransaction() {
        initializeTransactionLabelsForOptionPaneToAddTransaction();
        initializeTransactionDatePickerForOptionPaneToAddTransaction();
        initializeCategoryComboBox();
    }

    /**
     * Initializes the transaction labels for the option pane to add a transaction.
     */
    private void initializeTransactionLabelsForOptionPaneToAddTransaction() {
        transactionNameLabel = new JLabel("Enter the name for this transaction:");
        transactionAmountLabel = new JLabel("Enter the amount for this transaction:");
        transactionDateLabel = new JLabel("Pick the date for this transaction:");
        transactionCategoryLabel = new JLabel("Pick the category for this transaction:");
        transactionNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionNameLabel.setForeground(Color.WHITE);
        transactionAmountLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionAmountLabel.setForeground(Color.WHITE);
        transactionDateLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDateLabel.setForeground(Color.WHITE);
        transactionCategoryLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionCategoryLabel.setForeground(Color.WHITE);
    }

    /**
     * Initializes the transaction date picker for the option pane to add a transaction.
     */
    private void initializeTransactionDatePickerForOptionPaneToAddTransaction() {
        transactionDatePickerSettings = new DatePickerSettings();
        transactionDatePicker = new DatePicker(transactionDatePickerSettings);
        JButton transactionDatePickerButton = transactionDatePicker.getComponentToggleCalendarButton();
        ArrayList<CalendarBorderProperties> borderProperties = new ArrayList<>();
        borderProperties.add(new CalendarBorderProperties(new Point(1, 1), new Point(5, 5),
                null, null));
        transactionDatePickerSettings.setBorderCalendarPopup(BorderFactory.createEmptyBorder());
        transactionDatePickerSettings.setBorderPropertiesList(borderProperties);
        transactionDatePickerSettings.setEnableMonthMenu(false);
        transactionDatePickerSettings.setEnableYearMenu(false);
        transactionDatePickerSettings.setVisibleDateTextField(false);
        transactionDatePickerSettings.setVisibleClearButton(false);
        transactionDatePickerSettings.setVisibleTodayButton(false);
        setPropertiesForTransactionDatePicker();
        transactionDatePicker.setDateToToday();
        transactionDatePicker.addDateChangeListener(dateChangeEvent -> {
            transactionDateField.putClientProperty("JTextField.placeholderText", transactionDatePicker.getText());
            refresh(optionPaneToAddTransaction);
        });
        transactionDatePickerButton.setText("");
        transactionDatePickerButton.setIcon(new ImageIcon("./icons/Calendar.png"));
        transactionDateField.putClientProperty("JTextField.placeholderText", transactionDatePicker.getText());
        refresh(optionPaneToAddTransaction);
    }

    /**
     * Sets the properties for the transaction date picker.
     */
    private void setPropertiesForTransactionDatePicker() {
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels,
                BACKGROUND_COLOUR);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel,
                BACKGROUND_COLOUR);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates,
                BACKGROUND_COLOUR);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate,
                ACCENT_COLOUR);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate, ACCENT_COLOUR);
        transactionDatePickerSettings.setColorBackgroundWeekdayLabels(BACKGROUND_COLOUR, false);
        transactionDatePickerSettings.setFontCalendarDateLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDatePickerSettings.setFontCalendarWeekdayLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDatePickerSettings.setFontMonthAndYearMenuLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDatePickerSettings.setFontMonthAndYearNavigationButtons(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextNormalDates, Color.WHITE);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, Color.GRAY);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, Color.WHITE);
        transactionDatePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons,
                Color.WHITE);
    }

    /**
     * Adds a property change listener to the option pane to add a budget.
     */
    private void addPropertyChangeListenerToOptionPaneToAddBudget() {
        optionPaneToAddBudget.addPropertyChangeListener(event -> {
            if (dialogToAddBudget.isVisible() && (event.getSource() == optionPaneToAddBudget)
                    && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))) {
                Object option = optionPaneToAddBudget.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                optionPaneToAddBudget.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    addBudget();
                    if (timerForButtonToAddBudget != null && isBudgetAdded) {
                        userOnboardingStepTwo();
                    }
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddBudget.setVisible(false);
                }
                buttonToAddBudget.setBorderPainted(false);
            }
        });
    }

    /**
     * Adds a property change listener to the option pane to add a category.
     */
    private void addPropertyChangeListenerToOptionPaneToAddCategory() {
        optionPaneToAddCategory.addPropertyChangeListener(event -> {
            if (dialogToAddCategory.isVisible() && (event.getSource().equals(optionPaneToAddCategory))
                    && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))) {
                Object option = optionPaneToAddCategory.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                optionPaneToAddCategory.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    addCategory();
                    if (timerForButtonToAddCategory != null && isCategoryAdded) {
                        userOnboardingStepThree();
                    }
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddCategory.setVisible(false);
                }
                buttonToAddCategory.setBorderPainted(false);
            }
        });
    }

    /**
     * Adds a property change listener to the option pane to add a transaction.
     */
    private void addPropertyChangeListenerToOptionPaneToAddTransaction() {
        optionPaneToAddTransaction.addPropertyChangeListener(event -> {
            if (dialogToAddTransaction.isVisible() && (event.getSource().equals(optionPaneToAddTransaction))
                    && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))) {
                Object option = optionPaneToAddTransaction.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                optionPaneToAddTransaction.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    addTransaction((Category) Objects.requireNonNull(categoryComboBox.getSelectedItem()));
                    if (timerForButtonToAddTransaction != null && isTransactionAdded) {
                        userOnboardingStepFour();
                    }
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddTransaction.setVisible(false);
                }
                buttonToAddTransaction.setBorderPainted(false);
            }
        });
    }

    /**
     * Initializes the dialog to add a budget.
     */
    private void initializeDialogToAddBudget() {
        List<JTextField> budgetFields = Arrays.asList(budgetNameField, budgetAmountField);
        dialogToAddBudget = new ResettableDialog("bdgtr", budgetFields);
        dialogToAddBudget.setContentPane(optionPaneToAddBudget);
        dialogToAddBudget.pack();
        dialogToAddBudget.setLocationRelativeTo(null);
    }

    /**
     * Initializes the dialog to add a category.
     */
    private void initializeDialogToAddCategory() {
        List<JTextField> categoryFields = Collections.singletonList(categoryNameField);
        dialogToAddCategory = new ResettableDialog("bdgtr", categoryFields);
        dialogToAddCategory.setContentPane(optionPaneToAddCategory);
        dialogToAddCategory.pack();
        dialogToAddCategory.setLocationRelativeTo(null);
    }

    /**
     * Initializes the dialog to add a transaction.
     */
    private void initializeDialogToAddTransaction() {
        List<JTextField> transactionFields = Arrays.asList(transactionNameField, transactionAmountField);
        dialogToAddTransaction = new ResettableDialog("bdgtr", transactionFields);
        dialogToAddTransaction.setContentPane(optionPaneToAddTransaction);
        dialogToAddTransaction.pack();
        dialogToAddTransaction.setLocationRelativeTo(null);
    }

    /**
     * Initializes the combo box with the budgets in the account.
     */
    private void initializeBudgetComboBox() {
        budgetComboBox = new JComboBox<>(account.getBudgets().toArray(new Budget[0]));
        budgetComboBox.setPreferredSize(new Dimension(122, 30));
        budgetComboBox.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetComboBox.setForeground(Color.WHITE);
        addItemListenerForBudgetComboBox();
    }

    /**
     * Initializes the combo box with the categories in the active budget.
     */
    private void initializeCategoryComboBox() {
        if (!account.getBudgets().isEmpty()) {
            categoryComboBox = new JComboBox<>(budget.getCategories().toArray(new Category[0]));
            categoryComboBox.setPreferredSize(new Dimension(157, 30));
            categoryComboBox.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            categoryComboBox.setForeground(Color.WHITE);
        }
    }

    /**
     * Adds an item listener to the budget combo box.
     */
    private void addItemListenerForBudgetComboBox() {
        budgetComboBox.addItemListener(event -> {
            if (emptyBudgetsLabel != null) {
                activeBudgetPanel.remove(emptyBudgetsLabel);
                refresh(activeBudgetPanel);
            }
            if (event.getStateChange() == ItemEvent.DESELECTED) {
                budget = (Budget) event.getItem();
                updateActiveBudgetPanel();
                remove(categoriesPanel);
                remove(transactionsPanel);
                refresh();
            } else if (event.getStateChange() == ItemEvent.SELECTED) {
                budget = (Budget) event.getItem();
                if (budget.numberOfTransactions() > 0) {
                    emptyTransactionsLabel = null;
                }
                initializeComponentsForActiveBudgetPanel();
                initializeCategoriesPanel();
                initializeTransactionsPanel();
            }
        });
    }

    /**
     * Initializes the content for the active budget panel. Shows the "You have no budgets."
     * label if the account has no budgets, initializes the components for the active budget panel otherwise.
     */
    private void initializeContentForActiveBudgetPanel() {
        if (account.getBudgets().isEmpty()) {
            emptyBudgetsLabel = new JLabel("You have no budgets.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyBudgetsLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyBudgetsLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(0, 0, -60, 0);
            activeBudgetPanel.add(emptyBudgetsLabel, gridBagConstraints);
        } else {
            budget = (Budget) budgetComboBox.getSelectedItem();
            initializeComponentsForActiveBudgetPanel();
        }
    }

    /**
     * Initializes the content for the categories panel. Shows the "You have no categories."
     * label if the active budget has no categories, initializes the categories table otherwise.
     */
    private void initializeContentForCategoriesPanel() {
        if (account.getBudgets().isEmpty() || budget.getCategories().isEmpty()) {
            emptyCategoriesLabel = new JLabel("You have no categories.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyCategoriesLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyCategoriesLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(92, 0, 28, 0);
            categoriesPanel.add(emptyCategoriesLabel, gridBagConstraints);
        } else {
            initializeCategoriesTable();
        }
    }

    /**
     * Initializes the content for the transactions panel. Shows the "You have no transactions."
     * label if the active budget has no transactions, initializes the transactions table otherwise.
     */
    private void initializeContentForTransactionsPanel() {
        if (account.getBudgets().isEmpty() || budget.getCategories().isEmpty() || budget.numberOfTransactions() == 0) {
            emptyTransactionsLabel = new JLabel("You have no transactions.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyTransactionsLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyTransactionsLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(227, 0, 163, 0);
            transactionsPanel.add(emptyTransactionsLabel, gridBagConstraints);
        } else {
            initializeTransactionsTable();
        }
    }

    /**
     * Initializes the components for the active budget panel and adds them to the active budget panel.
     */
    private void initializeComponentsForActiveBudgetPanel() {
        budget.calculateAmountRemaining();
        initializeBudgetProgressBar();
        budgetAmountRemainingLabel = new JLabel(decimalFormat.format(budget.getAmountRemaining()) + " Left");
        budgetStartDateLabel = new JLabel(budget.getStartDate());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        budgetAmountRemainingLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountRemainingLabel.setForeground(Color.WHITE);
        budgetStartDateLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetStartDateLabel.setForeground(Color.GRAY);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -65, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        activeBudgetPanel.add(budgetProgressBar, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        activeBudgetPanel.add(budgetAmountRemainingLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -45, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        activeBudgetPanel.add(budgetComboBox, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -100, 0);
        activeBudgetPanel.add(budgetStartDateLabel, gridBagConstraints);
        refresh(activeBudgetPanel);
    }

    /**
     * Initializes the budget progress bar.
     */
    private void initializeBudgetProgressBar() {
        JProgressBar budgetProgressBar = new JProgressBar();
        budgetProgressBar.setPreferredSize(new Dimension(500, 30));
        budgetProgressBar.setStringPainted(true);
        budgetProgressBar.setString(decimalFormat.format(budget.getAmountSpent()) + " of "
                + decimalFormat.format(budget.getAmount()));
        budgetProgressBar.setValue(budget.getAmountSpent().divide(budget.getAmount(), 2,
                RoundingMode.HALF_EVEN).multiply(new BigDecimal("100.00")).intValue());
        this.budgetProgressBar = budgetProgressBar;
    }

    /**
     * Initializes the categories table.
     */
    private void initializeCategoriesTable() {
        initializeLeftHeaderRenderer();
        String[] columnNames = {"Name", "Amount Spent", ""};
        LastColumnEditableTableModel categoriesTableModel = new LastColumnEditableTableModel(columnNames, 0);
        JTable categoriesTable = new JTable(categoriesTableModel);
        ButtonColumn buttonColumn = new ButtonColumn(categoriesTable,
                deleteCategory(categoriesTableModel, categoriesTable), 2);
        updateCategoriesTableModel(categoriesTableModel);
        setGeneralPropertiesForTable(categoriesTable);
        for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
            categoriesTable.getColumnModel().getColumn(columnIndex).setHeaderRenderer(leftHeaderRenderer);
        }
        categoriesTable.getColumnModel().getColumn(2).setMaxWidth(30);
        initializeCategoriesScrollPane(categoriesTable);
    }

    /**
     * Initializes the transactions table.
     */
    private void initializeTransactionsTable() {
        initializeLeftHeaderRenderer();
        String[] columnNames = {"Name", "Category", "Date", "Amount", ""};
        LastColumnEditableTableModel transactionsTableModel = new LastColumnEditableTableModel(columnNames,
                0);
        JTable transactionsTable = new JTable(transactionsTableModel);
        ButtonColumn buttonColumn = new ButtonColumn(transactionsTable,
                deleteTransaction(transactionsTableModel, transactionsTable), 4);
        updateTransactionsTableModel(transactionsTableModel);
        setGeneralPropertiesForTable(transactionsTable);
        for (int columnIndex = 0; columnIndex < 5; columnIndex++) {
            transactionsTable.getColumnModel().getColumn(columnIndex).setHeaderRenderer(leftHeaderRenderer);
        }
        transactionsTable.getColumnModel().getColumn(4).setMaxWidth(30);
        initializeTransactionsScrollPane(transactionsTable);
    }

    /**
     * Initializes a renderer that aligns a table's header to the left.
     */
    private void initializeLeftHeaderRenderer() {
        leftHeaderRenderer = new DefaultTableCellRenderer();
        leftHeaderRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftHeaderRenderer.setBackground(new Color(40, 44, 52));
        leftHeaderRenderer.setForeground(Color.GRAY);
    }

    /**
     * Sets the general properties for the specified table.
     *
     * @param table the table
     */
    private void setGeneralPropertiesForTable(JTable table) {
        table.setRowHeight(30);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setOpaque(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowHorizontalLines(true);
        table.setBackground(BACKGROUND_COLOUR);
        table.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Updates the specified categories table model.
     *
     * @param categoriesTableModel the categories table model to be updated
     */
    private void updateCategoriesTableModel(DefaultTableModel categoriesTableModel) {
        for (Category nextCategory : budget.getCategories()) {
            Object[] categoryData = {nextCategory, decimalFormat.format(nextCategory.getAmountSpent()),
                    new ImageIcon("./icons/Delete.png")};
            categoriesTableModel.addRow(categoryData);
        }
    }

    /**
     * Updates the specified transactions table model.
     *
     * @param transactionsTableModel the transactions table model to be updated
     */
    private void updateTransactionsTableModel(DefaultTableModel transactionsTableModel) {
        for (Category nextCategory : budget.getCategories()) {
            for (Transaction nextTransaction : nextCategory.getTransactions()) {
                Object[] transactionData = {nextTransaction, nextCategory, nextTransaction.getDate(),
                        decimalFormat.format(nextTransaction.getAmount()), new ImageIcon("./icons/Delete.png")};
                transactionsTableModel.addRow(transactionData);
            }
        }
    }

    /**
     * Initializes the categories scroll pane and adds it to the categories panel.
     *
     * @param categoriesTable the categories table to be added to the categories scroll pane
     */
    private void initializeCategoriesScrollPane(JTable categoriesTable) {
        categoriesScrollPane = new JScrollPane(categoriesTable);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoriesScrollPane.setPreferredSize(new Dimension(310, 130));
        categoriesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        categoriesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        categoriesScrollPane.setOpaque(false);
        categoriesScrollPane.getViewport().setOpaque(false);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(38, 0, -30, 0);
        categoriesPanel.add(categoriesScrollPane, gridBagConstraints);
        refresh(categoriesPanel);
    }

    /**
     * Initializes the transactions scroll pane and adds it to the transactions panel.
     *
     * @param transactionsTable the transactions table to be added to the transactions scroll pane
     */
    private void initializeTransactionsScrollPane(JTable transactionsTable) {
        transactionsScrollPane = new JScrollPane(transactionsTable);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        transactionsScrollPane.setPreferredSize(new Dimension(632, 400));
        transactionsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        transactionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        transactionsScrollPane.setOpaque(false);
        transactionsScrollPane.getViewport().setOpaque(false);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(38, 0, -30, 0);
        transactionsPanel.add(transactionsScrollPane, gridBagConstraints);
        refresh(transactionsPanel);
    }

    /**
     * Initializes an empty doughnut chart if the account has no budgets, or if the active budget has no categories,
     * or if the active budget has no transactions, initializes a normal doughnut chart otherwise.
     */
    private void initializeDoughnutChart() {
        if (account.getBudgets().isEmpty() || budget.getCategories().isEmpty() || budget.numberOfTransactions() == 0) {
            initializeEmptyDoughnutChart();
        } else {
            initializeNormalDoughnutChart();
        }
    }

    /**
     * Initializes an empty doughnut chart and adds it to the categories panel.
     */
    private void initializeEmptyDoughnutChart() {
        DefaultPieDataset emptyDataset = new DefaultPieDataset();
        JFreeChart emptyDoughnutChart = ChartFactory.createRingChart(null, emptyDataset, false,
                false, false);
        ringPlot = (RingPlot) emptyDoughnutChart.getPlot();
        chartPanel = new ChartPanel(emptyDoughnutChart);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        emptyDataset.setValue("1", 20);
        emptyDataset.setValue("2", 10);
        emptyDataset.setValue("3", 5);
        setPropertiesForEmptyDoughnutChart();
        chartPanel.setPreferredSize(new Dimension(310, 220));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(75, 0, -25, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        categoriesPanel.add(chartPanel, gridBagConstraints);
    }

    /**
     * Initializes a normal doughnut chart that shows the breakdown of the active budget by category
     * and adds it to the categories panel.
     */
    private void initializeNormalDoughnutChart() {
        DefaultPieDataset normalDataset = new DefaultPieDataset();
        JFreeChart normalDoughnutChart = ChartFactory.createRingChart(null, normalDataset, true,
                true, false);
        ringPlot = (RingPlot) normalDoughnutChart.getPlot();
        chartPanel = new ChartPanel(normalDoughnutChart);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        updateNormalDataset(normalDataset);
        setPropertiesForNormalDoughnutChart(normalDoughnutChart);
        chartPanel.setPreferredSize(new Dimension(310, 220));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(75, 0, -25, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        categoriesPanel.add(chartPanel, gridBagConstraints);
        refresh(categoriesPanel);
    }

    /**
     * Updates the specified normal dataset.
     *
     * @param normalDataset the normal dataset to be updated
     */
    private void updateNormalDataset(DefaultPieDataset normalDataset) {
        for (int index = 0; index < budget.getCategories().size(); index++) {
            if (budget.getCategories().get(index).getAmountSpent().compareTo(BigDecimal.ZERO) == 0) {
                normalDataset.setValue(budget.getCategories().get(index).getName(), 0.00001);
            } else {
                normalDataset.setValue(budget.getCategories().get(index).getName(),
                        budget.getCategories().get(index).getAmountSpent());
            }
            ringPlot.setSectionPaint(budget.getCategories().get(index).getName(), DOUGHNUT_CHART_COLOURS[index]);
        }
    }

    /**
     * Sets the general properties for the doughnut chart.
     */
    private void setGeneralPropertiesForDoughnutChart() {
        ringPlot.setSectionDepth(0.3);
        ringPlot.setInsets(new RectangleInsets() {
            public void trim(Rectangle2D area) {}
        });
        ringPlot.setLabelGenerator(null);
        ringPlot.setBackgroundPaint(BACKGROUND_COLOUR);
        ringPlot.setShadowPaint(null);
        ringPlot.setCenterText("Breakdown");
        ringPlot.setCenterTextFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        ringPlot.setCenterTextColor(Color.WHITE);
        ringPlot.setCenterTextMode(CenterTextMode.FIXED);
        ringPlot.setOutlineVisible(false);
        ringPlot.setSectionOutlinesVisible(false);
        ringPlot.setSeparatorsVisible(false);
    }

    /**
     * Sets the properties for the empty doughnut chart.
     */
    private void setPropertiesForEmptyDoughnutChart() {
        setGeneralPropertiesForDoughnutChart();
        ringPlot.setSectionPaint("1", Color.DARK_GRAY);
        ringPlot.setSectionPaint("2", Color.GRAY);
        ringPlot.setSectionPaint("3", Color.LIGHT_GRAY);
    }

    /**
     * Sets the properties for the specified normal doughnut chart.
     *
     * @param normalDoughnutChart the normal doughnut chart
     */
    private void setPropertiesForNormalDoughnutChart(JFreeChart normalDoughnutChart) {
        DecimalFormat percentFormat = new DecimalFormat("#,##0.00%");
        normalDoughnutChart.setBackgroundPaint(BACKGROUND_COLOUR);
        normalDoughnutChart.getLegend().setPosition(RectangleEdge.RIGHT);
        normalDoughnutChart.getLegend().setBackgroundPaint(BACKGROUND_COLOUR);
        normalDoughnutChart.getLegend().setItemLabelPadding(new RectangleInsets(0, 5, 0, 10));
        normalDoughnutChart.getLegend().setItemFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        normalDoughnutChart.getLegend().setItemPaint(Color.WHITE);
        setGeneralPropertiesForDoughnutChart();
        ringPlot.setLegendItemShape(new RoundRectangle2D.Double(1, 1, 14, 14, 2, 2));
        ringPlot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}", decimalFormat,
                percentFormat));
        ringPlot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {2}", decimalFormat,
                percentFormat));
    }

    /**
     * Initializes the separator for the active budget panel.
     */
    private void initializeSeparatorForActiveBudgetPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-20, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        activeBudgetPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the first separator for the categories panel.
     */
    private void initializeFirstSeparatorForCategoriesPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, -35, 0);
        gridBagConstraints.weightx = 1;
        categoriesPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the second separator for the categories panel.
     */
    private void initializeSecondSeparatorForCategoriesPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, -100, 0);
        gridBagConstraints.weightx = 1;
        categoriesPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the transactions panel.
     */
    private void initializeSeparatorForTransactionsPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, -35, 0);
        gridBagConstraints.weightx = 1;
        transactionsPanel.add(separator, gridBagConstraints);
    }

    /**
     * Adds the budget if all fields are valid and it does not already exist in the account, try again otherwise.
     */
    private void addBudget() {
        isBudgetAdded = false;
        try {
            budget = new Budget(budgetNameField.getText(), new BigDecimal(budgetAmountField.getText()));
            account.addBudget(budget);
            budgetComboBox.addItem(budget);
            budgetComboBox.setSelectedItem(budget);
            if (account.getBudgets().size() > 1) {
                buttonToDeleteBudget.setEnabled(true);
            }
            addBudgetSuccess();
            isBudgetAdded = true;
        } catch (EmptyNameException | NegativeAmountException | ZeroAmountException
                | DuplicateBudgetException exception) {
            addBudgetFailure(exception.getMessage());
        } catch (NumberFormatException numberFormatException) {
            addBudgetFailure("You must enter an amount.");
        }
    }

    /**
     * Shows the "Budget has been successfully added." message dialog and closes the dialog to add a budget.
     */
    private void addBudgetSuccess() {
        budgetNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        budgetAmountField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        refresh(optionPaneToAddBudget);
        playSound(SUCCESS_SOUND);
        JOptionPane.showMessageDialog(null, "Budget has been successfully added.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        dialogToAddBudget.setVisible(false);
    }

    /**
     * Shows the error message dialog with the error that caused the addition of a budget to fail.
     *
     * @param errorMessage the error message
     */
    private void addBudgetFailure(String errorMessage) {
        if (budgetNameField.getText().isEmpty() && errorMessage.equals("You must enter an amount.")) {
            budgetNameField.putClientProperty("JComponent.outline", "error");
            budgetAmountField.putClientProperty("JComponent.outline", "error");
        } else if (!budgetNameField.getText().isEmpty() && errorMessage.equals("You must enter an amount.")) {
            budgetNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            budgetAmountField.putClientProperty("JComponent.outline", "error");
        } else if ((budgetAmountField.getText().startsWith("-") || budgetAmountField.getText().startsWith("0"))
                && (errorMessage.equals("Name cannot be empty."))) {
            budgetNameField.putClientProperty("JComponent.outline", "error");
            budgetAmountField.putClientProperty("JComponent.outline", "error");
        } else if (errorMessage.equals("Name cannot be empty.")) {
            budgetNameField.putClientProperty("JComponent.outline", "error");
            budgetAmountField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        } else if (errorMessage.equals("Amount cannot be negative.") || errorMessage.equals("Amount cannot be zero.")) {
            budgetNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            budgetAmountField.putClientProperty("JComponent.outline", "error");
        } else if (errorMessage.equals(budgetNameField.getText() + " already exists.")) {
            budgetNameField.putClientProperty("JComponent.outline", "error");
            budgetAmountField.putClientProperty("JComponent.outline", "error");
        }
        refresh(optionPaneToAddBudget);
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(null, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Adds the category if the name field is valid and it does not already exist in the active budget,
     * try again otherwise.
     */
    private void addCategory() {
        isCategoryAdded = false;
        try {
            Category category = new Category(categoryNameField.getText());
            budget.addCategory(category);
            categoryComboBox.addItem(category);
            categoryComboBox.setSelectedItem(category);
            updateCategoriesPanel();
            initializeCategoriesTable();
            initializeDoughnutChart();
            addCategorySuccess();
            isCategoryAdded = true;
        } catch (EmptyNameException | DuplicateCategoryException exception) {
            addCategoryFailure(exception.getMessage());
        }
    }

    /**
     * Shows the "Category has been successfully added." message dialog and closes the dialog to add a category.
     */
    private void addCategorySuccess() {
        categoryNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        refresh(optionPaneToAddCategory);
        playSound(SUCCESS_SOUND);
        JOptionPane.showMessageDialog(null, "Category has been successfully added.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        dialogToAddCategory.setVisible(false);
    }

    /**
     * Shows the error message dialog with the error that caused the addition of a category to fail.
     *
     * @param errorMessage the error message
     */
    private void addCategoryFailure(String errorMessage) {
        categoryNameField.putClientProperty("JComponent.outline", "error");
        refresh(optionPaneToAddCategory);
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(null, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Adds the transaction if all fields are valid, try again otherwise.
     *
     * @param category the category to which the transaction will be added
     */
    private void addTransaction(Category category) {
        isTransactionAdded = false;
        try {
            Transaction transaction = new Transaction(transactionNameField.getText(),
                    new BigDecimal(transactionAmountField.getText()), transactionDatePicker.getText());
            category.addTransaction(transaction);
            budget.calculateAmountRemaining();
            updateAllPanels();
            addTransactionSuccess();
            isTransactionAdded = true;
        } catch (EmptyNameException | NegativeAmountException | ZeroAmountException exception) {
            addTransactionFailure(exception.getMessage());
        } catch (NumberFormatException numberFormatException) {
            addTransactionFailure("You must enter an amount.");
        }
    }

    /**
     * Shows the "Transaction has been successfully added." message dialog and closes the dialog to add a transaction.
     * Shows the "This budget has been exhausted!" warning message dialog if the active budget has been exhausted.
     */
    private void addTransactionSuccess() {
        transactionNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        transactionAmountField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        refresh(optionPaneToAddTransaction);
        playSound(SUCCESS_SOUND);
        JOptionPane.showMessageDialog(null, "Transaction has been successfully added.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        dialogToAddTransaction.setVisible(false);
        if (budget.getAmountRemaining().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "This budget has been exhausted!", "bdgtr",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Shows the error message dialog with the error that caused the addition of a transaction to fail.
     *
     * @param errorMessage the error message
     */
    private void addTransactionFailure(String errorMessage) {
        if (transactionNameField.getText().isEmpty() && errorMessage.equals("You must enter an amount.")) {
            transactionNameField.putClientProperty("JComponent.outline", "error");
            transactionAmountField.putClientProperty("JComponent.outline", "error");
        } else if (!transactionNameField.getText().isEmpty() && errorMessage.equals("You must enter an amount.")) {
            transactionNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            transactionAmountField.putClientProperty("JComponent.outline", "error");
        } else if ((transactionAmountField.getText().startsWith("-")
                || transactionAmountField.getText().startsWith("0"))
                && (errorMessage.equals("Name cannot be empty."))) {
            transactionNameField.putClientProperty("JComponent.outline", "error");
            transactionAmountField.putClientProperty("JComponent.outline", "error");
        } else if (errorMessage.equals("Name cannot be empty.")) {
            transactionNameField.putClientProperty("JComponent.outline", "error");
            transactionAmountField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        } else if (errorMessage.equals("Amount cannot be negative.") || errorMessage.equals("Amount cannot be zero.")) {
            transactionNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            transactionAmountField.putClientProperty("JComponent.outline", "error");
        }
        refresh(optionPaneToAddTransaction);
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(null, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Deletes the active budget.
     */
    private void deleteBudget() {
        switch (initializeOptionPaneToDelete("Are you sure you want to delete this budget?")) {
            case JOptionPane.YES_OPTION:
                account.deleteBudget(budget);
                budgetComboBox.removeItem(budget);
                removeAll();
                refresh();
                initializeActiveBudgetPanel();
                initializeCategoriesPanel();
                initializeDoughnutChart();
                initializeTransactionsPanel();
                playSound(DELETE_SOUND);
                JOptionPane.showMessageDialog(null, "Budget has been successfully deleted.",
                        "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            case JOptionPane.NO_OPTION:
                break;
        }
        buttonToDeleteBudget.setBorderPainted(false);
    }

    /**
     * Deletes the category in the specified categories table from the specified categories table model.
     *
     * @param categoriesTableModel the categories table model from which the category will be deleted
     * @param categoriesTable the categories table with the category object
     * @return the delete category action
     */
    private Action deleteCategory(DefaultTableModel categoriesTableModel, JTable categoriesTable) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                switch (initializeOptionPaneToDelete("Are you sure you want to delete this category?")) {
                    case JOptionPane.YES_OPTION:
                        budget.deleteCategory((Category) categoriesTable.getValueAt(categoriesTable.getSelectedRow(),
                                0));
                        categoriesTableModel.removeRow(Integer.parseInt(event.getActionCommand()));
                        categoryComboBox.removeItemAt(Integer.parseInt(event.getActionCommand()));
                        updateAllPanels();
                        playSound(DELETE_SOUND);
                        JOptionPane.showMessageDialog(null,
                                "Category has been successfully deleted.", "bdgtr",
                                JOptionPane.INFORMATION_MESSAGE);
                    case JOptionPane.NO_OPTION:
                        break;
                }
            }
        };
    }

    /**
     * Deletes the transaction in the specified transactions table from the specified transactions table model.
     *
     * @param transactionsTableModel the transactions table model from which the transaction will be deleted
     * @param transactionsTable the transactions table with the transaction object and the category it belongs to
     * @return the delete transaction action
     */
    private Action deleteTransaction(DefaultTableModel transactionsTableModel, JTable transactionsTable) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                switch (initializeOptionPaneToDelete("Are you sure you want to delete this transaction?")) {
                    case JOptionPane.YES_OPTION:
                        Objects.requireNonNull((Category) transactionsTable.getValueAt(transactionsTable
                                .getSelectedRow(), 1)).deleteTransaction((Transaction) transactionsTable
                                .getValueAt(transactionsTable.getSelectedRow(), 0));
                        transactionsTableModel.removeRow(Integer.parseInt(event.getActionCommand()));
                        updateAllPanels();
                        playSound(DELETE_SOUND);
                        JOptionPane.showMessageDialog(null,
                                "Transaction has been successfully deleted.", "bdgtr",
                                JOptionPane.INFORMATION_MESSAGE);
                    case JOptionPane.NO_OPTION:
                        break;
                }
            }
        };
    }

    /**
     * Updates all panels.
     */
    private void updateAllPanels() {
        updateActiveBudgetPanel();
        categoriesPanel.remove(categoriesScrollPane);
        categoriesPanel.remove(chartPanel);
        refresh(categoriesPanel);
        updateTransactionsPanel();
        initializeComponentsForActiveBudgetPanel();
        initializeContentForCategoriesPanel();
        initializeDoughnutChart();
        initializeContentForTransactionsPanel();
    }

    /**
     * Updates the active budget panel.
     */
    private void updateActiveBudgetPanel() {
        activeBudgetPanel.remove(budgetProgressBar);
        activeBudgetPanel.remove(budgetAmountRemainingLabel);
        activeBudgetPanel.remove(budgetStartDateLabel);
        refresh(activeBudgetPanel);
    }

    /**
     * Updates the categories panel.
     */
    private void updateCategoriesPanel() {
        if (budget.getCategories().size() == 1) {
            categoriesPanel.remove(emptyCategoriesLabel);
        } else {
            categoriesPanel.remove(categoriesScrollPane);
        }
        categoriesPanel.remove(chartPanel);
        refresh(categoriesPanel);
    }

    /**
     * Updates the transactions panel.
     */
    private void updateTransactionsPanel() {
        if (emptyTransactionsLabel != null && budget.numberOfTransactions() == 1) {
            transactionsPanel.remove(emptyTransactionsLabel);
            emptyTransactionsLabel = null;
        } else {
            transactionsPanel.remove(transactionsScrollPane);
        }
        refresh(transactionsPanel);
    }

    /**
     * Plays the sound from the specified file.
     *
     * @param file the file with the sound to be played
     */
    public void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Refreshes this home panel.
     */
    private void refresh() {
        revalidate();
        repaint();
    }

    /**
     * Refreshes the specified component.
     *
     * @param component the component to be refreshed
     */
    private void refresh(Component component) {
        component.revalidate();
        component.repaint();
    }
}
