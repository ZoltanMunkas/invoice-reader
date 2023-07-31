package com.taboola.invoicereader.enums;

/***
 * This enum links the numerical values with the ASCII 7 segment representation that makes it more readable and configurable
 */
public enum AsciiDigit {
    ZERO(0, """
             _\s
            | |
            |_|"""),
    ONE(1, """
              \s
              |
              |\
            """),
    TWO(2, """
             _\s
             _|
            |_\s"""),
    THREE(3, """
             _\s
             _|
             _|\
            """),
    FOUR(4, """
              \s
            |_|
              |"""),
    FIVE(5, """
             _\s
            |_\s
             _|"""),
    SIX(6, """
             _\s
            |_\s
            |_|"""),
    SEVEN(7, """
             _\s
              |
              |\
            """),
    EIGHT(8, """
             _\s
            |_|
            |_|"""),
    NINE(9, """
             _\s
            |_|
             _|""");

    private int intValue;
    private String stringValue;

    AsciiDigit(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static AsciiDigit getByStringValue(String stringValue) {
        for (AsciiDigit asciiDigit : AsciiDigit.values()) {
            if (asciiDigit.getStringValue().equals(stringValue)) {
                return asciiDigit;
            }
        }
        throw new IllegalArgumentException("No enum constant with string value: " + stringValue);
    }
}
