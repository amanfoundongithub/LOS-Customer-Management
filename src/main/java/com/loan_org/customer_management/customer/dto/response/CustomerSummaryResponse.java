package com.loan_org.customer_management.customer.dto.response;

import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryResponse {

    private String id;

    private String customerNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private CustomerType customerType;

    private CustomerStatus status;

    private Instant createdAt;
}