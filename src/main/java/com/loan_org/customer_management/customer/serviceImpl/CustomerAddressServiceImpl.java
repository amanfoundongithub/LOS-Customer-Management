package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.customer.dto.response.AddressResponse;
import com.loan_org.customer_management.customer.entity.Address;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerAddressService;
import com.loan_org.customer_management.customer.validation.AddressValidator;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidAddressException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAddressServiceImpl
        implements CustomerAddressService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final AddressValidator addressValidator;


    // ============================================================
    // ADD ADDRESS
    // ============================================================

    @Override
    public AddressResponse addAddress(
            String customerId,
            CreateAddressRequest request
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        Address address =
                customerMapper.toAddress(request);

        addressValidator.validateAddress(address);

        List<Address> addresses =
                getOrInitializeAddresses(customer);

        /*
         * --------------------------------------------------------
         * If this is the first address, automatically make it
         * primary.
         * --------------------------------------------------------
         */
        if (addresses.isEmpty()) {
            address.setPrimary(true);
        }

        /*
         * --------------------------------------------------------
         * If the request explicitly asks for this address to be
         * primary, remove primary status from existing address.
         * --------------------------------------------------------
         */
        if (address.isPrimary()) {
            removePrimaryStatus(addresses);
        }

        addresses.add(address);

        customer.setAddresses(addresses);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toAddressResponse(
                findAddress(
                        savedCustomer,
                        address.getAddressId()
                )
        );
    }


    // ============================================================
    // GET ALL ADDRESSES
    // ============================================================

    @Override
    public List<AddressResponse> getAddresses(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        return getOrInitializeAddresses(customer)
                .stream()
                .map(customerMapper::toAddressResponse)
                .toList();
    }


    // ============================================================
    // GET ADDRESS
    // ============================================================

    @Override
    public AddressResponse getAddress(
            String customerId,
            String addressId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        Address address =
                findAddress(
                        customer,
                        addressId
                );

        return customerMapper.toAddressResponse(address);
    }


    // ============================================================
    // UPDATE ADDRESS
    // ============================================================

    @Override
    public AddressResponse updateAddress(
            String customerId,
            String addressId,
            UpdateAddressRequest request
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        Address existingAddress =
                findAddress(
                        customer,
                        addressId
                );

        updateAddressFields(
                existingAddress,
                request
        );

        addressValidator.validateAddress(
                existingAddress
        );

        List<Address> addresses =
                getOrInitializeAddresses(customer);

        /*
         * --------------------------------------------------------
         * If updated address becomes primary, all other addresses
         * must become non-primary.
         * --------------------------------------------------------
         */
        if (existingAddress.isPrimary()) {

            for (Address address : addresses) {

                if (!address.getAddressId()
                        .equals(existingAddress.getAddressId())) {

                    address.setPrimary(false);
                }
            }
        }

        customer.setAddresses(addresses);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toAddressResponse(
                findAddress(
                        savedCustomer,
                        addressId
                )
        );
    }


    // ============================================================
    // DELETE ADDRESS
    // ============================================================

    @Override
    public void deleteAddress(
            String customerId,
            String addressId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        List<Address> addresses =
                getOrInitializeAddresses(customer);

        Address address =
                findAddress(
                        customer,
                        addressId
                );

        boolean wasPrimary =
                address.isPrimary();

        addresses.remove(address);

        /*
         * --------------------------------------------------------
         * If the deleted address was primary, promote another
         * address.
         * --------------------------------------------------------
         */
        if (wasPrimary && !addresses.isEmpty()) {

            addresses.get(0).setPrimary(true);
        }

        customer.setAddresses(addresses);

        customerRepository.save(customer);
    }


    // ============================================================
    // SET PRIMARY ADDRESS
    // ============================================================

    @Override
    public void setPrimaryAddress(
            String customerId,
            String addressId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        List<Address> addresses =
                getOrInitializeAddresses(customer);

        Address targetAddress =
                findAddress(
                        customer,
                        addressId
                );

        /*
         * --------------------------------------------------------
         * Remove primary status from every address.
         * --------------------------------------------------------
         */
        for (Address address : addresses) {
            address.setPrimary(false);
        }

        /*
         * --------------------------------------------------------
         * Set requested address as primary.
         * --------------------------------------------------------
         */
        targetAddress.setPrimary(true);

        customer.setAddresses(addresses);

        customerRepository.save(customer);
    }


    // ============================================================
    // FIND CUSTOMER
    // ============================================================

    private CustomerDocument findCustomer(
            String customerId
    ) {

        return customerRepository
                .findById(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );
    }


    // ============================================================
    // FIND ADDRESS
    // ============================================================

    private Address findAddress(
            CustomerDocument customer,
            String addressId
    ) {

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


    // ============================================================
    // GET / INITIALIZE ADDRESSES
    // ============================================================

    private List<Address> getOrInitializeAddresses(
            CustomerDocument customer
    ) {

        if (customer.getAddresses() == null) {

            customer.setAddresses(
                    new ArrayList<>()
            );
        }

        return customer.getAddresses();
    }


    // ============================================================
    // REMOVE PRIMARY STATUS
    // ============================================================

    private void removePrimaryStatus(
            List<Address> addresses
    ) {

        for (Address address : addresses) {
            address.setPrimary(false);
        }
    }


    // ============================================================
    // UPDATE ADDRESS FIELDS
    // ============================================================

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


    // ============================================================
    // CUSTOMER STATE VALIDATION
    // ============================================================

    private void validateCustomerCanBeModified(
            CustomerDocument customer
    ) {

        if (customer.getStatus() == null) {
            return;
        }

        if (customer.getStatus()
                .name()
                .equals("CLOSED")) {

            throw new InvalidAddressException(
                    "Addresses cannot be modified for a closed customer"
            );
        }
    }
}