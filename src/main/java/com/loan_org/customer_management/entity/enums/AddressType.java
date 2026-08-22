package com.loan_org.customer_management.entity.enums;

public enum AddressType {

    /**
     * Customer's current residential address.
     *
     * Represents the address where the customer
     * currently lives.
     */
    RESIDENTIAL,

    /**
     * Customer's permanent/home address.
     *
     * Represents the customer's long-term or
     * officially declared home address.
     */
    PERMANENT,

    /**
     * Customer's employment or business address.
     *
     * Used when the customer needs to provide
     * an address associated with their occupation.
     */
    WORK,

    /**
     * Address used for receiving physical correspondence.
     *
     * This may be different from the customer's
     * residential or permanent address.
     */
    MAILING,

    /**
     * Any additional address that does not fit
     * into the predefined address categories.
     */
    OTHER
}