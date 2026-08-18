package com.loan_org.customer_management.customer.client.pincode.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.loan_org.customer_management.customer.client.pincode.PincodeApiResponse;
import com.loan_org.customer_management.customer.client.pincode.PincodeLookupResponse;
import com.loan_org.customer_management.customer.client.pincode.PincodeLookupService;
import com.loan_org.customer_management.http.GenericHttpClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PincodeLookupServiceImpl implements PincodeLookupService {

    private final GenericHttpClient httpClient;

    @Value("${external.pincode.requestUrl}")
    private String pincodeUrl;

    @Override
    public PincodeLookupResponse lookup(String pinCode) {

        String url = pincodeUrl + "/" + pinCode;

        List<PincodeApiResponse> responses = httpClient.get(
                url,
                new ParameterizedTypeReference<List<PincodeApiResponse>>() {
                }
        );

        if (responses == null || responses.isEmpty()) {
            log.warn("No response received from pincode API for pincode: {}", pinCode);
            return null;
        }

        PincodeApiResponse response = responses.get(0);

        if (!"Success".equalsIgnoreCase(response.getStatus())) {
            throw new RuntimeException(
                    "Pincode lookup failed: " + response.getMessage()
            );
        }

        List<PincodeLookupResponse.PostOffice> postOffices =
                response.getPostOffice() == null
                        ? List.of()
                        : response.getPostOffice()
                                .stream()
                                .map(postOffice ->
                                        PincodeLookupResponse.PostOffice.builder()
                                                .name(postOffice.getName())
                                                .branchType(postOffice.getBranchType())
                                                .deliveryStatus(postOffice.getDeliveryStatus())
                                                .district(postOffice.getDistrict())
                                                .division(postOffice.getDivision())
                                                .region(postOffice.getRegion())
                                                .block(postOffice.getBlock())
                                                .state(postOffice.getState())
                                                .country(postOffice.getCountry())
                                                .build()
                                )
                                .toList();

        return PincodeLookupResponse.builder()
                .pincode(pinCode)
                .status(response.getStatus())
                .message(response.getMessage())
                .postOffices(postOffices)
                .build();
    }
}