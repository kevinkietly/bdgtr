package ui;

import model.Account;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main panel.
 */
public class MainPanel extends JPanel implements FontRepository {
    private Account account;
    private JTabbedPane sidebarTabbedPane;
    private List<JLabel> sidebarLabels;

    /**
     * Creates a new main panel with the specified account.
     *
     * @param account the account the user is signed in to
     * @throws IOException if an error occurs reading data from file
     */
    public MainPanel(Account account) throws IOException {
        this.account = account;
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
        initializeSidebarIcons();
        initializeSidebarTabs();
        add(sidebarTabbedPane);
    }

    /**
     * Initializes the sidebar labels.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void initializeSidebarLabels() throws IOException {
        BufferedImage bdgtrIconImage = ImageIO.read(new File("./icons/Bdgtr_Icon.png"));
        ImageIcon bdgtrIconIcon = new ImageIcon(bdgtrIconImage);
        JLabel bdgtrIconLabel = new JLabel(bdgtrIconIcon);
        JLabel overviewLabel = new JLabel("Overview");
        JLabel budgetsLabel = new JLabel("Budgets");
        JLabel categoriesLabel = new JLabel("Categories");
        JLabel transactionsLabel = new JLabel("Transactions");
        sidebarLabels = new ArrayList<>();
        sidebarLabels.add(bdgtrIconLabel);
        sidebarLabels.add(overviewLabel);
        sidebarLabels.add(budgetsLabel);
        sidebarLabels.add(categoriesLabel);
        sidebarLabels.add(transactionsLabel);
        for (JLabel nextLabel : sidebarLabels) {
            if (nextLabel.equals(bdgtrIconLabel)) {
                nextLabel.setPreferredSize(new Dimension(200, 200));
            } else {
                nextLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
                nextLabel.setForeground(Color.WHITE);
                nextLabel.setPreferredSize(new Dimension(150, 50));
            }
        }
    }

    /**
     * Initializes the sidebar icons.
     */
    private void initializeSidebarIcons() {
        for (JLabel nextLabel : sidebarLabels.subList(1, sidebarLabels.size())) {
            nextLabel.setIcon(new ImageIcon("./icons/" + nextLabel.getText().toLowerCase() + ".png"));
            nextLabel.setIconTextGap(20);
        }
    }

    /**
     * Initializes the sidebar tabs and adds them to the sidebar tabbed pane.
     */
    private void initializeSidebarTabs() {
        sidebarTabbedPane.addTab(null, null);
        sidebarTabbedPane.addTab(null, new OverviewPanel(account));
        sidebarTabbedPane.addTab(null, null);
        sidebarTabbedPane.addTab(null, null);
        sidebarTabbedPane.addTab(null, null);
        sidebarTabbedPane.setEnabledAt(0, false);
        sidebarTabbedPane.setSelectedIndex(1);
        for (int index = 0; index < sidebarLabels.size(); index++) {
            sidebarTabbedPane.setTabComponentAt(index, sidebarLabels.get(index));
        }
    }
}
