package ui;

import model.Account;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Represents the settings panel.
 */
public class SettingsPanel extends JPanel implements ColourRepository, FontRepository {
    private static final String JSON_STORE = "./data/accounts.json";
    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private RoundedPanel autoSavePanel;
    private JRadioButton autoSaveButton;

    /**
     * Creates a new settings panel with the specified account.
     *
     * @param account the account the user is signed in to
     */
    public SettingsPanel(Account account) {
        this.account = account;
        setLayout(new GridBagLayout());
        initializeJson();
        initializeAutoSavePanel();
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the Auto Save panel and adds it to this settings panel.
     */
    private void initializeAutoSavePanel() {
        JLabel autoSaveLabel = new JLabel("Auto Save");
        autoSavePanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        autoSaveLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        autoSaveLabel.setForeground(Color.WHITE);
        autoSavePanel.setPreferredSize(new Dimension(1060, 117));
        autoSavePanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        autoSavePanel.setLayout(new GridBagLayout());
        initializeSeparatorForAutoSavePanel();
        initializeDescriptionLabelForAutoSavePanel();
        initializeAutoSaveButton();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        autoSavePanel.add(autoSaveLabel, gridBagConstraints);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-348, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        add(autoSavePanel, gridBagConstraints);
    }

    /**
     * Initializes the description label and adds it to the Auto Save panel.
     */
    private void initializeDescriptionLabelForAutoSavePanel() {
        JLabel descriptionLabel = new JLabel("Auto Save is a feature that automatically saves any changes you"
                + " make. Toggle the button to turn it on or off.");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        descriptionLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        descriptionLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -47, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        autoSavePanel.add(descriptionLabel, gridBagConstraints);
    }

    /**
     * Initializes the Auto Save button.
     */
    private void initializeAutoSaveButton() {
        autoSaveButton = new JRadioButton(autoSaveText());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        autoSaveButton.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        autoSaveButton.setForeground(Color.WHITE);
        autoSaveButton.setSelected(account.isAutoSave());
        autoSaveButton.setIconTextGap(10);
        addActionListenerToAutoSaveButton();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-28, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        autoSavePanel.add(autoSaveButton, gridBagConstraints);
    }

    /**
     * Adds an action listener to the Auto Save button.
     */
    private void addActionListenerToAutoSaveButton() {
        autoSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                account.setAutoSave(autoSaveButton.isSelected());
                autoSaveButton.setText(autoSaveText());
                refresh(autoSaveButton);
                try {
                    jsonWriter.open();
                    jsonWriter.write(account);
                    jsonWriter.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    /**
     * Returns the Auto Save state.
     *
     * @return the Auto Save state
     */
    private String autoSaveText() {
        String autoSave;
        if (account.isAutoSave()) {
            autoSave = "On";
        } else {
            autoSave = "Off";
        }
        return autoSave;
    }

    /**
     * Initializes the separator for the Auto Save panel.
     */
    private void initializeSeparatorForAutoSavePanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(13, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        autoSavePanel.add(separator, gridBagConstraints);
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
