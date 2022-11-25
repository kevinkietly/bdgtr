package ui;

import model.Account;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents the main panel.
 */
public class MainPanel extends JPanel implements FontRepository {
    private Account account;
    private boolean isNewAccount;
    private JTabbedPane sidebarTabbedPane;
    private JLabel bdgtrIconLabel;
    private JLabel homeLabel;
    private JLabel accountLabel;
    private JLabel settingsLabel;

    /**
     * Creates a new main panel with the specified account.
     *
     * @param account the account the user is signed in to
     * @param isNewAccount determines if the account is new
     * @throws IOException if an error occurs reading data from file
     */
    public MainPanel(Account account, boolean isNewAccount) throws IOException {
        this.account = account;
        this.isNewAccount = isNewAccount;
        setLayout(new BorderLayout());
        initializeSidebarTabbedPane();
    }

    /**
     * Initializes the sidebar tabbed pane and adds it to this main panel.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void initializeSidebarTabbedPane() throws IOException {
        sidebarTabbedPane = new JTabbedPane();
        sidebarTabbedPane.setTabPlacement(SwingConstants.LEFT);
        initializeSidebarLabels();
        initializeSidebarTabs();
        add(sidebarTabbedPane);
    }

    /**
     * Initializes the sidebar labels.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void initializeSidebarLabels() throws IOException {
        BufferedImage bdgtrIconImage = ImageIO.read(new File("./icons/Icon.png"));
        ImageIcon bdgtrIconIcon = new ImageIcon(bdgtrIconImage);
        bdgtrIconLabel = new JLabel(bdgtrIconIcon);
        homeLabel = new JLabel("Home");
        accountLabel = new JLabel("Account");
        settingsLabel = new JLabel("Settings");
        bdgtrIconLabel.setPreferredSize(new Dimension(200, 200));
        homeLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        homeLabel.setForeground(Color.WHITE);
        homeLabel.setPreferredSize(new Dimension(150, 50));
        homeLabel.setIcon(new ImageIcon("./icons/Home.png"));
        homeLabel.setIconTextGap(20);
        accountLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        accountLabel.setForeground(Color.WHITE);
        accountLabel.setPreferredSize(new Dimension(150, 50));
        accountLabel.setIcon(new ImageIcon("./icons/Account.png"));
        accountLabel.setIconTextGap(20);
        settingsLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        settingsLabel.setForeground(Color.WHITE);
        settingsLabel.setPreferredSize(new Dimension(150, 50));
        settingsLabel.setIcon(new ImageIcon("./icons/Settings.png"));
        settingsLabel.setIconTextGap(20);
    }

    /**
     * Initializes the sidebar tabs and adds them to the sidebar tabbed pane.
     */
    private void initializeSidebarTabs() {
        sidebarTabbedPane.addTab(null, null);
        sidebarTabbedPane.addTab(null, new HomePanel(account, isNewAccount));
        sidebarTabbedPane.addTab(null, new AccountPanel(account, this));
        sidebarTabbedPane.addTab(null, new SettingsPanel(account));
        if (isNewAccount) {
            sidebarTabbedPane.setEnabled(false);
        }
        sidebarTabbedPane.setEnabledAt(0, false);
        sidebarTabbedPane.setSelectedIndex(1);
        sidebarTabbedPane.setTabComponentAt(0, bdgtrIconLabel);
        sidebarTabbedPane.setTabComponentAt(1, homeLabel);
        sidebarTabbedPane.setTabComponentAt(2, accountLabel);
        sidebarTabbedPane.setTabComponentAt(3, settingsLabel);
    }
}
