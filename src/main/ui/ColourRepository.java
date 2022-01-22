package ui;

import java.awt.*;

/**
 * Represents a repository of colours.
 */
public interface ColourRepository {
    Color ACCENT_COLOUR = new Color(86, 138, 242);
    Color ACCENT_BORDER_COLOUR = new Color(66, 105, 185);
    Color BACKGROUND_COLOUR = new Color(33, 37, 43);
    Color SUCCESS_COLOUR = new Color(42, 82, 50);
    Color SUCCESS_FOCUS_COLOUR = new Color(100, 177, 116);
    Color[] SUCCESS_COLOURS = new Color[] {SUCCESS_FOCUS_COLOUR, SUCCESS_COLOUR};
    Color ERROR_COLOUR = new Color(128, 45, 67);
    Color ERROR_BORDER_COLOUR = new Color(82, 37, 48);
    Color[] DOUGHNUT_CHART_COLOURS = new Color[] {new Color(9, 14, 24), new Color(26, 41, 73),
            new Color(43, 69, 121), new Color(60, 97, 169), new Color(77, 124, 218),
            new Color(103, 150, 243), new Color(137, 173, 246), new Color(171, 197, 249),
            new Color(204, 220, 251), new Color(238, 243, 254)};
}
