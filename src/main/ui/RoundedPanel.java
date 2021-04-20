package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a rounded panel.
 */
public class RoundedPanel extends JPanel implements ColourRepository {

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Dimension arcs = new Dimension(12, 12);
        int width = getWidth();
        int height = getHeight();
        Graphics2D roundedPanel = (Graphics2D) graphics;
        roundedPanel.setColor(new Color(50, 54, 60));
        roundedPanel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        roundedPanel.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}
