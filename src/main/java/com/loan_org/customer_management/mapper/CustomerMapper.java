package com.loan_org.customer_management.mapper;

import com.loan_org.customer_management.api.dto.request.ContactInformationRequest;
import com.loan_org.customer_management.api.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.api.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.api.dto.request.PersonalInformationRequest;
import com.loan_org.customer_management.api.dto.response.AddressResponse;
import com.loan_org.customer_management.api.dto.response.ContactInformationResponse;
import com.loan_org.customer_management.api.dto.response.CustomerPreferencesResponse;
import com.loan_org.customer_management.api.dto.response.CustomerResponse;
import com.loan_org.customer_management.api.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.api.dto.response.IdentificationResponse;
import com.loan_org.customer_management.api.dto.response.PersonalInformationResponse;
import com.loan_org.customer_management.entity.Address;
import com.loan_org.customer_management.entity.ContactInformation;
import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.CustomerPreferences;
import com.loan_org.customer_management.entity.Identification;
import com.loan_org.customer_management.entity.PersonalInformation;

public interface CustomerMapper {
    CustomerDocument toDocument(CreateCustomerRequest request);
    PersonalInformation toPersonalInformation(PersonalInformationRequest request);
    ContactInformation toContactInformation(ContactInformationRequest request);
    Address toAddress(CreateAddressRequest request);
    Identification toIdentification(CreateIdentificationRequest request);
    CustomerResponse toResponse(CustomerDocument document);
    PersonalInformationResponse toPersonalInformationResponse(PersonalInformation entity);
    ContactInformationResponse toContactInformationResponse(ContactInformation entity);
    AddressResponse toAddressResponse(Address entity);
    IdentificationResponse toIdentificationResponse(Identification entity);
    CustomerPreferencesResponse toPreferencesResponse(CustomerPreferences entity);
    CustomerSummaryResponse toSummaryResponse(CustomerDocument document);
}
