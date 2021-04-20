package ui;

import java.awt.*;

/**
 * Represents a repository of colours.
 */
public interface ColourRepository {
    Color ACCENT_COLOUR = new Color(86, 138, 242);
    Color ACCENT_BORDER_COLOUR = new Color(66, 105, 185);
    Color BACKGROUND_COLOUR = new Color(33, 37, 43);
    Color TABLE_HEADER_COLOR = new Color(40, 44, 52);
    Color SUCCESS_COLOUR = new Color(42, 82, 50);
    Color SUCCESS_FOCUS_COLOUR = new Color(100, 177, 116);
    Color[] SUCCESS_COLOURS = new Color[] {SUCCESS_FOCUS_COLOUR, SUCCESS_COLOUR};
    Color[] DOUGHNUT_CHART_COLOURS = new Color[] {ACCENT_COLOUR, new Color(151, 183, 247),
            new Color(155, 244, 169), new Color(181, 129, 242), new Color(246, 233, 164),
            new Color(201, 143, 200), new Color(196, 223, 162), new Color(139, 130, 226),
            new Color(137, 186, 222), new Color(251, 227, 169), new Color(163, 130, 243),
            new Color(202, 157, 134), new Color(156, 228, 167), new Color(175, 135, 252),
            new Color(251, 255, 162), new Color(246, 187, 251), new Color(202, 230, 239),
            new Color(184, 145, 155), new Color(161, 149, 218), new Color(230, 250, 220),
            new Color(163, 160, 232)};
}
