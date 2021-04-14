package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Represents a text field with an icon.
 * Code referenced from:
 * https://gmigdos.wordpress.com/2010/03/30/java-a-custom-jtextfield-for-searching/
 */
public class IconTextField extends JTextField {
    private Icon icon;
    private Insets insets;

    /**
     * Creates a new icon text field with the specified number of columns.
     *
     * @param columns the number of columns to use to calculate the preferred width
     */
    public IconTextField(int columns) {
        super(columns);
        this.icon = null;
        Border border = UIManager.getBorder("TextField.border");
        JTextField textField = new JTextField();
        this.insets = border.getBorderInsets(textField);
    }

    /**
     * Sets the icon of this icon text field to the specified icon.
     *
     * @param icon the icon to be set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int textXCoordinate = 2;
        if (icon != null) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int iconXCoordinate = insets.left + 5;
            int iconYCoordinate = (this.getHeight() - iconHeight) / 2;
            textXCoordinate = iconXCoordinate + iconWidth + 5;
            icon.paintIcon(this, graphics, iconXCoordinate, iconYCoordinate);
        }
        setMargin(new Insets(5, textXCoordinate, 5, 5));
    }
}
