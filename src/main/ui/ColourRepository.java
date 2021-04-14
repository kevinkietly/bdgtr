package ui;

import java.awt.*;

/**
 * Represents a repository of colours.
 */
public interface ColourRepository {
    public static final Color ACCENT_COLOUR = new Color(86, 138, 242);
    public static final Color ACCENT_BORDER_COLOUR = new Color(66, 105, 185);
    public static final Color BACKGROUND_COLOUR = new Color(33, 37, 43);
    public static final Color COMPONENT_BACKGROUND_COLOUR = new Color(40, 44, 52);
    public static final Color SUCCESS_COLOUR = new Color(42, 82, 50);
    public static final Color SUCCESS_FOCUS_COLOUR = new Color(100, 177, 116);
    public static final Color[] SUCCESS_COLOURS = new Color[] {SUCCESS_FOCUS_COLOUR, SUCCESS_COLOUR};
}
