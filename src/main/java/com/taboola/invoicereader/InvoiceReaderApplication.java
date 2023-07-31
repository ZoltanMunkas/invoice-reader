package com.taboola.invoicereader;

import com.taboola.invoicereader.services.impl.InvoiceRepairService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class InvoiceReaderApplication {

	/***
	 * To start the application, enter the input file and the output file in an absolute path, example provided
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(InvoiceReaderApplication.class, args);
		InvoiceRepairService invoiceRepairService = context.getBean(InvoiceRepairService.class);
		invoiceRepairService.repairInvoices(
				"D:\\Documents\\Prog\\invoice-reader\\invoice-reader\\input\\invoice\\input_Q1a.txt",
				"D:\\Documents\\Prog\\invoice-reader\\invoice-reader\\output\\output_Q1a.txt");
	}

}
