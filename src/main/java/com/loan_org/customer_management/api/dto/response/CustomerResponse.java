package com.loan_org.customer_management.api.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.entity.enums.CustomerType;

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