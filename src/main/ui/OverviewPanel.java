package ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import model.Account;
import model.Budget;
import model.Category;
import model.Transaction;
import model.exceptions.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Represents the overview panel.
 */
public class OverviewPanel extends JPanel implements FontRepository, ColourRepository, SoundRepository {
    private final DecimalFormat decimalFormat;
    private Account account;
    private Budget budget;
    private RoundedPanel activeBudgetPanel;
    private RoundedPanel categoriesPanel;
    private RoundedPanel recentTransactionsPanel;
    private RoundedPanel breakdownPanel;
    private ResettableDialog dialogToAddBudget;
    private ResettableDialog dialogToAddCategory;
    private ResettableDialog dialogToAddTransaction;
    private JOptionPane optionPaneToAddBudget;
    private JOptionPane optionPaneToAddCategory;
    private JOptionPane optionPaneToAddTransaction;
    private JComboBox<Budget> budgetComboBox;
    private JComboBox<Category> categoryComboBox;
    private JScrollPane categoriesScrollPane;
    private JScrollPane recentTransactionsScrollPane;
    private JButton buttonToAddBudget;
    private JButton buttonToAddCategory;
    private JButton buttonToAddTransaction;
    private JTextField budgetNameField;
    private JTextField budgetAmountField;
    private JTextField categoryNameField;
    private JTextField transactionNameField;
    private JTextField transactionAmountField;
    private JTextField transactionDateField;
    private DatePickerSettings datePickerSettings;
    private DatePicker transactionDatePicker;
    private JLabel emptyBudgetsLabel;
    private JLabel emptyCategoriesLabel;
    private JLabel emptyTransactionsLabel;
    private JLabel budgetStartDateLabel;
    private JLabel budgetAmountRemainingLabel;
    private JLabel transactionNameLabel;
    private JLabel transactionAmountLabel;
    private JLabel transactionDateLabel;
    private JLabel transactionCategoryLabel;
    private JProgressBar budgetAmountProgressBar;

    /**
     * Creates a new overview panel with the specified account.
     *
     * @param account the account that is signed in
     */
    public OverviewPanel(Account account) {
        this.account = account;
        decimalFormat = new DecimalFormat("#,##0.00");
        setLayout(new GridBagLayout());
        initializeTextFields();
        initializeActiveBudgetPanel();
        initializeCategoriesPanel();
        initializeRecentTransactionsPanel();
        initializeBreakdownPanel();
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
        initializeTextFieldProperties();
    }

    /**
     * Initializes the properties for all text fields.
     */
    private void initializeTextFieldProperties() {
        budgetNameField.putClientProperty("JTextField.placeholderText", "Name");
        budgetNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetNameField.setForeground(Color.WHITE);
        budgetAmountField.putClientProperty("JTextField.placeholderText", "0.00");
        budgetAmountField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountField.setForeground(Color.WHITE);
        categoryNameField.putClientProperty("JTextField.placeholderText", "Name");
        categoryNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        categoryNameField.setForeground(Color.WHITE);
        transactionNameField.putClientProperty("JTextField.placeholderText", "Name");
        transactionNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionNameField.setForeground(Color.WHITE);
        transactionAmountField.putClientProperty("JTextField.placeholderText", "0.00");
        transactionAmountField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionAmountField.setForeground(Color.WHITE);
        transactionDateField.setEditable(false);
        transactionDateField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        transactionDateField.setForeground(Color.WHITE);
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
        activeBudgetPanel.setPreferredSize(new Dimension(1060, 150));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        activeBudgetPanel.add(activeBudgetLabel, gridBagConstraints);
        initializeButtonToAddBudget();
        initializeSeparatorForActiveBudgetPanel();
        initializeBudgetComboBox();
        initializeActiveBudgetPanelContent();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new Insets(-188, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        add(activeBudgetPanel, gridBagConstraints);
    }

    /**
     * Initializes the categories panel and adds it to this overview panel.
     */
    private void initializeCategoriesPanel() {
        categoriesPanel = new RoundedPanel();
        JLabel categoriesLabel = new JLabel("Categories");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoriesLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        categoriesLabel.setForeground(Color.WHITE);
        categoriesPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        categoriesPanel.setLayout(new GridBagLayout());
        categoriesPanel.setPreferredSize(new Dimension(350, 235));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        categoriesPanel.add(categoriesLabel, gridBagConstraints);
        initializeButtonToAddCategory();
        initializeSeparatorForCategoriesPanel();
        initializeCategoriesPanelContent();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        add(categoriesPanel, gridBagConstraints);
        refresh();
    }

    /**
     * Initializes the recent transactions panel and adds it to this overview panel.
     */
    private void initializeRecentTransactionsPanel() {
        recentTransactionsPanel = new RoundedPanel();
        JLabel recentTransactionsLabel = new JLabel("Recent Transactions");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        recentTransactionsLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        recentTransactionsLabel.setForeground(Color.WHITE);
        recentTransactionsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        recentTransactionsPanel.setLayout(new GridBagLayout());
        recentTransactionsPanel.setPreferredSize(new Dimension(672, 507));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        recentTransactionsPanel.add(recentTransactionsLabel, gridBagConstraints);
        initializeButtonToAddTransaction();
        initializeSeparatorForRecentTransactionsPanel();
        initializeRecentTransactionsPanelContent();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -188, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        add(recentTransactionsPanel, gridBagConstraints);
    }

    /**
     * Initializes the breakdown panel and adds it to this overview panel.
     */
    private void initializeBreakdownPanel() {
        breakdownPanel = new RoundedPanel();
        JLabel breakdownLabel = new JLabel("Breakdown (Coming Soon!)");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        breakdownLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        breakdownLabel.setForeground(Color.WHITE);
        breakdownPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        breakdownPanel.setLayout(new GridBagLayout());
        breakdownPanel.setPreferredSize(new Dimension(350, 235));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        breakdownPanel.add(breakdownLabel, gridBagConstraints);
        initializeSeparatorForBreakdownPanel();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -188, 0);
        gridBagConstraints.anchor = GridBagConstraints.LAST_LINE_START;
        add(breakdownPanel, gridBagConstraints);
    }

    /**
     * Initializes the button to add a budget and adds the button to the active budget panel.
     */
    private void initializeButtonToAddBudget() {
        buttonToAddBudget = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeDialogToAddBudget();
        buttonToAddBudget.setBackground(ACCENT_COLOUR);
        buttonToAddBudget.setBorderPainted(false);
        buttonToAddBudget.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToAddBudget.setForeground(Color.WHITE);
        buttonToAddBudget.setPreferredSize(new Dimension(30, 30));
        buttonToAddBudget.addActionListener(event -> {
            buttonToAddBudget.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddBudget.setBorderPainted(true);
            dialogToAddBudget.setVisible(true);
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        activeBudgetPanel.add(buttonToAddBudget, gridBagConstraints);
    }

    /**
     * Initializes the button to add a category and adds the button to the categories panel.
     */
    private void initializeButtonToAddCategory() {
        buttonToAddCategory = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeDialogToAddCategory();
        buttonToAddCategory.setBackground(ACCENT_COLOUR);
        buttonToAddCategory.setBorderPainted(false);
        buttonToAddCategory.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToAddCategory.setForeground(Color.WHITE);
        buttonToAddCategory.setPreferredSize(new Dimension(30, 30));
        initializeActionListenerForButtonToAddCategory();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        categoriesPanel.add(buttonToAddCategory, gridBagConstraints);
    }

    /**
     * Initializes the button to add a transaction and adds the button to the recent transactions panel.
     */
    private void initializeButtonToAddTransaction() {
        buttonToAddTransaction = new JButton("＋");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeDialogToAddTransaction();
        buttonToAddTransaction.setBackground(ACCENT_COLOUR);
        buttonToAddTransaction.setBorderPainted(false);
        buttonToAddTransaction.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToAddTransaction.setForeground(Color.WHITE);
        buttonToAddTransaction.setPreferredSize(new Dimension(30, 30));
        initializeActionListenerForButtonToAddTransaction();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        recentTransactionsPanel.add(buttonToAddTransaction, gridBagConstraints);
    }

    /**
     * Initializes the action listener for the button to add a category.
     */
    private void initializeActionListenerForButtonToAddCategory() {
        buttonToAddCategory.addActionListener(event -> {
            buttonToAddCategory.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddCategory.setBorderPainted(true);
            if (account.getBudgets().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a budget first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budgetAmountProgressBar.getValue() == 100) {
                JOptionPane.showMessageDialog(null, "You have exhausted this budget!",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else {
                dialogToAddCategory.setVisible(true);
            }
        });
    }

    /**
     * Initializes the action listener for the button to add a transaction.
     */
    private void initializeActionListenerForButtonToAddTransaction() {
        buttonToAddTransaction.addActionListener(event -> {
            buttonToAddTransaction.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToAddTransaction.setBorderPainted(true);
            if (account.getBudgets().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a budget first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budget.getCategories().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must add a category first.",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else if (budgetAmountProgressBar.getValue() == 100) {
                JOptionPane.showMessageDialog(null, "You have exhausted this budget!",
                        "bdgtr", JOptionPane.ERROR_MESSAGE);
            } else {
                dialogToAddTransaction.setVisible(true);
            }
        });
    }

    /**
     * Initializes and shows the dialog to add a budget.
     */
    private void initializeDialogToAddBudget() {
        JLabel budgetNameLabel = new JLabel("Enter the name for this budget:");
        JLabel budgetAmountLabel = new JLabel("Set the amount for this budget:");
        JPanel budgetLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel budgetFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToAddBudget = new JPanel(new BorderLayout(10, 10));
        List<JTextField> budgetFields = Arrays.asList(budgetNameField, budgetAmountField);
        optionPaneToAddBudget = new JOptionPane(panelToAddBudget, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        dialogToAddBudget = new ResettableDialog("bdgtr", budgetFields);
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
        initializePropertyChangeListenerForOptionPaneToAddBudget();
        dialogToAddBudget.setContentPane(optionPaneToAddBudget);
        dialogToAddBudget.pack();
        dialogToAddBudget.setLocationRelativeTo(null);
    }

    /**
     * Initializes and shows the dialog to add a category.
     */
    private void initializeDialogToAddCategory() {
        JLabel categoryNameLabel = new JLabel("Enter the name for this category:");
        JPanel categoryLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel categoryFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToAddCategory = new JPanel(new BorderLayout(10, 10));
        List<JTextField> categoryFields = Collections.singletonList(categoryNameField);
        optionPaneToAddCategory = new JOptionPane(panelToAddCategory, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        dialogToAddCategory = new ResettableDialog("bdgtr", categoryFields);
        categoryNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        categoryNameLabel.setForeground(Color.WHITE);
        categoryLabelsPanel.add(categoryNameLabel);
        categoryFieldsPanel.add(categoryNameField);
        panelToAddCategory.add(categoryLabelsPanel, BorderLayout.LINE_START);
        panelToAddCategory.add(categoryFieldsPanel, BorderLayout.CENTER);
        initializePropertyChangeListenerForOptionPaneToAddCategory();
        dialogToAddCategory.setContentPane(optionPaneToAddCategory);
        dialogToAddCategory.pack();
        dialogToAddCategory.setLocationRelativeTo(null);
    }

    /**
     * Initializes and shows the dialog to add a transaction.
     */
    private void initializeDialogToAddTransaction() {
        initializeOptionPaneToAddTransaction();
        List<JTextField> transactionFields = Arrays.asList(transactionNameField, transactionAmountField);
        dialogToAddTransaction = new ResettableDialog("bdgtr", transactionFields);
        dialogToAddTransaction.setContentPane(optionPaneToAddTransaction);
        dialogToAddTransaction.pack();
        dialogToAddTransaction.setLocationRelativeTo(null);
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
        initializesComponentsForOptionPaneToAddTransaction();
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
        initializePropertyChangeListenerForOptionPaneToAddTransaction();
    }

    /**
     * Initializes the components for the option pane to add a transaction.
     */
    private void initializesComponentsForOptionPaneToAddTransaction() {
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
        datePickerSettings = new DatePickerSettings();
        transactionDatePicker = new DatePicker(datePickerSettings);
        setBackgroundsForTransactionDatePicker();
        setFontsForTransactionDatePicker();
        setTextColoursForTransactionDatePicker();
        datePickerSettings.setFormatForTodayButton(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        datePickerSettings.setVisibleDateTextField(false);
        transactionDatePicker.setDateToToday();
        transactionDatePicker.addDateChangeListener(dateChangeEvent -> {
            transactionDateField.putClientProperty("JTextField.placeholderText", transactionDatePicker.getText());
            refresh(optionPaneToAddTransaction);
        });
        transactionDateField.putClientProperty("JTextField.placeholderText", transactionDatePicker.getText());
        refresh(optionPaneToAddTransaction);
    }

    /**
     * Sets the backgrounds for the transaction date picker.
     */
    private void setBackgroundsForTransactionDatePicker() {
        datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates,
                COMPONENT_BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels, BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundTodayLabel, BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundClearLabel, BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons,
                COMPONENT_BACKGROUND_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate, ACCENT_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate, ACCENT_BORDER_COLOUR);
        datePickerSettings.setColorBackgroundWeekdayLabels(ACCENT_COLOUR, true);
    }

    /**
     * Sets the fonts for the transaction date picker.
     */
    private void setFontsForTransactionDatePicker() {
        datePickerSettings.setFontMonthAndYearMenuLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        datePickerSettings.setFontMonthAndYearNavigationButtons(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        datePickerSettings.setFontTodayLabel(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        datePickerSettings.setFontClearLabel(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        datePickerSettings.setFontCalendarDateLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        datePickerSettings.setFontCalendarWeekdayLabels(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
    }

    /**
     * Sets the text colours for the transaction date picker.
     */
    private void setTextColoursForTransactionDatePicker() {
        datePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.TextTodayLabel, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.TextClearLabel, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextNormalDates, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, Color.WHITE);
        datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundCalendarPanelLabelsOnHover, ACCENT_COLOUR);
        datePickerSettings.setColor(DatePickerSettings.DateArea.TextCalendarPanelLabelsOnHover, Color.WHITE);
    }

    /**
     * Initializes the property change listener for the option pane to add a budget.
     */
    private void initializePropertyChangeListenerForOptionPaneToAddBudget() {
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
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddBudget.setVisible(false);
                }
                buttonToAddBudget.setBorderPainted(false);
            }
        });
    }

    /**
     * Initializes the property change listener for the option pane to add a category.
     */
    private void initializePropertyChangeListenerForOptionPaneToAddCategory() {
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
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddCategory.setVisible(false);
                }
                buttonToAddCategory.setBorderPainted(false);
            }
        });
    }

    /**
     * Initializes the property change listener for the option pane to add a transaction.
     */
    private void initializePropertyChangeListenerForOptionPaneToAddTransaction() {
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
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToAddTransaction.setVisible(false);
                }
                buttonToAddTransaction.setBorderPainted(false);
            }
        });
    }

    /**
     * Initializes the combo box with the budgets in the account.
     */
    private void initializeBudgetComboBox() {
        budgetComboBox = new JComboBox<>(account.getBudgets().toArray(new Budget[0]));
        budgetComboBox.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetComboBox.setForeground(Color.WHITE);
        budgetComboBox.setPreferredSize(new Dimension(120, 30));
        initializeItemListenerForBudgetComboBox();
    }

    /**
     * Initializes the item listener for the budget combo box.
     */
    private void initializeItemListenerForBudgetComboBox() {
        budgetComboBox.addItemListener(event -> {
            if (account.getBudgets().size() == 1) {
                activeBudgetPanel.remove(emptyBudgetsLabel);
                refresh(activeBudgetPanel);
            }
            if (event.getStateChange() == ItemEvent.DESELECTED) {
                budget = (Budget) event.getItem();
                activeBudgetPanel.remove(budgetStartDateLabel);
                activeBudgetPanel.remove(budgetAmountRemainingLabel);
                activeBudgetPanel.remove(budgetAmountProgressBar);
                refresh(activeBudgetPanel);
                remove(categoriesPanel);
                remove(recentTransactionsPanel);
                refresh();
            } else if (event.getStateChange() == ItemEvent.SELECTED) {
                budget = (Budget) event.getItem();
                initializeActiveBudgetPanelComponents();
                initializeCategoriesPanel();
                initializeRecentTransactionsPanel();
            }
        });
    }

    /**
     * Initializes the combo box with the categories in the active budget.
     */
    private void initializeCategoryComboBox() {
        if (!account.getBudgets().isEmpty()) {
            categoryComboBox = new JComboBox<>(budget.getCategories().toArray(new Category[0]));
            categoryComboBox.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            categoryComboBox.setForeground(Color.WHITE);
            categoryComboBox.setPreferredSize(new Dimension(157, 30));
        }
    }

    /**
     * Initializes the content of the active budget panel. Shows the "You have no budgets." label if the account has no
     * budgets, initialize the components of the active budget panel otherwise.
     */
    private void initializeActiveBudgetPanelContent() {
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
            Objects.requireNonNull(budget).calculateAmountRemaining();
            initializeActiveBudgetPanelComponents();
        }
    }

    /**
     * Initializes the content of the categories panel. Shows the "You have no categories." label if the active budget
     * has no categories, initialize the categories table otherwise.
     */
    private void initializeCategoriesPanelContent() {
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
     * Initializes the content of the recent transactions panel. Shows the "You have no transactions."
     * label if the active budget has no transactions, initialize the recent transactions table otherwise.
     */
    private void initializeRecentTransactionsPanelContent() {
        if (account.getBudgets().isEmpty() || budget.getCategories().isEmpty()) {
            emptyTransactionsLabel = new JLabel("You have no transactions.");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            emptyTransactionsLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
            emptyTransactionsLabel.setForeground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(227, 0, 163, 0);
            recentTransactionsPanel.add(emptyTransactionsLabel, gridBagConstraints);
        } else {
            initializeRecentTransactionsTable();
        }
    }

    /**
     * Initializes the components of the active budget panel and adds them to the active budget panel.
     */
    private void initializeActiveBudgetPanelComponents() {
        budgetStartDateLabel = new JLabel(budget.getStartDate());
        budgetAmountRemainingLabel = new JLabel("＄" + decimalFormat.format(budget.getAmountRemaining())
                + " Left");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        budgetStartDateLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetStartDateLabel.setForeground(Color.GRAY);
        budgetAmountRemainingLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountRemainingLabel.setForeground(Color.WHITE);
        initializeBudgetAmountProgressBar();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -45, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        activeBudgetPanel.add(budgetComboBox, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -100, 0);
        activeBudgetPanel.add(budgetStartDateLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 0, -65, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        activeBudgetPanel.add(budgetAmountRemainingLabel, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        activeBudgetPanel.add(budgetAmountProgressBar, gridBagConstraints);
        refresh(activeBudgetPanel);
    }

    /**
     * Initializes the budget amount progress bar.
     */
    private void initializeBudgetAmountProgressBar() {
        JProgressBar budgetAmountProgressBar = new JProgressBar();
        budgetAmountProgressBar.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        budgetAmountProgressBar.setPreferredSize(new Dimension(500, 30));
        budgetAmountProgressBar.setStringPainted(true);
        budgetAmountProgressBar.setString("＄" + decimalFormat.format(budget.getAmountSpent()) + " of ＄"
                + decimalFormat.format(budget.getAmount()));
        budgetAmountProgressBar.setValue(budget.getAmountSpent().divide(budget.getAmount(), 2,
                RoundingMode.HALF_EVEN).multiply(new BigDecimal("100.00")).intValue());
        this.budgetAmountProgressBar = budgetAmountProgressBar;
    }

    /**
     * Initializes the categories table.
     */
    private void initializeCategoriesTable() {
        Object[] columnNames = {"Name", "Amount Spent"};
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        NotEditableTableModel categoriesTableModel = new NotEditableTableModel(columnNames, 0);
        JTable categoriesTable = new JTable(categoriesTableModel);
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        rightRenderer.setOpaque(false);
        updateCategoriesTableModel(categoriesTableModel);
        categoriesTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        categoriesTable.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        categoriesTable.setForeground(Color.WHITE);
        categoriesTable.setOpaque(false);
        ((DefaultTableCellRenderer) categoriesTable.getDefaultRenderer(Object.class)).setOpaque(false);
        categoriesTable.setRowHeight(30);
        categoriesTable.setTableHeader(null);
        initializeCategoriesScrollPane(categoriesTable);
    }

    /**
     * Initializes the categories scroll pane and adds it to the categories panel.
     *
     * @param categoriesTable the categories table to be added to the categories scroll pane
     */
    private void initializeCategoriesScrollPane(JTable categoriesTable) {
        categoriesScrollPane = new JScrollPane(categoriesTable);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        categoriesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        categoriesScrollPane.setPreferredSize(new Dimension(310, 130));
        categoriesScrollPane.setOpaque(false);
        categoriesScrollPane.getViewport().setOpaque(false);
        categoriesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(38, 0, -30, 0);
        categoriesPanel.add(categoriesScrollPane, gridBagConstraints);
        refresh(categoriesPanel);
    }

    /**
     * Initializes the recent transactions table.
     */
    private void initializeRecentTransactionsTable() {
        Object[] columnNames = {"Name", "Date", "Category", "Amount"};
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        NotEditableTableModel recentTransactionsTableModel = new NotEditableTableModel(columnNames, 0);
        JTable recentTransactionsTable = new JTable(recentTransactionsTableModel);
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        rightRenderer.setOpaque(false);
        updateRecentTransactionsTableModel(recentTransactionsTableModel);
        recentTransactionsTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        recentTransactionsTable.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        recentTransactionsTable.setForeground(Color.WHITE);
        recentTransactionsTable.setOpaque(false);
        ((DefaultTableCellRenderer) recentTransactionsTable.getDefaultRenderer(Object.class)).setOpaque(false);
        recentTransactionsTable.setRowHeight(30);
        recentTransactionsTable.setTableHeader(null);
        initializeRecentTransactionsScrollPane(recentTransactionsTable);
    }

    /**
     * Initializes the recent transactions scroll pane and adds it to the recent transactions panel.
     *
     * @param recentTransactionsTable the recent transactions table to be added to the recent transactions scroll pane
     */
    private void initializeRecentTransactionsScrollPane(JTable recentTransactionsTable) {
        recentTransactionsScrollPane = new JScrollPane(recentTransactionsTable);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        recentTransactionsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        recentTransactionsScrollPane.setPreferredSize(new Dimension(632, 400));
        recentTransactionsScrollPane.setOpaque(false);
        recentTransactionsScrollPane.getViewport().setOpaque(false);
        recentTransactionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(38, 0, -30, 0);
        recentTransactionsPanel.add(recentTransactionsScrollPane, gridBagConstraints);
        refresh(recentTransactionsPanel);
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
     * Initializes the separator for the categories panel.
     */
    private void initializeSeparatorForCategoriesPanel() {
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
     * Initializes the separator for the recent transactions panel.
     */
    private void initializeSeparatorForRecentTransactionsPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, -35, 0);
        gridBagConstraints.weightx = 1;
        recentTransactionsPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the breakdown panel.
     */
    private void initializeSeparatorForBreakdownPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, -35, 0);
        gridBagConstraints.weightx = 1;
        breakdownPanel.add(separator, gridBagConstraints);
    }

    /**
     * Adds the budget if all fields are valid and it does not already exist in the account, try again otherwise.
     */
    private void addBudget() {
        try {
            budget = new Budget(budgetNameField.getText(), new BigDecimal(budgetAmountField.getText()));
            account.addBudget(budget);
            budgetComboBox.addItem(budget);
            budgetComboBox.setSelectedItem(budget);
            addBudgetSuccess();
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
     * Shows the error message dialog with the error that caused adding a budget to fail.
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
        try {
            Category category = new Category(categoryNameField.getText());
            budget.addCategory(category);
            categoryComboBox.addItem(category);
            categoryComboBox.setSelectedItem(category);
            updateCategoriesPanel();
            initializeCategoriesTable();
            addCategorySuccess();
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
     * Shows the error message dialog with the error that caused adding a category to fail.
     */
    private void addCategoryFailure(String errorMessage) {
        categoryNameField.putClientProperty("JComponent.outline", "error");
        refresh(optionPaneToAddCategory);
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(null, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Adds the transaction if all fields valid, try again otherwise.
     *
     * @param category the category to which the transaction will be added
     */
    private void addTransaction(Category category) {
        try {
            Transaction transaction = new Transaction(transactionNameField.getText(),
                    new BigDecimal(transactionAmountField.getText()), transactionDatePicker.getText());
            category.addTransaction(transaction);
            budget.calculateAmountRemaining();
            activeBudgetPanel.remove(budgetStartDateLabel);
            activeBudgetPanel.remove(budgetAmountRemainingLabel);
            activeBudgetPanel.remove(budgetAmountProgressBar);
            refresh(activeBudgetPanel);
            categoriesPanel.remove(categoriesScrollPane);
            refresh(categoriesPanel);
            updateRecentTransactionsPanel();
            initializeActiveBudgetPanelComponents();
            initializeCategoriesTable();
            initializeRecentTransactionsTable();
            addTransactionSuccess();
        } catch (EmptyNameException | NegativeAmountException | ZeroAmountException exception) {
            addTransactionFailure(exception.getMessage());
        } catch (NumberFormatException numberFormatException) {
            addTransactionFailure("You must enter an amount.");
        }
    }

    /**
     * Shows the "Transaction has been successfully added." message dialog and closes the dialog to add a transaction.
     */
    private void addTransactionSuccess() {
        transactionNameField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        transactionAmountField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
        refresh(optionPaneToAddTransaction);
        playSound(SUCCESS_SOUND);
        JOptionPane.showMessageDialog(null, "Transaction has been successfully added.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        dialogToAddTransaction.setVisible(false);
    }

    /**
     * Shows the error message dialog with the error that caused adding a transaction to fail.
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
        refresh(optionPaneToAddCategory);
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(null, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates the specified categories table model.
     *
     * @param categoriesTableModel the categories table model to be updated
     */
    private void updateCategoriesTableModel(DefaultTableModel categoriesTableModel) {
        for (Category nextCategory : budget.getCategories()) {
            Object[] categoryData = {nextCategory.getName(), "＄" + decimalFormat.format(nextCategory.getAmountSpent())};
            categoriesTableModel.addRow(categoryData);
        }
    }

    /**
     * Updates the specified recent transactions table model.
     *
     * @param recentTransactionsTableModel the recent transactions table model to be updated
     */
    private void updateRecentTransactionsTableModel(DefaultTableModel recentTransactionsTableModel) {
        for (Category nextCategory : budget.getCategories()) {
            for (Transaction nextTransaction : nextCategory.getTransactions()) {
                Object[] transactionData = {nextTransaction.getName(), nextCategory.getName(),
                        nextTransaction.getDate(), "＄" + decimalFormat.format(nextTransaction.getAmount())};
                recentTransactionsTableModel.addRow(transactionData);
            }
        }
    }

    /**
     * Updates the categories panel.
     * Removes the "You have no categories." label if the active budget has one category,
     * removes the categories scroll pane otherwise.
     */
    private void updateCategoriesPanel() {
        if (budget.getCategories().size() == 1) {
            categoriesPanel.remove(emptyCategoriesLabel);
        } else {
            categoriesPanel.remove(categoriesScrollPane);
        }
        refresh(categoriesPanel);
    }

    /**
     * Updates the recent transactions panel.
     * Removes the "You have no transactions." label if the active budget has one transaction,
     * removes the recent transactions scroll pane otherwise.
     */
    private void updateRecentTransactionsPanel() {
        if (numberOfTransactions() == 1) {
            recentTransactionsPanel.remove(emptyTransactionsLabel);
        } else {
            recentTransactionsPanel.remove(recentTransactionsScrollPane);
        }
        refresh(recentTransactionsPanel);
    }

    /**
     * Returns the number of transactions in the active budget.
     *
     * @return the number of transactions in the active budget
     */
    private int numberOfTransactions() {
        int numberOfTransactions = 0;
        for (Category nextCategory : budget.getCategories()) {
            numberOfTransactions += nextCategory.getTransactions().size();
        }
        return numberOfTransactions;
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
     * Refreshes this overview panel.
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
