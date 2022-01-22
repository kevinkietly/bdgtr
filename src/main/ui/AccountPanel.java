package ui;

import model.Account;
import model.exceptions.*;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;

/**
 * Represents the account panel.
 */
public class AccountPanel extends JPanel implements ColourRepository, FontRepository {
    private static final String JSON_STORE = "./data/accounts.json";
    private static final Dimension PANEL_DIMENSIONS = new Dimension(1060, 150);
    private static final Dimension BUTTON_DIMENSIONS = new Dimension(75, 30);

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private RoundedPanel personalInformationPanel;
    private RoundedPanel usernamePanel;
    private RoundedPanel passwordPanel;
    private JButton buttonToSavePersonalInformation;
    private JButton buttonToSaveUsername;
    private JButton buttonToSavePassword;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JTextField currentPasswordField;
    private JTextField newPasswordField;
    private JTextField confirmNewPasswordField;

    /**
     * Creates a new account panel with the specified account.
     *
     * @param account the account the user is signed in to
     */
    public AccountPanel(Account account) {
        this.account = account;
        setLayout(new GridBagLayout());
        initializeJson();
        initializePersonalInformationPanel();
        initializeUsernamePanel();
        initializePasswordPanel();
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the personal information panel and adds it to this account panel.
     */
    private void initializePersonalInformationPanel() {
        JLabel personalInformationLabel = new JLabel("Personal Information");
        personalInformationPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        personalInformationLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        personalInformationLabel.setForeground(Color.WHITE);
        personalInformationPanel.setPreferredSize(PANEL_DIMENSIONS);
        personalInformationPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        personalInformationPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        personalInformationPanel.add(personalInformationLabel, gridBagConstraints);
        initializeSeparatorForPersonalInformationPanel();
        initializeFirstNameField();
        initializeLastNameField();
        initializeButtonToSavePersonalInformation();
        gridBagConstraints.insets = new Insets(-168, 0, 38, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        add(personalInformationPanel, gridBagConstraints);
    }

    /**
     * Initializes the username panel and adds it to this account panel.
     */
    private void initializeUsernamePanel() {
        JLabel usernameLabel = new JLabel("Username");
        usernamePanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        usernameLabel.setForeground(Color.WHITE);
        usernamePanel.setPreferredSize(PANEL_DIMENSIONS);
        usernamePanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        usernamePanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-83, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(usernameLabel, gridBagConstraints);
        initializeSeparatorForUsernamePanel();
        initializeUsernameField();
        initializeButtonToSaveUsername();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 38, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(usernamePanel, gridBagConstraints);
    }

    /**
     * Initializes the password panel and adds it to this account panel.
     */
    private void initializePasswordPanel() {
        JLabel passwordLabel = new JLabel("Password");
        passwordPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        passwordLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_BOLD);
        passwordLabel.setForeground(Color.WHITE);
        passwordPanel.setPreferredSize(PANEL_DIMENSIONS);
        passwordPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        passwordPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-83, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        passwordPanel.add(passwordLabel, gridBagConstraints);
        initializeSeparatorForPasswordPanel();
        initializeCurrentPasswordField();
        initializeNewPasswordField();
        initializeConfirmNewPasswordField();
        initializeButtonToSavePassword();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        add(passwordPanel, gridBagConstraints);
    }

    /**
     * Initializes the first name field and adds it to the personal information panel.
     */
    private void initializeFirstNameField() {
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        firstNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        firstNameLabel.setForeground(Color.WHITE);
        firstNameField.putClientProperty("JTextField.placeholderText", account.getFirstName());
        firstNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToFirstNameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        personalInformationPanel.add(firstNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        personalInformationPanel.add(firstNameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the first name field.
     * Enables the button to save personal information if the first name field is not empty.
     */
    public void addListenerToFirstNameField() {
        firstNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSavePersonalInformation.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (firstNameField.getText().isEmpty()) {
                    buttonToSavePersonalInformation.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePersonalInformation.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the last name field and adds it to the personal information panel.
     */
    private void initializeLastNameField() {
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        lastNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        lastNameLabel.setForeground(Color.WHITE);
        lastNameField.putClientProperty("JTextField.placeholderText", account.getLastName());
        lastNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToLastNameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -187, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        personalInformationPanel.add(lastNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        personalInformationPanel.add(lastNameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the last name field.
     * Enables the button to save personal information if the last name field is not empty.
     */
    public void addListenerToLastNameField() {
        lastNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSavePersonalInformation.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (lastNameField.getText().isEmpty()) {
                    buttonToSavePersonalInformation.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePersonalInformation.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the button to save personal information and adds it to the personal information panel.
     */
    private void initializeButtonToSavePersonalInformation() {
        buttonToSavePersonalInformation = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSavePersonalInformation.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSavePersonalInformation.setBorderPainted(false);
        buttonToSavePersonalInformation.setBackground(ACCENT_COLOUR);
        buttonToSavePersonalInformation.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSavePersonalInformation.setForeground(Color.WHITE);
        buttonToSavePersonalInformation.setEnabled(false);
        addActionListenerToButtonToSavePersonalInformation();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        personalInformationPanel.add(buttonToSavePersonalInformation, gridBagConstraints);
    }

    /**
     * Initializes the button to save username and adds it to the username panel.
     */
    private void initializeButtonToSaveUsername() {
        buttonToSaveUsername = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSaveUsername.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSaveUsername.setBorderPainted(false);
        buttonToSaveUsername.setBackground(ACCENT_COLOUR);
        buttonToSaveUsername.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSaveUsername.setForeground(Color.WHITE);
        buttonToSaveUsername.setEnabled(false);
        addActionListenerToButtonToSaveUsername();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-82, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        usernamePanel.add(buttonToSaveUsername, gridBagConstraints);
    }

    /**
     * Initializes the button to save password and adds it to the password panel.
     */
    private void initializeButtonToSavePassword() {
        buttonToSavePassword = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSavePassword.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSavePassword.setBorderPainted(false);
        buttonToSavePassword.setBackground(ACCENT_COLOUR);
        buttonToSavePassword.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSavePassword.setForeground(Color.WHITE);
        buttonToSavePassword.setEnabled(false);
        addActionListenerToButtonToSavePassword();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-82, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        passwordPanel.add(buttonToSavePassword, gridBagConstraints);
    }

    /**
     * Adds an action listener to the button to save personal information.
     */
    private void addActionListenerToButtonToSavePersonalInformation() {
        buttonToSavePersonalInformation.addActionListener(event -> {
            buttonToSavePersonalInformation.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSavePersonalInformation.setBorderPainted(true);
            if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty()) {
                changeFullName();
            } else if (!firstNameField.getText().isEmpty()) {
                changeFirstName();
            } else if (!lastNameField.getText().isEmpty()) {
                changeLastName();
            } else {
                JOptionPane.showMessageDialog(null,
                        "There are no changes to save.", "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Adds an action listener to the button to save username.
     */
    private void addActionListenerToButtonToSaveUsername() {
        buttonToSaveUsername.addActionListener(event -> {
            buttonToSaveUsername.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSaveUsername.setBorderPainted(true);
            if (!usernameField.getText().isEmpty()) {
                try {
                    jsonReader.read(usernameField.getText());
                    usernameField.putClientProperty("JComponent.outline", "error");
                    JOptionPane.showMessageDialog(this, "Username has already been taken.",
                            "bdgtr", JOptionPane.ERROR_MESSAGE);
                } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                        | EmptyPasswordException | EmptyNameException | NegativeAmountException | ZeroAmountException
                        | DuplicateBudgetException | DuplicateCategoryException exception) {
                    exception.printStackTrace();
                } catch (JSONException jsonException) {
                    changeUsername();
                }
            }
        });
    }

    /**
     * Adds an action listener to the button to save password.
     */
    private void addActionListenerToButtonToSavePassword() {
        buttonToSavePassword.addActionListener(event -> {
            buttonToSavePassword.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSavePassword.setBorderPainted(true);
            checkPasswordStepOne();
            refresh(this);
        });
    }

    /**
     * Checks whether the current password matches the account's password.
     */
    private void checkPasswordStepOne() {
        if (currentPasswordField.getText().equals(account.getPassword())) {
            currentPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            checkPasswordStepTwo();
        } else {
            currentPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this, "Incorrect password.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks whether the new password matches the confirm new password.
     */
    private void checkPasswordStepTwo() {
        if (newPasswordField.getText().equals(confirmNewPasswordField.getText())) {
            newPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            confirmNewPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            checkPasswordStepThree();
        } else {
            newPasswordField.putClientProperty("JComponent.outline", "error");
            confirmNewPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks whether the new password matches the current password.
     */
    private void checkPasswordStepThree() {
        if (!newPasswordField.getText().equals(currentPasswordField.getText())) {
            changePassword();
        } else {
            newPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this,
                    "Your new password cannot be the same as your current password.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Changes the full name.
     */
    private void changeFullName() {
        try {
            account.setFirstName(firstNameField.getText());
            account.setLastName(lastNameField.getText());
            firstNameField.putClientProperty("JTextField.placeholderText", firstNameField.getText());
            lastNameField.putClientProperty("JTextField.placeholderText", lastNameField.getText());
            firstNameField.setText(null);
            lastNameField.setText(null);
            refresh(firstNameField);
            refresh(lastNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the first name.
     */
    private void changeFirstName() {
        try {
            account.setFirstName(firstNameField.getText());
            firstNameField.putClientProperty("JTextField.placeholderText", firstNameField.getText());
            firstNameField.setText(null);
            refresh(firstNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the last name.
     */
    private void changeLastName() {
        try {
            account.setLastName(lastNameField.getText());
            lastNameField.putClientProperty("JTextField.placeholderText", lastNameField.getText());
            lastNameField.setText(null);
            refresh(lastNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the username.
     */
    private void changeUsername() {
        String oldUsername = account.getUsername();
        account.setUsername(usernameField.getText());
        usernameField.putClientProperty("JTextField.placeholderText", usernameField.getText());
        usernameField.setText(null);
        refresh(usernameField);
        try {
            jsonWriter.open();
            jsonWriter.delete(account, oldUsername);
            jsonWriter.close();
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Changes have been successfully saved. They will be applied the next time you sign in.",
                    "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            buttonToSavePersonalInformation.setBorderPainted(false);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the password.
     */
    private void changePassword() {
        try {
            account.setPassword(newPasswordField.getText());
            saveActionPerformed();
            currentPasswordField.putClientProperty("JComponent.outline", null);
            newPasswordField.putClientProperty("JComponent.outline", null);
            confirmNewPasswordField.putClientProperty("JComponent.outline", null);
            currentPasswordField.setText(null);
            newPasswordField.setText(null);
            confirmNewPasswordField.setText(null);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Initializes the username field and adds it to the username panel.
     */
    private void initializeUsernameField() {
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        usernameLabel.setForeground(Color.WHITE);
        usernameField.putClientProperty("JTextField.placeholderText", account.getUsername());
        usernameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToUsernameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(usernameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        usernamePanel.add(usernameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the username field.
     * Enables the button to save username if the username field is not empty.
     */
    public void addListenerToUsernameField() {
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSaveUsername.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (usernameField.getText().isEmpty()) {
                    buttonToSaveUsername.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSaveUsername.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the current password field and adds it to the password panel.
     */
    private void initializeCurrentPasswordField() {
        JLabel currentPasswordLabel = new JLabel("Current Password");
        currentPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        currentPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        currentPasswordLabel.setForeground(Color.WHITE);
        currentPasswordField.putClientProperty("JTextField.placeholderText", "Current Password");
        currentPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToCurrentPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        passwordPanel.add(currentPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        passwordPanel.add(currentPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the current password field.
     */
    public void addListenerToCurrentPasswordField() {
        currentPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (currentPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the new password field and adds it to the password panel.
     */
    private void initializeNewPasswordField() {
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        newPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordField.putClientProperty("JTextField.placeholderText", "New Password");
        newPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToNewPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -163, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(newPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        passwordPanel.add(newPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the new password field.
     */
    public void addListenerToNewPasswordField() {
        newPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (newPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the confirm new password field and adds it to the password panel.
     */
    private void initializeConfirmNewPasswordField() {
        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password");
        confirmNewPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        confirmNewPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        confirmNewPasswordLabel.setForeground(Color.WHITE);
        confirmNewPasswordField.putClientProperty("JTextField.placeholderText", "Confirm New Password");
        confirmNewPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToConfirmNewPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -30, -650);
        passwordPanel.add(confirmNewPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        passwordPanel.add(confirmNewPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the confirm new password field.
     */
    public void addListenerToConfirmNewPasswordField() {
        confirmNewPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the separator for the personal information panel.
     */
    private void initializeSeparatorForPersonalInformationPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-20, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        personalInformationPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the username panel.
     */
    private void initializeSeparatorForUsernamePanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-17, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        usernamePanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the password panel.
     */
    private void initializeSeparatorForPasswordPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-17, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        passwordPanel.add(separator, gridBagConstraints);
    }

    /**
     * Saves changes and shows the "Changes have been successfully saved.
     * They will be applied the next time you sign in." message dialog.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void saveActionPerformed() throws IOException {
        jsonWriter.open();
        jsonWriter.write(account);
        jsonWriter.close();
        JOptionPane.showMessageDialog(null,
                "Changes have been successfully saved. They will be applied the next time you sign in.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        buttonToSavePersonalInformation.setBorderPainted(false);
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
