package com.loan_org.customer_management.event.publisher;

import com.loan_org.customer_management.config.mdc.MdcProperties;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.event.event.CustomerAddressChangedEvent;
import com.loan_org.customer_management.event.event.CustomerCreatedEvent;
import com.loan_org.customer_management.event.event.CustomerEvent;
import com.loan_org.customer_management.event.event.CustomerIdentificationChangedEvent;
import com.loan_org.customer_management.event.event.CustomerStatusChangedEvent;
import com.loan_org.customer_management.event.event.CustomerUpdatedEvent;
import com.loan_org.customer_management.event.routing.CustomerEventRoutingHelper;
import com.loan_org.customer_management.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxCustomerEventPublisher implements CustomerEventPublisher {

    private static final String AGGREGATE_TYPE = "CUSTOMER";

    private final OutboxService outboxService;
    private final MdcProperties mdcProperties;

    @Override
    public void publishCustomerCreated(CustomerDocument customer) {
        CustomerCreatedEvent data =
                CustomerCreatedEvent.builder()
                        .customerId(customer.getId())
                        .customerNumber(customer.getCustomerNumber())
                        .iamUserId(customer.getIamUserId())
                        .email(
                                customer.getContactInformation() != null
                                        ? customer.getContactInformation().getEmail()
                                        : null
                        )
                        .mobileNumber(
                                customer.getContactInformation() != null
                                        ? customer.getContactInformation().getMobileNumber()
                                        : null
                        )
                        .build();
        publishEvent(CustomerEventRoutingHelper.CUSTOMER_CREATED, customer, data);
    }

    @Override
    public void publishCustomerUpdated(CustomerDocument customer) {
        CustomerUpdatedEvent data =
                CustomerUpdatedEvent.builder()
                        .customerId(customer.getId())
                        .customerNumber(customer.getCustomerNumber())
                        .iamUserId(customer.getIamUserId())
                        .build();
        publishEvent(CustomerEventRoutingHelper.CUSTOMER_UPDATED, customer, data);
    }

    @Override
    public void publishCustomerStatusChanged(CustomerDocument customer, CustomerStatus previousStatus, CustomerStatus newStatus) {
        CustomerStatusChangedEvent data =
                CustomerStatusChangedEvent.builder()
                        .customerId(customer.getId())
                        .customerNumber(customer.getCustomerNumber())
                        .previousStatus(previousStatus)
                        .newStatus(newStatus)
                        .build();
        publishEvent(CustomerEventRoutingHelper.CUSTOMER_STATUS_CHANGED, customer, data);
    }

    @Override
    public void publishCustomerAddressChanged(CustomerDocument customer, String addressId, String changeType) {
        CustomerAddressChangedEvent data =
                CustomerAddressChangedEvent.builder()
                        .customerId(customer.getId())
                        .customerNumber(customer.getCustomerNumber())
                        .addressId(addressId)
                        .changeType(changeType)
                        .build();
        publishEvent(CustomerEventRoutingHelper.CUSTOMER_ADDRESS_CHANGED, customer, data);
    }

    @Override
    public void publishCustomerIdentificationChanged(CustomerDocument customer, String identificationId, String changeType) {
        CustomerIdentificationChangedEvent data =
                CustomerIdentificationChangedEvent.builder()
                        .customerId(customer.getId())
                        .customerNumber(customer.getCustomerNumber())
                        .identificationId(identificationId)
                        .changeType(changeType)
                        .build();
        publishEvent(CustomerEventRoutingHelper.CUSTOMER_IDENTIFICATION_CHANGED, customer, data);
    }

    private <T> void publishEvent(CustomerEventRoutingHelper.Event eventType, CustomerDocument customer,T customerEvent) {
        CustomerEvent<T> event = createEvent(eventType.name, customer, customerEvent);
        saveToOutbox(event, eventType.routingKey);
    }

    private <T> CustomerEvent<T> createEvent(String eventType, CustomerDocument customer, T data) {
        return CustomerEvent.<T>builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .occurredAt(Instant.now())
                .customerId(customer.getId())
                .customerNumber(customer.getCustomerNumber())
                .correlationId(MDC.get(mdcProperties.getCorrelation().getMdcKey()))
                .traceId(MDC.get(mdcProperties.getTrace().getMdcKey()))
                .data(data)
                .build();
    }

    private void saveToOutbox(CustomerEvent<?> event, String routingKey) {
        outboxService.createEvent(
                event.getEventType(),
                AGGREGATE_TYPE,
                event.getCustomerId(),
                routingKey,
                event
        );
    }
}