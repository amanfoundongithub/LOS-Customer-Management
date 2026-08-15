package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.customer.dto.response.IdentificationResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.entity.Identification;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerIdentificationService;
import com.loan_org.customer_management.customer.validation.IdentificationValidator;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidCustomerStateException;
import com.loan_org.customer_management.exception.InvalidIdentificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerIdentificationServiceImpl
        implements CustomerIdentificationService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final IdentificationValidator identificationValidator;


    // ============================================================
    // ADD IDENTIFICATION
    // ============================================================

    @Override
    public IdentificationResponse addIdentification(
            String customerId,
            CreateIdentificationRequest request
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        Identification identification =
                customerMapper.toIdentification(request);

        /*
         * --------------------------------------------------------
         * Generate embedded identification ID
         * --------------------------------------------------------
         */
        identification.setIdentificationId(
                UUID.randomUUID().toString()
        );

        identificationValidator.validateIdentification(
                identification
        );

        List<Identification> identifications =
                getOrInitializeIdentifications(customer);

        /*
         * --------------------------------------------------------
         * Prevent duplicate identification type.
         * --------------------------------------------------------
         */
        validateDuplicateIdentificationType(
                identifications,
                identification
        );

        identifications.add(identification);

        customer.setIdentifications(identifications);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toIdentificationResponse(
                findIdentification(
                        savedCustomer,
                        identification.getIdentificationId()
                )
        );
    }


    // ============================================================
    // GET ALL IDENTIFICATIONS
    // ============================================================

    @Override
    public List<IdentificationResponse> getIdentifications(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        return getOrInitializeIdentifications(customer)
                .stream()
                .map(customerMapper::toIdentificationResponse)
                .toList();
    }


    // ============================================================
    // GET IDENTIFICATION
    // ============================================================

    @Override
    public IdentificationResponse getIdentification(
            String customerId,
            String identificationId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        Identification identification =
                findIdentification(
                        customer,
                        identificationId
                );

        return customerMapper.toIdentificationResponse(
                identification
        );
    }


    // ============================================================
    // DELETE IDENTIFICATION
    // ============================================================

    @Override
    public void deleteIdentification(
            String customerId,
            String identificationId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateCustomerCanBeModified(customer);

        List<Identification> identifications =
                getOrInitializeIdentifications(customer);

        Identification identification =
                findIdentification(
                        customer,
                        identificationId
                );

        /*
         * --------------------------------------------------------
         * Verified identifications should not be deleted through
         * the normal customer API.
         * --------------------------------------------------------
         */
        if (identification.isVerified()) {

            throw new InvalidIdentificationException(
                    "Verified identification cannot be deleted"
            );
        }

        identifications.remove(identification);

        customer.setIdentifications(
                identifications
        );

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
    // FIND IDENTIFICATION
    // ============================================================

    private Identification findIdentification(
            CustomerDocument customer,
            String identificationId
    ) {

        return getOrInitializeIdentifications(customer)
                .stream()
                .filter(identification ->
                        identification
                                .getIdentificationId()
                                .equals(identificationId)
                )
                .findFirst()
                .orElseThrow(
                        () -> new InvalidIdentificationException(
                                "Identification not found with id: "
                                        + identificationId
                        )
                );
    }


    // ============================================================
    // GET / INITIALIZE IDENTIFICATIONS
    // ============================================================

    private List<Identification> getOrInitializeIdentifications(
            CustomerDocument customer
    ) {

        if (customer.getIdentifications() == null) {

            customer.setIdentifications(
                    new ArrayList<>()
            );
        }

        return customer.getIdentifications();
    }


    // ============================================================
    // DUPLICATE IDENTIFICATION VALIDATION
    // ============================================================

    private void validateDuplicateIdentificationType(
            List<Identification> existingIdentifications,
            Identification newIdentification
    ) {

        boolean duplicate =
                existingIdentifications
                        .stream()
                        .anyMatch(existing ->
                                existing.getType()
                                        == newIdentification.getType()
                        );

        if (duplicate) {

            throw new InvalidIdentificationException(
                    "Identification type "
                            + newIdentification.getType()
                            + " already exists for this customer"
            );
        }
    }


    // ============================================================
    // CUSTOMER STATE VALIDATION
    // ============================================================

    private void validateCustomerCanBeModified(
            CustomerDocument customer
    ) {

        if (customer.getStatus() == CustomerStatus.CLOSED) {

            throw new InvalidCustomerStateException(
                    "Identifications cannot be modified "
                            + "for a closed customer"
            );
        }
    }
}