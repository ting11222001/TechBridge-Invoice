package io.techbridge.invoice.techbridge_invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TechbridgeInvoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechbridgeInvoiceApplication.class, args);
	}
}
