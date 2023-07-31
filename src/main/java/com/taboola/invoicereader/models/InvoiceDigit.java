package com.taboola.invoicereader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDigit {

    private String asciiDigit;
    private int value;
    private boolean invalid = false;

    public InvoiceDigit(String asciiDigit) {
        this.asciiDigit = asciiDigit;
    }

    @Override
    public String toString() {
        if(invalid) {
            return "?";
        }
        return String.valueOf(value);
    }
}
