package com.loan_org.customer_management.customer.client.pincode;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PincodeLookupResponse {

    private String pincode;

    private String status;

    private String message;

    private List<PostOffice> postOffices;

    @Getter
    @Builder
    public static class PostOffice {

        private String name;

        private String branchType;

        private String deliveryStatus;

        private String district;

        private String division;

        private String region;

        private String block;

        private String state;

        private String country;
    }
}