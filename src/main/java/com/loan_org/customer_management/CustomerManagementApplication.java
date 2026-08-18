package com.loan_org.customer_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class CustomerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
	}

}
