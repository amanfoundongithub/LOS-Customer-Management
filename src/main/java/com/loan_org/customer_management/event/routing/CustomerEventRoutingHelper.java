package com.loan_org.customer_management.event.routing;

public class CustomerEventRoutingHelper {

    public static final Event CUSTOMER_CREATED = new Event(
        "CUSTOMER_CREATED",
        "customer.created"
    );

    public static final Event CUSTOMER_UPDATED = new Event(
        "CUSTOMER_UPDATED", 
        "customer.updated"
    );

    public static final Event CUSTOMER_STATUS_CHANGED = new Event(
        "CUSTOMER_STATUS_CHANGED", 
        "customer.status.changed"
    );

    public static final Event CUSTOMER_ADDRESS_CHANGED = new Event(
        "CUSTOMER_ADDRESS_CHANGED",
        "customer.address.changed"
    );

    public static final Event CUSTOMER_IDENTIFICATION_CHANGED = new Event(
        "CUSTOMER_IDENTIFICATION_CHANGED", 
        "customer.identification.changed"
    );


    public static class Event {
        public Event(String name, String routingKey) {
            this.name = name;
            this.routingKey = routingKey;
        }
        public String name;
        public String routingKey;
    }
    
}
