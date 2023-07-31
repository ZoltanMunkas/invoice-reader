package com.taboola.invoicereader.services.impl;

import com.taboola.invoicereader.models.InvoiceDigit;
import com.taboola.invoicereader.models.InvoiceLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AsciiDigitConverterTest {

    @InjectMocks
    AsciiDigitConverter asciiDigitConverter;

    private InvoiceLine createTestObject() {
        return InvoiceLine.builder()
                .rawLine(new String[][]{
                        new String[]{" ", "_", " ", " ", "_", " ", " ", "_", " ", " ", " ", " ", " ", " ", " ", " ", "_", " ", " ", " ", " ", " ", "_", " ", " ", "_", " "},
                        new String[]{"|", "_", " ", "|", " ", "|", "|", " ", "|", " ", " ", "|", "|", "_", "|", " ", "_", "|", " ", " ", "|", "|", "_", " ", "|", "_", " "},
                        new String[]{"|", "_", "|", "|", "_", "|", "|", "_", "|", " ", " ", "|", " ", " ", "|", " ", "_", "|", " ", " ", "|", " ", "_", "|", " ", "_", "|"},})
                .build();
    }
    @Test
    void processLine() {
        InvoiceLine invoiceLine = createTestObject();
        List<InvoiceDigit> digits = asciiDigitConverter.parseDigitLinesToDigits(invoiceLine.getRawLine());
        invoiceLine.setParsedLine(digits);
        asciiDigitConverter.processLine(invoiceLine);

        assertEquals(6, invoiceLine.getParsedLine().get(0).getValue());
        assertEquals(0, invoiceLine.getParsedLine().get(1).getValue());
        assertEquals(0, invoiceLine.getParsedLine().get(2).getValue());
        assertEquals(1, invoiceLine.getParsedLine().get(3).getValue());
        assertEquals(4, invoiceLine.getParsedLine().get(4).getValue());
        assertEquals(3, invoiceLine.getParsedLine().get(5).getValue());
        assertEquals(1, invoiceLine.getParsedLine().get(6).getValue());
        assertEquals(5, invoiceLine.getParsedLine().get(7).getValue());
        assertEquals(5, invoiceLine.getParsedLine().get(8).getValue());
    }

    @Test
    void convertSingleDigitInvalid() {
        InvoiceDigit testObj = new InvoiceDigit("invalid");
        asciiDigitConverter.convertSingleDigit(testObj);
        assertTrue(testObj.isInvalid());
    }
}