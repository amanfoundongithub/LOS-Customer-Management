package com.loan_org.customer_management;

import com.loan_org.customer_management.config.api.ApiProperties;
import com.loan_org.customer_management.config.logging.RequestLoggingProperties;
import com.loan_org.customer_management.config.mdc.MdcProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
    MdcProperties.class,
    RequestLoggingProperties.class,
    ApiProperties.class
})
public class CustomerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
	}

}
