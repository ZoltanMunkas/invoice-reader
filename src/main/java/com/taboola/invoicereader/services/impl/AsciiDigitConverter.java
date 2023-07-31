package com.taboola.invoicereader.services.impl;

import com.taboola.invoicereader.enums.AsciiDigit;
import com.taboola.invoicereader.models.InvoiceDigit;
import com.taboola.invoicereader.models.InvoiceLine;
import com.taboola.invoicereader.services.IAsciiDigitConverter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsciiDigitConverter implements IAsciiDigitConverter {

    private static final Logger logger = Logger.getLogger(AsciiDigitConverter.class);

    @Override
    public InvoiceLine processLine(InvoiceLine line) {
        line.getParsedLine().forEach(this::convertSingleDigit);
        return line;
    }

    @Override
    public void convertSingleDigit(InvoiceDigit digit) {
        try {
            digit.setValue(AsciiDigit.getByStringValue(digit.getAsciiDigit()).getIntValue());
        } catch(IllegalArgumentException e) {
            digit.setInvalid(true);
        }
    }

    @Override
    public List<InvoiceDigit> parseDigitLinesToDigits(String[][] digitLines) {
        List<InvoiceDigit> digits = new ArrayList<>();

        for (int i = 0; i < digitLines[0].length; i += 3) { // Take 3 columns and parse the number contained in the square
            digits.add(parseNextDigit(digitLines, i));
        }
        return digits;
    }

    private InvoiceDigit parseNextDigit(String[][] digitValues, int startColumn) {
        String asciiDigitValue = "";
        for (int row = 0; row < 3; row++) { // Go over the 3 rows of string
            for(int column = startColumn; column < startColumn + 3; column++) { // Go over the next 3 columns
                asciiDigitValue += digitValues[row][column];
            }
            asciiDigitValue += "\n";
        }
        return new InvoiceDigit(removeLastLineBreak(asciiDigitValue));
    }

    private String removeLastLineBreak(String value) {
        return value.substring(0, value.length() - 1);
    }
}
