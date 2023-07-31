package com.taboola.invoicereader.services.impl;

import com.taboola.invoicereader.models.InvoiceDigit;
import com.taboola.invoicereader.models.InvoiceLine;
import com.taboola.invoicereader.services.IAsciiDigitConverter;
import com.taboola.invoicereader.services.IFileHandlerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceRepairService {
    private static final Logger logger = Logger.getLogger(FileHandlerService.class);

    @Autowired
    IFileHandlerService fileHandlerService;

    @Autowired
    IAsciiDigitConverter asciiDigitConverter;


    /***
     * This is the function executing the invoice fixing, compiling all necessary data, processing the result
     * and writing the output at the given location
     * @param inputPath The location of the incorrect file
     * @param outputPath The location where the output will be written
     */
    public void repairInvoices(String inputPath, String outputPath) {
        List<String> fileContent = fileHandlerService.readFileAt(inputPath);
        List<InvoiceLine> invoiceLines = parseInputToDigitLines(fileContent);
        processInvoiceLines(invoiceLines);

        List<String> result = createOutputString(invoiceLines);
        fileHandlerService.writeFileTo(outputPath, result);
        logger.info("Process finished, result: " + result);
    }

    private void processInvoiceLines(List<InvoiceLine> invoiceLines) {
        logger.info("Parsing digits for lines");
        for(InvoiceLine current : invoiceLines) {
            current = asciiDigitConverter.processLine(current);
        }
    }

    private List<String> createOutputString(List<InvoiceLine> invoiceLines) {
        return invoiceLines.stream()
                .map(InvoiceLine::toString)
                .toList();
    }

    private List<InvoiceLine> parseInputToDigitLines(List<String> lines) {
        logger.info("Processing result into InvoiceLine chunks");
        List<InvoiceLine> invoiceLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 4) {
            String[][] nextLines = collectThreeLinesFromIndex(lines, i);
            InvoiceLine invoiceLine = new InvoiceLine(nextLines);
            List<InvoiceDigit> digits = asciiDigitConverter.parseDigitLinesToDigits(invoiceLine.getRawLine());
            invoiceLine.setParsedLine(digits);
            invoiceLines.add(invoiceLine);
        }
        return invoiceLines;
    }

    private String[][] collectThreeLinesFromIndex(List<String> lines, int i) {
        String[] line1 = lines.get(i).split("");
        String[] line2 = lines.get(i + 1).split("");
        String[] line3 = lines.get(i + 2).split("");
        return new String[][]{line1, line2, line3};
    }
}
