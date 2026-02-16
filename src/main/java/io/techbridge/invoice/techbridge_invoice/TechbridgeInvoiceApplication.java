package io.techbridge.invoice.techbridge_invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TechbridgeInvoiceApplication {
	private static final int STRENGTH = 12;

	public static void main(String[] args) {
		SpringApplication.run(TechbridgeInvoiceApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(STRENGTH);
	}
}
