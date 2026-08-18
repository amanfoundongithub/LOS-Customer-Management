package com.loan_org.customer_management.customer.service.impl;

import com.loan_org.customer_management.customer.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.customer.dto.response.AddressResponse;
import com.loan_org.customer_management.customer.entity.Address;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.AddressService;
import com.loan_org.customer_management.customer.validation.AddressValidator;
import com.loan_org.customer_management.event.publisher.CustomerEventPublisher;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidAddressException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper     customerMapper;
    private final AddressValidator   addressValidator;
    private final CustomerEventPublisher customerEventPublisher;


    @Override
    @Transactional
    public AddressResponse addAddress(String customerId, CreateAddressRequest request) {

        log.info("Adding new address for customer: {}", customerId);
        CustomerDocument customer = findCustomer(customerId);
        
        log.info("Validating consumer for update...");
        validateCustomerCanBeModified(customer);

        Address address = customerMapper.toAddress(request);

        log.info("Now validating address...");
        addressValidator.validateAddress(address);

        List<Address> addresses = getOrInitializeAddresses(customer);

        if (addresses.isEmpty()) {
            address.setPrimary(true);
        }

        if (address.isPrimary()) {
            removePrimaryStatus(addresses);
        }

        addresses.add(address);
        customer.setAddresses(addresses);
        CustomerDocument savedCustomer = customerRepository.save(customer);

        log.info("Address persisted in Mongo, now dispatching same to RabbitMQ...");
        customerEventPublisher.publishCustomerAddressChanged(savedCustomer, address.getAddressId(), "ADD");

        return customerMapper.toAddressResponse(findAddress(savedCustomer, address.getAddressId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(String customerId) {
        CustomerDocument customer = findCustomer(customerId);
        return getOrInitializeAddresses(customer)
                .stream()
                .map(customerMapper::toAddressResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddress(String customerId, String addressId) {
        CustomerDocument customer = findCustomer(customerId);
        Address address = findAddress(customer, addressId);
        return customerMapper.toAddressResponse(address);
    }


    @Override
    @Transactional
    public AddressResponse updateAddress(String customerId, String addressId, UpdateAddressRequest request) {

        log.info("Received request to update address for: {}", customerId);
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);

        Address existingAddress = findAddress(customer, addressId);
        updateAddressFields(existingAddress, request);

        log.info("Validating address...");
        addressValidator.validateAddress(existingAddress);

        List<Address> addresses = getOrInitializeAddresses(customer);

        if (existingAddress.isPrimary()) {
            for (Address address : addresses) {
                if (!address.getAddressId()
                        .equals(existingAddress.getAddressId())) {
                    address.setPrimary(false);
                }
            }
        }
        customer.setAddresses(addresses);
        CustomerDocument savedCustomer = customerRepository.save(customer);

        log.info("Address persisted in Mongo, now dispatching same to RabbitMQ...");
        customerEventPublisher.publishCustomerAddressChanged(savedCustomer, addressId, "UPDATE_ADDRESS");

        return customerMapper.toAddressResponse(findAddress(savedCustomer, addressId));
    }

    @Override
    @Transactional
    public void deleteAddress(String customerId, String addressId) {
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);
        List<Address> addresses = getOrInitializeAddresses(customer);
        Address address = findAddress(customer, addressId);
        boolean wasPrimary = address.isPrimary();
        addresses.remove(address);
        if (wasPrimary && !addresses.isEmpty()) {
            addresses.get(0).setPrimary(true);
        }
        customer.setAddresses(addresses);
        customerRepository.save(customer);
        customerEventPublisher.publishCustomerAddressChanged(customer, addressId, "DELETE");
    }


    @Override
    @Transactional
    public void setPrimaryAddress(String customerId, String addressId) {
        CustomerDocument customer = findCustomer(customerId);
        validateCustomerCanBeModified(customer);
        List<Address> addresses = getOrInitializeAddresses(customer);
        Address targetAddress = findAddress(customer, addressId);
        for (Address address : addresses) {
            address.setPrimary(false);
        }
        targetAddress.setPrimary(true);
        customer.setAddresses(addresses);
        customerRepository.save(customer);
        customerEventPublisher.publishCustomerAddressChanged(customer, addressId, "SET_PRIMARY");
    }


    private CustomerDocument findCustomer(String customerId) {
        return customerRepository
                .findByCustomerNumber(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found with id: " + customerId)
                );
    }

    private Address findAddress(CustomerDocument customer,  String addressId) {
        return getOrInitializeAddresses(customer)
                .stream()
                .filter(address ->
                        address.getAddressId()
                                .equals(addressId)
                )
                .findFirst()
                .orElseThrow(
                        () -> new InvalidAddressException(
                                "Address not found with id: "
                                        + addressId
                        )
                );
    }


    private List<Address> getOrInitializeAddresses(CustomerDocument customer) {
        if (customer.getAddresses() == null) {
            customer.setAddresses(new ArrayList<>());
        }
        return customer.getAddresses();
    }

    private void removePrimaryStatus(List<Address> addresses) {
        for (Address address : addresses) {
            address.setPrimary(false);
        }
    }

    private void updateAddressFields(
            Address address,
            UpdateAddressRequest request
    ) {

        if (request.getType() != null) {
            address.setType(request.getType());
        }

        if (request.getAddressLine1() != null) {
            address.setAddressLine1(
                    request.getAddressLine1()
            );
        }

        if (request.getAddressLine2() != null) {
            address.setAddressLine2(
                    request.getAddressLine2()
            );
        }

        if (request.getLandmark() != null) {
            address.setLandmark(
                    request.getLandmark()
            );
        }

        if (request.getCity() != null) {
            address.setCity(
                    request.getCity()
            );
        }

        if (request.getDistrict() != null) {
            address.setDistrict(
                    request.getDistrict()
            );
        }

        if (request.getState() != null) {
            address.setState(
                    request.getState()
            );
        }

        if (request.getPostalCode() != null) {
            address.setPostalCode(
                    request.getPostalCode()
            );
        }

        if (request.getCountry() != null) {
            address.setCountry(
                    request.getCountry()
            );
        }

        if (request.getPrimary() != null) {
            address.setPrimary(
                    request.getPrimary()
            );
        }
    }

    private void validateCustomerCanBeModified(CustomerDocument customer) {
        if (customer.getStatus() == null) {
            return;
        }
        if (customer.getStatus().equals(CustomerStatus.CLOSED)) {
            throw new InvalidAddressException("Addresses cannot be modified for a closed customer");
        }
    }
}