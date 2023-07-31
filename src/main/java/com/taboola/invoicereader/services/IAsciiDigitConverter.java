package com.taboola.invoicereader.services;

import com.taboola.invoicereader.models.InvoiceDigit;
import com.taboola.invoicereader.models.InvoiceLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IAsciiDigitConverter {

    /***
     * Takes in an InvoiceLine and goes over the 3x3 squares containing the digit representations and parses them into
     * actual numerical values
     */
    public InvoiceLine processLine(InvoiceLine line);

    /***
     * Converts a single digit representation into a numerical value and stores it in the object
     * @param digit The object containing the string defining the ASCII digit
     */
    public void convertSingleDigit(InvoiceDigit digit);

    /***
     * Takes in the raw input in a 2D string matrix, and processes the values into an InvoiceDigit
     */
    public List<InvoiceDigit> parseDigitLinesToDigits(String[][] digitLines);
}
