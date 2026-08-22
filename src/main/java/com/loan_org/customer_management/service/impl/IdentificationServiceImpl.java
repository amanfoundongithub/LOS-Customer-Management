package com.loan_org.customer_management.service.impl;

import com.loan_org.customer_management.api.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.api.dto.response.IdentificationResponse;
import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.Identification;
import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidCustomerStateException;
import com.loan_org.customer_management.exception.InvalidIdentificationException;
import com.loan_org.customer_management.mapper.CustomerMapper;
import com.loan_org.customer_management.repository.CustomerRepository;
import com.loan_org.customer_management.service.IdentificationService;
import com.loan_org.customer_management.validation.IdentificationValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentificationServiceImpl implements IdentificationService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final IdentificationValidator identificationValidator;

    @Override
    public IdentificationResponse addIdentification(String customerId, CreateIdentificationRequest request) {
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);

        Identification identification = customerMapper.toIdentification(request);
        identification.setIdentificationId(UUID.randomUUID().toString());

        identificationValidator.validateIdentification(identification);

        List<Identification> identifications = getOrInitializeIdentifications(customer);
        validateDuplicateIdentificationType(identifications, identification);

        identifications.add(identification);
        customer.setIdentifications(identifications);

        CustomerDocument savedCustomer = customerRepository.save(customer);
        return customerMapper.toIdentificationResponse(
                findIdentification(savedCustomer, identification.getIdentificationId()));
    }

    @Override
    public List<IdentificationResponse> getIdentifications(String customerId) {
        CustomerDocument customer = findCustomer(customerId);
        return getOrInitializeIdentifications(customer)
                .stream()
                .map(customerMapper::toIdentificationResponse)
                .toList();
    }

    @Override
    public IdentificationResponse getIdentification(String customerId, String identificationId) {
        CustomerDocument customer = findCustomer(customerId);
        Identification identification = findIdentification(customer, identificationId);
        return customerMapper.toIdentificationResponse(identification);
    }

    @Override
    public void deleteIdentification(String customerId, String identificationId) {
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);

        List<Identification> identifications = getOrInitializeIdentifications(customer);
        Identification identification = findIdentification(customer, identificationId);

        if (identification.isVerified()) {
            throw new InvalidIdentificationException("Verified identification cannot be deleted");
        }

        identifications.remove(identification);
        customer.setIdentifications(identifications);
        customerRepository.save(customer);
    }

    private CustomerDocument findCustomer(String customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    private Identification findIdentification(CustomerDocument customer, String identificationId) {
        return getOrInitializeIdentifications(customer)
                .stream()
                .filter(identification -> identification.getIdentificationId().equals(identificationId))
                .findFirst()
                .orElseThrow(() -> new InvalidIdentificationException("Identification not found with id: " + identificationId));
    }

    private List<Identification> getOrInitializeIdentifications(CustomerDocument customer) {
        if (customer.getIdentifications() == null) {
            customer.setIdentifications(new ArrayList<>());
        }
        return customer.getIdentifications();
    }

    private void validateDuplicateIdentificationType(List<Identification> existingIdentifications,
            Identification newIdentification) {
        boolean duplicate = existingIdentifications
                .stream()
                .anyMatch(existing -> existing.getType() == newIdentification.getType());

        if (duplicate) {
            throw new InvalidIdentificationException(
                    "Identification type " + newIdentification.getType() + " already exists for this customer");
        }
    }

    private void validateCustomerCanBeModified(CustomerDocument customer) {
        if (customer.getStatus() == CustomerStatus.CLOSED) {
            throw new InvalidCustomerStateException(
                    "Identifications cannot be modified for a closed customer");
        }
    }
}