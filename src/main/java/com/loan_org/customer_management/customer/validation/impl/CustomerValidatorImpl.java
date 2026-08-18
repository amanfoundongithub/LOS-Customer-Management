package com.loan_org.customer_management.customer.validation.impl;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.validation.CustomerValidator;
import com.loan_org.customer_management.exception.CustomerAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerValidatorImpl implements CustomerValidator{

    private final CustomerRepository customerRepository;

    @Override
    public void validateCreate(CreateCustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Customer creation request cannot be null");
        }
        validateIamUser(request.getIamUserId());
        validateCustomerType(request);
        validateRequiredInformation(request);
    }

    public void validateUpdate(CustomerDocument customer, UpdateCustomerRequest request) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }
        validateCustomerCanBeUpdated(customer);
    }


    private void validateIamUser(String iamUserId) {
        if (!StringUtils.hasText(iamUserId)) {
            return;
        }
        if (customerRepository.existsByIamUserId(iamUserId)) {
            throw new CustomerAlreadyExistsException("IAM user is already associated with a customer.");
        }
    }

    private void validateRequiredInformation(CreateCustomerRequest request) {
        if (request.getPersonalInformation() == null) {
            throw new IllegalArgumentException("Personal information is required");
        }
        if (request.getContactInformation() == null) {
            throw new IllegalArgumentException("Contact information is required");
        }
    }

    private void validateCustomerType(CreateCustomerRequest request) {
        if (request.getCustomerType() == null) {
            throw new IllegalArgumentException("Customer type is required");
        }
    }

    private void validateCustomerCanBeUpdated(CustomerDocument customer) {
        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new IllegalStateException("Closed customers cannot be updated");
        }
    }
}