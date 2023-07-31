package com.taboola.invoicereader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceLine {
    private String[][] rawLine;
    private List<InvoiceDigit> parsedLine;

    public InvoiceLine(String[][] rawLine) {
        this.rawLine = rawLine;
    }

    public boolean isLineValid() {
        for(InvoiceDigit current : parsedLine) {
            if(current.isInvalid()) {
                return true;
            }
        }
        return false;
    }

    /***
     * Returns the desired output value of the segment of the input. It contains the valid digits, "?" in places of invalid digits
     * and an ILLEGAL marker at the end of invalid lines.
     * @return
     */
    @Override
    public String toString() {
        if(isLineValid()) {
            return String.join(" ", getStringDigits(), "ILLEGAL\n");
        }
        return String.join("", getStringDigits(), "\n");
    }

    private String getStringDigits() {
        return String.join("", parsedLine.stream()
                .map(InvoiceDigit::toString)
                .toList());
    }
}
