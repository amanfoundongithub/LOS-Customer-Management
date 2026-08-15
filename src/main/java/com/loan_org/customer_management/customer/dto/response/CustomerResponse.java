package com.loan_org.customer_management.customer.dto.response;

import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String id;

    private Long version;

    private String customerNumber;

    private String iamUserId;

    private PersonalInformationResponse personalInformation;

    private ContactInformationResponse contactInformation;

    private List<AddressResponse> addresses;

    private List<IdentificationResponse> identifications;

    private CustomerType customerType;

    private CustomerStatus status;

    private CustomerPreferencesResponse preferences;

    private Map<String, String> attributes;

    private Instant createdAt;

    private Instant updatedAt;
}