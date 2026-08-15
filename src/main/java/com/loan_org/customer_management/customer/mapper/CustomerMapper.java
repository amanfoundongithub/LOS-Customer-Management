package com.loan_org.customer_management.customer.mapper;

import com.loan_org.customer_management.customer.dto.request.ContactInformationRequest;
import com.loan_org.customer_management.customer.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.customer.dto.request.PersonalInformationRequest;
import com.loan_org.customer_management.customer.dto.response.AddressResponse;
import com.loan_org.customer_management.customer.dto.response.ContactInformationResponse;
import com.loan_org.customer_management.customer.dto.response.CustomerPreferencesResponse;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.customer.dto.response.IdentificationResponse;
import com.loan_org.customer_management.customer.dto.response.PersonalInformationResponse;
import com.loan_org.customer_management.customer.entity.Address;
import com.loan_org.customer_management.customer.entity.ContactInformation;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.entity.CustomerPreferences;
import com.loan_org.customer_management.customer.entity.Identification;
import com.loan_org.customer_management.customer.entity.PersonalInformation;

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
