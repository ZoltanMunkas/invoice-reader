package com.taboola.invoicereader.services.impl;

import com.taboola.invoicereader.models.InvoiceDigit;
import com.taboola.invoicereader.models.InvoiceLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceRepairServiceTest {

    @Mock
    AsciiDigitConverter asciiDigitConverter;

    @Mock
    FileHandlerService fileHandlerService;

    @InjectMocks
    InvoiceRepairService invoiceRepairService;

    private static final List<String> TEST_INPUT = List.of(
            " _     _  _  _  _  _     _",
            "|_|  ||_| _||_ |_  _||_||_|",
            "|_|  | _||_ |_||_||_   ||_|",
            "",
            "_     _  _        _ ",
            "|  ||_   || ||_ |_||_||_|",
            "|  | _|  ||_||_|  |  ||_|");

    private static final InvoiceDigit VALID_DIGIT_1 = InvoiceDigit.builder()
            .value(8)
            .invalid(false)
            .build();
    private static final InvoiceDigit VALID_DIGIT_2 = InvoiceDigit.builder()
            .value(1)
            .invalid(false)
            .build();
    private static final InvoiceDigit VALID_DIGIT_3 = InvoiceDigit.builder()
            .value(5)
            .invalid(false)
            .build();
    private static final InvoiceDigit INVALID_DIGIT = InvoiceDigit.builder()
            .value(0)
            .invalid(true)
            .build();

    private static final InvoiceLine VALID_LINE = InvoiceLine.builder()
            .parsedLine(List.of(VALID_DIGIT_1, VALID_DIGIT_2))
            .build();

    private static final InvoiceLine INVALID_LINE = InvoiceLine.builder()
            .parsedLine(List.of(VALID_DIGIT_3, INVALID_DIGIT))
            .build();


    @Test
    void repairInvoices() {
        when(fileHandlerService.readFileAt("input")).thenReturn(TEST_INPUT);
        when(asciiDigitConverter.parseDigitLinesToDigits(any()))
                .thenReturn(List.of(VALID_DIGIT_1, VALID_DIGIT_2))
                .thenReturn(List.of(VALID_DIGIT_3, INVALID_DIGIT));
        when(asciiDigitConverter.processLine(any()))
                .thenReturn(VALID_LINE)
                .thenReturn(INVALID_LINE);
        invoiceRepairService.repairInvoices("input", "output");

        verify(fileHandlerService).writeFileTo("output", List.of("81\n", "5? ILLEGAL\n"));
    }
}