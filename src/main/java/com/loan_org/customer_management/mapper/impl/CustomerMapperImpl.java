package com.loan_org.customer_management.mapper.impl;

import com.loan_org.customer_management.api.dto.request.*;
import com.loan_org.customer_management.api.dto.response.*;
import com.loan_org.customer_management.entity.*;
import com.loan_org.customer_management.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomerMapperImpl implements CustomerMapper {


    public CustomerDocument toDocument(
            CreateCustomerRequest request
    ) {

        if (request == null) {
            return null;
        }

        return CustomerDocument.builder()
                .iamUserId(request.getIamUserId())
                .personalInformation(
                        toPersonalInformation(
                                request.getPersonalInformation()
                        )
                )
                .contactInformation(
                        toContactInformation(
                                request.getContactInformation()
                        )
                )
                .addresses(
                        request.getAddresses() == null
                                ? Collections.emptyList()
                                : request.getAddresses()
                                    .stream()
                                    .map(this::toAddress)
                                    .toList()
                )
                .identifications(
                        request.getIdentifications() == null
                                ? Collections.emptyList()
                                : request.getIdentifications()
                                    .stream()
                                    .map(this::toIdentification)
                                    .toList()
                )
                .customerType(request.getCustomerType())
                .build();
    }


    public PersonalInformation toPersonalInformation(
            PersonalInformationRequest request
    ) {

        if (request == null) {
            return null;
        }

        return PersonalInformation.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .build();
    }


    public ContactInformation toContactInformation(
            ContactInformationRequest request
    ) {

        if (request == null) {
            return null;
        }

        return ContactInformation.builder()
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .alternateMobileNumber(
                        request.getAlternateMobileNumber()
                )
                .emailVerified(false)
                .mobileVerified(false)
                .build();
    }


    public Address toAddress(
            CreateAddressRequest request
    ) {

        if (request == null) {
            return null;
        }

        return Address.builder()
                .type(request.getType())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .district(request.getDistrict())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .primary(
                        Boolean.TRUE.equals(request.getPrimary())
                )
                .build();
    }


    public Identification toIdentification(
            CreateIdentificationRequest request
    ) {

        if (request == null) {
            return null;
        }

        return Identification.builder()
                .type(request.getType())
                .value(request.getValue())
                .documentReference(
                        request.getDocumentReference()
                )
                .verified(false)
                .build();
    }


    public CustomerResponse toResponse(
            CustomerDocument document
    ) {

        if (document == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(document.getId())
                .version(document.getVersion())
                .customerNumber(document.getCustomerNumber())
                .iamUserId(document.getIamUserId())
                .personalInformation(
                        toPersonalInformationResponse(
                                document.getPersonalInformation()
                        )
                )
                .contactInformation(
                        toContactInformationResponse(
                                document.getContactInformation()
                        )
                )
                .addresses(
                        document.getAddresses() == null
                                ? Collections.emptyList()
                                : document.getAddresses()
                                    .stream()
                                    .map(this::toAddressResponse)
                                    .toList()
                )
                .identifications(
                        document.getIdentifications() == null
                                ? Collections.emptyList()
                                : document.getIdentifications()
                                    .stream()
                                    .map(this::toIdentificationResponse)
                                    .toList()
                )
                .customerType(document.getCustomerType())
                .status(document.getStatus())
                .preferences(
                        toPreferencesResponse(
                                document.getPreferences()
                        )
                )
                .attributes(document.getAttributes())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }


    public PersonalInformationResponse toPersonalInformationResponse(
            PersonalInformation entity
    ) {

        if (entity == null) {
            return null;
        }

        return PersonalInformationResponse.builder()
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .nationality(entity.getNationality())
                .build();
    }


    public ContactInformationResponse toContactInformationResponse(
            ContactInformation entity
    ) {

        if (entity == null) {
            return null;
        }

        return ContactInformationResponse.builder()
                .email(entity.getEmail())
                .mobileNumber(entity.getMobileNumber())
                .alternateMobileNumber(
                        entity.getAlternateMobileNumber()
                )
                .emailVerified(entity.isEmailVerified())
                .mobileVerified(entity.isMobileVerified())
                .build();
    }



    public AddressResponse toAddressResponse(
            Address entity
    ) {

        if (entity == null) {
            return null;
        }

        return AddressResponse.builder()
                .addressId(entity.getAddressId())
                .type(entity.getType())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .landmark(entity.getLandmark())
                .city(entity.getCity())
                .district(entity.getDistrict())
                .state(entity.getState())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .primary(entity.isPrimary())
                .build();
    }

    public IdentificationResponse toIdentificationResponse(
            Identification entity
    ) {

        if (entity == null) {
            return null;
        }

        return IdentificationResponse.builder()
                .identificationId(entity.getIdentificationId())
                .type(entity.getType())
                .value(maskIdentification(entity.getValue()))
                .documentReference(entity.getDocumentReference())
                .verified(entity.isVerified())
                .verifiedAt(entity.getVerifiedAt())
                .verificationReference(
                        entity.getVerificationReference()
                )
                .build();
    }

    public CustomerPreferencesResponse toPreferencesResponse(
            CustomerPreferences entity
    ) {

        if (entity == null) {
            return null;
        }

        return CustomerPreferencesResponse.builder()
                .emailNotificationsEnabled(
                        entity.isEmailNotificationsEnabled()
                )
                .smsNotificationsEnabled(
                        entity.isSmsNotificationsEnabled()
                )
                .marketingCommunicationEnabled(
                        entity.isMarketingCommunicationEnabled()
                )
                .preferredLanguage(
                        entity.getPreferredLanguage()
                )
                .preferredCommunicationChannel(
                        entity.getPreferredCommunicationChannel()
                )
                .build();
    }

    public CustomerSummaryResponse toSummaryResponse(
        CustomerDocument document
) {

    if (document == null) {
        return null;
    }

    return CustomerSummaryResponse.builder()
            .id(document.getId())
            .customerNumber(document.getCustomerNumber())
            .firstName(
                    document.getPersonalInformation() != null
                            ? document.getPersonalInformation().getFirstName()
                            : null
            )
            .lastName(
                    document.getPersonalInformation() != null
                            ? document.getPersonalInformation().getLastName()
                            : null
            )
            .email(
                    document.getContactInformation() != null
                            ? document.getContactInformation().getEmail()
                            : null
            )
            .mobileNumber(
                    document.getContactInformation() != null
                            ? document.getContactInformation().getMobileNumber()
                            : null
            )
            .customerType(document.getCustomerType())
            .status(document.getStatus())
            .createdAt(document.getCreatedAt())
            .build();
}


    private String maskIdentification(String value) {

        if (value == null || value.length() <= 4) {
            return "****";
        }

        return "*".repeat(value.length() - 4)
                + value.substring(value.length() - 4);
    }
}