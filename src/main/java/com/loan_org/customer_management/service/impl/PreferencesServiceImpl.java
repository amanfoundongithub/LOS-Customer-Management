package com.loan_org.customer_management.service.impl;

import com.loan_org.customer_management.api.dto.request.UpdatePreferencesRequest;
import com.loan_org.customer_management.api.dto.response.CustomerPreferencesResponse;
import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.CustomerPreferences;
import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidCustomerStateException;
import com.loan_org.customer_management.mapper.CustomerMapper;
import com.loan_org.customer_management.repository.CustomerRepository;
import com.loan_org.customer_management.service.PreferencesService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferencesServiceImpl implements PreferencesService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerPreferencesResponse getPreferences(String customerId) {
        CustomerDocument customer = findCustomer(customerId);
        CustomerPreferences preferences = getOrInitializePreferences(customer);
        return customerMapper.toPreferencesResponse(preferences);
    }

    @Override
    public CustomerPreferencesResponse updatePreferences(String customerId, UpdatePreferencesRequest request) {
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);

        CustomerPreferences preferences = getOrInitializePreferences(customer);
        applyUpdates(preferences, request);
        customer.setPreferences(preferences);

        CustomerDocument savedCustomer = customerRepository.save(customer);
        return customerMapper.toPreferencesResponse(savedCustomer.getPreferences());
    }

    private CustomerDocument findCustomer(String customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    private CustomerPreferences getOrInitializePreferences(CustomerDocument customer) {
        if (customer.getPreferences() == null) {
            customer.setPreferences(CustomerPreferences.builder().build());
        }
        return customer.getPreferences();
    }

    private void applyUpdates(CustomerPreferences preferences, UpdatePreferencesRequest request) {
        if (request.getEmailNotificationsEnabled() != null) {
            preferences.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        }
        if (request.getSmsNotificationsEnabled() != null) {
            preferences.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
        }
        if (request.getMarketingCommunicationEnabled() != null) {
            preferences.setMarketingCommunicationEnabled(request.getMarketingCommunicationEnabled());
        }
        if (request.getPreferredLanguage() != null) {
            preferences.setPreferredLanguage(request.getPreferredLanguage());
        }
        if (request.getPreferredCommunicationChannel() != null) {
            preferences.setPreferredCommunicationChannel(request.getPreferredCommunicationChannel());
        }
    }

    private void validateCustomerCanBeModified(CustomerDocument customer) {
        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidCustomerStateException("Preferences cannot be modified for a closed customer");
        }
    }
}