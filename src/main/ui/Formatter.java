package ui;

import java.math.BigDecimal;

// Provides general formatting methods
public abstract class Formatter {
    protected String formattedCategoryAmountSpent = "";
    protected BigDecimal ten = new BigDecimal("10.00");
    protected BigDecimal oneHundred = new BigDecimal("100.00");
    protected BigDecimal oneThousand = new BigDecimal("1000.00");
    protected BigDecimal tenThousand = new BigDecimal("10000.00");
    protected BigDecimal oneHundredThousand = new BigDecimal("100000.00");
    protected BigDecimal oneMillion = new BigDecimal("1000000.00");

    // MODIFIES: this
    // EFFECTS: formats the given amount spent in the category depending on the value and returns it in the table
    public abstract String formatCategoryAmountSpent(BigDecimal categoryAmountSpent);

    // MODIFIES: this
    // EFFECTS: formats the given date depending on the value and returns it in the table
    public String formatDate(String date, String singleDigitDayAndMonthSpacer, String mixedDigitsDayAndMonthSpacer,
                             String doubleDigitsDayAndMonthSpacer) {
        String formattedDate;
        if (date.length() == 8) {
            formattedDate = date + singleDigitDayAndMonthSpacer;
        } else if (date.length() == 9) {
            formattedDate = date + mixedDigitsDayAndMonthSpacer;
        } else {
            formattedDate = date + doubleDigitsDayAndMonthSpacer;
        }
        return formattedDate;
    }
}
