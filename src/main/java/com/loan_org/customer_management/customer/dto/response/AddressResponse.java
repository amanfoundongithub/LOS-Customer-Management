package com.loan_org.customer_management.customer.dto.response;

import com.loan_org.customer_management.customer.enums.AddressType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private String addressId;

    private AddressType type;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String city;

    private String district;

    private String state;

    private String postalCode;

    private String country;

    private boolean primary;
}