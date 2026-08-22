package com.loan_org.customer_management.event.publisher;

import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.enums.CustomerStatus;

public interface CustomerEventPublisher {
    void publishCustomerCreated(CustomerDocument customer);
    void publishCustomerUpdated(CustomerDocument customer);
    void publishCustomerStatusChanged(CustomerDocument customer, CustomerStatus previousStatus, CustomerStatus newStatus);
    void publishCustomerAddressChanged(CustomerDocument customer, String addressId, String changeType);
    void publishCustomerIdentificationChanged(CustomerDocument customer, String identificationId, String changeType);
}