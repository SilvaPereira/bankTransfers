package com.example.bankTransfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.bankTransfers")
public class BankTransfersApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransfersApplication.class, args);
	}

}
